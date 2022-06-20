package poll;

import java.text.SimpleDateFormat;
import java.util.*;

import auxiliary.Voter;
import pattern.SelectionStrategy;
import pattern.StatisticsStrategy;
import vote.VoteItem;
import vote.VoteType;
import vote.Vote;
import vote.voteInterface;

public class GeneralPollImpl<C> implements Poll<C> {

	// 投票活动的名称
	protected String name;
	// 投票活动发起的时间
	protected Calendar date;
	// 候选对象集合
	protected List<C> candidates = new ArrayList<>();
	// 投票人集合，key为投票人，value为其在本次投票中所占权重
	protected Map<Voter, Double> voters = new HashMap<>();
	// 拟选出的候选对象最大数量
	protected int quantity;
	// 本次投票拟采用的投票类型（合法选项及各自对应的分数）
	protected VoteType voteType;
	// 所有选票集合
	protected Set<voteInterface<C>> votes = new HashSet<>();
	// 计票结果，key为候选对象，value为其得分
	protected Map<C, Double> statistics = new HashMap<>();
	// 遴选结果，key为候选对象，value为其排序位次
	protected Map<C, Double> results = new HashMap<>();
	// 投票人提交选票次数(added)
	protected final Map<Voter,Integer> submitTimes = new HashMap<>();
	// 合法选票(added)
	protected Map<Voter,voteInterface<C>> legalVotes = new HashMap<>();
	// 是否添加过候选对象、投票人
	protected boolean flagCan=false,flagVot=false,flagStat = false;

	// Rep Invariants
	// 预选数量大于等于0
	// 候选对象个数不少于1
	// 没有重名的候选对象
	// 没有重名的投票人（map可以保证）
	// 投票人只能对应唯一的权重（map可以保证）
	// 投票人集合voters中权重非负
	// 拟选出的人数不大于候选人数
	// 投票人必须在voters里
	// Abstract Function
	// AF(name,date,candidates,voters,quantity,voteType,votes,statistics,results)
	// = 一次投票，包括名称、时间、候选对象、投票人、预选数量、投票类型、选票，计票和遴选结果
	// Safety from Rep Exposure
	// 所有成员变量都是protected
	// 可变类型赋值给成员变量时，new一个新对象，而不是直接赋值

	protected void checkRep() {
		assert quantity>=0;
		Set<C> set = new HashSet<>(candidates);
		assert !flagCan || !flagVot || set.size() >= 1;
		assert set.size() == candidates.size();// 没有重名的候选对象
		// 投票人集合voters中权重非负
		if(!voters.isEmpty()){
			for(Double i: voters.values()){
				assert i>=0;
			}
		}
		assert quantity <= candidates.size();// 拟选出的人数不大于候选人数
//		int totalVotes = 0;// 实际投票人数小于等于投票人数量
//		for(int i: submitTimes.values()){
//			totalVotes+=i;
//		}
//		assert totalVotes<=voters.size();
		for(Voter v: submitTimes.keySet()){// 投票人必须在voters里
			assert voters.containsKey(v);
		}
	}

	/**
	 * 构造函数
	 */
	public GeneralPollImpl() {
		this.quantity = 0;	// 对quantity初始化
	}
	@Override
	public void setInfo(String name, Calendar date, VoteType type, int quantity) {
		if(quantity<0){
			throw new IllegalArgumentException("预选数量必须非负");
		}
		this.name = name;
		this.date = date;
		Map<String, Integer> options = new HashMap<>(type.getOptions());
		this.voteType = new VoteType(options);
		this.quantity = quantity;
	}

	@Override
	public void addVoters(Map<Voter, Double> voters) {
		for(Double i: voters.values()){
			if(i<0){
				throw new IllegalArgumentException("投票人权重不能为负数");
			}
		}
		this.voters = new HashMap<>(voters);
		for(Voter v:this.voters.keySet()){
			submitTimes.put(v,0);
		}
		flagVot = true;
		checkRep();
	}

	@Override
	public void addCandidates(List<C> candidates) {
		Set<C> set = new HashSet<>(candidates);
		if(set.size()<1){
			throw new IllegalArgumentException("候选对象不能不少于一个");
		}
		if(set.size() < candidates.size()){
			throw new IllegalArgumentException("候选人不能重名");
		}
		if(set.size()<quantity){
			throw new IllegalArgumentException("预选人数须小于候选人数");
		}
		this.candidates = new ArrayList<>(candidates);
		flagCan = true;
		checkRep();
	}

