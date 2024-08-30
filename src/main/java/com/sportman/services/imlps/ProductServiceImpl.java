package com.sportman.services.imlps;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sportman.dto.request.ColorCreateRequest;
import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.request.ProductUpdateRequest;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.page.ProductPageResponse;
import com.sportman.entities.*;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.ColorMapper;
import com.sportman.mappers.ProductMapper;
import com.sportman.repositories.*;
import com.sportman.services.interfaces.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.imgscalr.Scalr;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    SeasonRepository seasonRepository;
    ClubRepository clubRepository;
    SizeRepository sizeRepository;
    ColorRepository colorRepository;
    ProductMapper productMapper;
    ColorMapper colorMapper;
    Cloudinary cloudinary;


    @Override
    public ProductPageResponse get() {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('CREATE_PRODUCT')")
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
        Club club = clubRepository.findById(request.getClub().toUpperCase()).orElseThrow(() -> new AppException(ErrorCode.CLUB_NOT_FOUND));

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
    public void delete(String productId) {

    }

    @Override
    public ProductCreateResponse update(String productId, ProductUpdateRequest request) {
        return null;
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
