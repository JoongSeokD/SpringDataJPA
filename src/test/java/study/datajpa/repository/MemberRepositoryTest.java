package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName(value = "멤버테스트")
    void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        //then
        assertEquals(member.getId(), findMember.getId());
        assertEquals(member.getUsername(), findMember.getUsername());
        assertEquals(member, findMember);
    }

    @Test
    void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        List<Member> all = memberRepository.findAll();

        Long count = memberRepository.count();

        //then
        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);
        assertEquals(all.size(), 2);
        assertEquals(count, 2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long count2 = memberRepository.count();
        assertEquals(count2,0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        //then
        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);

    }


    @Test
    void testQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertEquals(result.get(0), m1);
        assertEquals(result.get(0).getUsername(), "AAA");
    }

    @Test
    void findUserNameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

    }

    @Test
    void findMemberDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        m1.changeTeam(team);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findUserNames() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }

    @Test
    void paging() throws Exception {
        //given
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 10);
        Member m3 = new Member("member3", 10);
        Member m4 = new Member("member4", 10);
        Member m5 = new Member("member5", 10);
        Member m6 = new Member("member6", 10);
        Member m7 = new Member("member7", 10);
        Member m8 = new Member("member8", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);
        memberRepository.save(m8);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.DEFAULT_DIRECTION.DESC, "username"));

        int age = 10;

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> memberDtos = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> pageContent = page.getContent();
        long totalElements = page.getTotalElements();
        for (Member member : pageContent) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertEquals(pageContent.size(), 3);
        assertEquals(totalElements, 8);
        assertEquals(page.getNumber(), 0);
        assertEquals(page.getTotalPages(), 3);
        assertEquals(page.isFirst(), true);
        assertEquals(page.hasNext(), true);

    }
    @Test
    void slice() throws Exception {
        //given
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 10);
        Member m3 = new Member("member3", 10);
        Member m4 = new Member("member4", 10);
        Member m5 = new Member("member5", 10);
        Member m6 = new Member("member6", 10);
        Member m7 = new Member("member7", 10);
        Member m8 = new Member("member8", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);
        memberRepository.save(m8);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.DEFAULT_DIRECTION.DESC, "username"));

        int age = 10;

        //when
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

        //then
        List<Member> pageContent = slice.getContent();
        for (Member member : pageContent) {
            System.out.println("member = " + member);
        }

        assertEquals(pageContent.size(), 3);
        assertEquals(slice.getNumber(), 0);
        assertEquals(slice.isFirst(), true);
        assertEquals(slice.hasNext(), true);

    }

    @Test
    void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        //when
        int resultCount = memberRepository.bulkAgePlus(20);
//        em.clear();

        // 벌크 연산시 주의 점 (벌크 연산은 영속성 컨텍스트관리와 상관업이 그냥 DB에 쿼리를 날리기 때문에) 
        // 영속성 컨텍스트가 관리하는 엔티티들은 아직 업데이트가 되어있지 않음
        // 그래서 영속성 컨텍스트를 초기화 시켜야함
        // 1. em.clear()
        // 2. @Modifying(clearAutomatically = true)
        List<Member> result = memberRepository.findByNames(Arrays.asList("member5"));
        Member member5 = result.get(0);
        System.out.println("member5.getAge() = " + member5.getAge());
        //then
        assertEquals(resultCount, 3);
    }

    @Test
    void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }


        //then
    }


}