package vote;

import auxiliary.Voter;

import java.util.HashSet;
import java.util.Set;

public interface voteInterface<C> {
    /**
     * ��ѯ��ѡƱ�а���������ͶƱ��
     *
     * @return ����ͶƱ��
     */
    public Set<VoteItem<C>> getVoteItems();

    /**
     * һ���ض���ѡ���Ƿ�����ڱ�ѡƱ��
     *
     * @param candidate ����ѯ�ĺ�ѡ��
     * @return �������ú�ѡ�˵�ͶƱ��򷵻�true������false
     */
    public boolean candidateIncluded(C candidate);

    /**
     *
     * @return Voter's ID
     */
    public Voter getVoter();
}
