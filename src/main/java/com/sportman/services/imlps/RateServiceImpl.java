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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

        RateCreateResponse res = rateMapper.toCreateResponse(rateRepository.save(newRate));

        res.setUser(UserCommentResponse.builder()
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .build());

        return res;
    }

    @Override
    public RatePageResponse get(Pageable pageable) {

        long totalElement = rateRepository.count();
        List<RateResponse> rates =  rateRepository.findAll(pageable).map(rate -> {
            RateResponse rateResponse = rateMapper.toResponse(rate);
            rateResponse.setUser(UserCommentResponse
                    .builder()
                    .username(rate.getUser().getUsername())
                            .fullName(rate.getUser().getFirstName() +
                                    rate.getUser().getLastName())
                    .build());

            return rateResponse;
        }).toList();


        return RatePageResponse.builder()
                .rates(rates)
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(Math.ceilDiv(totalElement, pageable.getPageSize()))
                .totalElements(totalElement)
                .build();

    }

    @Override
    public RatePageResponse getByProductId(Pageable pageable, String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        long totalElement = rateRepository.countAllByProduct(product);

        List<RateResponse> rates =  rateRepository.findAllByProduct(pageable, product).map(rate -> {
            RateResponse rateResponse = rateMapper.toResponse(rate);
            rateResponse.setUser(UserCommentResponse
                    .builder()
                    .username(rate.getUser().getUsername())
                    .fullName(rate.getUser().getFirstName() +
                            rate.getUser().getLastName())
                    .build());

            return rateResponse;
        }).toList();

        return RatePageResponse.builder()
                .rates(rates)
                .currentPage(pageable.getPageNumber() + 1)
                .totalPage(Math.ceilDiv(totalElement, pageable.getPageSize()))
                .totalElements(totalElement)
                .build();
    }
}
