package pattern;

import auxiliary.Person;
import auxiliary.Voter;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;
import vote.voteInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElectionStat<Person> implements StatisticsStrategy<Person> {
    @Override
    public Map<Person, Double> stat(List<Person> candidates, Map<Voter, Double> voters,
                                    Map<Voter, voteInterface<Person>> legalVotes, VoteType voteType) {
        Map<Person,Double> m = new HashMap<>();
        for(Person c:candidates){
            m.put(c,0.0);
        }
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
        for (Map.Entry<Voter, voteInterface<Person>> entry : legalVotes.entrySet()) {
            Set<VoteItem<Person>> s = entry.getValue().getVoteItems();
            for (VoteItem<Person> vi : s) {
                Person c = vi.getCandidate();
                if (vi.getVoteValue().equals(support)) {
                    double pre = m.get(c);
                    m.put(c,pre+1);
                }
            }
        }
        return m;
    }
}
