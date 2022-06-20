package vote;

import auxiliary.Voter;

import java.util.Objects;

public class RealNameDecorator<C> extends voteDecorator<C>{
    private final Voter voter;

    public RealNameDecorator(voteInterface<C> input,Voter v) {
        super(input);
        this.voter = v;
    }

    @Override
    public Voter getVoter() {
        super.getVoter();
        return this.voter;
    }
}
