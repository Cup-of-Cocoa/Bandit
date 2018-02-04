package bandittest;

import java.util.HashMap;
import java.util.Map;

import bandit.BanditBernoulli;
import bandit.BanditProb;

public class BanditMaker {

	public Map<Integer, BanditProb> makeBanditBernoulli(double probs[]) {
		Map<Integer, BanditProb> banditList = new HashMap<Integer, BanditProb>();
		for (int i = 0; i < probs.length; i++) {
			banditList.put(i, new BanditBernoulli(probs[i]));
		}
		return banditList;
	}
}
