package com.sportman.services.imlps;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.RateCreateResponse;
import com.sportman.dto.response.RateResponse;
import com.sportman.dto.response.UserCommentResponse;
import com.sportman.dto.response.page.RatePageResponse;
import com.sportman.entities.Product;
import com.sportman.entities.Rate;
import com.sportman.entities.RateId;
import com.sportman.entities.User;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.RateMapper;
import com.sportman.repositories.ProductRepository;
import com.sportman.repositories.RateRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.services.interfaces.RateService;
import com.sportman.services.specifications.RateSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    //repos
    RateRepository rateRepository;
    ProductRepository productRepository;
    UserRepository userRepository;

    //mappers
    RateMapper rateMapper;

    @Override
    @PreAuthorize("hasAuthority('ADD_COMMENT')")
    @Transactional
    public RateCreateResponse create(RateCreateRequest request) {

        //check product exist
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        //get user id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.warn(product.getId());
        log.warn(user.getId());

        //create
        Rate newRate = rateMapper.toRate(request);
        newRate.setUser(user);
        newRate.setProduct(product);
        newRate.setId(RateId.builder()
                .productId(product.getId())
                .userId(user.getId())
                .build());

        //update product sum rate
        long totalElement = rateRepository.count(Specification.where(RateSpecification.hasProduct(product)));
        double sumRate = rateRepository.findAll(Specification.where(RateSpecification.hasProduct(product))).stream().mapToDouble(Rate::getRateStar).sum();
        product.setSumRate(sumRate / totalElement);
        productRepository.save(product);

        RateCreateResponse res = rateMapper.toCreateResponse(rateRepository.save(newRate));

        res.setUser(UserCommentResponse.builder()
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .build());

        return res;
    }


    @Override
    public RatePageResponse getByProductId(Pageable pageable, String productId, int rateNum) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Specification specs = Specification.where(RateSpecification.hasProduct(product)).and(RateSpecification.hasRate(rateNum));

        long totalElement = rateRepository.count(Specification.where(RateSpecification.hasProduct(product)));

        Page<Rate> ratePage = rateRepository.findAll(specs, pageable);
        List<RateResponse> rates = ratePage.map(rate -> {
         RateResponse rateResponse = rateMapper.toResponse(rate);
            rateResponse.setUser(UserCommentResponse
                    .builder()
                    .username(rate.getUser().getUsername())
                    .fullName(rate.getUser().getFirstName() +
                            (rate.getUser().getLastName()))
                    .build());
            return rateResponse;
        }).toList();

        return RatePageResponse.builder()
                .rates(rates)
                .sumRate(product.getSumRate())
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(Math.ceilDiv(totalElement, pageable.getPageSize()))
                .totalElements(totalElement)
                .build();
    }
}
