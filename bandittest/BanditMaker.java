package bandittest;

import bandit.*;
import java.util.List;
import java.util.ArrayList;

public class BanditMaker {

	
	public List<BanditProb> makeBanditBernoulli(double probs[]) {
		List<BanditProb> banditList = new ArrayList<BanditProb>();
		for (int i = 0; i < probs.length; i++) {
			banditList.add(new BanditBernoulli(probs[i]));
		}
		return banditList;		
	}
}
