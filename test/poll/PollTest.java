package poll;

import auxiliary.Dish;
import auxiliary.Person;
import auxiliary.Proposal;
import auxiliary.Voter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pattern.*;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;


import java.util.*;

import static org.junit.Assert.*;

public class PollTest {
	
	// test strategy
	// setInfo:
	// quantity非负/是负数
	// 比较实际与输入是/否一致
	// addCandidates:
	// 候选对象不少于1个
	// 候选对象不存在/已存在、
	// 拟选出的人数大于候选人数时是/否会抛出预期异常
	// addVoters:
	// 投票人参数合法（权重唯一，权重非负）、
	// 不合法（权重为负）
	// addVote:
	// 投票人是/否在voters名单里
	// 投票人已投过票/未投过票
	// 投票人是/否选择非法选项
	// 选票是/否包含所有候选人
	// 选票是/否包含不在候选名单里的候选人
	// 选票是/否对同一名候选人重复投票
	// statistics:
	// 一个投票人提交多次选票
	// 尚有投票人未投票
	// 分别测试针对三种应用的情况
	// selection:按照指定遴选方式是/否选出正确结果
	// result:输出字符串是/否与预期相同
	// accept:全是/不是非法选票、既有非法又有合法
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	//quantity是负数
	public void setInfo0() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		Calendar d = Calendar.getInstance();
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",d,vt,-1);
		assertEquals("投票",po.getName());
		assertEquals(d,po.getDate());
		assertEquals(vt,po.getVoteType());
		assertEquals(2,po.getQuantity());
	}
	@Test
	//正常
	public void setInfo() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		Calendar d = Calendar.getInstance();
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",d,vt,2);
		assertEquals("投票",po.getName());
		assertEquals(d,po.getDate());
		assertEquals(vt,po.getVoteType());
		assertEquals(2,po.getQuantity());
	}
	@Test
	//候选对象少于1个
	public void addCandidates0() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		List<String> l = new ArrayList<>();
		Poll<String> po = Poll.create();
		po.addCandidates(l);
	}
	@Test
	//候选人重名
	public void addCandidates() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		List<String> l = new ArrayList<>();
		l.add("张三");
		l.add("李四");
		l.add("张三");
		Poll<String> po = Poll.create();
		po.addCandidates(l);
	}
	@Test
	//预选人数大于候选人数
	public void addCandidates1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("张三");
		l.add("李四");
		l.add("王五");
		Poll<String> po = Poll.create();
		po.setInfo("投票", Calendar.getInstance(),vt,4);
		po.addCandidates(l);
	}
	@Test
	//正常
	public void addCandidates2() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("张三");
		l.add("李四");
		l.add("王五");
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票", Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		assertEquals(l,po.getCandidates());
	}
	@Test
	//投票人权重为负
	public void addVoters() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.2);
		m.put(v2,-0.2);
		Poll<String> po = Poll.create();
		po.addVoters(m);
	}
	@Test
	//正常
	public void addVoters2() {
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.6);
		m.put(v2,0.2);
		m.put(v3,0.2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.addVoters(m);
		assertEquals(m,po.getVoters());
	}
	@Test
	// 投票人不在voters名单里
	public void addVote() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("张三");
		l.add("李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.0);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","反对");
		VoteItem<String> vi4 = new VoteItem<>("李四","支持");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		Poll<String> po = Poll.create();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
	}
	@Test
	// 投票人选择非法选项
	public void addVote1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		// 非法选项
		VoteItem<String> vi4 = new VoteItem<>("李四","不支持");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(0.0,po.getStatistics().get("李四"),1e-6);
	}
	@Test
	// 选票没有包含所有候选人
	public void addVote2() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(0.0,po.getStatistics().get("李四"),1e-6);
	}
	@Test
	// 选票包含不在候选名单里的候选人
	public void addVote3() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		VoteItem<String> vi4 = new VoteItem<>("王五","弃权");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(0.0,po.getStatistics().get("李四"),1e-6);
	}

	@Test
	// 对同一名候选人重复投票
	public void addVote4() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		VoteItem<String> vi4 = new VoteItem<>("张三","弃权");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(0.0,po.getStatistics().get("李四"),1e-6);
	}
	@Test
	// 正常情况
	public void addVotesNormal() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		VoteItem<String> vi4 = new VoteItem<>("李四","支持");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(1.0,po.getStatistics().get("李四"),1e-6);
	}
	@Test
	// 一个投票人提交多次选票
	public void statistics() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		VoteItem<String> vi3 = new VoteItem<>("张三","弃权");
		VoteItem<String> vi4 = new VoteItem<>("李四","弃权");
		VoteItem<String> vi5 = new VoteItem<>("张三","支持");
		VoteItem<String> vi6 = new VoteItem<>("李四","反对");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Set<VoteItem<String>> s3 = new HashSet<>();
		s3.add(vi5);
		s3.add(vi6);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		Vote<String> V3 = new Vote<>(s3);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		// 投票人v2重复投票
		po.addVote(V3,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("张三"),1e-6);
		assertEquals(0.0,po.getStatistics().get("李四"),1e-6);
	}
	@Test
	// 尚有投票人未投票
	public void statistics1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"张三");
		l.add(1,"李四");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("张三","支持");
		VoteItem<String> vi2 = new VoteItem<>("李四","反对");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Vote<String> V1 = new Vote<>(s1);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.statistics(new ElectionStat<>());
		assertNull(po.getStatistics().get("张三"));
		assertNull(po.getStatistics().get("李四"));
	}
	@Test
	// 测试应用1
	public void statisticsBusiness() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("提案",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"支持");
		VoteItem<Proposal> vi2 = new VoteItem<>(p,"反对");
		VoteItem<Proposal> vi3 = new VoteItem<>(p,"弃权");
		VoteItem<Proposal> vi4 = new VoteItem<>(p,"支持");
		Set<VoteItem<Proposal>> s1 = new HashSet<>();
		s1.add(vi1);
		Set<VoteItem<Proposal>> s2 = new HashSet<>();
		s2.add(vi2);
		Set<VoteItem<Proposal>> s3 = new HashSet<>();
		s3.add(vi3);
		Set<VoteItem<Proposal>> s4 = new HashSet<>();
		s4.add(vi4);
		Vote<Proposal> V1 = new Vote<>(s1);
		Vote<Proposal> V2 = new Vote<>(s2);
		Vote<Proposal> V3 = new Vote<>(s3);
		Vote<Proposal> V4 = new Vote<>(s4);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new BusinessStat<>());
		assertEquals(0.5,po.getStatistics().get(p),1e-6);
	}
	@Test
	// 测试应用2
	public void statisticsElection() {
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
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
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
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new ElectionStat<>());
		assertEquals(3.0,po.getStatistics().get(p1),1e-6);
		assertEquals(2.0,po.getStatistics().get(p2),1e-6);
		assertEquals(1.0,po.getStatistics().get(p3),1e-6);
		assertEquals(1.0,po.getStatistics().get(p4),1e-6);
	}
	@Test
	// 测试应用3
	public void statisticsDinner() {
		Map<String,Integer> map = new HashMap<>();
		map.put("喜欢",2);
		map.put("无所谓",1);
		map.put("不喜欢",0);
		VoteType vt = new VoteType(map);
		List<Dish> l = new ArrayList<>();
		Dish p1 = new Dish("锅包肉",25.0);
		Dish p2 = new Dish("溜肉段",20.0);
		Dish p3 = new Dish("白灼菜心",10.0);
		Dish p4 = new Dish("杀猪菜",17.0);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.4);
		m.put(v2,0.3);
		m.put(v3,0.2);
		m.put(v4,0.1);
		Set<VoteItem<Dish>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"无所谓"));
		s1.add(new VoteItem<>(p2,"无所谓"));
		s1.add(new VoteItem<>(p3,"喜欢"));
		s1.add(new VoteItem<>(p4,"不喜欢"));
		Set<VoteItem<Dish>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"喜欢"));
		s2.add(new VoteItem<>(p2,"不喜欢"));
		s2.add(new VoteItem<>(p3,"不喜欢"));
		s2.add(new VoteItem<>(p4,"喜欢"));
		Set<VoteItem<Dish>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"不喜欢"));
		s3.add(new VoteItem<>(p2,"无所谓"));
		s3.add(new VoteItem<>(p3,"无所谓"));
		s3.add(new VoteItem<>(p4,"不喜欢"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"喜欢"));
		s4.add(new VoteItem<>(p2,"喜欢"));
		s4.add(new VoteItem<>(p3,"无所谓"));
		s4.add(new VoteItem<>(p4,"不喜欢"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new DinnerStat<>());
		assertEquals(1.2,po.getStatistics().get(p1),1e-6);
		assertEquals(0.8,po.getStatistics().get(p2),1e-6);
		assertEquals(1.1,po.getStatistics().get(p3),1e-6);
		assertEquals(0.6,po.getStatistics().get(p4),1e-6);
	}
	@Test
	// 测试应用1
	public void selectionBusiness() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("提案",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"支持");
		VoteItem<Proposal> vi2 = new VoteItem<>(p,"支持");
		VoteItem<Proposal> vi3 = new VoteItem<>(p,"弃权");
		VoteItem<Proposal> vi4 = new VoteItem<>(p,"支持");
		Set<VoteItem<Proposal>> s1 = new HashSet<>();
		s1.add(vi1);
		Set<VoteItem<Proposal>> s2 = new HashSet<>();
		s2.add(vi2);
		Set<VoteItem<Proposal>> s3 = new HashSet<>();
		s3.add(vi3);
		Set<VoteItem<Proposal>> s4 = new HashSet<>();
		s4.add(vi4);
		Vote<Proposal> V1 = new Vote<>(s1);
		Vote<Proposal> V2 = new Vote<>(s2);
		Vote<Proposal> V3 = new Vote<>(s3);
		Vote<Proposal> V4 = new Vote<>(s4);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new BusinessStat<>());
		po.selection(new BusinessSelect<>());
		Map<Proposal,Double> ref = new HashMap<>();
		ref.put(p,0.7);
		assertEquals(ref.keySet(),po.getResults().keySet());
		assertEquals(ref.get(p),po.getResults().get(p),1e-6);
	}
	@Test
	// 测试应用2
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
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
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
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
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
	}
	@Test
	// 测试应用3
	public void selectionDinner() {
		Map<String,Integer> map = new HashMap<>();
		map.put("喜欢",2);
		map.put("无所谓",1);
		map.put("不喜欢",0);
		VoteType vt = new VoteType(map);
		List<Dish> l = new ArrayList<>();
		Dish p1 = new Dish("锅包肉",25.0);
		Dish p2 = new Dish("溜肉段",20.0);
		Dish p3 = new Dish("白灼菜心",10.0);
		Dish p4 = new Dish("杀猪菜",17.0);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.4);
		m.put(v2,0.3);
		m.put(v3,0.2);
		m.put(v4,0.1);
		Set<VoteItem<Dish>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"无所谓"));
		s1.add(new VoteItem<>(p2,"无所谓"));
		s1.add(new VoteItem<>(p3,"喜欢"));
		s1.add(new VoteItem<>(p4,"不喜欢"));
		Set<VoteItem<Dish>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"喜欢"));
		s2.add(new VoteItem<>(p2,"不喜欢"));
		s2.add(new VoteItem<>(p3,"不喜欢"));
		s2.add(new VoteItem<>(p4,"喜欢"));
		Set<VoteItem<Dish>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"不喜欢"));
		s3.add(new VoteItem<>(p2,"无所谓"));
		s3.add(new VoteItem<>(p3,"无所谓"));
		s3.add(new VoteItem<>(p4,"无所谓"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"喜欢"));
		s4.add(new VoteItem<>(p2,"喜欢"));
		s4.add(new VoteItem<>(p3,"无所谓"));
		s4.add(new VoteItem<>(p4,"不喜欢"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new DinnerStat<>());
		po.selection(new DinnerSelect<>());
		assertEquals(3,po.getResults().size());
		assertTrue(po.getResults().containsKey(p1));
		assertTrue(po.getResults().containsKey(p2));
		assertTrue(po.getResults().containsKey(p2)||po.getResults().containsKey(p4));
	}
	// 基于应用3测试result方法
	@Test
	public void result() {
		Map<String,Integer> map = new HashMap<>();
		map.put("喜欢",2);
		map.put("无所谓",1);
		map.put("不喜欢",0);
		VoteType vt = new VoteType(map);
		List<Dish> l = new ArrayList<>();
		Dish p1 = new Dish("锅包肉",25.0);
		Dish p2 = new Dish("溜肉段",20.0);
		Dish p3 = new Dish("白灼菜心",10.0);
		Dish p4 = new Dish("杀猪菜",17.0);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.4);
		m.put(v2,0.3);
		m.put(v3,0.2);
		m.put(v4,0.1);
		Set<VoteItem<Dish>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"无所谓"));
		s1.add(new VoteItem<>(p2,"无所谓"));
		s1.add(new VoteItem<>(p3,"喜欢"));
		s1.add(new VoteItem<>(p4,"不喜欢"));
		Set<VoteItem<Dish>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"喜欢"));
		s2.add(new VoteItem<>(p2,"不喜欢"));
		s2.add(new VoteItem<>(p3,"不喜欢"));
		s2.add(new VoteItem<>(p4,"喜欢"));
		Set<VoteItem<Dish>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"不喜欢"));
		s3.add(new VoteItem<>(p2,"无所谓"));
		s3.add(new VoteItem<>(p3,"无所谓"));
		s3.add(new VoteItem<>(p4,"无所谓"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"喜欢"));
		s4.add(new VoteItem<>(p2,"喜欢"));
		s4.add(new VoteItem<>(p3,"无所谓"));
		s4.add(new VoteItem<>(p4,"不喜欢"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new DinnerStat<>());
		po.selection(new DinnerSelect<>());
		StringBuilder sb = new StringBuilder();
		sb.append("候选对象ID");
		sb.append('\t');
		sb.append("排序");
		sb.append('\n');
		for(int i=0;i<=1;i++){
			sb.append(i*2);
			sb.append('\t');
			sb.append('\t');
			sb.append('\t');
			sb.append(i+1);
			sb.append('\n');
		}
		sb.append(1);
		sb.append('\t');
		sb.append('\t');
		sb.append('\t');
		sb.append(3);
		sb.append('\n');
		StringBuilder sb1 = new StringBuilder();
		sb1.append("候选对象ID");
		sb1.append('\t');
		sb1.append("排序");
		sb1.append('\n');
		for(int i=0;i<=1;i++){
			sb1.append(i*2);
			sb1.append('\t');
			sb1.append('\t');
			sb1.append('\t');
			sb1.append(i+1);
			sb1.append('\n');
		}
		sb1.append(3);
		sb1.append('\t');
		sb1.append('\t');
		sb1.append('\t');
		sb1.append(3);
		sb1.append('\n');
		System.out.println(po.toString());
		assertTrue(sb.toString().equals(po.result())||sb1.toString().equals(po.result()));
		System.out.println(po.result());
	}
	@Test
	// 利用应用1测试accept方法，选票全是非法选票
	public void testAccept1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("提案",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"support");
		VoteItem<Proposal> vi2 = new VoteItem<>(new Proposal("新提案",Calendar.getInstance()),"支持");
		VoteItem<Proposal> vi3 = new VoteItem<>(p,"like");
		VoteItem<Proposal> vi4 = new VoteItem<>(p,"支持");
		VoteItem<Proposal> vi5 = new VoteItem<>(p,"支持");
		Set<VoteItem<Proposal>> s1 = new HashSet<>();
		s1.add(vi1);
		Set<VoteItem<Proposal>> s2 = new HashSet<>();
		s2.add(vi2);
		Set<VoteItem<Proposal>> s3 = new HashSet<>();
		s3.add(vi3);
		Set<VoteItem<Proposal>> s4 = new HashSet<>();
		s4.add(vi4);
		Set<VoteItem<Proposal>> s5 = new HashSet<>();
		s5.add(vi5);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(new Vote<>(s1),v1);
		po.addVote(new Vote<>(s2),v2);
		po.addVote(new Vote<>(s3),v3);
		po.addVote(new Vote<>(s4),v4);
		po.addVote(new Vote<>(s5),v4);
		assertEquals(0.0,po.accept(new LegalRatioVisitor<Proposal>()),1e-6);
	}
	@Test
	// 利用应用2测试accept方法，选票全是合法选票
	public void testAccept2() {
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
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
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
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(new Vote<>(s1),v1);
		po.addVote(new Vote<>(s2),v2);
		po.addVote(new Vote<>(s3),v3);
		po.addVote(new Vote<>(s4),v4);
		assertEquals(1.0,po.accept(new LegalRatioVisitor<Person>()),1e-6);
	}
	@Test
	// 利用应用3测试accept方法，既有合法选票，又有非法选票
	public void testAccept3() {
		Map<String,Integer> map = new HashMap<>();
		map.put("喜欢",2);
		map.put("无所谓",1);
		map.put("不喜欢",0);
		VoteType vt = new VoteType(map);
		List<Dish> l = new ArrayList<>();
		Dish p1 = new Dish("锅包肉",25.0);
		Dish p2 = new Dish("溜肉段",20.0);
		Dish p3 = new Dish("白灼菜心",10.0);
		Dish p4 = new Dish("杀猪菜",17.0);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.4);
		m.put(v2,0.3);
		m.put(v3,0.2);
		m.put(v4,0.1);
		Set<VoteItem<Dish>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"like"));
		s1.add(new VoteItem<>(p2,"无所谓"));
		s1.add(new VoteItem<>(p3,"喜欢"));
		s1.add(new VoteItem<>(p4,"不喜欢"));
		Set<VoteItem<Dish>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"喜欢"));
		s2.add(new VoteItem<>(p3,"不喜欢"));
		s2.add(new VoteItem<>(p4,"喜欢"));
		Set<VoteItem<Dish>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"不喜欢"));
		s3.add(new VoteItem<>(p2,"无所谓"));
		s3.add(new VoteItem<>(p3,"无所谓"));
		s3.add(new VoteItem<>(p4,"无所谓"));
		s3.add(new VoteItem<>(new Dish("铁锅炖",79.0),"喜欢"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"喜欢"));
		s4.add(new VoteItem<>(p2,"喜欢"));
		s4.add(new VoteItem<>(p3,"无所谓"));
		s4.add(new VoteItem<>(p4,"不喜欢"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
//		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		DinnerOrder po = new DinnerOrder();
		po.setInfo("投票",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		assertEquals(0.25,po.accept(new LegalRatioVisitor<Dish>()),1e-6);
	}
}
