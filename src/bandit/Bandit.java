package bandit;

public abstract class Bandit { //スロットマシン
	protected double reward; //報酬（スロットが当たったとき得られる点数）
	//報酬がどんな確率分布に従って排出されるかは子クラスで決める（例:ベルヌーイ分布、正規分布）
	//確率分布ですらないかもしれない。

	public abstract double play(); //スロットをプレイして点数を得る

}
