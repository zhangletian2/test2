package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VoteTypeTest {

	// test strategy
	// ���캯��VoteType(Map<String, Integer> ops):���ѡ���ǿմ��������ظ��׳��쳣
	// checkLegality:�����option���ڡ�������
	// getScoreByOption:�����option���ڡ�������
	// Equals:����VoteType����:options������ͬ�����ݲ�ͬ
	// HashCode:����VoteType�����HashCode:options������ͬ�����ݲ�ͬ
	// ���캯��VoteType(String regex):
	// ���ֲ��ֳ���<1/1/3/5/>5
	// ���ֲ�����/����ֿո�
	// ͶƱѡ���Ӧ�ķ�����/����С��
	// ��������/��ʹ�á�+��
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void testConstructor1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("ѡ����ǿմ�");
		Map<String,Integer> map = new HashMap<>();
		map.put("",1);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
	}
	@Test
	public void testConstructor2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ͬѡ��ķ���Ӧ��ͬ");
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("����",1);
		VoteType vt = new VoteType(map);
	}
	@Test
	public void checkLegality() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		assertTrue(vt.checkLegality("֧��"));
		assertFalse(vt.checkLegality("��Ȩ"));
	}
	@Test
	public void getScoreByOption1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		assertEquals(1,vt.getScoreByOption("֧��"));
		assertEquals(0,vt.getScoreByOption("��Ȩ"));
		assertEquals(-1,vt.getScoreByOption("����"));
	}
	@Test
	public void getScoreByOption2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("û�и�ѡ��");
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		VoteType vt = new VoteType(map);
		vt.getScoreByOption("�޳�");
	}
	@Test
	public void testEquals() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("֧��",2);
		map1.put("��Ȩ",0);
		map1.put("����",-2);
		VoteType vt1 = new VoteType(map);
		VoteType vt2 = new VoteType(map);
		VoteType vt3 = new VoteType(map1);
		assertEquals(vt1, vt2);
		assertNotEquals(vt1, vt3);
	}

	@Test
	public void testHashCode() {
		Map<String,Integer> map = new HashMap<>();
		map.put("֧��",1);
		map.put("��Ȩ",0);
		map.put("����",-1);
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("֧��",2);
		map1.put("��Ȩ",0);
		map1.put("����",-2);
		VoteType vt1 = new VoteType(map);
		VoteType vt2 = new VoteType(map);
		VoteType vt3 = new VoteType(map1);
		assertEquals(vt1.hashCode(),vt2.hashCode());
		assertNotEquals(vt1.hashCode(), vt3.hashCode());
	}
	// ���캯��VoteType(String regex):
	// ���ֲ��ֳ���<1/1/3/5/>5
	// ���ֲ�����/����ֿո�
	// ͶƱѡ���Ӧ�ķ�������������/0/������
	// ͶƱѡ���Ӧ�ķ�����/����С��
	// ��������/��ʹ�á�+��

	// ���ֲ��ֳ���>5
	@Test
	public void testRegexConstructor1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ʽ������Ҫ��");
		String regex = "����ķǳ�ϲ����(2)|����ϲ����(0)|������ν��(1)";
		VoteType vt = new VoteType(regex);
	}
	// ���ֲ��ֳ���<1
	@Test
	public void testRegexConstructor2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ʽ������Ҫ��");
		String regex = "��ϲ����(2)|����(0)|������ν��(1)";
		VoteType vt = new VoteType(regex);
	}
	// ���ֲ��ֳ��ֿո�
	@Test
	public void testRegexConstructor3() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ʽ������Ҫ��");
		String regex = "��ϲ����(2)|����(0)|���� �� ν��(1)";
		VoteType vt = new VoteType(regex);
	}
	// ͶƱѡ���Ӧ�ķ�����С��
	@Test
	public void testRegexConstructor4() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ʽ������Ҫ��");
		String regex = "��ϲ����(1.5)|����ϲ����(0)|������ν��(1)";
		VoteType vt = new VoteType(regex);
	}
	// ͶƱѡ���Ӧ����������������ʹ���ˡ�+��
	@Test
	public void testRegexConstructor5() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("��ʽ������Ҫ��");
		String regex = "��ϲ����(2)|����ϲ����(0)|������ν��(+1)";
		VoteType vt = new VoteType(regex);
	}
	// �������
	@Test
	public void testRegexConstructor6(){
		// ���ֲ��ֳ���1/3/5
		String regex1 = "���á�(2)|�����Ǻ�ϲ����(0)|������ν��(1)";
		// ͶƱѡ���Ӧ�ķ�������������/0/������
		String regex2 = "��ϲ����(2)|����ϲ����(0)|������ν��(-1)";
		// ��һ�֣���û�з����ı��ʽ�����ֲ��ֳ���1/3/5
		String regex3 = "����ĺ�ϲ����|����ϲ����|���ޡ�";
		VoteType vt1 = new VoteType(regex1);
		VoteType vt2 = new VoteType(regex2);
		VoteType vt3 = new VoteType(regex3);
		assertEquals(2,vt1.getScoreByOption("��"));
		assertEquals(0,vt1.getScoreByOption("���Ǻ�ϲ��"));
		assertEquals(1,vt1.getScoreByOption("����ν"));
		assertEquals(2,vt2.getScoreByOption("ϲ��"));
		assertEquals(0,vt2.getScoreByOption("��ϲ��"));
		assertEquals(-1,vt2.getScoreByOption("����ν"));
		assertEquals(1,vt3.getScoreByOption("��ĺ�ϲ��"));
		assertEquals(1,vt3.getScoreByOption("��ϲ��"));
		assertEquals(1,vt3.getScoreByOption("��"));
	}
}
