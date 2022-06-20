package vote;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//immutable
public class VoteType {

	// keyΪѡ������valueΪѡ������Ӧ�ķ���
	private Map<String, Integer> options = new HashMap<>();

	// Rep Invariants
	// û��ͬ����ѡ�map���Ա�֤��
	// ѡ����ǿմ�
	// Abstract Function
	// AF(options) = ͶƱѡ�����Ӧ������һһ��Ӧ��ϵ
	// Safety from Rep Exposure
	// ��Ա��������private����
	// ���캯���жԲ���options�����������������ͳ�Ա����optionsָ��ͬһ������
	// getOptionʹ�÷���ʽ����

	private void checkRep() {
		for(String s: options.keySet()){
			assert !s.equals("");
		}
	}

	/**
	 * ����һ��ͶƱ���Ͷ���
	 * 
	 * ����������Ƹ÷��������õĲ���
	 */
	public VoteType(Map<String, Integer> ops) {
		for(String s: ops.keySet()){
			if(s.equals("")){
				throw new IllegalArgumentException("ѡ����ǿմ�");
			}
		}
		Set<Integer> set = new HashSet<>(ops.values());
		if(set.size() != ops.size()){
			throw new IllegalArgumentException("��ͬѡ��ķ���Ӧ��ͬ");
		}
		this.options.putAll(ops);
		checkRep();
	}

	/**
	 * ���������ض��﷨������ַ���������һ��ͶƱ���Ͷ���
	 * 
	 * @param regex ��ѭ�ض��﷨�ġ�����ͶƱ������Ϣ���ַ�����������12�ٿ��ǣ�
	 */
	public VoteType(String regex) {
		String s1="",s2="",s3="";
		int i1=1,i2=1,i3=1;
		Map<String, Integer> ops = new HashMap<>();
		Pattern pattern1 = Pattern.compile("��([\u4e00-\u9fa5]{1,5})��\\((-?[1-9]\\d*|0)\\)\\|��([\u4e00-\u9fa5]{1,5})��\\((-?[1-9]\\d*|0)\\)\\|��([\u4e00-\u9fa5]{1,5})��\\((-?[1-9]\\d*|0)\\)");
		Pattern pattern2 = Pattern.compile("��([\u4e00-\u9fa5]{1,5})��\\|��([\u4e00-\u9fa5]{1,5})��\\|��([\u4e00-\u9fa5]{1,5})��");
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
			throw new IllegalArgumentException("��ʽ������Ҫ��");
		}
		ops.put(s1,i1);
		ops.put(s2,i2);
		ops.put(s3,i3);
		this.options.putAll(ops);
		checkRep();
	}

	/**
	 * �ж�һ��ͶƱѡ���Ƿ�Ϸ�������Poll�ж�ѡƱ�ĺϷ��Լ�飩
	 * 
	 * ���磬ͶƱ�˸�����ͶƱѡ���ǡ�Strongly reject������options�в��������ѡ�������ǷǷ���
	 * 
	 * ���ܸĶ��÷����Ĳ���
	 * 
	 * @param option һ��ͶƱѡ��
	 * @return �Ϸ���true������false
	 */
	public boolean checkLegality(String option) {
		if(options.keySet().contains(option)){
			checkRep();
			return true;
		}
		return false;
	}

	/**
	 * ����һ��ͶƱѡ��õ����Ӧ�ķ���
	 * 
	 * ���磬ͶƱ�˸�����ͶƱѡ���ǡ�֧�֡�����ѯ�õ����Ӧ�ķ�����1
	 * 
	 * ���ܸĶ��÷����Ĳ���
	 * 
	 * @param option һ��ͶƱ��ȡֵ
	 * @return ��ѡ������Ӧ�ķ���
	 */
	public int getScoreByOption(String option) {
		if(!checkLegality(option)){
			throw new IllegalArgumentException("û�и�ѡ��");
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
