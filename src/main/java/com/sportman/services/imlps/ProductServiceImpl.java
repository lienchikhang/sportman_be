package com.sportman.services.imlps;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
