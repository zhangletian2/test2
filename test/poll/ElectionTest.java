package poll;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Person;
import auxiliary.Voter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pattern.ElectionSelect;
import pattern.ElectionStat;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

public class ElectionTest {
	
	// test strategy
	// addVoters:
	// ͶƱ��Ȩ�ز�ͬ/��ͬ
	// addVotes:
	// ѡƱ��֧�ֵ���������/������k
	// ��������:
	// ֻ���Ӧ��2�����ۺϲ��ԣ���֤������д�����صķ���
	// ���������������Ϸ����Ѿ���PollTest�в���
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	//ͶƱ��Ȩ�ز�ͬ
	public void addVoters() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.2);
		m.put(v2,0.2);
		Election po = new Election();
		po.addVoters(m);
	}
	@Test
	//ͶƱ��Ȩ����ͬ
	public void addVoters1() {
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.6);
		m.put(v2,0.6);
		m.put(v3,0.6);
		Election po = new Election();
		po.addVoters(m);
		assertEquals(m,po.getVoters());
	}
	@Test
	// ѡƱ��֧�ֵ���������k
	public void addVotes() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("����",19);
		Person p2 = new Person("����",20);
		Person p3 = new Person("����",19);
		Person p4 = new Person("����",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.0);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"֧��"));
		s1.add(new VoteItem<>(p2,"֧��"));
		s1.add(new VoteItem<>(p3,"֧��"));
		s1.add(new VoteItem<>(p4,"֧��"));
		Vote<Person> V1 = new Vote<>(s1);
		Election po = new Election();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.statistics(new ElectionStat<>());
		assertEquals(0.0,po.getStatistics().get(p1),1e-6);
		assertEquals(0.0,po.getStatistics().get(p2),1e-6);
		assertEquals(0.0,po.getStatistics().get(p3),1e-6);
		assertEquals(0.0,po.getStatistics().get(p4),1e-6);
	}
	@Test
	// �ۺϲ���Ӧ��2
	public void selectionElection() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("����",19);
		Person p2 = new Person("����",20);
		Person p3 = new Person("����",19);
		Person p4 = new Person("����",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.0);
		m.put(v2,1.0);
		m.put(v3,1.0);
		m.put(v4,1.0);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"֧��"));
		s1.add(new VoteItem<>(p2,"����"));
		s1.add(new VoteItem<>(p3,"��Ȩ"));
		s1.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"֧��"));
		s2.add(new VoteItem<>(p2,"֧��"));
		s2.add(new VoteItem<>(p3,"��Ȩ"));
		s2.add(new VoteItem<>(p4,"��Ȩ"));
		Set<VoteItem<Person>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"֧��"));
		s3.add(new VoteItem<>(p2,"֧��"));
		s3.add(new VoteItem<>(p3,"֧��"));
		s3.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"����"));
		s4.add(new VoteItem<>(p2,"��Ȩ"));
		s4.add(new VoteItem<>(p3,"��Ȩ"));
		s4.add(new VoteItem<>(p4,"֧��"));
		Vote<Person> V1 = new Vote<>(s1);
		Vote<Person> V2 = new Vote<>(s2);
		Vote<Person> V3 = new Vote<>(s3);
		Vote<Person> V4 = new Vote<>(s4);
		Election po = new Election();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new ElectionStat<>());
		po.selection(new ElectionSelect<>());
		Map<Person,Double> ref = new HashMap<>();
		ref.put(p1,3.0);
		ref.put(p2,2.0);
		assertEquals(ref.keySet(),po.getResults().keySet());
		assertEquals(ref.get(p1),po.getResults().get(p1),1e-6);
		assertEquals(ref.get(p2),po.getResults().get(p2),1e-6);
		System.out.println(po.result());
	}
}
