package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {
    @Autowired

    EntityManager em;
    // test 끝난 후 rollback 한다.
    // Rollback방지 (value=false)
    @Test
    @Rollback(value = false)
    public void entity() throws Exception {
        Member member =  new Member();
        member.setName("몽타");
        Address address =  new Address("서울","동작","12345");
        //Member에 Address를 저장
        member.setAddress(address);
        // 영속성 컨텍스트에 저장
        em.persist(member);

        Order order =  new Order();
        //order와 member연결
        order.setMember(member);

        // Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        order.setDelivery(delivery);
        // cascade type 이 ALL 임으로 order를 저장 하면 delivery도 저장된다.
        // em.persist(delivery);를 하지 않아도 된다.
        // em.persist(delivery);

        // Item - Book 생성
        Book book = new Book();
        book.setName("부트책");
        book.setPrice(1000);
        book.setStockQuantity(10);
        book.setAuthor("참가자");
        book.setIsbn("111-11-11");
        // 영속성 컨텍스트 저장
        em.persist(book);

        // OrderItem생성
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);
        orderItem.setOrderPrice(20000);
        orderItem.setItem(book);
        //life cycle이
        order.addOrderItem(orderItem);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);


        em.persist(order);
    }
}