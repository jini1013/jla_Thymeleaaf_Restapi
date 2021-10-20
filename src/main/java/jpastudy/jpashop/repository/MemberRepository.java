package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // final로 선언된 인자르 받는 constructor 럴 생성 해준다- autowired불필요
public class MemberRepository {
//    @PersistenceContext
    // of @Autowired로 대체 가능
     private final EntityManager em;

    //등록
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Member member) {
        em.persist(member);
    }
    public Member findOne (Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findAll() {
        //TypedQuery
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
    // name으로 Member 하나 또는 여러개 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) //TypedQuery
                .getResultList();
    }
}
