package app;

import auxiliary.Proposal;
import auxiliary.Voter;
import pattern.BusinessSelect;
import pattern.BusinessStat;
import poll.BusinessVoting;
import vote.RealNameVote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;


public class BusinessVotingApp {

	public static void main(String[] args) throws Exception {
		VoteType vt = new VoteType("��֧�֡�(1)|�����ԡ�(-1)|����Ȩ��(0)");
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("�᰸", Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("���³�");
		Voter v2 = new Voter("�ܾ���");
		Voter v3 = new Voter("����");
		Voter v4 = new Voter("HR");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.4);
		m.put(v2,0.2);
		m.put(v3,0.2);
		m.put(v4,0.2);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"֧��");
		VoteItem<Proposal> vi2 = new VoteItem<>(p,"֧��");
		VoteItem<Proposal> vi3 = new VoteItem<>(p,"��Ȩ");
		VoteItem<Proposal> vi4 = new VoteItem<>(p,"֧��");
		Set<VoteItem<Proposal>> s1 = new HashSet<>();
		s1.add(vi1);
		Set<VoteItem<Proposal>> s2 = new HashSet<>();
		s2.add(vi2);
		Set<VoteItem<Proposal>> s3 = new HashSet<>();
		s3.add(vi3);
		Set<VoteItem<Proposal>> s4 = new HashSet<>();
		s4.add(vi4);
		// ʵ��ͶƱ��ʹ�õ�һ�ַ�ʽ����Vote������RealNameVote
		RealNameVote<Proposal> V1 = new RealNameVote<>(s1,v1);
		RealNameVote<Proposal> V2 = new RealNameVote<>(s2,v2);
		RealNameVote<Proposal> V3 = new RealNameVote<>(s3,v3);
		RealNameVote<Proposal> V4 = new RealNameVote<>(s4,v4);
		BusinessVoting poll = new BusinessVoting();
		poll.setInfo("�᰸һ��ͶƱ",Calendar.getInstance(),vt,1);
		poll.addCandidates(l);
		poll.addVoters(m);
		poll.addVote(V1);
		poll.addVote(V2);
		poll.addVote(V3);
		poll.addVote(V4);
		poll.statistics(new BusinessStat<>());
		poll.selection(new BusinessSelect<>());
		System.out.print("��ѡ����\t\tID\n");
		System.out.println(p.getTitle()+"\t\t"+0);
		System.out.println("��ѡ���"+'\n'+poll.result());
		if(poll.getResults().get(p)-0.6>1e-6){
			System.out.println("�᰸ͨ����");
		}
		else{
			System.out.println("�᰸δͨ����");
		}
	}
}
