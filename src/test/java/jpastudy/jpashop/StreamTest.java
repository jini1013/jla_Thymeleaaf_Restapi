package jpastudy.jpashop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
    @Test
    public void stream() {
        List<User> users = List.of(new User("길동", 10),new User("몽타", 20),new User("부트", 20));
        // User의 Name 추출해서 List <String>으로 변환해서 출력해라
        List<String> nameList =
                users.stream() // List의 user가 Stream<user>로 바뀜
                     .map(user -> user.getName()) // Stream<String>: 이름만 추출
                        //.map(User::getName()) -위와 동일
                     .collect(Collectors.toList()); // string을 담은 List를 반환한다.
        nameList.forEach(name -> System.out.println(name));
//        nameList.forEach(System.out::println);

        // 20살 이상의 User 추출 후 List<String>으로 변환해서 출력
        System.out.println("----------------> 20살 이상");
        users.stream()
                .filter(user->user.getAge() >=20)
                .forEach(user -> System.out.println(user.getName()));

        List<String> names = users.stream() // users를 stream으료변환
                .filter(user-> user.getAge() >=20) //stream<User>
                .map(user->user.getName())          // Stream<String>
                .collect(Collectors.toList());      // LIst<String>
        names.forEach(System.out::println); // 매개변수와 출력 내영이 같은 경우 사용

        int sum = users.stream()  //Stream<User>
                .mapToInt(user-> user.getAge()) // user가 가진 getAge추출 IntStream
                .sum();
        System.out.println("나이합계 "+sum);
    }
    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        public int age;

    }
}
