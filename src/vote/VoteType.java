package vote;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//immutable
public class VoteType {

	// key为选项名、value为选项名对应的分数
	private Map<String, Integer> options = new HashMap<>();

	// Rep Invariants
	// 没有同名的选项（map可以保证）
	// 选项不能是空串
	// Abstract Function
	// AF(options) = 投票选项和相应分数的一一对应关系
	// Safety from Rep Exposure
	// 成员变量都是private类型
	// 构造函数中对参数options进行深拷贝，避免参数和成员变量options指向同一个对象
	// getOption使用防御式拷贝

	private void checkRep() {
		for(String s: options.keySet()){
			assert !s.equals("");
		}
	}

	/**
	 * 创建一个投票类型对象
	 * 
	 * 可以自行设计该方法所采用的参数
	 */
	public VoteType(Map<String, Integer> ops) {
		for(String s: ops.keySet()){
			if(s.equals("")){
				throw new IllegalArgumentException("选项不能是空串");
			}
		}
		Set<Integer> set = new HashSet<>(ops.values());
		if(set.size() != ops.size()){
			throw new IllegalArgumentException("不同选项的分数应不同");
		}
		this.options.putAll(ops);
		checkRep();
	}

	/**
	 * 根据满足特定语法规则的字符串，创建一个投票类型对象
	 * 
	 * @param regex 遵循特定语法的、包含投票类型信息的字符串（待任务12再考虑）
	 */
	public VoteType(String regex) {
		String s1="",s2="",s3="";
		int i1=1,i2=1,i3=1;
		Map<String, Integer> ops = new HashMap<>();
		Pattern pattern1 = Pattern.compile("“([\u4e00-\u9fa5]{1,5})”\\((-?[1-9]\\d*|0)\\)\\|“([\u4e00-\u9fa5]{1,5})”\\((-?[1-9]\\d*|0)\\)\\|“([\u4e00-\u9fa5]{1,5})”\\((-?[1-9]\\d*|0)\\)");
		Pattern pattern2 = Pattern.compile("“([\u4e00-\u9fa5]{1,5})”\\|“([\u4e00-\u9fa5]{1,5})”\\|“([\u4e00-\u9fa5]{1,5})”");
		Matcher m1 = pattern1.matcher(regex);
		Matcher m2 = pattern2.matcher(regex);
		if(m1.matches()){
			s1 = m1.group(1);
			i1 = Integer.parseInt(m1.group(2));
			s2 = m1.group(3);
			i2 = Integer.parseInt(m1.group(4));
			s3 = m1.group(5);
			i3 = Integer.parseInt(m1.group(6));
		}
		else if(m2.matches()){
			s1 = m2.group(1);
			s2 = m2.group(2);
			s3 = m2.group(3);
		}
		else{
			throw new IllegalArgumentException("格式不符合要求");
		}
		ops.put(s1,i1);
		ops.put(s2,i2);
		ops.put(s3,i3);
		this.options.putAll(ops);
		checkRep();
	}

	/**
	 * 判断一个投票选项是否合法（用于Poll中对选票的合法性检查）
	 * 
	 * 例如，投票人给出的投票选项是“Strongly reject”，但options中不包含这个选项，因此它是非法的
	 * 
	 * 不能改动该方法的参数
	 * 
	 * @param option 一个投票选项
	 * @return 合法则true，否则false
	 */
	public boolean checkLegality(String option) {
		if(options.keySet().contains(option)){
			checkRep();
			return true;
		}
		return false;
	}

	/**
	 * 根据一个投票选项，得到其对应的分数
	 * 
	 * 例如，投票人给出的投票选项是“支持”，查询得到其对应的分数是1
	 * 
	 * 不能改动该方法的参数
	 * 
	 * @param option 一个投票项取值
	 * @return 该选项所对应的分数
	 */
	public int getScoreByOption(String option) {
		if(!checkLegality(option)){
			throw new IllegalArgumentException("没有该选项");
		}
		checkRep();
		return options.get(option);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VoteType voteType = (VoteType) o;
		return options.equals(voteType.options);
	}

	@Override
	public int hashCode() {
		return Objects.hash(options);
	}

	public Map<String, Integer> getOptions() {
		return new HashMap<>(this.options);
	}
}
