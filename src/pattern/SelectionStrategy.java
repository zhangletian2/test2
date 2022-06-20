package pattern;

import java.util.Map;

public interface SelectionStrategy<C> {
	Map<C, Double> select(Map<C, Double> statistics,int quantity);
}
