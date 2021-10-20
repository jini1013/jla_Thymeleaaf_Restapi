package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;


//-- 양방향 연관관계의 주인(Owner)
// 객체의 두 관계 중 하나를 연관관계의 주인으로 지정해야 한다.
// 연관관계의 주인만이 외래 키를 관리(등록, 수정)
// 주인이 아닌쪽은 읽기만 가능하고, mappedBy 속성으로 주인을 지정한다.
// 외래 키(Foreign Key)가 있는 있는 곳을 주인으로 정해라
// Many인 Order table를 양방향 관계의 주인으로 하고 foreign key를 지정한다.

// FetchType.LAZY: order에서 getMember호출시에 가져온다.
// FetchType.EAGER: order를 조회하면 member를 무조건 당겨온다.
// data 건수가 많을 경우 성능에 문제 ManyToOne일 경우 LAZY로 한다.
// EAGER 가 default 임으로 LAZY로 변경한다.

    // Member N:1관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //  주문회원

    // delivery 1:1 관계
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  //
    @JoinColumn(name = "delivery_id") // forean key 와 mapping
    private Delivery delivery;

    // OrderItem 관의 관계 1:N 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문날짜
    private LocalDateTime orderDate;

    // 주문상테
    @Enumerated(EnumType.STRING)
    private  OrderStatus status;

    //  연관관계 - member에서도 order를 참조 가능 N:1
    public void setMember(Member member) {
        this.member =  member;
        member.getOrders().add(this);
    }

    // Order와 Delivery 1:1
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this); // delivery와도 양방향 관계임으로 delivery에도 넣어준다, this==order
    }

    // Order와 OrderItem관계 1:N
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //== 비즈니스 로직 : 주문생성 메서드==//
    public static Order createOrder (Member member, Delivery delivery,OrderItem... orderItems) {
        Order order = new Order();
        // Order(주문)의 Member(회월) 연결
        order.setMember(member);
        order.setDelivery(delivery);
        // Order
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 주문상태
        order.setStatus(OrderStatus.ORDER);
        // 주문날짜
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        // Delivery(배송) 상태가 완료되었다면 주문취소가 처리되지 않아야 한다
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        //Order(주문) 상태를 취소로 변경
        this.setStatus(OrderStatus.CANCEL);
        // Order(주문)이 취소되면 재고수량 중가
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //==비즈니스 로직 : 전체 주문 가격 조회 ==//
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
