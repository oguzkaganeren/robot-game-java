package robotGameNew;

import java.util.Random;

public class ArtIntel {
	private Module[] mdl = new Module[4];
	private Robot rbt;
	//private Register rgs;
	private Random rnd = new Random();

	public ArtIntel(Team[] tm) {
		for (int i = 1; i < tm.length; i++) {
			//tm[i].setRegCount(0);
			tm[i].resetRegister();
			for (int j = 0; j < tm[i].getRobotT().length; j++) {
				if (tm[i].getRobotT()[j].getRobotNo() != -1) {
					if (controlOfDurability(tm[i].getRobotT()[j])) {
						tm[i].sellRobot(j);
						// parasý yetiyorsa robot alsýn
						if (controlOfCredits(tm[i])) {
							buyRobot(tm[i]);
						}
					}
					int gameNo=1;
					tm[i].getRobotT()[j].setRegGameName(findGameRandom());
					for (int k = 0; k < 9; k++) {
						if (tm[i].isFullGameChannel(gameNo,tm[i].getRobotT()[j].getRegGameName())) {
							gameNo++;
						}else {
							break;
						}
					}
					tm[i].getRobotT()[j].setRegGameNo(gameNo);//burayý duzelt
				} else {
					if (controlOfCredits(tm[i])) {
						buyRobot(tm[i]);
					}
				}
			}

		}
	}

	public boolean controlOfDurability(Robot rbt) {
		for (int i = 0; i < rbt.getMdl().length; i++) {
			if (rbt.getMdl()[i].getDurability() < 60) {
				return true;
			}
		}
		return false;
	}

	public char findGameRandom() {
		int randomValue = rnd.nextInt(4) + 1;
		if (randomValue == 1) {
			return 'c';
		} else if (randomValue == 2) {
			return 'r';
		} else if (randomValue == 3) {
			return 's';
		} else {
			return 'p';
		}
	}

	public int countOfRobot(Team tm) {
		int count = 0;
		if (tm.getRobotT() != null) {
			for (int i = 0; i < tm.getRobotT().length; i++) {
				if (tm.getRobotT()[i].getRobotNo() != -1) {
					count++;
				}
			}
		}
		return count;
	}

	public boolean controlOfCredits(Team t) {
		mdl[0] = new Module("tr", 1, 100, false);
		mdl[1] = new Module("ar", 1, 100, false);
		mdl[2] = new Module("lg", 1, 100, false);
		mdl[3] = new Module("hd", 1, 100, false);
		if (t.getCredits() > (mdl[0].getPriceOfModule() + mdl[1].getPriceOfModule() + mdl[2].getPriceOfModule()
				+ mdl[3].getPriceOfModule())) {
			return true;
		} else {
			return false;
		}
	}

	public void buyRobot(Team t) {
		int rbtNo = 0;
		for (int i = 0; i < t.getRobotT().length; i++) {
			if (t.getRobotT()[i].getRobotNo() == -1) {
				rbtNo = i;
			}
		}
		rbt = new Robot(rbtNo, mdl);
		t.buildRobot(rbt);
	}
}
