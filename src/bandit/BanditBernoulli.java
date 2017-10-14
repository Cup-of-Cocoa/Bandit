package bandit;

public class BanditBernoulli extends BanditProb{//確率pで1,1-pで0をとる分布
	//平均＝1をとる確率
	private MTRandom r = new MTRandom();
	public BanditBernoulli(double prob) {
		mean = prob;
	}

	public double play() { 
		if(r.nextDouble() <= mean) reward = 1;
		else reward = 0;
		return reward;
	}
		
	double getMean() {
		return mean;
	}
}
