package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class VoteTest {

	// test strategy
	// getVoteItems:�Ƿ�������һ��
	// candidateIncluded:��ѡ�˲����ڡ���ѡ�˴���
	// testEquals:����Vote����:VoteItems��date��ͬ��ͬ
	// testHashCode:����Vote�����hashֵ:VoteItems��date��ͬ��ͬ

//	@Rule
//	public ExpectedException expectedEx = ExpectedException.none();
//	@Test
//	public void testVoteItem() throws IllegalArgumentException{
//		expectedEx.expect(IllegalArgumentException.class);
//		expectedEx.expectMessage("һ����ѡ��ֻ�ܶ�Ӧһ��ͶƱѡ��");
//		VoteItem<String> vi = new VoteItem<>("����","֧��");
//		VoteItem<String> vi1 = new VoteItem<>("����","����");
//		Set<VoteItem<String>> set = new HashSet<>();
//		set.add(vi);
//		set.add(vi1);
//		Vote<String> vote = new Vote<>(set);
//	}

	@Test
	public void getVoteItems() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		assertEquals(set,vote.getVoteItems());
	}

	@Test
	public void candidateIncluded() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		assertTrue(vote.candidateIncluded("����"));
		assertFalse(vote.candidateIncluded("����"));
	}

	@Test
	public void testEquals() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		VoteItem<String> vi2 = new VoteItem<>("����","��Ȩ");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		set.add(vi2);
		Vote<String> vote1 = new Vote<>(set);
		assertNotEquals(vote,vote1);
		set.remove(vi2);
		Vote<String> vote2 = new Vote<>(set);
		assertEquals(vote,vote2);
	}

	@Test
	public void testHashCode() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		VoteItem<String> vi2 = new VoteItem<>("����","��Ȩ");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		set.add(vi2);
		Vote<String> vote1 = new Vote<>(set);
		assertNotEquals(vote.hashCode(),vote1.hashCode());
		set.remove(vi2);
		Vote<String> vote2 = new Vote<>(set);
		assertEquals(vote.hashCode(),vote2.hashCode());
	}
}
