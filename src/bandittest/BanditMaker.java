package bandittest;

import java.util.HashMap;
import java.util.Map;

import bandit.BanditBernoulli;
import bandit.BanditProb;

public class BanditMaker {

	// ある確率で当たりを出すスロットマシンを作る。
	// 「当たる確率」の配列を引数として渡すと、
	// その確率で当たるスロットマシンの集合を返す。
	// 配列のインデックスと、そのインデックスで与えられる確率で当たるスロットマシンが紐づけられる。
	public Map<Integer, BanditProb> makeBanditBernoulli(double probs[]) {
		Map<Integer, BanditProb> banditList = new HashMap<Integer, BanditProb>();
		for (int i = 0; i < probs.length; i++) {
			banditList.put(i, new BanditBernoulli(probs[i]));
		}
		return banditList;
	}
}
