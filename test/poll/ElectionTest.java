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
	// 投票人权重不同/相同
	// addVotes:
	// 选票中支持的人数大于/不大于k
	// 其他方法:
	// 只针对应用2进行综合测试，保证覆盖重写与重载的方法
	// 其他情况如参数不合法等已经在PollTest中测试
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	//投票人权重不同
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
	//投票人权重相同
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
	// 选票中支持的人数大于k
	public void addVotes() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("张三",19);
		Person p2 = new Person("李四",20);
		Person p3 = new Person("王五",19);
		Person p4 = new Person("赵六",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.0);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"支持"));
		s1.add(new VoteItem<>(p2,"支持"));
		s1.add(new VoteItem<>(p3,"支持"));
		s1.add(new VoteItem<>(p4,"支持"));
		Vote<Person> V1 = new Vote<>(s1);
		Election po = new Election();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
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
	// 综合测试应用2
	public void selectionElection() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("张三",19);
		Person p2 = new Person("李四",20);
		Person p3 = new Person("王五",19);
		Person p4 = new Person("赵六",21);
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
		s1.add(new VoteItem<>(p1,"支持"));
		s1.add(new VoteItem<>(p2,"反对"));
		s1.add(new VoteItem<>(p3,"弃权"));
		s1.add(new VoteItem<>(p4,"反对"));
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"支持"));
		s2.add(new VoteItem<>(p2,"支持"));
		s2.add(new VoteItem<>(p3,"弃权"));
		s2.add(new VoteItem<>(p4,"弃权"));
		Set<VoteItem<Person>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"支持"));
		s3.add(new VoteItem<>(p2,"支持"));
		s3.add(new VoteItem<>(p3,"支持"));
		s3.add(new VoteItem<>(p4,"反对"));
		Set<VoteItem<Person>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"反对"));
		s4.add(new VoteItem<>(p2,"弃权"));
		s4.add(new VoteItem<>(p3,"弃权"));
		s4.add(new VoteItem<>(p4,"支持"));
		Vote<Person> V1 = new Vote<>(s1);
		Vote<Person> V2 = new Vote<>(s2);
		Vote<Person> V3 = new Vote<>(s3);
		Vote<Person> V4 = new Vote<>(s4);
		Election po = new Election();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
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
