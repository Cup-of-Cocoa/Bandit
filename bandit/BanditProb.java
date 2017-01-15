package bandit;

public abstract class BanditProb extends Bandit{
	//Šm—¦•ª•z‚É]‚Á‚½•ñV‚ª“¾‚ç‚ê‚é
	double mean;
	
	public abstract double play();
	abstract double getMean();
}
