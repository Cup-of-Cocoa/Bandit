package bandit;

public abstract class BanditProb extends Bandit{
	//�m�����z�ɏ]������V��������
	double mean;
	
	public abstract double play();
	abstract double getMean();
}
