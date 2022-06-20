package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class VoteItemTest {

	// test strategy
	// ���캯��:�ԷǷ������Ƿ��׳��쳣
	// getVoteValue:�����Ƿ�������һ��
	// getCandidate:�����Ƿ�������һ��
	// testEquals:����VoteItem����:candidate��value��ͬ��ͬ
	// testHashCode:����VoteItem�����hashֵ:candidate��value��ͬ��ͬ
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void VoteItem() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ѡ�˲���Ϊ��");
		VoteItem<String> vi = new VoteItem<>(null,"֧��");
	}
	@Test
	public void VoteItem1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("ѡ���Ϊ��");
		VoteItem<String> vi = new VoteItem<>("����","");
	}
	@Test
	public void getVoteValue() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		assertEquals("֧��",vi.getVoteValue());
		assertEquals("����",vi1.getVoteValue());
	}

	@Test
	public void getCandidate() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		assertEquals("����",vi.getCandidate());
		assertEquals("����",vi1.getCandidate());
	}

	@Test
	public void testEquals() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		VoteItem<String> vi2 = new VoteItem<>("����","֧��");
		assertEquals(vi, vi2);
		assertNotEquals(vi, vi1);
	}

	@Test
	public void testHashCode() {
		VoteItem<String> vi = new VoteItem<>("����","֧��");
		VoteItem<String> vi1 = new VoteItem<>("����","����");
		VoteItem<String> vi2 = new VoteItem<>("����","֧��");
		assertEquals(vi.hashCode(), vi2.hashCode());
		assertNotEquals(vi.hashCode(), vi1.hashCode());
	}
}
