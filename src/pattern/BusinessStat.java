package pattern;

import auxiliary.Proposal;
import auxiliary.Voter;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;
import vote.voteInterface;

import java.util.*;

public class BusinessStat<Proposal> implements StatisticsStrategy<Proposal>{
    @Override
    public Map<Proposal,Double> stat(List<Proposal> candidates, Map<Voter, Double> voters,
                                     Map<Voter, voteInterface<Proposal>> legalVotes, VoteType voteType) {
        double total = 0;
        Proposal c = candidates.get(0);
        int max = -1;
        String support = "";
        // 获取“支持”选项的具体名字形式（可能是“支持”、“support”等等），默认支持的分数是最大的
        Map<String,Integer> option = new HashMap<>(voteType.getOptions());
        for(String s:option.keySet()){
            if(option.get(s)>max){
                support = s;
                max = option.get(s);
            }
        }
        Map<Proposal,Double> m = new HashMap<>();
        m.put(c,total);
        for (Map.Entry<Voter, voteInterface<Proposal>> entry : legalVotes.entrySet()) {
            Set<VoteItem<Proposal>> s = entry.getValue().getVoteItems();
            for (VoteItem<Proposal> vi : s) {
                if (vi.getVoteValue().equals(support)) {
                    double weight = voters.get(entry.getKey());
                    total += 1 * weight;
                }
            }
        }
        m.put(c,total);
        return m;
    }
}
