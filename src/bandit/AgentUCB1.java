package bandit;

import java.util.Map;

public class AgentUCB1 extends AgentUCB {

	public AgentUCB1() {
		super();
		initialize();
	}

	public AgentUCB1(Map<Integer, BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
		initialize();
	}

	public void initialize() {//オーバーライド
		super.initialize();
		for (int i = 0; i < NUM_OF_BANDIT; i++) {
			double ucb = Math.sqrt((2 * Math.log(trialTime)) / (double) trialTimeList.get(i));
			ucbList.put(i, rewardMeanList.get(i) + ucb);
		}
		renewOptimal(ucbList);
	}

	protected void renewBanditInfo(int banditIndex, double reward) {//オーバーライド
		super.renewBanditInfo(banditIndex, reward);
		for (int i = 0; i < NUM_OF_BANDIT; i++) {
			double ucb = Math.sqrt((2 * Math.log(trialTime)) / (double) trialTimeList.get(i));
			ucbList.put(i, rewardMeanList.get(i) + ucb);
		}
	}
}