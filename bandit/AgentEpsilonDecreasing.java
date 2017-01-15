package bandit;

import java.util.List;

public class AgentEpsilonDecreasing extends Agent{
	private double c,d;//epsilon-Decreasing�A���S���Y���p ������d�͕W�{���ς���v�Z����
	private MTRandom r = new MTRandom();

	public AgentEpsilonDecreasing() {
		super();
		initialize();
	}
	
	public AgentEpsilonDecreasing(List<BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
		initialize();
	}

	protected void initialize() {
		super.initialize();
		c = 5;
		d = 1;
	}

	protected int selectBandit() {
		d = rewardMeanList.get(optimalBanditIndex) - rewardMeanList.get(suboptimalBanditIndex);
		//�m��epsilon�Ń����_���ȃX���b�g��I��
		if (r.nextDouble() > Math.min(1,(c*NUM_OF_BANDIT)/(d*d*trialTime))) {
			return optimalBanditIndex;
		}
		else {
			return Math.abs(r.nextInt())%NUM_OF_BANDIT;
		}
	}

	public static void main(String args[]) {
		Agent a = new AgentEpsilonDecreasing();
		a.run();
		System.out.println(a.getAccuracyRateData().get(999999));
	}
}
