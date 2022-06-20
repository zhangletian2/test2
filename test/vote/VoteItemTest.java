package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class VoteItemTest {

	// test strategy
	// 构造函数:对非法输入是否抛出异常
	// getVoteValue:测试是否与输入一致
	// getCandidate:测试是否与输入一致
	// testEquals:两个VoteItem对象:candidate、value相同或不同
	// testHashCode:两个VoteItem对象的hash值:candidate、value相同或不同
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void VoteItem() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("候选人不能为空");
		VoteItem<String> vi = new VoteItem<>(null,"支持");
	}
	@Test
	public void VoteItem1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("选项不能为空");
		VoteItem<String> vi = new VoteItem<>("李四","");
	}
	@Test
	public void getVoteValue() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		assertEquals("支持",vi.getVoteValue());
		assertEquals("反对",vi1.getVoteValue());
	}

	@Test
	public void getCandidate() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		assertEquals("张三",vi.getCandidate());
		assertEquals("李四",vi1.getCandidate());
	}

	@Test
	public void testEquals() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		VoteItem<String> vi2 = new VoteItem<>("张三","支持");
		assertEquals(vi, vi2);
		assertNotEquals(vi, vi1);
	}

	@Test
	public void testHashCode() {
		VoteItem<String> vi = new VoteItem<>("张三","支持");
		VoteItem<String> vi1 = new VoteItem<>("李四","反对");
		VoteItem<String> vi2 = new VoteItem<>("张三","支持");
		assertEquals(vi.hashCode(), vi2.hashCode());
		assertNotEquals(vi.hashCode(), vi1.hashCode());
	}
}
