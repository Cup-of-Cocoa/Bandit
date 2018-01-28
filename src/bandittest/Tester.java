package bandittest;

import bandit.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tester {
	static final String DEFALT_FOLDER = "C:\\Users\\shuhei\\Documents\\Bandit\\";
	public boolean running = true;
	String rewardFileName, regretFileName, accuracyFileName;
	String dateName, dirName;

	Date date = new Date();
	SimpleDateFormat dateForFileName = new SimpleDateFormat("yyyyMMddHHmmss");

	public Tester() {
		rewardFileName = "";
		regretFileName = "";
		accuracyFileName = "";
		dateName = dateForFileName.format(date);
		dirName = DEFALT_FOLDER + dateName;
	}

	public Tester(String dirName) {
		rewardFileName = "";
		regretFileName = "";
		accuracyFileName = "";
		dateName = dateForFileName.format(date);
		this.dirName = DEFALT_FOLDER + dateName + dirName;
	}

	public Tester(String rFileName, String regFileName, String aFileName) {
		rewardFileName = rFileName;
		regretFileName = regFileName;
		accuracyFileName = aFileName;
		dateName = dateForFileName.format(date);
		dirName = DEFALT_FOLDER + dateName;
	}

	public void setText(String rFileName, String regFileName, String aFileName) {
		rewardFileName = rFileName;
		regretFileName = regFileName;
		accuracyFileName = aFileName;
	}

	public String test(Agent a) {
		List<Double> totalRewardDataMean = new ArrayList<Double>();
		List<Double> totalRegretDataMean = new ArrayList<Double>();
		List<Double> accuracyRateDataMean = new ArrayList<Double>();

		a.run();
		for (int i = 0; i < a.getNUM_OF_TRIAL(); i++) {
			totalRewardDataMean.add(a.getTotalRewardData().get(i));
			totalRegretDataMean.add(a.getTotalRegretData().get(i));
			accuracyRateDataMean.add(a.getAccuracyRateData().get(i));
		}
		for (int i = 1; i < 100; i++) {
			a.rerun();
			for (int j = 0; j < a.getNUM_OF_TRIAL(); j++) {
				totalRewardDataMean.set(j, (totalRewardDataMean.get(j) * i + a.getTotalRewardData().get(j)) / (i + 1));
				totalRegretDataMean.set(j, (totalRegretDataMean.get(j) * i + a.getTotalRegretData().get(j)) / (i + 1));
				accuracyRateDataMean.set(j,
						(accuracyRateDataMean.get(j) * i + a.getAccuracyRateData().get(j)) / (i + 1));
			}
		}

		try {
			File newDir = new File(dirName);
			newDir.mkdir();
			File rewardData = new File(dirName + "\\" + rewardFileName + "reward");
			FileWriter reward = new FileWriter(rewardData);
			File regretData = new File(dirName + "\\" + regretFileName + "regret");
			FileWriter regret = new FileWriter(regretData);
			File accuracyData = new File(dirName + "\\" + accuracyFileName + "accuracy");
			FileWriter accuracy = new FileWriter(accuracyData);
			for (int i = 0; i < a.getNUM_OF_TRIAL(); i++) {
				reward.write(i + 1 + " " + Double.toString(totalRewardDataMean.get(i)) + "\r\n");
				regret.write(i + 1 + " " + Double.toString(totalRegretDataMean.get(i)) + "\r\n");
				accuracy.write(i + 1 + " " + Double.toString(accuracyRateDataMean.get(i)) + "\r\n");
			}
			reward.close();
			regret.close();
			accuracy.close();
			return ("正常に終了しました。");
		} catch (IOException e) {
			if (!(new File(DEFALT_FOLDER).exists())) {
				return ("デフォルトのフォルダが存在しません");
			} else {
				return ("ファイルを作成できません");
			}
		}
	}

	public static void main(String args[]) {
		Tester t = new Tester();
		double probs[] = { 0.9, 0.6, 0.6 };
		t.test(new AgentUCBVariance(new BanditMaker().makeBanditBernoulli(probs), 100000));
	}
}
