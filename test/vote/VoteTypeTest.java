package vote;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VoteTypeTest {

	// test strategy
	// 构造函数VoteType(Map<String, Integer> ops):针对选项是空串、分数重复抛出异常
	// checkLegality:输入的option存在、不存在
	// getScoreByOption:输入的option存在、不存在
	// Equals:两个VoteType对象:options内容相同、内容不同
	// HashCode:两个VoteType对象的HashCode:options内容相同、内容不同
	// 构造函数VoteType(String regex):
	// 文字部分长度<1/1/3/5/>5
	// 文字部分是/否出现空格
	// 投票选项对应的分数是/否有小数
	// 正整数是/否使用“+”
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void testConstructor1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("选项不能是空串");
		Map<String,Integer> map = new HashMap<>();
		map.put("",1);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
	}
	@Test
	public void testConstructor2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("不同选项的分数应不同");
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("反对",1);
		VoteType vt = new VoteType(map);
	}
	@Test
	public void checkLegality() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		assertTrue(vt.checkLegality("支持"));
		assertFalse(vt.checkLegality("弃权"));
	}
	@Test
	public void getScoreByOption1() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		assertEquals(1,vt.getScoreByOption("支持"));
		assertEquals(0,vt.getScoreByOption("弃权"));
		assertEquals(-1,vt.getScoreByOption("反对"));
	}
	@Test
	public void getScoreByOption2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("没有该选项");
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		VoteType vt = new VoteType(map);
		vt.getScoreByOption("赞成");
	}
	@Test
	public void testEquals() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("支持",2);
		map1.put("弃权",0);
		map1.put("反对",-2);
		VoteType vt1 = new VoteType(map);
		VoteType vt2 = new VoteType(map);
		VoteType vt3 = new VoteType(map1);
		assertEquals(vt1, vt2);
		assertNotEquals(vt1, vt3);
	}

	@Test
	public void testHashCode() {
		Map<String,Integer> map = new HashMap<>();
		map.put("支持",1);
		map.put("弃权",0);
		map.put("反对",-1);
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("支持",2);
		map1.put("弃权",0);
		map1.put("反对",-2);
		VoteType vt1 = new VoteType(map);
		VoteType vt2 = new VoteType(map);
		VoteType vt3 = new VoteType(map1);
		assertEquals(vt1.hashCode(),vt2.hashCode());
		assertNotEquals(vt1.hashCode(), vt3.hashCode());
	}
	// 构造函数VoteType(String regex):
	// 文字部分长度<1/1/3/5/>5
	// 文字部分是/否出现空格
	// 投票选项对应的分数包含正整数/0/负整数
	// 投票选项对应的分数是/否有小数
	// 正整数是/否使用“+”

	// 文字部分长度>5
	@Test
	public void testRegexConstructor1() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("格式不符合要求");
		String regex = "“真的非常喜欢”(2)|“不喜欢”(0)|“无所谓”(1)";
		VoteType vt = new VoteType(regex);
	}
	// 文字部分长度<1
	@Test
	public void testRegexConstructor2() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("格式不符合要求");
		String regex = "“喜欢”(2)|“”(0)|“无所谓”(1)";
		VoteType vt = new VoteType(regex);
	}
	// 文字部分出现空格
	@Test
	public void testRegexConstructor3() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("格式不符合要求");
		String regex = "“喜欢”(2)|“”(0)|“无 所 谓”(1)";
		VoteType vt = new VoteType(regex);
	}
	// 投票选项对应的分数有小数
	@Test
	public void testRegexConstructor4() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("格式不符合要求");
		String regex = "“喜欢”(1.5)|“不喜欢”(0)|“无所谓”(1)";
		VoteType vt = new VoteType(regex);
	}
	// 投票选项对应分数是正整数，但使用了“+”
	@Test
	public void testRegexConstructor5() throws IllegalArgumentException{
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("格式不符合要求");
		String regex = "“喜欢”(2)|“不喜欢”(0)|“无所谓”(+1)";
		VoteType vt = new VoteType(regex);
	}
	// 正常情况
	@Test
	public void testRegexConstructor6(){
		// 文字部分长度1/3/5
		String regex1 = "“好”(2)|“不是很喜欢”(0)|“无所谓”(1)";
		// 投票选项对应的分数包含正整数/0/负整数
		String regex2 = "“喜欢”(2)|“不喜欢”(0)|“无所谓”(-1)";
		// 另一种，即没有分数的表达式，文字部分长度1/3/5
		String regex3 = "“真的很喜欢”|“不喜欢”|“无”";
		VoteType vt1 = new VoteType(regex1);
		VoteType vt2 = new VoteType(regex2);
		VoteType vt3 = new VoteType(regex3);
		assertEquals(2,vt1.getScoreByOption("好"));
		assertEquals(0,vt1.getScoreByOption("不是很喜欢"));
		assertEquals(1,vt1.getScoreByOption("无所谓"));
		assertEquals(2,vt2.getScoreByOption("喜欢"));
		assertEquals(0,vt2.getScoreByOption("不喜欢"));
		assertEquals(-1,vt2.getScoreByOption("无所谓"));
		assertEquals(1,vt3.getScoreByOption("真的很喜欢"));
		assertEquals(1,vt3.getScoreByOption("不喜欢"));
		assertEquals(1,vt3.getScoreByOption("无"));
	}
}
