package robotGameNew;

public class Team {
	/* icerisinde modulAdi ve mdlDurability olacaktýr */
	private Module[] inventory = new Module[20];
	/* robotAdi,sonraki 4 alan moduller(moduller içine tr-68 seklinde olacak) */
	private Robot[] robotT = new Robot[9];
	/* baslangic kredileri 1500 */
	private double credits = 1500;
	/* her team en fazla 9 kayýt yapabilir (9 robot var) */
	//private Register[] register = new Register[9];

/*	public Register[] getRegister() {
		return register;
	}
*/
	private static int regCount;

	public static int getRegCount() {
		return regCount;
	}

	public static void setRegCount(int regCount) {
		Team.regCount = regCount;
	}

/*	public void setRegister(Register register) {
		if (isSameRegRobot(register)) {
			System.out.println("You have already registered the robot.");
		} else {
			this.register[regCount] = register;
			regCount++;
		}

	}*/


	public Module[] getInventory() {
		return inventory;
	}

	public void setInventory(Module inventory, int index) {
		this.inventory[index] = inventory;
	}

	public double getCredits() {
		return credits;
	}

	public void setCredits(double inputCredits) {
		this.credits = inputCredits;
	}

	public void setRobotT(Robot robotT, int robotNo) {
		this.robotT[robotNo] = robotT;
	}

