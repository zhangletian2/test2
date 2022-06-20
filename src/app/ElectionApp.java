package app;

import java.util.*;

import auxiliary.Person;
import auxiliary.Voter;
import pattern.ElectionSelect;
import pattern.ElectionStat;
import poll.BusinessVoting;
import poll.Election;
import poll.Poll;
import vote.*;

public class ElectionApp {

	public static void main(String[] args) {

		// ����2��ͶƱ��
		Voter vr1 = new Voter("v1");
		Voter vr2 = new Voter("v2");

		// �趨2��ͶƱ�˵�Ȩ��
		Map<Voter, Double> weightedVoters = new HashMap<>();
		weightedVoters.put(vr1, 1.0);
		weightedVoters.put(vr2, 1.0);

		// �趨ͶƱ����
		Map<String, Integer> types = new HashMap<>();
		types.put("֧��", 1);
		types.put("����", -1);
		types.put("��Ȩ", 0);
		VoteType vt = new VoteType(types);
		// ������ѡ���󣺺�ѡ��
		Person p1 = new Person("ABC", 19);
		Person p2 = new Person("DEF", 20);
		Person p3 = new Person("GHI", 21);
		List<Person> l = new ArrayList<>();
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		// ����ͶƱ�ǰ������ͶƱ��vr1��������ѡ�����ͶƱ���������vr2��ͶƱ��
		VoteItem<Person> vi11 = new VoteItem<>(p1, "֧��");
		VoteItem<Person> vi12 = new VoteItem<>(p2, "����");
		VoteItem<Person> vi13 = new VoteItem<>(p3, "֧��");
		VoteItem<Person> vi21 = new VoteItem<>(p1, "����");
		VoteItem<Person> vi22 = new VoteItem<>(p2, "��Ȩ");
		VoteItem<Person> vi23 = new VoteItem<>(p3, "��Ȩ");
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(vi11);
		s1.add(vi12);
		s1.add(vi13);
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(vi21);
		s2.add(vi22);
		s2.add(vi23);
		// ����2��ͶƱ��vr1��vr2��ѡƱ������ͶƱ
		voteInterface<Person> rv1 = new Vote<Person>(s1);
		voteInterface<Person> rv2 = new Vote<Person>(s2);

		// ����ͶƱ�
		Election poll = new Election();
		// �趨ͶƱ������Ϣ�����ơ����ڡ�ͶƱ���͡�ѡ��������
		poll.setInfo("��ɾ�ѡ",Calendar.getInstance(),vt,2);
		// ����ͶƱ�˼���Ȩ��
		poll.addVoters(weightedVoters);
		poll.addCandidates(l);
		// ����ͶƱ�˵�ѡƱ
		poll.addVote(rv1,vr1);
		poll.addVote(rv2,vr2);

		// �������Ʊ
		poll.statistics(new ElectionStat<>());
		// ��������ѡ
		poll.selection(new ElectionSelect<>());
		// �����ѡ���
		System.out.print("��ѡ����\t\tID\n");
		for(int i=0;i<l.size();i++){
			System.out.println(l.get(i).getName()+"\t\t\t"+i);
		}
		System.out.println("��ѡ���"+'\n'+poll.result());
	}

}
