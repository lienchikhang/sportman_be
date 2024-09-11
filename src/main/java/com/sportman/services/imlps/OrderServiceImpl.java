package com.sportman.services.imlps;

import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.OrderResponse;
import com.sportman.dto.response.page.OrderPageResponse;
import com.sportman.entities.Order;
import com.sportman.entities.OrderDetail;
import com.sportman.entities.OrderDetailId;
import com.sportman.entities.User;
import com.sportman.enums.OrderStatus;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.OrderMapper;
import com.sportman.repositories.OrderRepository;
import com.sportman.repositories.ProductRepository;
import com.sportman.repositories.SizeRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.services.interfaces.OrderService;
import com.sportman.services.specifications.OrderSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    //repos
    OrderRepository orderRepository;
    UserRepository userRepository;
    SizeRepository sizeRepository;
    ProductRepository productRepository;

    //mapper
    OrderMapper orderMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderPageResponse getAllByUser(Pageable pageable, String sort) {

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //get orders
        List<OrderResponse> orders = orderRepository.findAllByUser(pageable, user)
                .stream().map(order -> orderMapper.toResponse(order)).toList();

        //calc total
        long totalOrder = orderRepository.count();

        return OrderPageResponse.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .orders(orders)
                .totalElements(totalOrder)
                .totalPage(Math.ceilDiv(totalOrder, pageable.getPageSize()))
                .build();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderResponse create(OrderCreateRequest request) {

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //create list orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();

        request.getOrders().forEach(order -> {
            //create orderDetails
            OrderDetail orderDetail = OrderDetail.builder()
                    .size(sizeRepository.findById(order.getSizeTag()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND)))
                    .product(productRepository.findById(order.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                    .amount(order.getAmount())
                    .build();
            orderDetails.add(orderDetail);
        });

        //create new order
        Order newOrder = Order.builder().build();
        newOrder.setStatus(OrderStatus.UNPAID);
        newOrder.setOrderDetails(orderDetails);

        newOrder.getOrderDetails().forEach(orderDetail -> {
            orderDetail.setOrder(newOrder);
            orderDetail.setId(OrderDetailId.builder()
                    .orderId(newOrder.getId())
                    .productId(orderDetail.getProduct().getId())
                    .sizeTag(orderDetail.getSize().getSizeTag())
                    .build());
        });

        return orderMapper.toResponse(orderRepository.save(newOrder));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderResponse cancel(String orderId) {

        //get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //get order
        Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        //cancel
        order.setStatus(OrderStatus.CANCEL);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public OrderPageResponse getAll(Pageable pageable, String sort, OrderStatus status) {

        //get orders
        List<OrderResponse> orders = orderRepository.findAll(Specification
                        .where(OrderSpecification.hasSort(sort))
                        .and(OrderSpecification.hasStatus(status)), pageable)
                .stream().map(order -> orderMapper.toResponse((Order) order)).toList();

        //calc total
        long totalOrder = orderRepository.count();

        return OrderPageResponse.builder()
                .currentPage(pageable.getPageNumber() + 1)
                .orders(orders)
                .totalElements(totalOrder)
                .totalPage(Math.ceilDiv(totalOrder, pageable.getPageSize()))
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') || hasRole('DELIVERY')")
    public OrderResponse confirmPaid(String orderId) {

        //get order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        //cancel
        order.setStatus(OrderStatus.PAID);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') || hasRole('DELIVERY')")
    public OrderResponse confirmDelivered(String orderId) {
        //get order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        //cancel
        order.setStatus(OrderStatus.RECEIVED);

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
