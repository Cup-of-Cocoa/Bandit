package bandittest;

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

public class TesterFrame extends JFrame implements ActionListener{
	//ウィンドウでGUIによりバンディットアルゴリズムのテストに使うスロットなどを定義する

	private static final long serialVersionUID = 1L;

	JPanel distroPanel, algoPanel, trialPanel, dirNamePanel, statusPanel, probPanel;
	JLabel distroLabel, algoLabel, statusLabel, statusLabel1, statusLabel2, trialLabel, dirNameLabel, probLabel;
	JLabel spaceLabel = new JLabel("");
	JTextField  trialField, dirNameField, probField;
	JButton okButton;
	ButtonGroup distroGroup, algoGroup;
	JRadioButton bernoulliButton, gaussianButton, ucb1Button, vucbButton, epdButton;
	final static double[] DEFALT_PROBS = {0.9, 0.6};


	public TesterFrame() {
		setTitle("Tester");
		setLayout(new GridLayout(8,1));
		setSize(350, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×を押したらウィンドウを閉じる

		distroPanel = new JPanel();
		distroLabel = new JLabel("Distribution");
		distroGroup = new ButtonGroup();
		bernoulliButton = new JRadioButton("Bernoulli", true);
		gaussianButton = new JRadioButton("Normal");
		distroGroup.add(bernoulliButton);
		distroGroup.add(gaussianButton);

		distroPanel.add(distroLabel);
		distroPanel.add(bernoulliButton);
		distroPanel.add(gaussianButton);

		add(distroPanel);

		algoPanel = new JPanel();
		algoLabel = new JLabel("Algorithm");
		algoGroup = new ButtonGroup();
		ucb1Button = new JRadioButton("UCB1", true);
		vucbButton = new JRadioButton("VUCB");
		epdButton = new JRadioButton("ε-Greedy");
		algoGroup.add(ucb1Button);
		algoGroup.add(vucbButton);
		algoGroup.add(epdButton);
		algoPanel.add(algoLabel);
		algoPanel.add(ucb1Button);
		algoPanel.add(vucbButton);
		algoPanel.add(epdButton);
		add(algoPanel);

		add(spaceLabel);

		statusPanel = new JPanel(new GridLayout(3,1));
		statusLabel = new JLabel("STATUS");
		statusLabel1 = new JLabel("");
		statusLabel2 = new JLabel("");
		statusPanel.add(statusLabel);
		statusPanel.add(statusLabel1);
		statusPanel.add(statusLabel2);
		add(statusPanel);

		add(spaceLabel);

		trialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		trialLabel = new JLabel("Num of Trial");
		trialField = new JTextField(8);
		trialField.setActionCommand("TRIAL");
		trialField.addActionListener(this);
		trialPanel.add(trialLabel);
		trialPanel.add(trialField);
		add(trialPanel);

		dirNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dirNameLabel = new JLabel("Directory Name");
		dirNameField = new JTextField(30);
		dirNameField.setActionCommand("DIRNAME");
		dirNameField.addActionListener(this);
		dirNamePanel.add(dirNameLabel);
		dirNamePanel.add(dirNameField);
		add(dirNamePanel);

		probPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		probLabel = new JLabel("Distribution of Bandits");
		probField = new JTextField(50);
		probField.setActionCommand("PROB");
		probField.addActionListener(this);
		probPanel.add(probLabel);
		probPanel.add(probField);
		add(probPanel);


		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		add(okButton);
	}

	public void testSetting() {
		Tester tester = null;
		Agent agent = null;
		List<BanditProb> banditList = null;
		BanditMaker maker = new BanditMaker();
		String dirName = dirNameField.getText();
		int num_of_trial = 0;
		try {
			num_of_trial = Integer.parseInt(trialField.getText());
		} catch (NumberFormatException e) {
			statusLabel1.setText("試行回数が指定されていないか整数ではありません");
			return;
		}
		if (num_of_trial <= 0) {
			statusLabel1.setText("試行回数が０以下です");
			return;
		}

		String testName = Integer.toString(num_of_trial) + "回　";

		//バンディットマシンの分布
		double probs[] = DEFALT_PROBS;
		if(!probField.getText().equals("")) {			
			String probString[] = probField.getText().split(" ");
			probs = new double[probString.length];
			for(int i=0; i < probString.length; i++) {
				probs[i] = Double.parseDouble(probString[i]);
			}
		}	

		//分布をラジオボタンから取得する
		if(bernoulliButton.isSelected()) {
			testName += "ベルヌーイ分布";
			banditList = maker.makeBanditBernoulli(probs);
		}
		else if(gaussianButton.isSelected()) {
			statusLabel1.setText("Normalは未実装です");
			return;
		}
		else {
			statusLabel1.setText("スロットの分布が選択されていません");
			return;
		}
		//アルゴリズムをラジオボタンから取得する

		if(ucb1Button.isSelected()) {
			agent = new AgentUCB1(banditList, num_of_trial);
			dirName += "UCB1";
			testName += " UCB1アルゴリズム";
			statusLabel1.setText(testName);
		}
		else if(vucbButton.isSelected()) {
			agent = new AgentUCBVariance(banditList, num_of_trial);
			dirName += "VUCB";
			testName += " VUCBアルゴリズム";
		}
		else if(epdButton.isSelected()) {
			agent = new AgentEpsilonDecreasing(banditList, num_of_trial);
			dirName += "ε-Greedy";
			testName += " ε-Greedyアルゴリズム";
		}
		else {
			statusLabel1.setText("アルゴリズムが選択されていません");
			return;
		}
		tester = new Tester(dirName);
		statusLabel1.setText(testName);
		String tmp = tester.test(agent);
		statusLabel2.setText(tmp);
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			testSetting();
		}
		else if (e.getActionCommand().equals("PROB") || e.getActionCommand().equals("TRIAL") || e.getActionCommand().equals("DIRNAME")) {
			testSetting();
		}
	}

	public static void main(String args[]){ 
		TesterFrame tf = new TesterFrame();
		tf.setVisible(true);
	}
}
