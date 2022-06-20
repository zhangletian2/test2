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
        // mapת����list��������
        List<Map.Entry<Dish, Double>> list = new ArrayList<Map.Entry<Dish, Double>>(statistics.entrySet());
        list.sort(valueComparator);
        // ��ѡ�����պõ�����ѡ���ĺ�ѡ�����������
        if(list.size()==quantity){
            for(Map.Entry<Dish,Double> e:list){
                result.put(e.getKey(),e.getValue());
            }
            return result;
        }
        // ��ѡ����������ѡ���ĺ�ѡ�����������
        // �Ƚ�list�ĵ�quantity���͵�quantity+1���ĵ÷֣�������ȣ�����ǰquantity������
        Map.Entry<Dish,Double> entry1 = list.get(quantity);
        Map.Entry<Dish,Double> entry2 = list.get(quantity-1);
        if(Math.abs(entry1.getValue()- entry2.getValue())>1e-6){
            for(int i=0;i<quantity;i++){
                Map.Entry<Dish,Double> entry = list.get(i);
                result.put(entry.getKey(),entry.getValue());
            }
            return result;
        }
        // �����ȣ���ǰ�����ң�����÷����quantity����ȵĺ�ѡ�˵ļ��ϣ����ѡ
        int head=-1,tail= list.size()-1;
        // ��ǰ��
        for(int i=quantity-2;i>=0;i--){
            Map.Entry<Dish,Double> entry = list.get(i);
            if(Math.abs(entry1.getValue()- entry.getValue())>1e-6){
                head=i;
                break;
            }
        }
        // ������
        for(int i=quantity+1;i<list.size();i++){
            Map.Entry<Dish,Double> entry = list.get(i);
            if(Math.abs(entry1.getValue()- entry.getValue())>1e-6){
                tail=i;
                break;
            }
        }
        // �Ƚ�ǰhead��Ԫ�ؼ���result��
        for(int i=0;i<=head;i++){
            Map.Entry<Dish,Double> entry = list.get(i);
            result.put(entry.getKey(),entry.getValue());
        }
        // ��head��tail֮���Ԫ����(�±�Ϊhead+1~tail-1)���ѡ(quantity-head-1)��
        Random rand = new Random();
        while(result.size()<quantity){
            int randNumber = rand.nextInt(tail-head-1) + head+1;
            Map.Entry<Dish,Double> entry = list.get(randNumber);
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
}
