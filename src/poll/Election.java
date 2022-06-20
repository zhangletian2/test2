package poll;

import auxiliary.Person;
import auxiliary.Proposal;
import auxiliary.Voter;
import pattern.BusinessSelect;
import pattern.BusinessStat;
import pattern.ElectionSelect;
import pattern.ElectionStat;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;
import vote.voteInterface;

import java.util.*;

public class Election extends GeneralPollImpl<Person> implements Poll<Person> {
    // Rep Invariants(added)
    // ͶƱ��Ȩ����ͬ�����ϸ�
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
        if(!voters.isEmpty()){
            Set<Double> s = new HashSet<>(voters.values());
            assert s.size() == 1;
        }
    }
    public Election(){
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
        if(!voters.isEmpty()){
            Set<Double> s = new HashSet<>(voters.values());
            if(s.size() != 1){
                throw new IllegalArgumentException("ͶƱ��Ȩ�ر������");
            }
        }
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

    /**
     *
     * @param candidates ��ѡ���嵥
     */
    @Override
    public void addCandidates(List<Person> candidates) {
        super.addCandidates(candidates);
        checkRep();
    }
    /**
     *
     * @param vote һ��ͶƱ�˶�ȫ���ѡ�����ͶƱ��¼����
     * @param v ͶƱ��
     */
    @Override
    public void addVote(voteInterface<Person> vote, Voter v) {
        // ͶƱ�˲���voters������
        if(!voters.containsKey(v)){
            throw new IllegalArgumentException("ͶƱ�˲���voters������");
        }
        // �˴�Ӧ���ȼ���ѡƱ�ĺϷ��Բ���ǣ�Ϊ������չ���޸�rep
        //? һ��ѡƱ��û�а�������ͶƱ��е����к�ѡ��
        //? һ��ѡƱ�а����˲��ڱ���ͶƱ��еĺ�ѡ��
        //? һ��ѡƱ�г����˱���ͶƱ�������ѡ��ֵ
        //? һ��ѡƱ���ж�ͬһ����ѡ����Ķ��ͶƱ
        //? һ��ѡƱ�ж����к�ѡ�����֧��Ʊ��������k��������
        votes.add(vote);// ���ܺϷ����Ϸ���Ҫ�Ƚ���
        int voteNum = 0;
        int supSum = 0;
        int flag = 1;
        Set<Person> can = new HashSet<>(candidates);//��ѡ�˼���
        Set<Person> voteCan = new HashSet<>();//ѡƱ�еĺ�ѡ�˼���
        for(VoteItem<Person> vi:vote.getVoteItems()){
            if(!voteType.getOptions().containsKey(vi.getVoteValue())){
                System.out.println("ѡƱ�Ƿ��������˱���ͶƱ�������ѡ��ֵ");
                flag = 0;
            }
            if(vi.getVoteValue().equals("֧��")){
                supSum++;
            }
            voteNum++;
            voteCan.add(vi.getCandidate());
        }
        if(supSum>quantity){
            System.out.println("ѡƱ�Ƿ���һ��ѡƱ�ж����к�ѡ�����֧��Ʊ��������k");
            flag = 0;
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
