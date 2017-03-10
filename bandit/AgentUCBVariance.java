package bandit;

import java.util.List;
import java.util.ArrayList;

public class AgentUCBVariance extends AgentUCB{//分散を取り入れたUCBアルゴリズム

	public AgentUCBVariance() {
		super();
		initialize();
	}
	
	public AgentUCBVariance(List<BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
		initialize();
	}

	public void initialize() {//オーバーライド
		super.initialize();
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			double vjs = Math.sqrt(2*Math.log(trialTime));
			double ucb_tuned = Math.sqrt(Math.log(trialTime)*Math.min(1/4, vjs));
			ucbList.add(rewardMeanList.get(i) + ucb_tuned);
		}
		renewOptimal(ucbList);		
	}

	protected void renewBanditInfo(int banditIndex, double reward) {//オーバーライド
		super.renewBanditInfo(banditIndex, reward);
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			double V = rewardMeanList.get(i) * (1.0 - rewardMeanList.get(i));
			double vjs = V + Math.sqrt((2*Math.log(trialTime))/trialTimeList.get(i));
			double ucb_tuned = Math.sqrt((Math.log(trialTime)/trialTimeList.get(i))*Math.min(1/4, vjs));
			ucbList.set(i, rewardMeanList.get(i) + ucb_tuned);
		}
	}
	
	public static void main(String args[]) {
		BanditProb bp1 = new BanditNormal(10);
		BanditProb bp2 = new BanditNormal(30);
		List<BanditProb> l = new ArrayList<BanditProb>();
		l.add(bp1);
		l.add(bp2);
		Agent a = new AgentUCBVariance(l, 1000);
		a.run();
		System.out.println(a.getAccuracyRateData().get(999));
	}
	
}