package bandit;

import java.util.List;
import java.util.ArrayList;

public abstract class Agent {
	//Agentには試行回数と用いるスロットマシンの集合を与える。
	//三つのデータが得られるのでAgentによるひとまとまりの試行をさらに繰り返すことでそのデータの平均が得られる。
	
	protected List<Double> totalRewardData = new ArrayList<Double>();//各試行時点における合計得点のデータ
	protected List<Double> accuracyRateData = new ArrayList<Double>();//各試行時点における「正解率」(=実際に最もよいスロットを選べている割合）のデータ
	protected List<Double> totalRegretData = new ArrayList<Double>();//各試行時点におけるリグレットのデータ
	
	protected List<BanditProb> banditList = new ArrayList<BanditProb>();//スロットの集合
	protected List<Double> rewardMeanList = new ArrayList<Double>();//各スロットが今までに排出した得点の平均
	protected List<Integer> trialTimeList = new ArrayList<Integer>();//各スロットの試行回数
	protected List<Double> valueList = new ArrayList<Double>();
	
	protected int bestBanditIndex;

	protected int NUM_OF_TRIAL; //試行回数（＝スロットをプレイする回数）
	protected int NUM_OF_BANDIT;
	protected int optimalBanditIndex; //Agentが「最適だ」と考えるスロットのインデックス
	protected int suboptimalBanditIndex;
	protected int trialTime;
	protected double totalReward;

	protected Agent() {//テスト用
		banditList.add(new BanditBernoulli(0.9));
		banditList.add(new BanditBernoulli(0.6));
		NUM_OF_TRIAL = 1000000;
		NUM_OF_BANDIT = banditList.size();
		bestBanditIndex = bestBanditIndex();
	}
	
	protected Agent(List<BanditProb> bandits, int num_of_trial) {
		//試行に使うスロット集合と試行回数を引数とし、
		//各スロットの試行回数と標本平均を初期化する
		banditList = bandits;
		NUM_OF_TRIAL = num_of_trial;
		NUM_OF_BANDIT = banditList.size();
		//設定した値のうち最も平均が高いもの
		bestBanditIndex = bestBanditIndex();
		//initializeは各子クラスで行う
	}
	
	protected void initialize() {//同じオブジェクトを再利用するときにも使える
		optimalBanditIndex = 0;
		suboptimalBanditIndex = 1;
		trialTime = 0;
		totalReward = 0;
		
		totalRewardData.clear();
		accuracyRateData.clear();
		totalRegretData.clear();
		
		rewardMeanList.clear();
		trialTimeList.clear();
		
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			rewardMeanList.add(0.0);
			trialTimeList.add(0);
		}
	}


	protected void renewOptimal(List<Double> list)  {
		//「最適」なスロットがどれかという情報を更新する
		//「最適」を選ぶために各スロットにつけられた情報はアルゴリズムにより異なる
		//引数はその「情報」を持ったリスト
		double tmp_highest = list.get(optimalBanditIndex);
		for(int i=0; i < NUM_OF_BANDIT; i++){
			if(list.get(i) > tmp_highest) {
				suboptimalBanditIndex = optimalBanditIndex;
				optimalBanditIndex = i;
			}
		}
	}

	protected void renewBanditInfo(int banditIndex, double reward) {
		//プレイしたスロットの試行回数・標本平均を更新する
		//ここの更新は共通
		//UCBなど、他の値の更新が必要なときはオーバーライドする
		int tmpTrialTime = trialTimeList.get(banditIndex);
		trialTimeList.set(banditIndex, tmpTrialTime+1);
		rewardMeanList.set(banditIndex, (rewardMeanList.get(banditIndex)*(double)tmpTrialTime + reward)/(double)trialTimeList.get(banditIndex));
	}

	public void run() {
		while(trialTime < NUM_OF_TRIAL) {
			//この試行でプレイするスロット（のインデックス）をアルゴリズムに従い選ぶ
			int selectedBanditIndex = selectBandit();
			//スロットをプレイする
			double tmpReward = banditList.get(selectedBanditIndex).play();
			//合計得点（報酬）を計算しデータリストに加える
			totalReward += tmpReward;
			totalRewardData.add(totalReward);
			//試行回数を増やし「正解率」をデータリストに加える
			trialTime++;
			accuracyRateData.add((double)(trialTimeList.get(bestBanditIndex))/(double)trialTime);
			//プレイしたマシンの試行回数と標本平均を更新する（他に更新すべきものがあればオーバーライド）
			renewBanditInfo(selectedBanditIndex, tmpReward);			
			//リグレットをデータリストに加える
			double idealExpectedReward = trialTime*banditList.get(bestBanditIndex).getMean();
			double accutualExpectedReward = 0;
			for (int i=0; i < NUM_OF_BANDIT; i++) {
				accutualExpectedReward += trialTimeList.get(i)*banditList.get(i).getMean();
			}
			totalRegretData.add(idealExpectedReward - accutualExpectedReward);
			//他のマシンが最適になればそれを新しい最適なマシンにする
			renewOptimal(rewardMeanList);
		}
	}
	
	public void rerun() {
		initialize();
		run();
	}

	public int bestBanditIndex() {
		int bestBanditIndex = 0;
		for (int i=0; i < NUM_OF_BANDIT; i++) {
			if(banditList.get(bestBanditIndex).getMean() < banditList.get(i).getMean()) {
				bestBanditIndex = i;
			}
		}
		return bestBanditIndex;
	}
	
	public double getTotalReward() { return totalReward; }	
	public List<BanditProb> getBanditList() { return banditList; }
	public List<Double> getRewardMeanList() { return rewardMeanList; }
	public List<Integer> getTrialTimeList() { return trialTimeList; }
	public List<Double> getTotalRewardData() { return totalRewardData; }
	public List<Double> getAccuracyRateData() { return accuracyRateData; }
	public List<Double> getTotalRegretData() { return totalRegretData; }
	public int getNUM_OF_TRIAL() {return NUM_OF_TRIAL; }
	
	protected abstract int selectBandit();//選び方はアルゴリズムによる


}
