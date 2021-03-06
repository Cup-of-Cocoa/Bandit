﻿package bandit;

import java.util.Map;

public class AgentUCBVariance extends AgentUCB {//分散を取り入れたUCBアルゴリズム

	public AgentUCBVariance() {
		super();
		initialize();
	}

	public AgentUCBVariance(Map<Integer, BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
		initialize();
	}

	public void initialize() {//オーバーライド
		super.initialize();
		for (int i = 0; i < NUM_OF_BANDIT; i++) {
			double vjs = Math.sqrt(2 * Math.log(trialTime));
			double ucb_tuned = Math.sqrt(Math.log(trialTime) * Math.min(1 / 4, vjs));
			ucbList.put(i, rewardMeanList.get(i) + ucb_tuned);
		}
		renewOptimal(ucbList);
	}

	protected void renewBanditInfo(int banditIndex, double reward) {//オーバーライド
		super.renewBanditInfo(banditIndex, reward);
		for (int i = 0; i < NUM_OF_BANDIT; i++) {
			double V = rewardMeanList.get(i) * (1.0 - rewardMeanList.get(i));
			double vjs = V + Math.sqrt((2 * Math.log(trialTime)) / trialTimeList.get(i));
			double ucb_tuned = Math.sqrt((Math.log(trialTime) / trialTimeList.get(i)) * Math.min(1 / 4, vjs));
			ucbList.put(i, rewardMeanList.get(i) + ucb_tuned);
		}
	}
}