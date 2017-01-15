package bandit;

public class BanditBernoulli extends BanditProb{//Šm—¦p‚Å1,1-p‚Å0‚ğ‚Æ‚é•ª•z
	//•½‹Ï1‚ğ‚Æ‚éŠm—¦
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
