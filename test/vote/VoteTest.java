package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class VoteTest {

	// test strategy
	// getVoteItems:是否与输入一致
	// candidateIncluded:候选人不存在、候选人存在
	// testEquals:两个Vote对象:VoteItems、date相同或不同
	// testHashCode:两个Vote对象的hash值:VoteItems、date相同或不同

//	@Rule
//	public ExpectedException expectedEx = ExpectedException.none();
//	@Test
//	public void testVoteItem() throws IllegalArgumentException{
//		expectedEx.expect(IllegalArgumentException.class);
//		expectedEx.expectMessage("一个候选人只能对应一个投票选项");
//		VoteItem<String> vi = new VoteItem<>("张三","支持");
//		VoteItem<String> vi1 = new VoteItem<>("张三","反对");
//		Set<VoteItem<String>> set = new HashSet<>();
//		set.add(vi);
//		set.add(vi1);
//		Vote<String> vote = new Vote<>(set);
//	}

	@Test
	public void getVoteItems() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		assertEquals(set,vote.getVoteItems());
	}

	@Test
	public void candidateIncluded() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		Set<VoteItem<String>> set = new HashSet<>();
		set.add(vi);
		set.add(vi1);
		Vote<String> vote = new Vote<>(set);
		assertTrue(vote.candidateIncluded("张三"));
		assertFalse(vote.candidateIncluded("王五"));
	}

	@Test
	public void testEquals() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		VoteItem<String> vi2 = new VoteItem<>("王五","弃权");
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
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		VoteItem<String> vi2 = new VoteItem<>("王五","弃权");
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
