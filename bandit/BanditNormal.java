package bandit;

public class BanditNormal extends BanditProb{//³‹K•ª•z
	private MTRandom r = new MTRandom();
	public BanditNormal(double prob) {
		mean = prob;
	}

	public double play() {
		reward = (int)Math.abs(r.nextGaussian()*10+mean);
		return reward;
	}
		
	double getMean() {
		return mean;
	}
	
}
