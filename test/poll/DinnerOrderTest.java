package poll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import auxiliary.Voter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pattern.DinnerSelect;
import pattern.DinnerStat;
import vote.*;

import java.util.*;

public class DinnerOrderTest {

	// test strategy
	// setInfo:
	// quantityС��/��С��ͶƱ����+5
	// addVoters:
	// quantityС��/��С��ͶƱ����+5��addVoters��setInfoû���Ⱥ��ϵ�����Զ�Ҫ���ǣ�
	// ��������:
	// ֻ���Ӧ��3�����ۺϲ��ԣ���֤������д�����صķ���
	// ���������������Ϸ����Ѿ���PollTest�в���
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Test
    //quantity ��С��ͶƱ����+5
    public void setInfo() throws IllegalArgumentException{
        expectedEx.expect(IllegalArgumentException.class);
        Map<String,Integer> map = new HashMap<>();
        map.put("ϲ��",2);
        map.put("����ν",1);
        map.put("��ϲ��",0);
        VoteType vt = new VoteType(map);
        Voter v1 = new Voter("v1");
        Voter v2 = new Voter("v2");
        Voter v3 = new Voter("v3");
        Map<Voter,Double> m = new HashMap<>();
        m.put(v1,0.6);
        m.put(v2,0.2);
        m.put(v3,0.2);
        DinnerOrder po = new DinnerOrder();
        po.addVoters(m);
        po.setInfo("���", Calendar.getInstance(),vt,10);
    }
    @Test
    //quantity ��С��ͶƱ����+5
    public void addVoters() throws IllegalArgumentException{
        expectedEx.expect(IllegalArgumentException.class);
        Map<String,Integer> map = new HashMap<>();
        map.put("ϲ��",2);
        map.put("����ν",1);
        map.put("��ϲ��",0);
        VoteType vt = new VoteType(map);
        Voter v1 = new Voter("v1");
        Voter v2 = new Voter("v2");
        Voter v3 = new Voter("v3");
        Map<Voter,Double> m = new HashMap<>();
        m.put(v1,0.6);
        m.put(v2,0.2);
        m.put(v3,0.2);
        DinnerOrder po = new DinnerOrder();
        po.setInfo("���", Calendar.getInstance(),vt,10);
        po.addVoters(m);
    }
    //�����������Ӧ��3����
    @Test
    public void testDinnerOrder() {
        Map<String,Integer> map = new HashMap<>();
        map.put("ϲ��",2);
        map.put("����ν",1);
        map.put("��ϲ��",0);
        VoteType vt = new VoteType(map);
        List<Dish> l = new ArrayList<>();
        Dish p1 = new Dish("������",25.0);
        Dish p2 = new Dish("�����",20.0);
        Dish p3 = new Dish("���Ʋ���",10.0);
        Dish p4 = new Dish("ɱ���",17.0);
        l.add(0,p1);
        l.add(1,p2);
        l.add(2,p3);
        l.add(3,p4);
        Voter v1 = new Voter("v1");
        Voter v2 = new Voter("v2");
        Voter v3 = new Voter("v3");
        Voter v4 = new Voter("v4");
        Map<Voter,Double> m = new HashMap<>();
        m.put(v1,0.4);
        m.put(v2,0.3);
        m.put(v3,0.2);
        m.put(v4,0.1);
        Set<VoteItem<Dish>> s1 = new HashSet<>();
        s1.add(new VoteItem<>(p1,"����ν"));
        s1.add(new VoteItem<>(p2,"����ν"));
        s1.add(new VoteItem<>(p3,"ϲ��"));
        s1.add(new VoteItem<>(p4,"��ϲ��"));
        Set<VoteItem<Dish>> s2 = new HashSet<>();
        s2.add(new VoteItem<>(p1,"ϲ��"));
        s2.add(new VoteItem<>(p2,"��ϲ��"));
        s2.add(new VoteItem<>(p3,"��ϲ��"));
        s2.add(new VoteItem<>(p4,"ϲ��"));
        Set<VoteItem<Dish>> s3 = new HashSet<>();
        s3.add(new VoteItem<>(p1,"��ϲ��"));
        s3.add(new VoteItem<>(p2,"����ν"));
        s3.add(new VoteItem<>(p3,"����ν"));
        s3.add(new VoteItem<>(p4,"����ν"));
        Set<VoteItem<Dish>> s4 = new HashSet<>();
        s4.add(new VoteItem<>(p1,"ϲ��"));
        s4.add(new VoteItem<>(p2,"ϲ��"));
        s4.add(new VoteItem<>(p3,"����ν"));
        s4.add(new VoteItem<>(p4,"��ϲ��"));
        // ʹ��decorator
        voteInterface<Dish> V1 = new RealNameDecorator<>(new Vote<>(s1),v1);
        voteInterface<Dish> V2 = new RealNameDecorator<>(new Vote<>(s2),v2);
        voteInterface<Dish> V3 = new RealNameDecorator<>(new Vote<>(s3),v3);
        voteInterface<Dish> V4 = new RealNameDecorator<>(new Vote<>(s4),v4);
        DinnerOrder po = new DinnerOrder();
        po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
        po.addCandidates(l);
        po.addVoters(m);
        po.addVote(V1);
        po.addVote(V2);
        po.addVote(V3);
        po.addVote(V4);
        po.statistics(new DinnerStat<>());
        po.selection(new DinnerSelect<>());
        StringBuilder sb = new StringBuilder();
        sb.append("��ѡ����ID");
        sb.append('\t');
        sb.append("����");
        sb.append('\n');
        for(int i=0;i<=1;i++){
            sb.append(i*2);
            sb.append('\t');
            sb.append('\t');
            sb.append('\t');
            sb.append(i+1);
            sb.append('\n');
        }
        sb.append(1);
        sb.append('\t');
        sb.append('\t');
        sb.append('\t');
        sb.append(3);
        sb.append('\n');
        StringBuilder sb1 = new StringBuilder();
        sb1.append("��ѡ����ID");
        sb1.append('\t');
        sb1.append("����");
        sb1.append('\n');
        for(int i=0;i<=1;i++){
            sb1.append(i*2);
            sb1.append('\t');
            sb1.append('\t');
            sb1.append('\t');
            sb1.append(i+1);
            sb1.append('\n');
        }
        sb1.append(3);
        sb1.append('\t');
        sb1.append('\t');
        sb1.append('\t');
        sb1.append(3);
        sb1.append('\n');
        System.out.println(po.toString());
        assertTrue(sb.toString().equals(po.result())||sb1.toString().equals(po.result()));
        System.out.println(po.result());
    }
}
