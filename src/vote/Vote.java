package vote;

import auxiliary.Voter;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//immutable
public class Vote<C> implements voteInterface<C>{

	// 缺省为“匿名”投票，即不需要管理投票人的信息

	// 一个投票人对所有候选对象的投票项集合
	private Set<VoteItem<C>> voteItems = new HashSet<>();
	// 投票时间
	private Calendar date = Calendar.getInstance();

	// Rep Invariants
	// VoteItem互不相同（Set可以保证）
	// Abstract Function
	// AF(voteItems,date) = 一个投票人在某个时间对所有候选对象的投票项集合
	// Safety from Rep Exposure
	// 所有成员变量都是private类型
	// 构造函数使用set的深拷贝
	// getVoteItems使用防御式拷贝

	private void checkRep() {
//		Set<C> set = new HashSet<>();
//		for(VoteItem<C> v:voteItems){
//			set.add(v.getCandidate());
//		}
//		assert set.size() == voteItems.size();
	}
	/**
	 * 创建一个选票对象
	 * @param votes 一个投票人的一张选票
	 */
	public Vote(Set<VoteItem<C>> votes) {
//		Set<C> set = new HashSet<>();
//		for(VoteItem<C> v:votes){
//			set.add(v.getCandidate());
//		}
//		if(set.size() != votes.size()){
//			throw new IllegalArgumentException("一个候选人只能对应一个投票选项");
//		}
		this.voteItems.addAll(votes);
		checkRep();
	}

	/**
	 * 查询该选票中包含的所有投票项
	 * 
	 * @return 所有投票项
	 */
	public Set<VoteItem<C>> getVoteItems() {
		Set<VoteItem<C>> set = new HashSet<>(voteItems);
		checkRep();
		return set;
	}

	/**
	 * 一个特定候选人是否包含本选票中
	 * 
	 * @param candidate 待查询的候选人
	 * @return 若包含该候选人的投票项，则返回true，否则false
	 */
	public boolean candidateIncluded(C candidate) {
		for(VoteItem<C> v:voteItems){
			if(v.getCandidate().equals(candidate)){
				checkRep();
				return true;
			}
		}
		return false;
	}

	/**
	 * 基础实现
	 * @return 投票者身份ID
	 */
	@Override
	public Voter getVoter() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vote<?> vote = (Vote<?>) o;
		return voteItems.equals(vote.voteItems) && date.equals(vote.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(voteItems, date);
	}
}
