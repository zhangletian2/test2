package vote;

import auxiliary.Voter;

import java.util.Set;

//immutable
public class RealNameVote<C> extends Vote<C>{
	
	//ͶƱ��
	private final Voter voter;
	// Rep Invariants
	// VoteItem������ͬ��Set���Ա�֤��
	// Abstract Function
	// AF(voteItems,date,voter) = һ��ͶƱ����ĳ��ʱ������к�ѡ�����ͶƱ���
	// Safety from Rep Exposure
	// ���г�Ա��������private���ͣ�voter��private final����
	// ���캯��ʹ��set�����
	// getVoteItemsʹ�÷���ʽ����

	public RealNameVote(Set<VoteItem<C>> votes,Voter v) {
		super(votes);
		this.voter = v;
	}
	
	public Voter getVoter() {
		return this.voter;
	}
}
