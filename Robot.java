package robotGameNew;


public class Robot {
	private int robotNo = 0;
	private double speed;
	private int totalWeigth;
	private int chessScore;
	private int runScore;
	private int sumoScore;
	private int pingPongScore;
	private char regGameName;
	private int regGameNo;
	private Module[] mdl= new Module[4];
	
	
	public char getRegGameName() {
		return regGameName;
	}

	public void setRegGameName(char regGameName) {
		this.regGameName = regGameName;
	}

	public int getRegGameNo() {
		return regGameNo;
	}

	public void setRegGameNo(int regGameNo) {
		this.regGameNo = regGameNo;
	}

	public int getChessScore() {
		return chessScore;
	}

	public void setChessScore(int chessScore) {
		this.chessScore = chessScore;
	}

	public int getRunScore() {
		return runScore;
	}

	public void setRunScore(int runScore) {
		this.runScore = runScore;
	}

	public int getSumoScore() {
		return sumoScore;
	}

	public void setSumoScore(int sumoScore) {
		this.sumoScore = sumoScore;
	}

	public int getPingPongScore() {
		return pingPongScore;
	}

	public void setPingPongScore(int pingPongScore) {
		this.pingPongScore = pingPongScore;
	}

	public int getRobotNo() {
		return robotNo;
	}

	public void setRobotNo(int robotNo) {
		this.robotNo = robotNo;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getTotalWeigth() {
		return totalWeigth;
	}

	public void setTotalWeigth(int totalWeigth) {
		this.totalWeigth = totalWeigth;
	}

	public Module[] getMdl() {
		return mdl;
	}

	public void setMdl(Module[] mdl) {
		this.mdl = mdl;
	}

	public Robot(int inputRobotNo, Module[] inputMdl) {
		this.robotNo = inputRobotNo;
		this.regGameName=' ';
		this.regGameNo=0;
		this.totalWeigth = (int) (inputMdl[0].getWeight() + inputMdl[1].getWeight() + inputMdl[2].getWeight()
				+ inputMdl[3].getWeight());
		for (int i = 0; i < mdl.length; i++) {
			this.mdl[i]= new Module(inputMdl[i].getModuleName(), inputMdl[i].getDegree(), inputMdl[i].getDurability(),inputMdl[i].isInventory());
			if (inputMdl[i].getModuleName().equals("lg")) {
				this.speed = (250 * inputMdl[i].getForce()) / totalWeigth;
			}
		}
		this.calScore();
	}
	public double calTotalCreditsOfModules(Robot inputRbt) {
		double total = 0;
		for (int i = 0; i < inputRbt.getMdl().length; i++) {
			if (!inputRbt.getMdl()[i].isInventory()) {
				total += inputRbt.getMdl()[i].getPriceOfModule();
			}
		}
		return total;
	}
	public void calScore(){
		int torso=0;
		int arm=0;
		for (int i = 0; i < mdl.length; i++) {
			if (mdl[i].getModuleName().equals("hd")) {
				setChessScore((int)(mdl[i].getIntelligence()*(mdl[i].getDurability()*0.01)));
			}else if (mdl[i].getModuleName().equals("lg")) {
				setRunScore((int)(this.getSpeed()*(mdl[i].getDurability()*0.01)));
			}else if (mdl[i].getModuleName().equals("tr")) {
				torso=(int)(mdl[i].getForce()*(mdl[i].getDurability()*0.01)*0.7);
			}else if (mdl[i].getModuleName().equals("ar")) {
				arm=(int)(mdl[i].getSkill()*(mdl[i].getDurability()*0.01)*0.6);
			}
		}
		setSumoScore((int)(torso+(getRunScore()*0.3)));
		setPingPongScore((int)(arm+(getChessScore()*0.2)+(getRunScore()*0.2)));
		
	}

}
