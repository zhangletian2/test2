package poll;

import auxiliary.Dish;
import auxiliary.Person;
import auxiliary.Proposal;
import auxiliary.Voter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pattern.*;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;


import java.util.*;

import static org.junit.Assert.*;

public class PollTest {
	
	// test strategy
	// setInfo:
	// quantity�Ǹ�/�Ǹ���
	// �Ƚ�ʵ����������/��һ��
	// addCandidates:
	// ��ѡ��������1��
	// ��ѡ���󲻴���/�Ѵ��ڡ�
	// ��ѡ�����������ں�ѡ����ʱ��/����׳�Ԥ���쳣
	// addVoters:
	// ͶƱ�˲����Ϸ���Ȩ��Ψһ��Ȩ�طǸ�����
	// ���Ϸ���Ȩ��Ϊ����
	// addVote:
	// ͶƱ����/����voters������
	// ͶƱ����Ͷ��Ʊ/δͶ��Ʊ
	// ͶƱ����/��ѡ��Ƿ�ѡ��
	// ѡƱ��/��������к�ѡ��
	// ѡƱ��/��������ں�ѡ������ĺ�ѡ��
	// ѡƱ��/���ͬһ����ѡ���ظ�ͶƱ
	// statistics:
	// һ��ͶƱ���ύ���ѡƱ
	// ����ͶƱ��δͶƱ
	// �ֱ�����������Ӧ�õ����
	// selection:����ָ����ѡ��ʽ��/��ѡ����ȷ���
	// result:����ַ�����/����Ԥ����ͬ
	// accept:ȫ��/���ǷǷ�ѡƱ�����зǷ����кϷ�
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	//quantity�Ǹ���
	public void setInfo0() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		Calendar d = Calendar.getInstance();
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",d,vt,-1);
		assertEquals("ͶƱ",po.getName());
		assertEquals(d,po.getDate());
		assertEquals(vt,po.getVoteType());
		assertEquals(2,po.getQuantity());
	}
	@Test
	//����
	public void setInfo() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		Calendar d = Calendar.getInstance();
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",d,vt,2);
		assertEquals("ͶƱ",po.getName());
		assertEquals(d,po.getDate());
		assertEquals(vt,po.getVoteType());
		assertEquals(2,po.getQuantity());
	}
	@Test
	//��ѡ��������1��
	public void addCandidates0() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		List<String> l = new ArrayList<>();
		Poll<String> po = Poll.create();
		po.addCandidates(l);
	}
	@Test
	//��ѡ������
	public void addCandidates() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		List<String> l = new ArrayList<>();
		l.add("����");
		l.add("����");
		l.add("����");
		Poll<String> po = Poll.create();
		po.addCandidates(l);
	}
	@Test
	//Ԥѡ�������ں�ѡ����
	public void addCandidates1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("����");
		l.add("����");
		l.add("����");
		Poll<String> po = Poll.create();
		po.setInfo("ͶƱ", Calendar.getInstance(),vt,4);
		po.addCandidates(l);
	}
	@Test
	//����
	public void addCandidates2() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("����");
		l.add("����");
		l.add("����");
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ", Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		assertEquals(l,po.getCandidates());
	}
	@Test
	//ͶƱ��Ȩ��Ϊ��
	public void addVoters() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.2);
		m.put(v2,-0.2);
		Poll<String> po = Poll.create();
		po.addVoters(m);
	}
	@Test
	//����
	public void addVoters2() {
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.6);
		m.put(v2,0.2);
		m.put(v3,0.2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.addVoters(m);
		assertEquals(m,po.getVoters());
	}
	@Test
	// ͶƱ�˲���voters������
	public void addVote() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add("����");
		l.add("����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,1.0);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","����");
		VoteItem<String> vi4 = new VoteItem<>("����","֧��");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		Poll<String> po = Poll.create();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
	}
	@Test
	// ͶƱ��ѡ��Ƿ�ѡ��
	public void addVote1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		// �Ƿ�ѡ��
		VoteItem<String> vi4 = new VoteItem<>("����","��֧��");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(0.0,po.getStatistics().get("����"),1e-6);
	}
	@Test
	// ѡƱû�а������к�ѡ��
	public void addVote2() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(0.0,po.getStatistics().get("����"),1e-6);
	}
	@Test
	// ѡƱ�������ں�ѡ������ĺ�ѡ��
	public void addVote3() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		VoteItem<String> vi4 = new VoteItem<>("����","��Ȩ");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(0.0,po.getStatistics().get("����"),1e-6);
	}

	@Test
	// ��ͬһ����ѡ���ظ�ͶƱ
	public void addVote4() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		VoteItem<String> vi4 = new VoteItem<>("����","��Ȩ");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(0.0,po.getStatistics().get("����"),1e-6);
	}
	@Test
	// �������
	public void addVotesNormal() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		VoteItem<String> vi4 = new VoteItem<>("����","֧��");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
	}
	@Test
	// һ��ͶƱ���ύ���ѡƱ
	public void statistics() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		VoteItem<String> vi3 = new VoteItem<>("����","��Ȩ");
		VoteItem<String> vi4 = new VoteItem<>("����","��Ȩ");
		VoteItem<String> vi5 = new VoteItem<>("����","֧��");
		VoteItem<String> vi6 = new VoteItem<>("����","����");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Set<VoteItem<String>> s2 = new HashSet<>();
		s2.add(vi3);
		s2.add(vi4);
		Set<VoteItem<String>> s3 = new HashSet<>();
		s3.add(vi5);
		s3.add(vi6);
		Vote<String> V1 = new Vote<>(s1);
		Vote<String> V2 = new Vote<>(s2);
		Vote<String> V3 = new Vote<>(s3);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		// ͶƱ��v2�ظ�ͶƱ
		po.addVote(V3,v2);
		po.statistics(new ElectionStat<>());
		assertEquals(1.0,po.getStatistics().get("����"),1e-6);
		assertEquals(0.0,po.getStatistics().get("����"),1e-6);
	}
	@Test
	// ����ͶƱ��δͶƱ
	public void statistics1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<String> l = new ArrayList<>();
		l.add(0,"����");
		l.add(1,"����");
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.5);
		m.put(v2,0.5);
		VoteItem<String> vi1 = new VoteItem<>("����","֧��");
		VoteItem<String> vi2 = new VoteItem<>("����","����");
		Set<VoteItem<String>> s1 = new HashSet<>();
		s1.add(vi1);
		s1.add(vi2);
		Vote<String> V1 = new Vote<>(s1);
		GeneralPollImpl<String> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.statistics(new ElectionStat<>());
		assertNull(po.getStatistics().get("����"));
		assertNull(po.getStatistics().get("����"));
	}
	@Test
	// ����Ӧ��1
	public void statisticsBusiness() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("�᰸",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"֧��");
		VoteItem<Proposal> vi2 = new VoteItem<>(p,"����");
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
		Vote<Proposal> V1 = new Vote<>(s1);
		Vote<Proposal> V2 = new Vote<>(s2);
		Vote<Proposal> V3 = new Vote<>(s3);
		Vote<Proposal> V4 = new Vote<>(s4);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new BusinessStat<>());
		assertEquals(0.5,po.getStatistics().get(p),1e-6);
	}
	@Test
	// ����Ӧ��2
	public void statisticsElection() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("����",19);
		Person p2 = new Person("����",20);
		Person p3 = new Person("����",19);
		Person p4 = new Person("����",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"֧��"));
		s1.add(new VoteItem<>(p2,"����"));
		s1.add(new VoteItem<>(p3,"��Ȩ"));
		s1.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"֧��"));
		s2.add(new VoteItem<>(p2,"֧��"));
		s2.add(new VoteItem<>(p3,"��Ȩ"));
		s2.add(new VoteItem<>(p4,"��Ȩ"));
		Set<VoteItem<Person>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"֧��"));
		s3.add(new VoteItem<>(p2,"֧��"));
		s3.add(new VoteItem<>(p3,"֧��"));
		s3.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"����"));
		s4.add(new VoteItem<>(p2,"��Ȩ"));
		s4.add(new VoteItem<>(p3,"��Ȩ"));
		s4.add(new VoteItem<>(p4,"֧��"));
		Vote<Person> V1 = new Vote<>(s1);
		Vote<Person> V2 = new Vote<>(s2);
		Vote<Person> V3 = new Vote<>(s3);
		Vote<Person> V4 = new Vote<>(s4);
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new ElectionStat<>());
		assertEquals(3.0,po.getStatistics().get(p1),1e-6);
		assertEquals(2.0,po.getStatistics().get(p2),1e-6);
		assertEquals(1.0,po.getStatistics().get(p3),1e-6);
		assertEquals(1.0,po.getStatistics().get(p4),1e-6);
	}
	@Test
	// ����Ӧ��3
	public void statisticsDinner() {
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
		s3.add(new VoteItem<>(p4,"��ϲ��"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"ϲ��"));
		s4.add(new VoteItem<>(p2,"ϲ��"));
		s4.add(new VoteItem<>(p3,"����ν"));
		s4.add(new VoteItem<>(p4,"��ϲ��"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new DinnerStat<>());
		assertEquals(1.2,po.getStatistics().get(p1),1e-6);
		assertEquals(0.8,po.getStatistics().get(p2),1e-6);
		assertEquals(1.1,po.getStatistics().get(p3),1e-6);
		assertEquals(0.6,po.getStatistics().get(p4),1e-6);
	}
	@Test
	// ����Ӧ��1
	public void selectionBusiness() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("�᰸",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
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
		Vote<Proposal> V1 = new Vote<>(s1);
		Vote<Proposal> V2 = new Vote<>(s2);
		Vote<Proposal> V3 = new Vote<>(s3);
		Vote<Proposal> V4 = new Vote<>(s4);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new BusinessStat<>());
		po.selection(new BusinessSelect<>());
		Map<Proposal,Double> ref = new HashMap<>();
		ref.put(p,0.7);
		assertEquals(ref.keySet(),po.getResults().keySet());
		assertEquals(ref.get(p),po.getResults().get(p),1e-6);
	}
	@Test
	// ����Ӧ��2
	public void selectionElection() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("����",19);
		Person p2 = new Person("����",20);
		Person p3 = new Person("����",19);
		Person p4 = new Person("����",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"֧��"));
		s1.add(new VoteItem<>(p2,"����"));
		s1.add(new VoteItem<>(p3,"��Ȩ"));
		s1.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"֧��"));
		s2.add(new VoteItem<>(p2,"֧��"));
		s2.add(new VoteItem<>(p3,"��Ȩ"));
		s2.add(new VoteItem<>(p4,"��Ȩ"));
		Set<VoteItem<Person>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"֧��"));
		s3.add(new VoteItem<>(p2,"֧��"));
		s3.add(new VoteItem<>(p3,"֧��"));
		s3.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"����"));
		s4.add(new VoteItem<>(p2,"��Ȩ"));
		s4.add(new VoteItem<>(p3,"��Ȩ"));
		s4.add(new VoteItem<>(p4,"֧��"));
		Vote<Person> V1 = new Vote<>(s1);
		Vote<Person> V2 = new Vote<>(s2);
		Vote<Person> V3 = new Vote<>(s3);
		Vote<Person> V4 = new Vote<>(s4);
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new ElectionStat<>());
		po.selection(new ElectionSelect<>());
		Map<Person,Double> ref = new HashMap<>();
		ref.put(p1,3.0);
		ref.put(p2,2.0);
		assertEquals(ref.keySet(),po.getResults().keySet());
		assertEquals(ref.get(p1),po.getResults().get(p1),1e-6);
		assertEquals(ref.get(p2),po.getResults().get(p2),1e-6);
	}
	@Test
	// ����Ӧ��3
	public void selectionDinner() {
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
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		po.statistics(new DinnerStat<>());
		po.selection(new DinnerSelect<>());
		assertEquals(3,po.getResults().size());
		assertTrue(po.getResults().containsKey(p1));
		assertTrue(po.getResults().containsKey(p2));
		assertTrue(po.getResults().containsKey(p2)||po.getResults().containsKey(p4));
	}
	// ����Ӧ��3����result����
	@Test
	public void result() {
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
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
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
	@Test
	// ����Ӧ��1����accept������ѡƱȫ�ǷǷ�ѡƱ
	public void testAccept1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Proposal> l = new ArrayList<>();
		Proposal p = new Proposal("�᰸",Calendar.getInstance());
		l.add(p);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		VoteItem<Proposal> vi1 = new VoteItem<>(p,"support");
		VoteItem<Proposal> vi2 = new VoteItem<>(new Proposal("���᰸",Calendar.getInstance()),"֧��");
		VoteItem<Proposal> vi3 = new VoteItem<>(p,"like");
		VoteItem<Proposal> vi4 = new VoteItem<>(p,"֧��");
		VoteItem<Proposal> vi5 = new VoteItem<>(p,"֧��");
		Set<VoteItem<Proposal>> s1 = new HashSet<>();
		s1.add(vi1);
		Set<VoteItem<Proposal>> s2 = new HashSet<>();
		s2.add(vi2);
		Set<VoteItem<Proposal>> s3 = new HashSet<>();
		s3.add(vi3);
		Set<VoteItem<Proposal>> s4 = new HashSet<>();
		s4.add(vi4);
		Set<VoteItem<Proposal>> s5 = new HashSet<>();
		s5.add(vi5);
		GeneralPollImpl<Proposal> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,1);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(new Vote<>(s1),v1);
		po.addVote(new Vote<>(s2),v2);
		po.addVote(new Vote<>(s3),v3);
		po.addVote(new Vote<>(s4),v4);
		po.addVote(new Vote<>(s5),v4);
		assertEquals(0.0,po.accept(new LegalRatioVisitor<Proposal>()),1e-6);
	}
	@Test
	// ����Ӧ��2����accept������ѡƱȫ�ǺϷ�ѡƱ
	public void testAccept2() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		List<Person> l = new ArrayList<>();
		Person p1 = new Person("����",19);
		Person p2 = new Person("����",20);
		Person p3 = new Person("����",19);
		Person p4 = new Person("����",21);
		l.add(0,p1);
		l.add(1,p2);
		l.add(2,p3);
		l.add(3,p4);
		Voter v1 = new Voter("v1");
		Voter v2 = new Voter("v2");
		Voter v3 = new Voter("v3");
		Voter v4 = new Voter("v4");
		Map<Voter,Double> m = new HashMap<>();
		m.put(v1,0.1);
		m.put(v2,0.2);
		m.put(v3,0.3);
		m.put(v4,0.4);
		Set<VoteItem<Person>> s1 = new HashSet<>();
		s1.add(new VoteItem<>(p1,"֧��"));
		s1.add(new VoteItem<>(p2,"����"));
		s1.add(new VoteItem<>(p3,"��Ȩ"));
		s1.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"֧��"));
		s2.add(new VoteItem<>(p2,"֧��"));
		s2.add(new VoteItem<>(p3,"��Ȩ"));
		s2.add(new VoteItem<>(p4,"��Ȩ"));
		Set<VoteItem<Person>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"֧��"));
		s3.add(new VoteItem<>(p2,"֧��"));
		s3.add(new VoteItem<>(p3,"֧��"));
		s3.add(new VoteItem<>(p4,"����"));
		Set<VoteItem<Person>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"����"));
		s4.add(new VoteItem<>(p2,"��Ȩ"));
		s4.add(new VoteItem<>(p3,"��Ȩ"));
		s4.add(new VoteItem<>(p4,"֧��"));
		GeneralPollImpl<Person> po = new GeneralPollImpl<>();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(new Vote<>(s1),v1);
		po.addVote(new Vote<>(s2),v2);
		po.addVote(new Vote<>(s3),v3);
		po.addVote(new Vote<>(s4),v4);
		assertEquals(1.0,po.accept(new LegalRatioVisitor<Person>()),1e-6);
	}
	@Test
	// ����Ӧ��3����accept���������кϷ�ѡƱ�����зǷ�ѡƱ
	public void testAccept3() {
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
		s1.add(new VoteItem<>(p1,"like"));
		s1.add(new VoteItem<>(p2,"����ν"));
		s1.add(new VoteItem<>(p3,"ϲ��"));
		s1.add(new VoteItem<>(p4,"��ϲ��"));
		Set<VoteItem<Dish>> s2 = new HashSet<>();
		s2.add(new VoteItem<>(p1,"ϲ��"));
		s2.add(new VoteItem<>(p3,"��ϲ��"));
		s2.add(new VoteItem<>(p4,"ϲ��"));
		Set<VoteItem<Dish>> s3 = new HashSet<>();
		s3.add(new VoteItem<>(p1,"��ϲ��"));
		s3.add(new VoteItem<>(p2,"����ν"));
		s3.add(new VoteItem<>(p3,"����ν"));
		s3.add(new VoteItem<>(p4,"����ν"));
		s3.add(new VoteItem<>(new Dish("������",79.0),"ϲ��"));
		Set<VoteItem<Dish>> s4 = new HashSet<>();
		s4.add(new VoteItem<>(p1,"ϲ��"));
		s4.add(new VoteItem<>(p2,"ϲ��"));
		s4.add(new VoteItem<>(p3,"����ν"));
		s4.add(new VoteItem<>(p4,"��ϲ��"));
		Vote<Dish> V1 = new Vote<>(s1);
		Vote<Dish> V2 = new Vote<>(s2);
		Vote<Dish> V3 = new Vote<>(s3);
		Vote<Dish> V4 = new Vote<>(s4);
//		GeneralPollImpl<Dish> po = new GeneralPollImpl<>();
		DinnerOrder po = new DinnerOrder();
		po.setInfo("ͶƱ",Calendar.getInstance(),vt,3);
		po.addCandidates(l);
		po.addVoters(m);
		po.addVote(V1,v1);
		po.addVote(V2,v2);
		po.addVote(V3,v3);
		po.addVote(V4,v4);
		assertEquals(0.25,po.accept(new LegalRatioVisitor<Dish>()),1e-6);
	}
}
