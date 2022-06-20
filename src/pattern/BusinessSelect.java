package pattern;

import auxiliary.Proposal;

import java.util.HashMap;
import java.util.Map;

public class BusinessSelect<Proposal> implements SelectionStrategy<Proposal> {
    @Override
    public Map<Proposal, Double> select (Map<Proposal, Double> statistics,int quantity) {
        Map<Proposal, Double> result = new HashMap<>();
        for (Map.Entry<Proposal, Double> entry : statistics.entrySet()) {
            if(entry.getValue()>2.0/3){
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}