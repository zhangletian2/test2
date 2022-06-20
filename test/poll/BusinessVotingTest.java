package poll;

import auxiliary.Proposal;
import auxiliary.Voter;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pattern.BusinessSelect;
import pattern.BusinessStat;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class BusinessVotingTest {

	// test strategy:
	// setInfo:
	// quantity为/不为0或1
	// addCandidates:
	// 候选对象个数是/不是1
	// 其他方法:
	// 只针对应用1进行综合测试，保证覆盖重写与重载的方法
	// 其他情况如参数不合法等已经在PollTest中测试
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	// 测试应用1，quantity不为0或1
	public void testBusinessVoting() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		BusinessVoting po = new BusinessVoting();
		po.setInfo("投票",Calendar.getInstance(),vt,2);
	}
	@Test
	// 测试应用1，候选对象不是一个
	public void testBusinessVoting1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("提案1", Calendar.getInstance());
		Proposal q = new Proposal("提案2",Calendar.getInstance());
		l.add(p);
		l.add(q);
		BusinessVoting po = new BusinessVoting();
		po.addCandidates(l);
	}
	@Test
	// 测试应用1，只有一个候选对象
	public void testBusinessVoting2() {
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
		// 使用Vote子类型RealNameVote
		RealNameVote<Proposal> V1 = new RealNameVote<>(s1,v1);
		RealNameVote<Proposal> V2 = new RealNameVote<>(s2,v2);
		RealNameVote<Proposal> V3 = new RealNameVote<>(s3,v3);
		RealNameVote<Proposal> V4 = new RealNameVote<>(s4,v4);
		BusinessVoting po = new BusinessVoting();
		po.setInfo("投票",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1);
		po.addVote(V2);
		po.addVote(V3);
		po.addVote(V4);
		po.statistics(new BusinessStat<>());
		po.selection(new BusinessSelect<>());
		Map<Proposal,Double> ref = new HashMap<>();
		ref.put(p,0.7);
		assertEquals(ref.keySet(),po.getResults().keySet());
		assertEquals(ref.get(p),po.getResults().get(p),1e-6);
		System.out.println(po.result());
	}
}
