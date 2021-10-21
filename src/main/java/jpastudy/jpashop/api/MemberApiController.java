package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    @PostMapping("/api/v1/members")
    // @Valid : Member에 Notempty등 check annotation 검사
    // Member member: eneity객체를 입력으로 사용하는 것은 적절하지 않다
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    // static inner class
    // 응답이나 요청에 사용할 DTO
    // entity class 에는 의존 객체가 찍히지 않도록 ToString을 절대 사용하년 안된다.
    //  @Data : setter, getter, rewuredArgumentConstrer 가 포함된  어너테이션
    @Data
    static class CreateMemberResponse {
        private final Long id;
//        public CreateMemberResponse(Long id) {
//            this.id = id;
//        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid
                                  CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

/**
 * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다
 */
@GetMapping("apt/v2/members")
public List<MemberDto> membersV2() {
    List<Member> findMembers = memberService.findMembers();
    List<MemberDto> memberDtoList = findMembers.stream()
            .map(member -> new MemberDto(member.getName()))
            .collect(Collectors.toList());
    return memberDtoList;
}

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다
     */
    @GetMapping("api/v2.1/members")
    public Result membersV2_1() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> memberDtoList = findMembers.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());
        return new Result(memberDtoList.size(), memberDtoList);
    }
    /**
     * 수정 API
     */
    @PatchMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               // 입력 출역에 관겨된 항목반 DTO로 만들어 사용한다.
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        // 객체를 return 햐면 spring이 Jackon을 이용 하여 JSON type으로 전송
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    @Data
    static class UpdateMemberRequest {
        // 받고 싶은 입력 항목반 지정한다.
        // Member type으로 보내면 전체가 다 전송 된다.
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


    @Data
    @AllArgsConstructor
    class Result<T> {
        private int count;
        private T data;
    }
    @Data
    @AllArgsConstructor
    class MemberDto {
        private String name;
    }
}
