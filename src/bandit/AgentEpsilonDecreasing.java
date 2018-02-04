package bandit;

import java.util.Map;

public class AgentEpsilonDecreasing extends Agent {
	private double c, d;//epsilon-Decreasingアルゴリズム用 ただしdは標本平均から計算する
	private MTRandom r = new MTRandom();

	public AgentEpsilonDecreasing() {
		super();
		initialize();
	}

	public AgentEpsilonDecreasing(Map<Integer, BanditProb> bandits, int num_of_trial) {
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
		//確率epsilonでランダムなスロットを選ぶ
		if (r.nextDouble() > Math.min(1, (c * NUM_OF_BANDIT) / (d * d * trialTime))) {
			return optimalBanditIndex;
		} else {
			return Math.abs(r.nextInt()) % NUM_OF_BANDIT;
		}
	}

	public static void main(String args[]) {
		AgentEpsilonDecreasing a = new AgentEpsilonDecreasing();
		a.run();
		for (int i = 0; i < 100000; i++) {
			System.out.println(a.getTotalRegretData().get(i));
		}
	}
}
