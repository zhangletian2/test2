package vote;

import auxiliary.Voter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class voteDecorator<C> implements voteInterface<C>{
    protected final voteInterface<C> input;
    public voteDecorator(voteInterface<C> input) {
        this.input = input;
    }
    /**
     * ��ѯ��ѡƱ�а���������ͶƱ��
     *
     * @return ����ͶƱ��
     */
    public Set<VoteItem<C>> getVoteItems() {
        return input.getVoteItems();
    }

    /**
     * һ���ض���ѡ���Ƿ������ѡƱ��
     *
     * @param candidate ����ѯ�ĺ�ѡ��
     * @return �������ú�ѡ�˵�ͶƱ��򷵻�true������false
     */
    public boolean candidateIncluded(C candidate) {
        return input.candidateIncluded(candidate);
    }

    /**
     * ί�л���
     * @return ͶƱ�����ID
     */
    public Voter getVoter(){
        return input.getVoter();
    }
}
