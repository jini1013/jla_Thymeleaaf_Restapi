package jpastudy.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //  기본생성자는 사용하지 말고 파라미터 있는 constructor 를 사용해라
    // lombok의 @NoArgsConstructor(access = AccessLevel.PROTECTED)사용도 가능
//    protected Address() {
//
//    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