	@Override
	public void addVote(voteInterface<C> vote, Voter v) {
		// 投票人不在voters名单中
		if(!voters.containsKey(v)){
			throw new IllegalArgumentException("投票人不在voters名单中");
		}
		// 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
		//? 一张选票中没有包含本次投票活动中的所有候选人
		//? 一张选票中包含了不在本次投票活动中的候选人
		//? 一张选票中出现了本次投票不允许的选项值
		//? 一张选票中有对同一个候选对象的多次投票
		votes.add(vote);// 不管合法不合法都要先接收
		int voteNum = 0;
		int flag = 1;
		Set<C> can = new HashSet<>(candidates);//候选人集合
		Set<C> voteCan = new HashSet<>();//选票中的候选人集合
		for(VoteItem<C> vi:vote.getVoteItems()){
			if(!voteType.getOptions().containsKey(vi.getVoteValue())){
				System.out.println("选票非法！出现了本次投票不允许的选项值");
				flag = 0;
			}
			voteNum++;
			voteCan.add(vi.getCandidate());
		}
		if(!can.equals(voteCan)||voteNum!= can.size()){
			System.out.println("选票非法！包含了不在本次投票中的候选人，或没有包含所有候选人，或对同一名候选人重复投票");
			flag = 0;
		}
		//若一个投票人提交了多次选票，则它们均为非法
		if(submitTimes.get(v)!=0){
			legalVotes.remove(v);
			flag = 0;
		}
		if(flag == 1){
			legalVotes.put(v,vote);
		}
		int preTimes = submitTimes.get(v);
		submitTimes.put(v,preTimes+1);
		checkRep();
	}

	@Override
	public void statistics(StatisticsStrategy<C> ss) {
		// 此处应首先检查当前所拥有的选票的合法性
		// ? 若尚有投票人还未投票，则不能开始计票；
		// ? 若一个投票人提交了多次选票，则它们均为非法，计票时不计算在内（addVote已检查）
		for(int i: submitTimes.values()){
			if(i == 0){
				System.out.println("尚有投票人还未投票，不能开始计票");
				return;
			}
		}
		statistics = ss.stat(candidates,voters,legalVotes,voteType);
		flagStat = true;
	}

	@Override
	public void selection(SelectionStrategy<C> ss) {
		if(!flagStat){
			System.out.println("请先进行统计然后再遴选！");
			return;
		}
		results = ss.select(statistics,quantity);
	}

	@Override
	public String result() {
		int serial = 0;
		double lastValue = -1.0;
		StringBuilder sb = new StringBuilder();
		sb.append("候选对象ID");
		sb.append('\t');
		sb.append("排序");
		sb.append('\n');
		Comparator<Map.Entry<C, Double>> valueComparator = new Comparator<Map.Entry<C, Double>>() {
			@Override
			public int compare(Map.Entry<C, Double> o1,
							   Map.Entry<C, Double> o2) {
				if(o2.getValue()-o1.getValue()>1e-6) return 1;
				else if(Math.abs(o1.getValue()- o2.getValue())<1e-6) return 0;
				else return -1;
			}
		};
		// map转换成list进行排序
		List<Map.Entry<C, Double>> list = new ArrayList<Map.Entry<C, Double>>(results.entrySet());
		// 排序
		list.sort(valueComparator);
		// 构造字符串
		for(int i=0;i<=list.size()-1;i++){
			Map.Entry<C,Double> entry = list.get(i);
			int id = candidates.indexOf(entry.getKey());
			// 得分不同的名次不同，得分相同的名次相同
			if(Math.abs(lastValue-entry.getValue())>1e-6){
				serial++;
			}
			sb.append(id);
			sb.append('\t');
			sb.append('\t');
			sb.append('\t');
			sb.append(serial);
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public double accept(Visitor<C> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "GeneralPollImpl{" +
				"name='" + name + '\'' +
				", date=" + format.format(date.getTime()) +
				", number of candidates=" + candidates.size() +
				",number of voters=" + voters.size() +
				", quantity=" + quantity +
				'}';
	}

	public String getName() {
		return name;
	}

	public Calendar getDate() {
		return date;
	}

	public int getQuantity() {
		return quantity;
	}

	public VoteType getVoteType() {
		return new VoteType(this.voteType.getOptions());
	}

	public List<C> getCandidates() {
		return new ArrayList<>(this.candidates);
	}

	public Map<Voter, Double> getVoters() {
		return new HashMap<>(this.voters);
	}

	public Map<C, Double> getStatistics() {
		return new HashMap<>(statistics);
	}

	public Map<C, Double> getResults() {
		return new HashMap<>(results);
	}
	@Override
	public Set<voteInterface<C>> getLegalVotes(){
		return new HashSet<>(legalVotes.values());
	}

	@Override
	public Set<voteInterface<C>> getVotes() {
		return new HashSet<>(votes);
	}

}
