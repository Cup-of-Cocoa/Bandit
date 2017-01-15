package bandit;

public class BanditBernoulli extends BanditProb{//�m��p��1,1-p��0���Ƃ镪�z
	//���ρ�1���Ƃ�m��
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
