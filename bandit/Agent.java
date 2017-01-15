package bandit;

import java.util.List;
import java.util.ArrayList;

public abstract class Agent {
	//Agent�ɂ͎��s�񐔂Ɨp����X���b�g�}�V���̏W����^����B
	//�O�̃f�[�^��������̂�Agent�ɂ��ЂƂ܂Ƃ܂�̎��s������ɌJ��Ԃ����Ƃł��̃f�[�^�̕��ς�������B
	
	protected List<Double> totalRewardData = new ArrayList<Double>();//�e���s���_�ɂ����鍇�v���_�̃f�[�^
	protected List<Double> accuracyRateData = new ArrayList<Double>();//�e���s���_�ɂ�����u���𗦁v(=���ۂɍł��悢�X���b�g��I�ׂĂ��銄���j�̃f�[�^
	protected List<Double> totalRegretData = new ArrayList<Double>();//�e���s���_�ɂ����郊�O���b�g�̃f�[�^
	
	protected List<BanditProb> banditList = new ArrayList<BanditProb>();//�X���b�g�̏W��
	protected List<Double> rewardMeanList = new ArrayList<Double>();//�e�X���b�g�����܂łɔr�o�������_�̕���
	protected List<Integer> trialTimeList = new ArrayList<Integer>();//�e�X���b�g�̎��s��
	protected List<Double> valueList = new ArrayList<Double>();
	
	protected int bestBanditIndex;

	protected int NUM_OF_TRIAL; //���s�񐔁i���X���b�g���v���C����񐔁j
	protected int NUM_OF_BANDIT;
	protected int optimalBanditIndex; //Agent���u�œK���v�ƍl����X���b�g�̃C���f�b�N�X
	protected int suboptimalBanditIndex;
	protected int trialTime;
	protected double totalReward;

	protected Agent() {//�e�X�g�p
		banditList.add(new BanditBernoulli(0.9));
		banditList.add(new BanditBernoulli(0.6));
		NUM_OF_TRIAL = 1000000;
		NUM_OF_BANDIT = banditList.size();
		bestBanditIndex = bestBanditIndex();
	}
	
	protected Agent(List<BanditProb> bandits, int num_of_trial) {
		//���s�Ɏg���X���b�g�W���Ǝ��s�񐔂������Ƃ��A
		//�e�X���b�g�̎��s�񐔂ƕW�{���ς�����������
		banditList = bandits;
		NUM_OF_TRIAL = num_of_trial;
		NUM_OF_BANDIT = banditList.size();
		//�ݒ肵���l�̂����ł����ς���������
		bestBanditIndex = bestBanditIndex();
		//initialize�͊e�q�N���X�ōs��
	}
	
	protected void initialize() {//�����I�u�W�F�N�g���ė��p����Ƃ��ɂ��g����
		optimalBanditIndex = 0;
		suboptimalBanditIndex = 1;
		trialTime = 0;
		totalReward = 0;
		
		totalRewardData.clear();
		accuracyRateData.clear();
		totalRegretData.clear();
		
		rewardMeanList.clear();
		trialTimeList.clear();
		
		for(int i=0; i < NUM_OF_BANDIT; i++) {
			rewardMeanList.add(0.0);
			trialTimeList.add(0);
		}
	}


	protected void renewOptimal(List<Double> list)  {
		//�u�œK�v�ȃX���b�g���ǂꂩ�Ƃ��������X�V����
		//�u�œK�v��I�Ԃ��߂Ɋe�X���b�g�ɂ���ꂽ���̓A���S���Y���ɂ��قȂ�
		//�����͂��́u���v�����������X�g
		double tmp_highest = list.get(optimalBanditIndex);
		for(int i=0; i < NUM_OF_BANDIT; i++){
			if(list.get(i) > tmp_highest) {
				suboptimalBanditIndex = optimalBanditIndex;
				optimalBanditIndex = i;
			}
		}
	}

	protected void renewBanditInfo(int banditIndex, double reward) {
		//�v���C�����X���b�g�̎��s�񐔁E�W�{���ς��X�V����
		//�����̍X�V�͋���
		//UCB�ȂǁA���̒l�̍X�V���K�v�ȂƂ��̓I�[�o�[���C�h����
		int tmpTrialTime = trialTimeList.get(banditIndex);
		trialTimeList.set(banditIndex, tmpTrialTime+1);
		rewardMeanList.set(banditIndex, (rewardMeanList.get(banditIndex)*(double)tmpTrialTime + reward)/(double)trialTimeList.get(banditIndex));
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
			renewOptimal(rewardMeanList);
		}
	}
	
	public void rerun() {
		initialize();
		run();
	}

	public int bestBanditIndex() {
		int bestBanditIndex = 0;
		for (int i=0; i < NUM_OF_BANDIT; i++) {
			if(banditList.get(bestBanditIndex).getMean() < banditList.get(i).getMean()) {
				bestBanditIndex = i;
			}
		}
		return bestBanditIndex;
	}
	
	public double getTotalReward() { return totalReward; }	
	public List<BanditProb> getBanditList() { return banditList; }
	public List<Double> getRewardMeanList() { return rewardMeanList; }
	public List<Integer> getTrialTimeList() { return trialTimeList; }
	public List<Double> getTotalRewardData() { return totalRewardData; }
	public List<Double> getAccuracyRateData() { return accuracyRateData; }
	public List<Double> getTotalRegretData() { return totalRegretData; }
	public int getNUM_OF_TRIAL() {return NUM_OF_TRIAL; }
	
	protected abstract int selectBandit();//�I�ѕ��̓A���S���Y���ɂ��


}
