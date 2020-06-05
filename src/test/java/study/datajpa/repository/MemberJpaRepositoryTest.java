package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName(value = "테스트")
    void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(member.getId());

        //then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
    }


    @Test
    void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        List<Member> all = memberJpaRepository.findAll();

        Long count = memberJpaRepository.count();

        //then
        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);
        assertEquals(all.size(), 2);
        assertEquals(count, 2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long count2 = memberJpaRepository.count();
        assertEquals(count2,0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);
        //then
        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    void bulkUpdate() throws Exception {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));
        //when
         int resultCount = memberJpaRepository.bulkAgePlus(20);
        //then
        assertEquals(resultCount, 3);
    }

}