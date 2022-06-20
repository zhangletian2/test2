package vote;

import java.util.Objects;

//immutable
public class VoteItem<C> {

	// ��ͶƱ������Եĺ�ѡ����
	private C candidate;
	// �Ժ�ѡ����ľ���ͶƱѡ����硰֧�֡��������ԡ���
	// ���豣�������ֵ����Ҫʱ���Դ�ͶƱ���VoteType�����в�ѯ�õ�
	private String value;

	// Rep Invariants
	// candidate����Ϊ��
	// value�����ǿմ�
	// Abstract Function
	// AF(candidate,value) = һ��ͶƱ�˶�һ����ѡ����ľ�������
	// Safety from Rep Exposure
	// ���г�Ա��������private����

	private void checkRep() {
		assert candidate != null;
		assert !value.equals("");
	}

	/**
	 * ����һ��ͶƱ����� ���磺��Ժ�ѡ������������ͶƱѡ���ǡ�֧�֡�
	 * 
	 * @param candidate ����Եĺ�ѡ����
	 * @param value     ��������ͶƱѡ��
	 */
	public VoteItem(C candidate, String value) {
		if (candidate == null){
			throw new IllegalArgumentException("��ѡ�˲���Ϊ��");
		}
		if(value.equals("")){
			throw new IllegalArgumentException("ѡ���Ϊ��");
		}
		this.candidate = candidate;
		this.value = value;
		checkRep();
	}

	/**
	 * �õ���ͶƱѡ��ľ���ͶƱ��
	 * 
	 * @return ��ͶƱѡ��ľ���ͶƱ��
	 */
	public String getVoteValue() {
		return this.value;
	}

	/**
	 * �õ���ͶƱѡ������Ӧ�ĺ�ѡ��
	 * 
	 * @return ��ͶƱѡ������Ӧ�ĺ�ѡ��
	 */
	public C getCandidate() {
		return this.candidate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VoteItem<?> voteItem = (VoteItem<?>) o;
		return candidate.equals(voteItem.candidate) && value.equals(voteItem.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(candidate, value);
	}
}
