package bandit;

import java.util.List;
import java.util.ArrayList;

public class AgentUCB extends Agent {
	protected List<Double> ucbList = new ArrayList<Double>(); //UCBアルゴリズム用

	protected AgentUCB() {
		super();
	}

	protected AgentUCB(List<BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
	}

	public void initialize() {//オーバーライド
		super.initialize();
		for (int i = 0; i < NUM_OF_BANDIT; i++) {
			double tmpReward = banditList.get(i).play();
			totalReward += tmpReward;
			totalRewardData.add(totalReward);
			trialTime++;
			accuracyRateData.add((double) (trialTimeList.get(bestBanditIndex)) / (double) trialTime);
			totalRegretData.add(trialTime * banditList.get(bestBanditIndex).getMean() - totalReward);
			trialTimeList.set(i, 1);
			rewardMeanList.set(i, tmpReward);
		}
		ucbList.clear();
	}

	public void run() {
		while (trialTime < NUM_OF_TRIAL) {
			//この試行でプレイするスロット（のインデックス）をアルゴリズムに従い選ぶ
			int selectedBanditIndex = selectBandit();
			//スロットをプレイする
			double tmpReward = banditList.get(selectedBanditIndex).play();
			//合計得点（報酬）を計算しデータリストに加える
			totalReward += tmpReward;
			totalRewardData.add(totalReward);
			//試行回数を増やし「正解率」をデータリストに加える
			trialTime++;
			accuracyRateData.add((double) (trialTimeList.get(bestBanditIndex)) / (double) trialTime);
			//プレイしたマシンの試行回数と標本平均を更新する（他に更新すべきものがあればオーバーライド）
			renewBanditInfo(selectedBanditIndex, tmpReward);
			//リグレットをデータリストに加える
			double idealExpectedReward = trialTime * banditList.get(bestBanditIndex).getMean();
			double accutualExpectedReward = 0;
			for (int i = 0; i < NUM_OF_BANDIT; i++) {
				accutualExpectedReward += trialTimeList.get(i) * banditList.get(i).getMean();
			}
			totalRegretData.add(idealExpectedReward - accutualExpectedReward);
			//他のマシンが最適になればそれを新しい最適なマシンにする
			renewOptimal(ucbList);
		}
	}

	protected int selectBandit() {
		return optimalBanditIndex;
	}
}