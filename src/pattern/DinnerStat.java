package pattern;

import auxiliary.Dish;
import auxiliary.Voter;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;
import vote.voteInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DinnerStat<Dish> implements StatisticsStrategy<Dish>{
    @Override
    public Map<Dish, Double> stat(List<Dish> candidates, Map<Voter, Double> voters,
                                  Map<Voter, voteInterface<Dish>> legalVotes, VoteType voteType) {
        Map<Dish,Double> m = new HashMap<>();
        for(Dish c:candidates){
            m.put(c,0.0);
        }
        Map<String, Integer> options = voteType.getOptions();
        for (Map.Entry<Voter, voteInterface<Dish>> entry : legalVotes.entrySet()) {
            double weight = voters.get(entry.getKey());
            Set<VoteItem<Dish>> s = entry.getValue().getVoteItems();
            for (VoteItem<Dish> vi : s) {
                Dish c = vi.getCandidate();
                int score = options.get(vi.getVoteValue());
                double pre = m.get(c);
                m.put(c,pre+weight*score);
            }
        }
        return m;
    }
}
