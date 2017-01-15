package bandit;

import java.util.List;

public class AgentUCB1 extends AgentUCB{
	
	public AgentUCB1(){
		super();
		initialize();
	}
	
	public AgentUCB1(List<BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
		initialize();
	}

	public void initialize() {//�I�[�o�[���C�h
		super.initialize();
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			double ucb = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(i));
			ucbList.add(rewardMeanList.get(i) + ucb);
		}
		renewOptimal(ucbList);		
	}

	protected void renewBanditInfo(int banditIndex, double reward) {//�I�[�o�[���C�h
		super.renewBanditInfo(banditIndex, reward);
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			double ucb = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(i));
			ucbList.set(i, rewardMeanList.get(i) + ucb);
		}
	}
}