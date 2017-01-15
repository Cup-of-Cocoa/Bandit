package bandit;

import java.util.List;
import java.util.ArrayList;

public class AgentUCB extends Agent{
	protected List<Double> ucbList = new ArrayList<Double>(); //UCB�A���S���Y���p

	protected AgentUCB() {
		super();
	}

	protected AgentUCB(List<BanditProb> bandits, int num_of_trial) {
		super(bandits, num_of_trial);
	}

	public void initialize() {//�I�[�o�[���C�h
		super.initialize();
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			double tmpReward = banditList.get(i).play();
			totalReward += tmpReward;
			totalRewardData.add(totalReward);
			trialTime++;
			accuracyRateData.add((double)(trialTimeList.get(bestBanditIndex))/(double)trialTime);
			totalRegretData.add(trialTime*banditList.get(bestBanditIndex).getMean() - totalReward);
			trialTimeList.set(i, 1);
			rewardMeanList.set(i, tmpReward);
		}
		ucbList.clear();
	}

	public void run() {
		while(trialTime < NUM_OF_TRIAL) {
			//���̎��s�Ńv���C����X���b�g�i�̃C���f�b�N�X�j���A���S���Y���ɏ]���I��
			int selectedBanditIndex = selectBandit();
			//�X���b�g���v���C����
			double tmpReward = banditList.get(selectedBanditIndex).play();
			//���v���_�i��V�j���v�Z���f�[�^���X�g�ɉ�����
			totalReward += tmpReward;
			totalRewardData.add(totalReward);
			//���s�񐔂𑝂₵�u���𗦁v���f�[�^���X�g�ɉ�����
			trialTime++;
			accuracyRateData.add((double)(trialTimeList.get(bestBanditIndex))/(double)trialTime);
			//�v���C�����}�V���̎��s�񐔂ƕW�{���ς��X�V����i���ɍX�V���ׂ����̂�����΃I�[�o�[���C�h�j
			renewBanditInfo(selectedBanditIndex, tmpReward);			
			//���O���b�g���f�[�^���X�g�ɉ�����
			double idealExpectedReward = trialTime*banditList.get(bestBanditIndex).getMean();
			double accutualExpectedReward = 0;
			for (int i=0; i < NUM_OF_BANDIT; i++) {
				accutualExpectedReward += trialTimeList.get(i)*banditList.get(i).getMean();
			}
			totalRegretData.add(idealExpectedReward - accutualExpectedReward);
			//���̃}�V�����œK�ɂȂ�΂����V�����œK�ȃ}�V���ɂ���
			renewOptimal(ucbList);
		}
	}

	protected int selectBandit() {
		return optimalBanditIndex;
	}
}