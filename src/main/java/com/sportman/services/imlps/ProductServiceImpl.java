package com.sportman.services.imlps;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sportman.dto.request.*;
import com.sportman.dto.response.*;
import com.sportman.dto.response.page.ProductPageResponse;
import com.sportman.dto.response.page.RatePageResponse;
import com.sportman.entities.*;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.ColorMapper;
import com.sportman.mappers.ProductMapper;
import com.sportman.mappers.RateMapper;
import com.sportman.repositories.*;
import com.sportman.services.interfaces.ProductService;
import com.sportman.services.specifications.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.imgscalr.Scalr;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    //repos
    ProductRepository productRepository;
    SeasonRepository seasonRepository;
    ClubRepository clubRepository;
    SizeRepository sizeRepository;
    ColorRepository colorRepository;
    ProductSizeRepository productSizeRepository;
    RateRepository rateRepository;

    //mappers
    ProductMapper productMapper;
    ColorMapper colorMapper;
    RateMapper rateMapper;

    //others
    Cloudinary cloudinary;


    @Override
    public ProductPageResponse get(
            Pageable pageable,
            String name,
            String club,
            String season,
            Integer price,
            String sizes,
            Boolean isDeleted,
            String sort
    ) {

        Specification<Product> productSpec = Specification.where(ProductSpecification.hasDeleted(isDeleted));

        if (Objects.nonNull(name)) {
            productSpec = productSpec.and(ProductSpecification.hasName(name));
        }

        if (Objects.nonNull(club)) {
            productSpec = productSpec.and(ProductSpecification.hasClub(club));
        }

        if (Objects.nonNull(price)) {
            productSpec = productSpec.and(ProductSpecification.hasPrice(price));
        }

        if (Objects.nonNull(season)) {
            List<Integer> intSeasons = Arrays.stream(season.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            productSpec = productSpec.and(ProductSpecification.hasSeason(intSeasons.getFirst(), intSeasons.getLast()));
        }

        if (Objects.nonNull(sizes)) {
            List<String> splitSizes = Arrays.stream(sizes.split("-")).toList();
            productSpec = productSpec.and(ProductSpecification.hasSize(splitSizes));
        }

        if (Objects.nonNull(sort)) {
            productSpec = productSpec.and(ProductSpecification.hasSort(sort.toLowerCase()));
        }

        List<Product> products = productRepository.findAll(productSpec, pageable).toList();
        List<ProductGetResponse> productGetResponseList = products.stream().map(pro -> productMapper.toGetResponse(pro)).toList();

        for (int i = 0; i < products.size(); i++) {
            List<Color> colors = products.get(i).getColors();
            productGetResponseList.get(i).setColors(colors.stream().map(color -> color.getColorHex()).toList());
        }

        long totalPros = productRepository.count(productSpec);

        return ProductPageResponse.builder()
                .products(Collections.singletonList(productGetResponseList))
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(Math.ceilDiv(totalPros, pageable.getPageSize()))
                .totalElements(totalPros)
                .build();
    }

    @Override
    public ProductPageResponse getListName(Pageable pageable, String name ) {

        log.info(pageable.toString());

        Specification<Product> specs = Specification.where(ProductSpecification.hasDeleted(false))
                .and(ProductSpecification.hasName(name));

        List<ProductNameResponse> pros = productRepository.findAll(specs, pageable)
                .stream().map(pro -> productMapper.toNameResponse(pro)).toList();

        return ProductPageResponse.builder()
                .totalElements(productRepository.count(specs))
                .totalPage(Math.ceilDiv(productRepository.count(specs), pageable.getPageSize()))
                .currentPage(pageable.getPageNumber() + 1)
                .products(Collections.singletonList(pros))
                .build();
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // && hasAuthority('CREATE_PRODUCT')
    public ProductCreateResponse create(ProductCreateRequest request) {

        //check product name exist
        if (productRepository.existsByProductName(request.getProductName())) throw new AppException(ErrorCode.PRODUCT_EXISTED);

        //check season exist
        Season season = seasonRepository.findById(SeasonId
                .builder()
                .yearStart(request.getSeason().getFirst())
                .yearEnd(request.getSeason().getLast())
                .build()).orElseThrow(() -> new AppException(ErrorCode.SEASON_NOT_FOUND));

        //check club exist
        Club club = clubRepository.findById(request.getClub().toUpperCase().replaceAll(" ", "-")).orElseThrow(() -> new AppException(ErrorCode.CLUB_NOT_FOUND));

        //create colors
        List<Color> colors = request.getColors()
                .stream()
                .map(color -> colorMapper.toColor(ColorCreateRequest
                            .builder()
                            .colorHex(color)
                            .build())
                )
                .toList();

        //create stocks
        List<ProductSize> stocks = new ArrayList<>();
        request.getSizes().forEach(size -> {
            stocks.add(ProductSize
                    .builder()
                    .stock(size.getStock())
                    .sizeTag(sizeRepository.findById(size.getSize()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND)))
                    .build());
        });


        //create new product
        Product newProduct = productMapper.toProduct(request);
        newProduct.setProductName(newProduct.getProductName().toLowerCase().replaceAll(" ", "-"));
        newProduct.setSeason(season);
        newProduct.setClub(club);
        newProduct.setIsDeleted(false);

        //set images
        newProduct.setFrontImage("");
        newProduct.setBackImage("");

        //set colors
        newProduct.setColors(colors);
        newProduct.getColors().forEach(color -> color.setProduct(newProduct));

        //set size & stock
        newProduct.setStocks(stocks);
        newProduct.getStocks().forEach(stock -> {
            stock.setProduct(newProduct);
            stock.setId(ProductSizeId
                    .builder()
                    .productId(newProduct.getId())
                    .sizeTag(stock.getSizeTag().getSizeTag())
                    .build());
        });

        return productMapper.toResponse(productRepository.save(newProduct));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String productId) {
        //check product exist
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //delete
        product.setIsDeleted(true);

        //save
        productRepository.save(product);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductCreateResponse update(String productId, ProductUpdateRequest request) {

        //check product exist
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (Objects.nonNull(request.getProductName())) {
            product.setProductName(request.getProductName().replaceAll(" ", "-"));
        }

        if (Objects.nonNull(request.getProductDesc())) {
            product.setProductDesc(request.getProductDesc());
        }

        if (Objects.nonNull(request.getProductPrice())) {
            product.setProductPrice(request.getProductPrice());
        }

        if (Objects.nonNull(request.getSeason())) {
            Season season = seasonRepository.findById(SeasonId.builder()
                    .yearStart(request.getSeason().getFirst())
                    .yearEnd(request.getSeason().getLast())
                    .build()).orElseThrow(() -> new AppException(ErrorCode.SEASON_NOT_FOUND));
            product.setSeason(season);
        }

        if (Objects.nonNull(request.getClub())) {
            Club club = clubRepository.findById(request.getClub()).orElseThrow(() -> new AppException(ErrorCode.CLUB_NOT_FOUND));
            product.setClub(club);
        }

        if (!CollectionUtils.isEmpty(request.getColors())) {

            List<Color> colors = colorRepository.findAllById(request.getColors());

            product.setColors(colors);
            product.getColors().forEach(color -> color.setProduct(product));
        }

        Product savedProduct = productRepository.save(product);
        ProductCreateResponse res = productMapper.toResponse(savedProduct);
        List<SizeAndStock> sizeAndStocks = new ArrayList<>();

        savedProduct.getStocks().forEach(stock -> {
            sizeAndStocks.add(SizeAndStock.builder()
                    .size(stock.getSizeTag().getSizeTag())
                    .stock(stock.getStock())
                    .build());
        });
        res.setStocks(sizeAndStocks);

        return res;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void uploadImage(List<MultipartFile> files, String productId) {

        //check list file
        if (files.size() != 2) throw new AppException(ErrorCode.PRODUCT_INVALID_FILE);

        //check product exist
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            List<String> imgUrls = new ArrayList<>();

            files.forEach(file -> {

                try {
                    //convert MultipartFile --> bufferedImage
                    BufferedImage oriImg = ImageIO.read(file.getInputStream());

                    //compress
                    String format = file.getContentType().split("/")[1];
                    byte[] bytes = compressImage(oriImg, format);

                    //create cloudinary options
                    Map c = ObjectUtils.asMap(
                            "folder", "sportman",
                            "upload_preset", "awdqa2wj",
                            "public_id", productId + "-" + files.indexOf(file),
                            "overwrite", true
                    );

                    //update to cloudinary
                    Map rs = cloudinary.uploader().upload(bytes, c);

                    imgUrls.add((String) rs.get("url"));

                } catch (IOException e) {
                    log.warn(e.getMessage());
                    throw new RuntimeException(e);
                }

            });

            product.setFrontImage(imgUrls.get(0));
            product.setBackImage(imgUrls.get(1));
            productRepository.save(product);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStock(String productId, String sizeTag, ProductUpdateStockRequest request) {

        log.warn(sizeTag);

        //check product exist
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //check size exist
        Size size = sizeRepository.findById(sizeTag.toUpperCase()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));

        ProductSize productSize = productSizeRepository.findById(ProductSizeId.builder().
                sizeTag(size.getSizeTag())
                .productId(product.getId())
                .build()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productSize.setStock(request.getStock());

        productSizeRepository.save(productSize);
    }

    @Override
    public ProductGetDetailResponse getById(String productId) {
        //check exist
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductGetDetailResponse res = productMapper.toGetDetailResponse(product);

        //get sizes
        List<ProductSize> sizes = productSizeRepository.findAllByProduct(product);
        res.setStocks(sizes.stream().map(size -> Stocks.builder()
                        .sizeTag(size.getSizeTag().getSizeTag())
                        .stocks(size.getStock())
                        .build())
                .toList());

        //get colors
        res.setColors(product.getColors().stream().map(color -> color.getColorHex()).toList());

        //get season
        Season productSeason = product.getSeason();
        List<Integer> dates = new ArrayList<>();
        dates.add(productSeason.getId().getYearStart());
        dates.add(productSeason.getId().getYearEnd());
        res.setSeasons(dates);

        return res;

    }

    @Override
    public RatePageResponse getRatesByProductId(String productId, Pageable pageable) {

        //check product exist
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<RateResponse> rates = rateRepository.findAllByProduct(pageable, product)
                .stream().map(rate -> {
                    RateResponse rs = rateMapper.toResponse(rate);
                    rs.setUser(UserCommentResponse.builder()
                            .avatar(rate.getUser().getAvatar())
                            .username(rate.getUser().getUsername())
                            .build());
                    return rs;
                })
                .toList();

        long totalRates = rateRepository.count();

        return RatePageResponse.builder()
                .rates(rates)
                .currentPage(pageable.getPageNumber() + 1)
                .totalElements(totalRates)
                .totalPage(Math.ceilDiv(totalRates, pageable.getPageSize()))
                .build();
    }


    private byte[] compressImage(BufferedImage orgImage, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(orgImage)
                .size(orgImage.getWidth(), orgImage.getHeight())
                .outputQuality(.5)
                .outputFormat(format)
                .toOutputStream(baos);

        return baos.toByteArray();
    }

}
