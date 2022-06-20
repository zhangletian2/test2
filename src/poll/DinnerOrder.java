package poll;

import auxiliary.Dish;
import auxiliary.Proposal;
import auxiliary.Voter;
import pattern.BusinessSelect;
import pattern.BusinessStat;
import pattern.DinnerSelect;
import pattern.DinnerStat;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;
import vote.voteInterface;

import java.util.*;

public class DinnerOrder extends GeneralPollImpl<Dish>  {
    // Rep Invariants
    // Ԥѡ����С��ͶƱ����+5�����ϸ�
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
    // = һ����ҵ�᰸ͶƱ���������ơ�ʱ�䡢��ѡ����ͶƱ�ˡ�Ԥѡ������ͶƱ���͡�ѡƱ����Ʊ����ѡ���
    // Safety from Rep Exposure
    // ���г�Ա��������protected
    // �ɱ����͸�ֵ����Ա����ʱ��newһ���¶��󣬶�����ֱ�Ӹ�ֵ
    @Override
    protected void checkRep(){
        super.checkRep();
        assert voters.isEmpty() || quantity < voters.size() + 5;
    }
    public DinnerOrder(){
        super();
    }

    /**
     *
     * @param name     ͶƱ�������
     * @param date     ͶƱ��������
     * @param type     ͶƱ���ͣ�������ͶƱѡ���Լ���Ӧ�ķ���
     * @param quantity ��ѡ��������
     */
    @Override
    public void setInfo(String name, Calendar date, VoteType type, int quantity) {
        if(!voters.isEmpty()&&quantity>= voters.size()+5){
            throw new IllegalArgumentException("Ԥѡ��������С�ڵ������+5");
        }
        this.name = name;
        this.date = date;
        Map<String, Integer> options = new HashMap<>(type.getOptions());
        this.voteType = new VoteType(options);
        this.quantity = quantity;
    }

    /**
     *
     * @param voters KeyΪͶƱ�ˣ�ValueΪͶƱ�˵�Ȩ��
     */
    @Override
    public void addVoters(Map<Voter, Double> voters) {
        if(voters.size()+5<quantity){
            throw new IllegalArgumentException("Ԥѡ��������С��ͶƱ����+5");
        }
        super.addVoters(voters);
        checkRep();
    }
    /**
     *
     * @param candidates ��ѡ���嵥
     */
    @Override
    public void addCandidates(List<Dish> candidates) {
        super.addCandidates(candidates);
        checkRep();
    }

    public void addVote(voteInterface<Dish> vote) {
        Voter v = vote.getVoter();
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
        Set<Dish> can = new HashSet<>(candidates);//��ѡ�˼���
        Set<Dish> voteCan = new HashSet<>();//ѡƱ�еĺ�ѡ�˼���
        for(VoteItem<Dish> vi:vote.getVoteItems()){
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
}
