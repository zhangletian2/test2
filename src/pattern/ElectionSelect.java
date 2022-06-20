package pattern;

import auxiliary.Person;

import java.util.*;

public class ElectionSelect<Person> implements SelectionStrategy<Person> {
    @Override
    public Map<Person, Double> select(Map<Person, Double> statistics,int quantity) {
        Map<Person, Double> result = new HashMap<>();

        Comparator<Map.Entry<Person, Double>> valueComparator = new Comparator<Map.Entry<Person, Double>>() {
            @Override
            public int compare(Map.Entry<Person, Double> o1,
                               Map.Entry<Person, Double> o2) {
                if(o2.getValue()-o1.getValue()>1e-6) return 1;
                else if(Math.abs(o1.getValue()- o2.getValue())<1e-6) return 0;
                else return -1;
            }
        };
        // mapת����list��������
        List<Map.Entry<Person, Double>> list = new ArrayList<Map.Entry<Person, Double>>(statistics.entrySet());
        // ����
        list.sort(valueComparator);
        // ��ѡ�����պõ�����ѡ���ĺ�ѡ�����������
        if(list.size()==quantity){
            for(Map.Entry<Person,Double> e:list){
                result.put(e.getKey(),e.getValue());
            }
            return result;
        }
        // ��ѡ����������ѡ���ĺ�ѡ�����������
        // �Ƚ�list�ĵ�quantity���͵�quantity+1����֧��Ʊ����������ȣ�����ǰquantity������
        Map.Entry<Person,Double> entry1 = list.get(quantity);
        Map.Entry<Person,Double> entry2 = list.get(quantity-1);
        if(Math.abs(entry1.getValue()- entry2.getValue())>1e-6){
            for(int i=0;i<quantity;i++){
                Map.Entry<Person,Double> entry = list.get(i);
                result.put(entry.getKey(),entry.getValue());
            }
            return result;
        }
        // �����ȣ���ǰ�ң��ҵ���һ�����ȵ�λ��p,����ǰp������
        int p=-1;
        for(int i=quantity-2;i>=0;i--){
            Map.Entry<Person,Double> entry = list.get(i);
            if(Math.abs(entry1.getValue()- entry.getValue())>1e-6){
                p=i;
                break;
            }
        }
        for(int i=0;i<=p;i++){
            Map.Entry<Person,Double> entry = list.get(i);
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
}
