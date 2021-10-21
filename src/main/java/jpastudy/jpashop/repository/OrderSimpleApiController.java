package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order, Order -> Member , Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        all.forEach(order -> {
            order.getMember().getName(); // Lazy 강제초기화
            order.getDelivery().getAddress(); // Delivery 강제초기화
        });
        return all;
    }
    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용함)
     * fetch join으로 쿼리 1번 호출
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = new ArrayList<>();
        for (Order o : orders) {
            SimpleOrderDto simpleOrderDto = new SimpleOrderDto(o);
            result.add(simpleOrderDto);
        }
        return result;
    }

    /**
     * v2: 엩티티를 DTO로 변환
     * 문제점: 지연로딩으로 쿼리 11qjs ghcnf
     * N + 1 문제
     * Order2건  Member 3건 Delivery 2건 쿼리 실행됨
     * Order1, member 2, delivery 2
     * --> fetch join으로 해결
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream() // Stream<Order>
                .map(order -> new SimpleOrderDto(order)) // Stream<SimpleOrderDto>
                .collect(Collectors.toList()); // List<SimpleOrderDto>
    }


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 강제 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
    }
}