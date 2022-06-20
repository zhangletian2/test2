package poll;

import vote.voteInterface;

import java.util.Set;

public class LegalRatioVisitor<C> implements Visitor<C>{
    @Override
    public double visit(Poll<C> poll) {
        Set<voteInterface<C>> allVotes = poll.getVotes();
        Set<voteInterface<C>> legalVotes = poll.getLegalVotes();
        return 1.0* legalVotes.size()/allVotes.size();
    }
}
