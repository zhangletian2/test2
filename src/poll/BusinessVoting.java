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
    // 预选数量小于等于1（更强）
    // 预选数量大于等于0
    // 候选对象个数为1（更严格）
    // 没有重名的候选对象
    // 没有重名的投票人（map可以保证）
    // 投票人只能对应唯一的权重（map可以保证）
    // 投票人集合voters中权重非负
    // 拟选出的人数不大于候选人数
    // 投票人必须在voters里
    // Abstract Function
    // AF(name,date,candidates,voters,quantity,voteType,votes,statistics,results)
    // = 一次商业提案投票，包括名称、时间、候选对象、投票人、预选数量、投票类型、选票，计票和遴选结果
    // Safety from Rep Exposure
    // 所有成员变量都是protected
    // 可变类型赋值给成员变量时，new一个新对象，而不是直接赋值
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
     * @param name     投票活动的名称
     * @param date     投票创建日期
     * @param type     投票类型，包含各投票选项以及相应的分数
     * @param quantity 拟选出的数量
     */
    @Override
    public void setInfo(String name, Calendar date, VoteType type, int quantity) {
        if(quantity<0||quantity>1){
            throw new IllegalArgumentException("预选数量必须是0或1");
        }
        this.name = name;
        this.date = date;
        Map<String, Integer> options = new HashMap<>(type.getOptions());
        this.voteType = new VoteType(options);
        this.quantity = quantity;
    }

    /**
     *
     * @param candidates 候选人清单
     */
    @Override
    public void addCandidates(List<Proposal> candidates) {
        Set<Proposal> set = new HashSet<>(candidates);
        if(set.size()!=1){
            throw new IllegalArgumentException("候选对象必须是一个");
        }
        if(set.size() < candidates.size()){
            throw new IllegalArgumentException("候选人不能重名");
        }
        if(quantity != -1&&set.size()<quantity){
            throw new IllegalArgumentException("预选人数须小于候选人数");
        }
        this.candidates = new ArrayList<>(candidates);
        flagCan = true;
        checkRep();
    }

    /**
     * Overload:public void addVote(Vote<C> vote,Voter v)
     * @param vote 一个投票人对全体候选对象的投票记录集合
     */

    public void addVote(voteInterface<Proposal> vote) {
        Voter v = vote.getVoter();
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
        Set<Proposal> can = new HashSet<>(candidates);//候选人集合
        Set<Proposal> voteCan = new HashSet<>();//选票中的候选人集合
        for(VoteItem<Proposal> vi:vote.getVoteItems()){
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
}