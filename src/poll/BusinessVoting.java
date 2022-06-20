package poll;

import auxiliary.Proposal;
import auxiliary.Voter;
import pattern.BusinessSelect;
import pattern.BusinessStat;
import pattern.SelectionStrategy;
import pattern.StatisticsStrategy;
import vote.*;

import java.util.*;

public class BusinessVoting extends GeneralPollImpl<Proposal> implements Poll<Proposal> {
    // Rep Invariants
    // Ԥѡ����С�ڵ���1����ǿ��
    // Ԥѡ�������ڵ���0
    // ��ѡ�������Ϊ1�����ϸ�
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
        assert quantity<=1;
        assert getCandidates().size() == 1;
    }
    public BusinessVoting() {
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
        if(quantity<0||quantity>1){
            throw new IllegalArgumentException("Ԥѡ����������0��1");
        }
        this.name = name;
        this.date = date;
        Map<String, Integer> options = new HashMap<>(type.getOptions());
        this.voteType = new VoteType(options);
        this.quantity = quantity;
    }

    /**
     *
     * @param candidates ��ѡ���嵥
     */
    @Override
    public void addCandidates(List<Proposal> candidates) {
        Set<Proposal> set = new HashSet<>(candidates);
        if(set.size()!=1){
            throw new IllegalArgumentException("��ѡ���������һ��");
        }
        if(set.size() < candidates.size()){
            throw new IllegalArgumentException("��ѡ�˲�������");
        }
        if(quantity != -1&&set.size()<quantity){
            throw new IllegalArgumentException("Ԥѡ������С�ں�ѡ����");
        }
        this.candidates = new ArrayList<>(candidates);
        flagCan = true;
        checkRep();
    }

    /**
     * Overload:public void addVote(Vote<C> vote,Voter v)
     * @param vote һ��ͶƱ�˶�ȫ���ѡ�����ͶƱ��¼����
     */

    public void addVote(voteInterface<Proposal> vote) {
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
        Set<Proposal> can = new HashSet<>(candidates);//��ѡ�˼���
        Set<Proposal> voteCan = new HashSet<>();//ѡƱ�еĺ�ѡ�˼���
        for(VoteItem<Proposal> vi:vote.getVoteItems()){
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