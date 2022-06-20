package vote;

import auxiliary.Voter;

import java.util.Set;

//immutable
public class RealNameVote<C> extends Vote<C>{
	
	//投票人
	private final Voter voter;
	// Rep Invariants
	// VoteItem互不相同（Set可以保证）
	// Abstract Function
	// AF(voteItems,date,voter) = 一个投票人在某个时间对所有候选对象的投票项集合
	// Safety from Rep Exposure
	// 所有成员变量都是private类型，voter是private final类型
	// 构造函数使用set的深拷贝
	// getVoteItems使用防御式拷贝

	public RealNameVote(Set<VoteItem<C>> votes,Voter v) {
		super(votes);
		this.voter = v;
	}
	
	public Voter getVoter() {
		return this.voter;
	}
}
