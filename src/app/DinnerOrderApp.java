package app;

import auxiliary.Dish;
import auxiliary.Voter;
import pattern.DinnerSelect;
import pattern.DinnerStat;
import poll.DinnerOrder;
import vote.*;

import java.util.*;


public class DinnerOrderApp {
	
	public static void main(String[] args) throws Exception {
		VoteType vt = new VoteType("“喜欢”(2)|“不喜欢”(0)|“无所谓”(1)");
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
		// 实名投票，使用第二种方式，即decorator
		voteInterface<Dish> V1 = new RealNameDecorator<>(new Vote<>(s1),v1);
		voteInterface<Dish> V2 = new RealNameDecorator<>(new Vote<>(s2),v2);
		voteInterface<Dish> V3 = new RealNameDecorator<>(new Vote<>(s3),v3);
		voteInterface<Dish> V4 = new RealNameDecorator<>(new Vote<>(s4),v4);
		DinnerOrder po = new DinnerOrder();
		po.setInfo("聚餐点菜",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1);
		po.addVote(V2);
		po.addVote(V3);
		po.addVote(V4);
		po.statistics(new DinnerStat<>());
		po.selection(new DinnerSelect<>());
		System.out.print("候选对象\t\tID\n");
		for(int i=0;i<l.size();i++){
			System.out.println(l.get(i).getName()+"\t\t"+i);
		}
		System.out.println("遴选结果"+'\n'+po.result());
	}
}
