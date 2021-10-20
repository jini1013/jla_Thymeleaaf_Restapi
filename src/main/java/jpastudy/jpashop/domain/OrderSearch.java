package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    //외원이름 으로 검색
    private  String memberName;
    //주문샹태(ORDER, CANCEL)
    private OrderStatus orderStatus;
}
