package app;

import java.util.*;

import auxiliary.Person;
import auxiliary.Voter;
import pattern.ElectionSelect;
import pattern.ElectionStat;
import poll.BusinessVoting;
import poll.Election;
import poll.Poll;
import vote.*;

public class ElectionApp {

	public static void main(String[] args) {

		// 创建2个投票人
		Voter vr1 = new Voter("v1");
		Voter vr2 = new Voter("v2");

		// 设定2个投票人的权重
		Map<Voter, Double> weightedVoters = new HashMap<>();
		weightedVoters.put(vr1, 1.0);
		weightedVoters.put(vr2, 1.0);

		// 设定投票类型
		Map<String, Integer> types = new HashMap<>();
		types.put("支持", 1);
		types.put("反对", -1);
		types.put("弃权", 0);
		VoteType vt = new VoteType(types);
		// 创建候选对象：候选人
		Person p1 = new Person("ABC", 19);
		Person p2 = new Person("DEF", 20);
		Person p3 = new Person("GHI", 21);
		List<Person> l = new ArrayList<>();
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		// 创建投票项，前三个是投票人vr1对三个候选对象的投票项，后三个是vr2的投票项
		VoteItem<Person> vi11 = new VoteItem<>(p1, "支持");
		VoteItem<Person> vi12 = new VoteItem<>(p2, "反对");
		VoteItem<Person> vi13 = new VoteItem<>(p3, "支持");
		VoteItem<Person> vi21 = new VoteItem<>(p1, "反对");
		VoteItem<Person> vi22 = new VoteItem<>(p2, "弃权");
		VoteItem<Person> vi23 = new VoteItem<>(p3, "弃权");
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(vi11);
		s1.add(vi12);
		s1.add(vi13);
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(vi21);
		s2.add(vi22);
		s2.add(vi23);
		// 创建2个投票人vr1、vr2的选票，匿名投票
		voteInterface<Person> rv1 = new Vote<Person>(s1);
		voteInterface<Person> rv2 = new Vote<Person>(s2);

		// 创建投票活动
		Election poll = new Election();
		// 设定投票基本信息：名称、日期、投票类型、选出的数量
		poll.setInfo("班干竞选",Calendar.getInstance(),vt,2);
		// 增加投票人及其权重
		poll.addVoters(weightedVoters);
		poll.addCandidates(l);
		// 增加投票人的选票
		poll.addVote(rv1,vr1);
		poll.addVote(rv2,vr2);

		// 按规则计票
		poll.statistics(new ElectionStat<>());
		// 按规则遴选
		poll.selection(new ElectionSelect<>());
		// 输出遴选结果
		System.out.print("候选对象\t\tID\n");
		for(int i=0;i<l.size();i++){
			System.out.println(l.get(i).getName()+"\t\t\t"+i);
		}
		System.out.println("遴选结果"+'\n'+poll.result());
	}

}
