package pattern;

import auxiliary.Dish;

import java.util.*;

public class DinnerSelect<Dish> implements SelectionStrategy<Dish>{
    @Override
    public Map<Dish, Double> select(Map<Dish, Double> statistics, int quantity) {
        Map<Dish, Double> result = new HashMap<>();
        Map<Dish, Double> temp = new HashMap<>();
        Comparator<Map.Entry<Dish, Double>> valueComparator = new Comparator<Map.Entry<Dish, Double>>() {
            @Override
            public int compare(Map.Entry<Dish, Double> o1,
                               Map.Entry<Dish, Double> o2) {
                if(o2.getValue()-o1.getValue()>1e-6) return 1;
                else if(Math.abs(o1.getValue()- o2.getValue())<1e-6) return 0;
                else return -1;
            }
        };
        // map转换成list进行排序
        List<Map.Entry<Dish, Double>> list = new ArrayList<Map.Entry<Dish, Double>>(statistics.entrySet());
        list.sort(valueComparator);
        // 候选菜数刚好等于拟选出的候选对象最大数量
        if(list.size()==quantity){
            for(Map.Entry<Dish,Double> e:list){
                result.put(e.getKey(),e.getValue());
            }
            return result;
        }
        // 候选菜数大于拟选出的候选对象最大数量
        // 比较list的第quantity个和第quantity+1个的得分，如果不等，加入前quantity个即可
        Map.Entry<Dish,Double> entry1 = list.get(quantity);
        Map.Entry<Dish,Double> entry2 = list.get(quantity-1);
        if(Math.abs(entry1.getValue()- entry2.getValue())>1e-6){
            for(int i=0;i<quantity;i++){
                Map.Entry<Dish,Double> entry = list.get(i);
                result.put(entry.getKey(),entry.getValue());
            }
            return result;
        }
        // 如果相等，往前往后找，求出得分与第quantity个相等的候选菜的集合，随机选
        int head=-1,tail= list.size()-1;
        // 往前找
        for(int i=quantity-2;i>=0;i--){
            Map.Entry<Dish,Double> entry = list.get(i);
            if(Math.abs(entry1.getValue()- entry.getValue())>1e-6){
                head=i;
                break;
            }
        }
        // 往后找
        for(int i=quantity+1;i<list.size();i++){
            Map.Entry<Dish,Double> entry = list.get(i);
            if(Math.abs(entry1.getValue()- entry.getValue())>1e-6){
                tail=i;
                break;
            }
        }
        // 先将前head个元素加入result中
        for(int i=0;i<=head;i++){
            Map.Entry<Dish,Double> entry = list.get(i);
            result.put(entry.getKey(),entry.getValue());
        }
        // 在head与tail之间的元素中(下标为head+1~tail-1)随机选(quantity-head-1)个
        Random rand = new Random();
        while(result.size()<quantity){
            int randNumber = rand.nextInt(tail-head-1) + head+1;
            Map.Entry<Dish,Double> entry = list.get(randNumber);
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
}
