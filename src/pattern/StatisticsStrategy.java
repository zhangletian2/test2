package pattern;

import auxiliary.Voter;
import vote.Vote;
import vote.VoteType;
import vote.voteInterface;

import java.util.List;
import java.util.Map;

public interface StatisticsStrategy<C> {
	Map<C,Double> stat(List<C> candidates, Map<Voter, Double> voters, Map<Voter,
			voteInterface<C>> legalVotes, VoteType voteType);
}
