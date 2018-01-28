package bandit;

public abstract class BanditProb extends Bandit {
	//確率分布に従った報酬が得られる
	double mean;

	public abstract double play();

	abstract double getMean();
}
