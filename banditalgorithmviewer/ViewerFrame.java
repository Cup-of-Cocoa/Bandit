package banditalgorithmviewer;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import bandit.*;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import bandit.AgentUCBVariance;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ViewerFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	JPanel banditPanel1, banditPanel2;
	JLabel tr, banditLabel1, banditLabel2, resultLabel1, resultLabel2, ucbLabel1, ucbLabel2, regretLabel1, regretLabel2;
	JLabel trialTime1, trialTime2, rewardMean1, rewardMean2;
	JButton banditButton1, banditButton2;

	BanditBernoulli bandit1 = new BanditBernoulli(0.9);
	BanditBernoulli bandit2 = new BanditBernoulli(0.6);

	int trialTime = 0;

	List<Double> rewardMeanList = new ArrayList<Double>();//各スロットが今までに排出した得点の平均
	List<Integer> trialTimeList = new ArrayList<Integer>();//各スロットの試行回数


	public ViewerFrame() {
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

		banditPanel1 = new JPanel(new GridLayout(7,1));
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
		rewardMean1 = new JLabel("Reward: ");
		banditPanel1.add(rewardMean1);
		trialTime1 = new JLabel("Trial1" + Integer.toString(trialTimeList.get(0)));
		banditPanel1.add(trialTime1);
		regretLabel1 = new JLabel("Regret: ");
		banditPanel1.add(regretLabel1);
		add(banditPanel1);

		banditPanel2 = new JPanel(new GridLayout(7,1));
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
		rewardMean2 = new JLabel("Reward: ");
		banditPanel2.add(rewardMean2);
		trialTime2 = new JLabel("Trial2" + Integer.toString(trialTimeList.get(1)));
		banditPanel2.add(trialTime2);
		tr = new JLabel("Trial: " + Integer.toString(trialTime));
		banditPanel2.add(tr);
		add(banditPanel2);
		
		banditButton1.doClick();
		banditButton2.doClick();
	}

	public void actionPerformed(ActionEvent e){
		trialTime++;
		tr.setText("Trial: " + Integer.toString(trialTime));
		if(e.getActionCommand().equals("bandit1")) {
			double reward = bandit1.play();
			if(reward == 1.0) resultLabel1.setText("あたり");
			else resultLabel1.setText("はずれ");
			trialTimeList.set(0, trialTimeList.get(0)+1);
			trialTime1.setText("Trial1: " + Integer.toString(trialTimeList.get(0)));
			double ucb = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(0));
			ucbLabel1.setText("UCB: " + Double.toString(ucb));
			double newRewardMean = rewardMeanList.get(0)*(trialTimeList.get(0)-1)+reward;
			rewardMeanList.set(0, newRewardMean/(double)trialTimeList.get(0));
			rewardMean1.setText("Reward: " + Double.toString(rewardMeanList.get(0)));
			
		}
		else if(e.getActionCommand().equals("bandit2")) {
			double reward = bandit2.play();
			if(reward == 1.0) resultLabel2.setText("あたり");
			else resultLabel2.setText("はずれ");
			trialTimeList.set(1, trialTimeList.get(1)+1);
			trialTime2.setText("Trial2: " + Integer.toString(trialTimeList.get(1)));
			double ucb = Math.sqrt((2*Math.log(trialTime))/(double)trialTimeList.get(1));
			ucbLabel2.setText("UCB: " + Double.toString(ucb));
			double newRewardMean = rewardMeanList.get(1)*(trialTimeList.get(1)-1)+reward;
			rewardMeanList.set(1, newRewardMean/(double)trialTimeList.get(1));
			rewardMean2.setText("Reward: " + Double.toString(rewardMeanList.get(1)));
		}
		double idealExpectedReward = trialTime*0.9;
		double accutualExpectedReward = 0;
		accutualExpectedReward += trialTimeList.get(0)*0.9;
		accutualExpectedReward += trialTimeList.get(1)*0.6;
		regretLabel1.setText("Regret:" + Double.toString(idealExpectedReward - accutualExpectedReward));
	}

	public static void main(String[] args) {
		new ViewerFrame().setVisible(true);;
	}
}
