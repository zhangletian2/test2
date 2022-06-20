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

	// ͶƱ�������
	protected String name;
	// ͶƱ������ʱ��
	protected Calendar date;
	// ��ѡ���󼯺�
	protected List<C> candidates = new ArrayList<>();
	// ͶƱ�˼��ϣ�keyΪͶƱ�ˣ�valueΪ���ڱ���ͶƱ����ռȨ��
	protected Map<Voter, Double> voters = new HashMap<>();
	// ��ѡ���ĺ�ѡ�����������
	protected int quantity;
	// ����ͶƱ����õ�ͶƱ���ͣ��Ϸ�ѡ����Զ�Ӧ�ķ�����
	protected VoteType voteType;
	// ����ѡƱ����
	protected Set<voteInterface<C>> votes = new HashSet<>();
	// ��Ʊ�����keyΪ��ѡ����valueΪ��÷�
	protected Map<C, Double> statistics = new HashMap<>();
	// ��ѡ�����keyΪ��ѡ����valueΪ������λ��
	protected Map<C, Double> results = new HashMap<>();
	// ͶƱ���ύѡƱ����(added)
	protected final Map<Voter,Integer> submitTimes = new HashMap<>();
	// �Ϸ�ѡƱ(added)
	protected Map<Voter,voteInterface<C>> legalVotes = new HashMap<>();
	// �Ƿ���ӹ���ѡ����ͶƱ��
	protected boolean flagCan=false,flagVot=false,flagStat = false;

	// Rep Invariants
	// Ԥѡ�������ڵ���0
	// ��ѡ�������������1
	// û�������ĺ�ѡ����
	// û��������ͶƱ�ˣ�map���Ա�֤��
	// ͶƱ��ֻ�ܶ�ӦΨһ��Ȩ�أ�map���Ա�֤��
	// ͶƱ�˼���voters��Ȩ�طǸ�
	// ��ѡ�������������ں�ѡ����
	// ͶƱ�˱�����voters��
	// Abstract Function
	// AF(name,date,candidates,voters,quantity,voteType,votes,statistics,results)
	// = һ��ͶƱ���������ơ�ʱ�䡢��ѡ����ͶƱ�ˡ�Ԥѡ������ͶƱ���͡�ѡƱ����Ʊ����ѡ���
	// Safety from Rep Exposure
	// ���г�Ա��������protected
	// �ɱ����͸�ֵ����Ա����ʱ��newһ���¶��󣬶�����ֱ�Ӹ�ֵ

	protected void checkRep() {
		assert quantity>=0;
		Set<C> set = new HashSet<>(candidates);
		assert !flagCan || !flagVot || set.size() >= 1;
		assert set.size() == candidates.size();// û�������ĺ�ѡ����
		// ͶƱ�˼���voters��Ȩ�طǸ�
		if(!voters.isEmpty()){
			for(Double i: voters.values()){
				assert i>=0;
			}
		}
		assert quantity <= candidates.size();// ��ѡ�������������ں�ѡ����
//		int totalVotes = 0;// ʵ��ͶƱ����С�ڵ���ͶƱ������
//		for(int i: submitTimes.values()){
//			totalVotes+=i;
//		}
//		assert totalVotes<=voters.size();
		for(Voter v: submitTimes.keySet()){// ͶƱ�˱�����voters��
			assert voters.containsKey(v);
		}
	}

	/**
	 * ���캯��
	 */
	public GeneralPollImpl() {
		this.quantity = 0;	// ��quantity��ʼ��
	}
	@Override
	public void setInfo(String name, Calendar date, VoteType type, int quantity) {
		if(quantity<0){
			throw new IllegalArgumentException("Ԥѡ��������Ǹ�");
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
				throw new IllegalArgumentException("ͶƱ��Ȩ�ز���Ϊ����");
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
			throw new IllegalArgumentException("��ѡ�����ܲ�����һ��");
		}
		if(set.size() < candidates.size()){
			throw new IllegalArgumentException("��ѡ�˲�������");
		}
		if(set.size()<quantity){
			throw new IllegalArgumentException("Ԥѡ������С�ں�ѡ����");
		}
		this.candidates = new ArrayList<>(candidates);
		flagCan = true;
		checkRep();
	}

	@Override
	public void addVote(voteInterface<C> vote, Voter v) {
		// ͶƱ�˲���voters������
		if(!voters.containsKey(v)){
			throw new IllegalArgumentException("ͶƱ�˲���voters������");
		}
		// �˴�Ӧ���ȼ���ѡƱ�ĺϷ��Բ���ǣ�Ϊ������չ���޸�rep
		//? һ��ѡƱ��û�а�������ͶƱ��е����к�ѡ��
		//? һ��ѡƱ�а����˲��ڱ���ͶƱ��еĺ�ѡ��
		//? һ��ѡƱ�г����˱���ͶƱ�������ѡ��ֵ
		//? һ��ѡƱ���ж�ͬһ����ѡ����Ķ��ͶƱ
		votes.add(vote);// ���ܺϷ����Ϸ���Ҫ�Ƚ���
		int voteNum = 0;
		int flag = 1;
		Set<C> can = new HashSet<>(candidates);//��ѡ�˼���
		Set<C> voteCan = new HashSet<>();//ѡƱ�еĺ�ѡ�˼���
		for(VoteItem<C> vi:vote.getVoteItems()){
			if(!voteType.getOptions().containsKey(vi.getVoteValue())){
				System.out.println("ѡƱ�Ƿ��������˱���ͶƱ�������ѡ��ֵ");
				flag = 0;
			}
			voteNum++;
			voteCan.add(vi.getCandidate());
		}
		if(!can.equals(voteCan)||voteNum!= can.size()){
			System.out.println("ѡƱ�Ƿ��������˲��ڱ���ͶƱ�еĺ�ѡ�ˣ���û�а������к�ѡ�ˣ����ͬһ����ѡ���ظ�ͶƱ");
			flag = 0;
		}
		//��һ��ͶƱ���ύ�˶��ѡƱ�������Ǿ�Ϊ�Ƿ�
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
		// �˴�Ӧ���ȼ�鵱ǰ��ӵ�е�ѡƱ�ĺϷ���
		// ? ������ͶƱ�˻�δͶƱ�����ܿ�ʼ��Ʊ��
		// ? ��һ��ͶƱ���ύ�˶��ѡƱ�������Ǿ�Ϊ�Ƿ�����Ʊʱ���������ڣ�addVote�Ѽ�飩
		for(int i: submitTimes.values()){
			if(i == 0){
				System.out.println("����ͶƱ�˻�δͶƱ�����ܿ�ʼ��Ʊ");
				return;
			}
		}
		statistics = ss.stat(candidates,voters,legalVotes,voteType);
		flagStat = true;
	}

	@Override
	public void selection(SelectionStrategy<C> ss) {
		if(!flagStat){
			System.out.println("���Ƚ���ͳ��Ȼ������ѡ��");
			return;
		}
		results = ss.select(statistics,quantity);
	}

	@Override
	public String result() {
		int serial = 0;
		double lastValue = -1.0;
		StringBuilder sb = new StringBuilder();
		sb.append("��ѡ����ID");
		sb.append('\t');
		sb.append("����");
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
		// mapת����list��������
		List<Map.Entry<C, Double>> list = new ArrayList<Map.Entry<C, Double>>(results.entrySet());
		// ����
		list.sort(valueComparator);
		// �����ַ���
		for(int i=0;i<=list.size()-1;i++){
			Map.Entry<C,Double> entry = list.get(i);
			int id = candidates.indexOf(entry.getKey());
			// �÷ֲ�ͬ�����β�ͬ���÷���ͬ��������ͬ
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
