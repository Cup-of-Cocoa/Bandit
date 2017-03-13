package banditalgorithmviewer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bandit.BanditBernoulli;


public class ViewerFrame extends JFrame implements ActionListener{
	//役に立つかはわからないけどUCBアルゴリズムを可視化（？）してみた
	private static final long serialVersionUID = 1L;

	JPanel banditPanel1, banditPanel2;
	JLabel totalTrialLabel, banditLabel1, banditLabel2, regretLabel, regretBound;

	JLabel trialTime1, trialTime2;
	List<JLabel> trialTimes = new ArrayList<JLabel>();
	JLabel resultLabel1, resultLabel2;
	List<JLabel> resultLabels = new ArrayList<JLabel>();
	JLabel rewardMeanLabel1, rewardMeanLabel2;
	List<JLabel> rewardMeans = new ArrayList<JLabel>();
	JLabel ucbLabel1, ucbLabel2;
	List<JLabel> ucbLabels = new ArrayList<JLabel> ();

	JButton banditButton1, banditButton2;

	final double prob1 = 0.9;
	final double prob2 = 0.8;
	final double delta = prob1 - prob2;
	double setProb1;
	double setProb2;
	List<BanditBernoulli> bandits = new ArrayList<BanditBernoulli>();
	BanditBernoulli bandit1, bandit2;

	double ucb1=0, ucb2=0;
	List<Double> ucbs = new ArrayList<Double>();
	int trialTime = 0;

	List<Double> rewardMeanList = new ArrayList<Double>();//各スロットが今までに排出した得点の平均
	List<Integer> trialTimeList = new ArrayList<Integer>();//各スロットの試行回数


	public ViewerFrame() {
		if (Math.random() < 0.5) {
			bandit1 = new BanditBernoulli(prob1);
			setProb1 = prob1;
			bandit2 = new BanditBernoulli(prob2);
			setProb2 = prob2;
		}
		else {
			bandit1 = new BanditBernoulli(prob2);
			setProb1 = prob2;
			bandit2 = new BanditBernoulli(prob1);
			setProb2 = prob1;
		}
		bandits.add(bandit1);
		bandits.add(bandit2);
		rewardMeanList.add(0.0);
		rewardMeanList.add(0.0);
		trialTimeList.add(0);
		trialTimeList.add(0);
		/*double reward1 = bandit1.play();
		rewardMeanList.add(bandit1.play());
		rewardMeanList.add(bandit2.play());
		trialTimeList.add(1);
		trialTimeList.add(1);
		trialTime++;trialTime++;
		double ucb1 = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(0));
		double ucb2 = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(1));*/

		setTitle("Viewer");
		setLayout(new GridLayout(1,2));
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×を押したらウィンドウを閉じる

		banditPanel1 = new JPanel(new GridLayout(8,1));
		banditLabel1 = new JLabel("Bandit1");
		banditPanel1.add(banditLabel1);
		banditButton1 = new JButton("Bandit1");
		banditButton1.setActionCommand("bandit1");
		banditButton1.addActionListener(this);
		banditPanel1.add(banditButton1);
		resultLabel1 = new JLabel("");
		banditPanel1.add(resultLabel1);
		ucbLabel1 = new JLabel("UCB: ");
		banditPanel1.add(ucbLabel1);
		rewardMeanLabel1 = new JLabel("Reward: ");
		banditPanel1.add(rewardMeanLabel1);
		trialTime1 = new JLabel("Trial1" + Integer.toString(trialTimeList.get(0)));
		banditPanel1.add(trialTime1);
		regretLabel = new JLabel("Regret: ");
		banditPanel1.add(regretLabel);
		regretBound = new JLabel("Bound: ");
		banditPanel1.add(regretBound);
		add(banditPanel1);

		banditPanel2 = new JPanel(new GridLayout(8,1));
		banditLabel2 = new JLabel("Bandit2");
		banditPanel2.add(banditLabel2);
		banditButton2 = new JButton("Bandit2");
		banditButton2.setActionCommand("bandit2");
		banditButton2.addActionListener(this);
		banditPanel2.add(banditButton2);
		resultLabel2 = new JLabel("");
		banditPanel2.add(resultLabel2);
		ucbLabel2 = new JLabel("UCB: ");
		banditPanel2.add(ucbLabel2);
		rewardMeanLabel2 = new JLabel("Reward: ");
		banditPanel2.add(rewardMeanLabel2);
		trialTime2 = new JLabel("Trial2" + Integer.toString(trialTimeList.get(1)));
		banditPanel2.add(trialTime2);
		totalTrialLabel = new JLabel("Trial: " + Integer.toString(trialTime));
		banditPanel2.add(totalTrialLabel);
		add(banditPanel2);

		trialTimes.add(trialTime1);
		trialTimes.add(trialTime2);
		resultLabels.add(resultLabel1);
		resultLabels.add(resultLabel2);
		ucbs.add(ucb1);
		ucbs.add(ucb2);
		rewardMeans.add(rewardMeanLabel1);
		rewardMeans.add(rewardMeanLabel2);
		ucbLabels.add(ucbLabel1);
		ucbLabels.add(ucbLabel2);

		banditButton1.doClick();
		banditButton2.doClick();
	}

	public void actionPerformed(ActionEvent e){
		trialTime++;
		totalTrialLabel.setText("TotalTrial: " + Integer.toString(trialTime));

		int selcted = Integer.parseInt(e.getActionCommand().substring(6)) -1;
		double reward = bandits.get(selcted).play();
		if(reward == 1.0) resultLabels.get(selcted).setText("あたり");
		else resultLabels.get(selcted).setText("はずれ");
		trialTimeList.set(selcted, trialTimeList.get(selcted)+1);
		trialTimes.get(selcted).setText("Trial: " + Integer.toString(trialTimeList.get(selcted)));
		double newRewardMean = rewardMeanList.get(selcted)*(trialTimeList.get(selcted)-1)+reward;
		rewardMeanList.set(selcted, newRewardMean/(double)trialTimeList.get(selcted));
		rewardMeans.get(selcted).setText("Reward: " + Double.toString(rewardMeanList.get(selcted)));
		if (trialTime == 1) ucbs.set(selcted, Math.sqrt((2*Math.log(trialTime+1))/(double)trialTimeList.get(selcted))) ;
		else  ucbs.set(selcted, rewardMeanList.get(selcted) + Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(selcted)));
		ucbLabels.get(selcted).setText("UCB: " + Double.toString(ucbs.get(selcted)));

		double idealExpectedReward = trialTime*prob1;
		double accutualExpectedReward = 0;
		accutualExpectedReward += trialTimeList.get(0)*setProb1;
		accutualExpectedReward += trialTimeList.get(1)*setProb2;
		regretLabel.setText("Regret:" + Double.toString(idealExpectedReward - accutualExpectedReward));
		if (ucbs.get(0) > ucbs.get(1)) {
			banditLabel1.setText("Bandit1 *");
			banditLabel2.setText("Bandit2");
			if(trialTime >= 2) {
				banditButton1.setEnabled(true);
				banditButton2.setEnabled(false);
			}

		}
		else {
			banditLabel1.setText("Bandit1");
			banditLabel2.setText("Bandit2 *");
			if(trialTime > 2) {
				banditButton1.setEnabled(false);
				banditButton2.setEnabled(true);
			}
		}
		regretBound.setText("Bound: " + Double.toString(8*(Math.log(trialTime)/delta) + (1 + Math.PI*Math.PI/3)*delta));
	}

	public static void main(String[] args) {
		new ViewerFrame().setVisible(true);;
	}
}