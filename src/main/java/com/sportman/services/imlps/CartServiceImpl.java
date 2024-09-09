package com.sportman.services.imlps;

import com.sportman.dto.request.CartCreateRequest;
import com.sportman.dto.response.CartCreateResponse;
import com.sportman.dto.response.page.CartPageResponse;
import com.sportman.entities.*;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.CartDetailMapper;
import com.sportman.repositories.*;
import com.sportman.services.interfaces.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    //repos
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartDetailRepository cartDetailRepository;
    SizeRepository sizeRepository;
    ProductSizeRepository productSizeRepository;

    //mappers
    CartDetailMapper cartDetailMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    public CartPageResponse get(Pageable pageable) {

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //check cart exist
        Cart cart = cartRepository.findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        List<CartCreateResponse> cartDetails = cartDetailRepository.findAllByCart(cart, pageable)
                .stream().map(cartDetail -> {
                    CartCreateResponse res = cartDetailMapper.toResponse(cartDetail);
                    res.setProductName(cartDetail.getProduct().getProductName());
                    res.setFrontImage(cartDetail.getProduct().getFrontImage());
                    res.setProductPrice(cartDetail.getProduct().getProductPrice());
                    res.setSizeTag(cartDetail.getSize().getSizeTag());
                    return res;
                }).toList();

        long totalElements = cartDetailRepository.count();

        return CartPageResponse.builder()
                .carts(cartDetails)
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(Math.ceilDiv(totalElements, pageable.getPageSize()))
                .totalElements(totalElements)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void add(CartCreateRequest request) {

        //check product exist
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //check size exist
        Size size = sizeRepository.findById(request.getSizeTag().toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));

        //check product & size
        if (!productSizeRepository.existsById(ProductSizeId.builder()
                        .productId(product.getId())
                        .sizeTag(size.getSizeTag())
                        .build())) throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //get cart
        Cart cart = cartRepository.findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        //check cartDetailExist
        CartDetail cartDetail = cartDetailRepository.findByCartAndProductAndSize(cart, product, size);

        if (Objects.nonNull(cartDetail)) {
            cartDetail.setAmount(request.getAmount());
            cartDetailRepository.save(cartDetail);
        } else {
            cartDetailRepository.save(CartDetail.builder()
                    .cart(cart)
                    .product(product)
                    .id(CartDetailId.builder()
                            .cartId(cart.getId())
                            .productId(product.getId())
                            .sizeTag(size.getSizeTag())
                            .build())
                    .size(size)
                    .amount(request.getAmount())
                    .build());
        }

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void delete(String productId, String sizeTag) {

        //check product exist
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //check size exist
        Size size = sizeRepository.findById(sizeTag.toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //get cart
        Cart cart = cartRepository.findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        //check cartDetailExist
        CartDetail cartDetail = cartDetailRepository.findByCartAndProductAndSize(cart, product, size);

        cartDetailRepository.delete(cartDetail);
    }
}
