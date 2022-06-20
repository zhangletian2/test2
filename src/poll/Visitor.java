package poll;

public interface Visitor<C> {
    public double visit(Poll<C> poll);
}
