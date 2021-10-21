package jpastudy.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

//    @NotEmpty 검증을 위한 annotation을 사용하지 않아야함
    private String name;
    @Embedded
    private Address address;

    // list 생성시 ArrayList를 미리 생성해주면 null  check에 유리하다
    // 상대방이 1 임으로 @OneToMany 사용 -> order.jaja에는 @ManyToOne
    // mappedBy사용하여 OneToMany의 foreign key를 지정해준다
    //
    // order의 양방향 참조 방지 - JSON을 무시해랴

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
