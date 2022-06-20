package vote;

import auxiliary.Voter;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//immutable
public class Vote<C> implements voteInterface<C>{

	// ȱʡΪ��������ͶƱ��������Ҫ����ͶƱ�˵���Ϣ

	// һ��ͶƱ�˶����к�ѡ�����ͶƱ���
	private Set<VoteItem<C>> voteItems = new HashSet<>();
	// ͶƱʱ��
	private Calendar date = Calendar.getInstance();

	// Rep Invariants
	// VoteItem������ͬ��Set���Ա�֤��
	// Abstract Function
	// AF(voteItems,date) = һ��ͶƱ����ĳ��ʱ������к�ѡ�����ͶƱ���
	// Safety from Rep Exposure
	// ���г�Ա��������private����
	// ���캯��ʹ��set�����
	// getVoteItemsʹ�÷���ʽ����

	private void checkRep() {
//		Set<C> set = new HashSet<>();
//		for(VoteItem<C> v:voteItems){
//			set.add(v.getCandidate());
//		}
//		assert set.size() == voteItems.size();
	}
	/**
	 * ����һ��ѡƱ����
	 * @param votes һ��ͶƱ�˵�һ��ѡƱ
	 */
	public Vote(Set<VoteItem<C>> votes) {
//		Set<C> set = new HashSet<>();
//		for(VoteItem<C> v:votes){
//			set.add(v.getCandidate());
//		}
//		if(set.size() != votes.size()){
//			throw new IllegalArgumentException("һ����ѡ��ֻ�ܶ�Ӧһ��ͶƱѡ��");
//		}
		this.voteItems.addAll(votes);
		checkRep();
	}

	/**
	 * ��ѯ��ѡƱ�а���������ͶƱ��
	 * 
	 * @return ����ͶƱ��
	 */
	public Set<VoteItem<C>> getVoteItems() {
		Set<VoteItem<C>> set = new HashSet<>(voteItems);
		checkRep();
		return set;
	}

	/**
	 * һ���ض���ѡ���Ƿ������ѡƱ��
	 * 
	 * @param candidate ����ѯ�ĺ�ѡ��
	 * @return �������ú�ѡ�˵�ͶƱ��򷵻�true������false
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
	 * ����ʵ��
	 * @return ͶƱ�����ID
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