	public Robot[] getRobotT() {
		return robotT;
	}
	/*daha önce robotu register ettiyse true doner*/
	/*private boolean isSameRegRobot(Register inRegister)
	{
		for (int i = 0; i < this.register.length; i++) {
			if (this.register[i] != null) {
				if (this.register[i].getRbt().getRobotNo() == inRegister.getRbt().getRobotNo()) {
					return true;
				}
			}

		}
		return false;
	}
*/
	public void resetRegister() {
		for (int i = 0; i < getRobotT().length; i++) {
			if (getRobotT()!=null) {
				if (getRobotT()[i].getRegGameNo()!=0) {
					getRobotT()[i].setRegGameNo(0);
					getRobotT()[i].setRegGameName(' ');
				}
			}
		}
	}
	public Team() {
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = new Module("", 0, -1, false);
		}
		for (int i = 0; i < robotT.length; i++) {
			robotT[i] = new Robot(-1, new Module[] { new Module("", 0, -1, false), new Module("", 0, -1, false),
					new Module("", 0, -1, false), new Module("", 0, -1, false) });
		}
		//this.regCount = 0;
	}

	public Module findBestModule(String moduleName,int degree){
		int bestDurability=0;
		int invNo=0;
		Module bestM=new Module("", -1, 0, false);
		for (int i = 0; i < getInventory().length; i++) {
			if (searchInventory(i).getModuleName().equals(moduleName)&&searchInventory(i).getDurability()>=60&&searchInventory(i).getDegree()==degree) {
				if (searchInventory(i).getDurability()>bestDurability) {
					bestDurability=searchInventory(i).getDurability();
					invNo=i;
				}
			}
		}
		bestM=searchInventory(invNo);
		bestM.setInventory(true);
		setInventory(new Module("", 0, -1, true),invNo);
		return bestM;
	}
	public int countOfSameModules(String moduleName,int degree){
		int count=0;
		for (int i = 0; i < getInventory().length; i++) {
			if (searchInventory(i).getModuleName().equals(moduleName)&&searchInventory(i).getDurability()>=60&&searchInventory(i).getDegree()==degree) {
				count++;
			}
		}
		return count;
	}
	public void listRobots() {
		for (int i = 0; i < robotT.length; i++) {
			System.out.print("r" + (i + 1) + ":\t");
			for (int j = 0; j < robotT[i].getMdl().length; j++) {
				/* Degree 0 ise robot yoktur ve parca gostermez*/
				if (robotT[i].getMdl()[j].getDegree() != 0) {
					System.out.print(robotT[i].getMdl()[j].getModuleName() + robotT[i].getMdl()[j].getDegree() + "-"
							+ robotT[i].getMdl()[j].getDurability());
				}
				System.out.print("  ");
			}
			/*-1 robot yok demektir*/
			if (robotT[i].getRobotNo() != -1) 
			{
				System.out.println("( Ch: " + robotT[i].getChessScore() + "\tRn: " + robotT[i].getRunScore() + "\tSm: "
						+ robotT[i].getSumoScore() + "\tPp: " + robotT[i].getPingPongScore() + " )");
			} else {
				System.out.println();
			}
		}
	}

	public void listInventory() {
		for (int i = 0; i < inventory.length; i++) {
			/*ekrana yazdýrýrken m01,m02 þeklinde yazdýrýyoruz*/
			if (i < 9) {
				System.out.print("m0" + (i + 1) + ".");
			} else {
				System.out.print("m" + (i + 1) + ".");
			}
			if (inventory[i].getDegree() == 0) {
				System.out.print(inventory[i].getModuleName() + "-");
			} else {
				System.out.print(inventory[i].getModuleName() + inventory[i].getDegree() + "-");
			}

			if (inventory[i].getDurability() != -1) {
				System.out.print(inventory[i].getDurability() + "  ");
			} else {
				System.out.print("\t" + "\t");
			}
			if (i != 0 && (i + 1) % 5 == 0) {
				/*5 ögeden sonra bir alt satýra geçmeye yarar*/
				System.out.println();
			}
		}
		System.out.println();
	}

	public Module searchInventory(int inventoryNo) {
		return inventory[inventoryNo];
	}

	public Robot searchRobot(int rbtNo) {
		return robotT[rbtNo];
	}

	public int countOfRobot() {
		int count = 0;
		if (robotT!=null) {
			for (int i = 0; i < robotT.length; i++) {
				if (this.getRobotT()[i].getRobotNo() != -1) {
					count++;
				}
			}
		}

		return count;
	}

	public int nmOfTeamReg() {
		int count = 0;
		for (int i = 0; i < getRobotT().length; i++) {
			if (getRobotT()!=null) {
				if (getRobotT()[i].getRobotNo()!=0) {
					count++;
				}
			}
		}
		return count;
	}
	public boolean isFullGameChannel(int gameNo,char gameName){
		for (int i = 0; i < getRobotT().length; i++) {
			if (getRobotT()!=null) {
				if (getRobotT()[i].getRegGameNo()==gameNo&&getRobotT()[i].getRegGameName()==gameName) {
					return true;
				}
			}
		}
		return false;
	}
	public void listRegisters() {
		for (int i = 0; i < getRobotT().length; i++) {
			if (getRobotT()!=null) {
				if (getRobotT()[i].getRegGameNo()!=0) {
					System.out.print("r" + (getRobotT()[i].getRobotNo() + 1) + ">" + getRobotT()[i].getRegGameName()
							+ getRobotT()[i].getRegGameNo() + "\t");
				}
			}
		}
	}
	public void sellRobot(int rbtNo) {// satýn alma metodu
		if (getRobotT()[rbtNo].getRobotNo()!=-1) {
			for (int i = 0; i < getRobotT()[rbtNo].getMdl().length; i++) {
				setCredits(getCredits() + (0.5 * getRobotT()[rbtNo].getMdl()[i].getPriceOfModule()
						* getRobotT()[rbtNo].getMdl()[i].getDurability() / 100));
			}
			setRobotT(new Robot(-1, new Module[] { new Module("", 0, -1,false), new Module("", 0, -1,false),
					new Module("", 0, -1,false), new Module("", 0, -1,false) }),rbtNo);
		} else {
			System.out.println("You can not sell non-existent robot.");
		}
	}
	public void buildRobot(Robot inputRbt) {
		if (getCredits() - inputRbt.calTotalCreditsOfModules(inputRbt) > 0) {
			setRobotT(inputRbt,inputRbt.getRobotNo());
			setCredits(getCredits() - inputRbt.calTotalCreditsOfModules(inputRbt));
			inputRbt.calScore();
		} else {
			System.out.println("There is not enough money.");
		}
	}
	public void divideRobot(int robotNo) {
		if (getRobotT()[robotNo]!=null) {
		
		if (getRobotT()[robotNo].getRobotNo() != -1)// robot numarasýnýn -1
														// olmasý o slot'un bos
														// oldugu anlamýna gelir
		{
			int count=0;
			for (int i = 0; i < getInventory().length; i++) {
				if (getInventory()[i].getModuleName().equals("")) {
					count++;
				}
				if (count==4) {
					break;
				}
			}
			if (count==4) {
				count=0;
				for (int i = 0; i < getInventory().length; i++) {
					if (getInventory()[i].getModuleName().equals("")) {
					setInventory(getRobotT()[robotNo].getMdl()[count], i);
					count++;
					}
					if (count==4) {
						break;
					}
				}
				setRobotT(new Robot(-1, new Module[] { new Module("", 0, -1,false), new Module("", 0, -1,false),
						new Module("", 0, -1,false), new Module("", 0, -1,false) }),robotNo);
			}
			else {
				System.out.println("There is not empty in your inventory");
			}
			

		} else {
			System.out.println("There is no robot.");
		}
	}

	}
	public void buyModule(Module mdl) {// satýn alma metodu
		boolean isEmptyInv = false;
		switch (mdl.getModuleName()) {
		case "tr":
			if (getCredits() - mdl.getPriceOfModule() > 0) {// kredi negatif
																// olmasýn
				for (int i = 0; i < getInventory().length; i++) {
					if (getInventory()[i].getModuleName().equals("")) {// bos
																			// ise
																			// modulu
																			// yolla
						
						//mdl.priceOfModule = tr[mdl.degree - 1];
						setInventory(mdl, i);// inventory'e modulu
													// gonderiyoruz
						setCredits(getCredits() - mdl.getPriceOfModule());// krediden
																					// modul
																					// parasini
																					// dusuyoruz
						isEmptyInv = true;// yeri varsa hata vermez yoksa false
											// kalir ve hata verir
						break;
					}

				}
				if (!isEmptyInv) {
					System.out.println("We apologize to you for not making enough inventory :(");// inventory
																									// dolarsa
																									// buraya
																									// duser
				}
			} else {
				System.out.println("You do not have enough money to buy it.");
			}
			break;
		case "hd":
			if (getCredits() - mdl.getPriceOfModule() > 0) {// kredi negatif
																// olmasýn
				for (int i = 0; i < getInventory().length; i++) {
					if (getInventory()[i].getModuleName().equals("")) {
						//mdl.priceOfModule = hd[mdl.degree - 1];
						setInventory(mdl, i);
						setCredits(getCredits() - mdl.getPriceOfModule());
						isEmptyInv = true;
						break;
					}

				}
				if (!isEmptyInv) {
					System.out.println("We apologize to you for not making enough inventory :(");// inventory
																									// dolarsa
																									// buraya
																									// duser
				}
			} else {
				System.out.println("You do not have enough money to buy it.");
			}
			break;
		case "lg":
			if (getCredits() - mdl.getPriceOfModule() > 0) {// kredi negatif
																// olmasýn
				for (int i = 0; i < getInventory().length; i++) {
					if (getInventory()[i].getModuleName().equals("")) {
						//mdl.priceOfModule = lg[mdl.degree - 1];
						setInventory(mdl, i);
						setCredits(getCredits() - mdl.getPriceOfModule());
						isEmptyInv = true;
						break;
					}

				}
				if (!isEmptyInv) {
					System.out.println("We apologize to you for not making enough inventory :(");// inventory
																									// dolarsa
																									// buraya
																									// duser
				}
			} else {
				System.out.println("You do not have enough money to buy it.");
			}
			break;
		case "ar":
			if (getCredits() - mdl.getPriceOfModule() > 0) {// kredi negatif
																// olmasýn
				for (int i = 0; i < getInventory().length; i++) {
					if (getInventory()[i].getModuleName().equals("")) {
						//mdl.priceOfModule = ar[mdl.degree - 1];
						setInventory(mdl, i);//
						setCredits(getCredits() - mdl.getPriceOfModule());
						isEmptyInv = true;
						break;
					}

				}
				if (!isEmptyInv) {
					System.out.println("We apologize to you for not making enough inventory :(");// inventory
																									// dolarsa
																									// buraya
																									// duser
				}
			} else {
				System.out.println("You do not have enough money to buy it.");
			}
			break;

		default:
			break;
		}
	}
	public void sellModule(int invNo) {// satýn alma metodu
		if (!getInventory()[invNo].getModuleName().equals("")) {
			setCredits(getCredits() + (0.5 * getInventory()[invNo].getPriceOfModule()
					* getInventory()[invNo].getDurability() / 100));
			Module mdl = new Module("", 0, -1,false);
			setInventory(mdl, invNo);
		} else {
			System.out.println("You can not sell non-existent item.");
		}
	}
	public void changeModule(int rbtNo,int invNo) {
		if (getRobotT()[rbtNo].getRobotNo()!=-1) {
			Module[] mdl=getRobotT()[rbtNo].getMdl();
			String cName= searchInventory(invNo).getModuleName();
			if (cName.equals("")) {
				System.out.println("You can not change non-existent item.");
			}
			else {
				for (int i = 0; i < mdl.length; i++) {
					if(getRobotT()[rbtNo].getMdl()[i].getModuleName().equals(cName)){
						Module tempMdl=mdl[i];
						mdl[i]=searchInventory(invNo);
						getRobotT()[rbtNo].setMdl(mdl);
						setInventory(tempMdl, invNo);
						getRobotT()[rbtNo].calScore();
						break;
					}
				}
			}
			
		}else {
			System.out.println("You can not change non-existent robot.");
		}
		
	}
	

}