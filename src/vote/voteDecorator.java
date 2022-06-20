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
     * 查询该选票中包含的所有投票项
     *
     * @return 所有投票项
     */
    public Set<VoteItem<C>> getVoteItems() {
        return input.getVoteItems();
    }

    /**
     * 一个特定候选人是否包含本选票中
     *
     * @param candidate 待查询的候选人
     * @return 若包含该候选人的投票项，则返回true，否则false
     */
    public boolean candidateIncluded(C candidate) {
        return input.candidateIncluded(candidate);
    }

    /**
     * 委托机制
     * @return 投票者身份ID
     */
    public Voter getVoter(){
        return input.getVoter();
    }
}
