package robotGameNew;

import java.awt.Color;
import java.util.Random;

import enigma.console.TextAttributes;
import enigma.core.Enigma;

public class Game {
	private Team[] t = new Team[] { new Team(), new Team(), new Team(), new Team(), new Team(), new Team() };
	private Module mdl;
	private Robot rbt;
	private ArtIntel ai;
	private int lastCursorPositionY = 26;
	public static enigma.console.Console cn;
	private Random random = new Random();
	public static TextAttributes attrs = new TextAttributes(Color.YELLOW);
	private String[] cmdSplit;
	private int week = 1;
	private double chessPrize = 200;
	private double runPrize = 200;
	private double sumoPrize = 250;
	private double pingPongPrize = 250;
	private String[] differentGames = new String[] { "", "", "", "", "", "" };
	private boolean isFinish;
	/* pl yazdýysa komut satýrýndan ust kýsmý yazmamasý icin kullandýk */
	private boolean isPressPL;

	public boolean isPressPL() {
		return isPressPL;
	}

	public void setPressPL(boolean isPressPL) {
		this.isPressPL = isPressPL;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public void Command(String inputCMD) {
		setPressPL(false);
		/* komut boþ gelmediyse */
		if (inputCMD != "") {
			/* basindaki ve sonundaki bosluklarý al */
			inputCMD.trim();
			/* tum harfleri kucult */
			inputCMD.toLowerCase();
			/* bosluga gore parcalara bol */
			cmdSplit = inputCMD.split(" ");
			/* eksik komut girmesini önledik */
			if (cmdSplit.length > 0) {
				switch (cmdSplit[0]) {
				case "by":// buy module
					if (isModule(cmdSplit[1]) && cmdSplit.length == 2) {
						int degree = Integer.parseInt(cmdSplit[1].substring(2, 3));
						/*
						 * if icindeki length ile fazla modul girmesini
						 * engelliyoruz get Module.buy();
						 */
						String modulName = cmdSplit[1].substring(0, 2);
						mdl = new Module(modulName, degree, 100, false);
						t[0].buyModule(mdl);
						attrs = new TextAttributes(Color.GREEN);
						cn.setTextAttributes(attrs);
						System.out.println(modulName.toUpperCase() + " was bought.");
						attrs = new TextAttributes(Color.YELLOW);
						cn.setTextAttributes(attrs);
					} else {
						error();
					}
					break;
				/* sell module */
				case "sl":
					if (isInventory(cmdSplit[1]) && cmdSplit.length == 2) {
						if (calInventoryNo(cmdSplit[1]) == -2) {
							error();
						} else {
							int invNo = calInventoryNo(cmdSplit[1]);
							String mdlName = t[0].searchInventory(invNo).getModuleName();
							t[0].sellModule(invNo);
							attrs = new TextAttributes(Color.GREEN);
							cn.setTextAttributes(attrs);
							System.out.println(mdlName.toUpperCase() + " was sold.");
							attrs = new TextAttributes(Color.YELLOW);
							cn.setTextAttributes(attrs);
						}
					} else if (isRobot(cmdSplit[1]) && cmdSplit.length == 2) {
						/* robot satmak istediyse */
						t[0].sellRobot(Integer.parseInt((cmdSplit[1].substring(1, 2))) - 1);
					} else {
						error();
					}
					/* tüm robotu satma kýsmýný ekle */
					break;
				/* build new robot */
				case "++":
					boolean error = false;
					if (cmdSplit.length == 7) {
						if (isRobot(cmdSplit[1]) && cmdSplit[2].equals("=")
								&& (isModule(cmdSplit[3]) || isInventory(cmdSplit[3]))
								&& (isModule(cmdSplit[4]) || isInventory(cmdSplit[4]))
								&& (isModule(cmdSplit[5]) || isInventory(cmdSplit[5]))
								&& (isModule(cmdSplit[6]) || isInventory(cmdSplit[6]))
								&& !isSameModule(cmdSplit[3], cmdSplit[4], cmdSplit[5], cmdSplit[6])
								&& !isSameInventory(cmdSplit[3], cmdSplit[4], cmdSplit[5], cmdSplit[6])
								&& !compareInventoryAndModule(cmdSplit)) {
							if (t[0].getRobotT()[Integer.parseInt(cmdSplit[1].substring(1, 2)) - 1].getRobotNo() == -1)// robot
							/*
							 * numarasýnýn -1 olmasý o slot'un bos oldugu
							 * anlamýna gelir
							 */
							{
								/*
								 * Robot için Modul
								 * iþlemleri-----------------------------
								 */
								Module[] modul = new Module[4];
								for (int i = 0; i < modul.length; i++) {
									//Eðer inventory yazdýysa
									if (isInventory(cmdSplit[3 + i])) {
										/* m01 gelir, 01'i alýr ve arar */
										if (t[0].searchInventory(calInventoryNo(cmdSplit[3 + i])) != null) {
											if (t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
													.getDurability() > 60) {

												String mdlName = t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
														.getModuleName();
												int mdlDegree = t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
														.getDegree();
												int mdlDrb = t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
														.getDurability();
												//hata yoksa modulu oluþturur ve inventory'ý boþaltýr
													/*
													 * sondaki true inventory
													 * oldugunu belirtir
													 */
													modul[i] = new Module(mdlName, mdlDegree, mdlDrb, true);
													t[0].setInventory(new Module("", 0, -1, false),
															calInventoryNo(cmdSplit[3 + i]));
											} else {
												System.out.println("Module which is "
														+ t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
																.getModuleName()
														+ " is broken, please try another module.");
												error = true;
											}
										} else {
											error();
											error = true;
											break;
										}
									} else if (isModule(cmdSplit[3 + i])) {
										/*
										 * girilen modul inventory'de var mý
										 * diye bakýyoruz sayýsý 0'dan buyuk
										 * gelirse
										 */
										/*
										 * durabilityleri 60 altýnda ise bakmaz
										 */
										if (t[0].countOfSameModules(cmdSplit[3 + i].substring(0, 2),
												Integer.parseInt(cmdSplit[3 + i].substring(2, 3))) > 0) {
											modul[i] = t[0].findBestModule(cmdSplit[3 + i].substring(0, 2),
													Integer.parseInt(cmdSplit[3 + i].substring(2, 3)));
										} else {
											System.out.println("There was not a " + cmdSplit[3 + i]
													+ " that you could use in your inventory, we bought it for you.");
											modul[i] = new Module(cmdSplit[3 + i].substring(0, 2),
													Integer.parseInt(cmdSplit[3 + i].substring(2, 3)), 100, false);
										}

									}
								}
								/*------------------------------------------------------------*/
								if (!error && !isSameModule(modul[0].getModuleName(), modul[1].getModuleName(),
										modul[2].getModuleName(), modul[3].getModuleName())) {
									rbt = new Robot(Integer.parseInt(cmdSplit[1].substring(1, 2)) - 1, modul);
									t[0].buildRobot(rbt);
									attrs = new TextAttributes(Color.GREEN);
									cn.setTextAttributes(attrs);
									System.out.println("R" + (rbt.getRobotNo() + 1) + " was created.");
									attrs = new TextAttributes(Color.YELLOW);
									cn.setTextAttributes(attrs);
								}

							} else {
								System.out.println("The robot is already in your robot list.");
							}
						} else {
							error();
						}
					} else {
						error();
					}

					break;
				/* divide robot */
				case "--":
					if (isRobot(cmdSplit[1]) && cmdSplit.length == 2) {
						/* robot parcala */
						t[0].divideRobot(Integer.parseInt(cmdSplit[1].substring(1, 2)) - 1);
					}
					break;
				/* change related module */
				case "ch":
					/*
					 * üc tane degerden fazla veya az girilmesi durumunda hata
					 * almamak icin
					 */
					if (cmdSplit.length == 3) {
						/* eðer robot ismi girdiyse */
						if (isRobot(cmdSplit[1]) && isInventory(
								cmdSplit[2])) {/*
												 * robot isminden sonra modul
												 * girdiyse
												 */
							t[0].changeModule(Integer.parseInt(cmdSplit[1].substring(1, 2)) - 1,
									calInventoryNo(cmdSplit[2]));

						} else {
							/* robot veya modul kýsmýný yanlýþ girmiþ */
							error();
						}
					} else {
						error();
					}
					break;
				case "ls":// list team
					if (cmdSplit.length == 2) {
						/*
						 * matches ile girilen deðerin sayý olup olmadýðýna
						 * bakýyoruz
						 */
						if (cmdSplit[1].matches("^-?\\d+$")) {
							if ((Integer.parseInt(cmdSplit[1])) > 0 && (Integer.parseInt(cmdSplit[1])) < 7) {
								/* parseInt ile stringi integera ceviriyoruz */
								/* get Team.list */
								t[Integer.parseInt(cmdSplit[1]) - 1].listRobots();
							} else {
								/*
								 * 0-7 arasý girilmediyse hata verecektir(6
								 * takým var)
								 */
								error();

							}
						} else {
							/* sayý girilmediyse hata verecektir */
							error();
						}
					} else {
						/* fazla veya eksik komut girdiyse */
						error();
					}
					break;
				case "rg":/* register */
					if (cmdSplit.length == 4) {
						if (isRobot(cmdSplit[1]) && cmdSplit[2].equals(">") && isGame(cmdSplit[3]))// eðer
						/* robot ismi girdiyse */
						{

							/* change iþlemini yap */
							Robot rRbt = t[0].searchRobot(Integer.parseInt(cmdSplit[1].substring(1, 2)) - 1);
							if (!controlOfDurability(rRbt)) {
								// int rbtNo =
								// Integer.parseInt(cmdSplit[3].substring(1,
								// 2));
								if (rRbt.getRobotNo() != -1) {
									// rgs = new Register(rRbt,
									// cmdSplit[3].charAt(0), rbtNo, 0);//
									// sonraki
									// par.
									// teamNodur
									// t[0].setRegister(rgs);
									if (rRbt.getRegGameNo() == 0) {
										if (t[0].isFullGameChannel(Integer.parseInt(cmdSplit[3].substring(1, 2)),
												cmdSplit[3].charAt(0))) {
											System.out.println(
													"The game channel is filled, please try the next channel.");
										} else {
											rRbt.setRegGameName(cmdSplit[3].charAt(0));
											rRbt.setRegGameNo(Integer.parseInt(cmdSplit[3].substring(1, 2)));
										}
									} else {
										System.out.println("You already registered this robot.");
									}
								} else {
									System.out.println("There is no such a robot.");
								}
							}
						} else {
							error();
							/* robot veya modul kýsmýný yanlýþ girmiþ */
						}
					} else {
						error();
						/* fazla veya eksik komut girdiyse */
					}
					break;
				case "pl":/* play games */
					/* run game.play */
					/* Ai'yi çalýþtýr once sonra play */
					ai = new ArtIntel(t);
					calPrize();
					attrs = new TextAttributes(Color.ORANGE);
					cn.setTextAttributes(attrs);
					System.out.println("---Games (Results)---");
					attrs = new TextAttributes(Color.YELLOW);
					cn.setTextAttributes(attrs);
					System.out.println("---RoboChess: " + chessPrize + "\t(" + calCountRegTeam('c') + " Teams)");

					attrs = new TextAttributes(Color.WHITE);
					cn.setTextAttributes(attrs);
					listRegisterTeams('c');
					calScoresAndWinner('c');
					attrs = new TextAttributes(Color.YELLOW);
					cn.setTextAttributes(attrs);
					System.out.println("---RoboRun: " + runPrize + "\t(" + calCountRegTeam('r') + " Teams)");
					attrs = new TextAttributes(Color.WHITE);
					cn.setTextAttributes(attrs);
					listRegisterTeams('r');
					calScoresAndWinner('r');
					attrs = new TextAttributes(Color.YELLOW);
					cn.setTextAttributes(attrs);
					System.out.println("---RoboSumo: " + sumoPrize + "\t(" + calCountRegTeam('s') + " Teams)");
					attrs = new TextAttributes(Color.WHITE);
					cn.setTextAttributes(attrs);
					listRegisterTeams('s');
					calScoresAndWinner('s');
					attrs = new TextAttributes(Color.YELLOW);
					cn.setTextAttributes(attrs);
					System.out.println("---RoboPingPong: " + pingPongPrize + "\t(" + calCountRegTeam('p') + " Teams)");
					attrs = new TextAttributes(Color.WHITE);
					cn.setTextAttributes(attrs);
					listRegisterTeams('p');
					calScoresAndWinner('p');
					/*
					 * 3 hafta boyunca 2'den fazla farklý oyuna girdiyse 150
					 * kredi ekleyecek
					 */
					if (week % 3 == 0) {
						for (int i = 0; i < differentGames.length; i++) {
							if (differentGames[i].length() > 1) {
								t[i].setCredits(t[i].getCredits() + 150);
							}
						}
						/*
						 * diffrentGame'ý sýfýrlýyoruz ki gelecek 3 hafta için
						 * boþ olsun
						 */
						resetDifferentGame();
					} else {
						controlOfDifferentGame();
					}
					resetRegisters();
					reduceDurabilty();
					week++;
					t[0].resetRegister();
					controlOfFinish();
					setPressPL(true);
					break;
				case "/help":
					System.out.println("Example Commands:");
					System.out.println("\tby hd1\t\t\t\t\t buy hd1 module");
					System.out.println("\tsl m03\t\t\t\t\t sell module m03");
					System.out.println("\tsl r1\t\t\t\t\t  sell all parts of robot r1");
					System.out.println("\t++ r1 = tr1 hd1 ar1 lg1\tbuild new robot");
					System.out.println("\t++ r1 = tr1 m01 lg1 hd1\tbuild new robot");
					System.out.println("\t-- r1\t\t\t\t\t  divide robot");
					System.out.println("\tch r4 m01\t\t\t\t  change related module of robot r4");
					System.out.println("\trg r1 > c1\t\t\t\t register robot r2 as the first robot of the chess game");
					System.out.println("\tpl \t\t\t\t\t    play games and list results.");
					break;
				default:
					error();
					break;
				}
			} else {/* eksik komut girdiyse */
				error();
			}

		}

	}
	public boolean compareInventoryAndModule(String[] cmd){
		for (int i = 0; i < cmd.length-3; i++) {
			if (isInventory(cmdSplit[3 + i]))
			{
				if (t[0].searchInventory(calInventoryNo(cmdSplit[3 + i])) != null) {
					if (t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
							.getDurability() > 60) {

						String mdlName = t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
								.getModuleName();
						int mdlDegree = t[0].searchInventory(calInventoryNo(cmdSplit[3 + i]))
								.getDegree();
						//o modul takýlmýþmý diye bakar
						for (int j = 0; j < cmd.length-3; j++) {
								if (cmd[j+3].equals(mdlName+mdlDegree)) {
									return true;
								}
						}
						
					}
				}
			}
		}
		return false;
	}
	public boolean controlOfDurability(Robot rbt) {
		for (int i = 0; i < rbt.getMdl().length; i++) {
			if (rbt.getMdl()[i].getDurability() < 60) {
				System.out.println(rbt.getMdl()[i].getModuleName() + " is broken, please change it.");
				return true;
			}
		}
		return false;
	}

	public void resetRegisters() {
		for (int i = 0; i < t.length; i++) {
			t[i].resetRegister();
		}
	}

	public int findWinner(double[] scores) {
		double bestScore = 0;
		int winner = -1;
		for (int i = 0; i < scores.length; i++) {

			if (scores[i] != 0) {
				if (bestScore < scores[i]) {
					bestScore = scores[i];
					winner = (i + 1);
				}
			}
		}
		return winner;
	}

	public void listRegisterTeams(char gameName) {
		for (int i = 0; i < t.length; i++) {
			if (t[i].getRobotT() != null) {
				for (int j = 0; j < t[i].getRobotT().length; j++) {
					if (t[i].getRobotT()[j] != null && t[i].getRobotT()[j].getRegGameNo() != 0
							&& t[i].getRobotT()[j].getRegGameName() == gameName) {
						System.out.print("Team-" + (i + 1) + ":");
						for (int j2 = 0; j2 < t[i].getRobotT()[j].getMdl().length; j2++) {

							System.out.print(t[i].getRobotT()[j].getMdl()[j2].getModuleName()
									+ t[i].getRobotT()[j].getMdl()[j2].getDegree() + "-"
									+ t[i].getRobotT()[j].getMdl()[j2].getDurability());
							System.out.print(" ");

						}
						System.out.println("( Ch: " + t[i].getRobotT()[j].getChessScore() + "\tRn: "
								+ t[i].getRobotT()[j].getRunScore() + "\tSm: " + t[i].getRobotT()[j].getSumoScore()
								+ "\tPp: " + t[i].getRobotT()[j].getPingPongScore() + " )");
					}
				}
			}
		}
	}

	public void calScoresAndWinner(char gameName) {
		boolean isNoTeam = true;/*
								 * eðer oyuna katýlan olmadýysa true kalacaktýr
								 */
		double[] tmsScore = new double[6];
		for (int i = 0; i < t.length; i++) {
			if (t[i].getRobotT() != null) {
				for (int j = 0; j < t[i].getRobotT().length; j++) {
					if (t[i].getRobotT()[j] != null)
						if (t[i].getRobotT()[j].getRegGameName() == gameName) {
							/* durabilityleri düþürüyorum */
							if (t[i].getRobotT()[j].getMdl()[0].getDurability() > 0) {
								t[i].getRobotT()[j].getMdl()[0]
										.setDurability(t[i].getRobotT()[j].getMdl()[0].getDurability() - 2);
							}
							if (t[i].getRobotT()[j].getMdl()[1].getDurability() > 0) {
								t[i].getRobotT()[j].getMdl()[1]
										.setDurability(t[i].getRobotT()[j].getMdl()[1].getDurability() - 2);
							}
							if (t[i].getRobotT()[j].getMdl()[2].getDurability() > 0) {
								t[i].getRobotT()[j].getMdl()[2]
										.setDurability(t[i].getRobotT()[j].getMdl()[2].getDurability() - 2);
							}
							if (t[i].getRobotT()[j].getMdl()[3].getDurability() > 0) {
								t[i].getRobotT()[j].getMdl()[3]
										.setDurability(t[i].getRobotT()[j].getMdl()[3].getDurability() - 2);
							}
							/* Takýmlarýn skorlarýný hesaplýyorum */
							switch (gameName) {
							case 'c':
								if (t[i].getRobotT()[j].getRegGameNo() > 1) {
									tmsScore[i] += t[i].getRobotT()[j].getChessScore()
											/ ((t[i].getRobotT()[j].getRegGameNo() - 1) * 4);

								} else {
									tmsScore[i] += t[i].getRobotT()[j].getChessScore();
								}
								break;
							case 'r':
								if (t[i].getRobotT()[j].getRegGameNo() > 1) {
									tmsScore[i] += t[i].getRobotT()[j].getRunScore()
											/ ((t[i].getRobotT()[j].getRegGameNo() - 1) * 4);
								} else {
									tmsScore[i] += t[i].getRobotT()[j].getRunScore();
								}
								break;
							case 's':
								if (t[i].getRobotT()[j].getRegGameNo() > 1) {

									tmsScore[i] += t[i].getRobotT()[j].getSumoScore()
											/ ((t[i].getRobotT()[j].getRegGameNo() - 1) * 4);
								} else {
									tmsScore[i] += t[i].getRobotT()[j].getSumoScore();
								}
								break;
							case 'p':
								if (t[i].getRobotT()[j].getRegGameNo() > 1) {
									tmsScore[i] += t[i].getRobotT()[j].getPingPongScore()
											/ ((t[i].getRobotT()[j].getRegGameNo() - 1) * 4);
								} else {
									tmsScore[i] += t[i].getRobotT()[j].getPingPongScore();
								}
								break;
							}

						}
				}

			}
			double randomValue = 0.950 + (1.050 - 0.950) * random.nextDouble();
			tmsScore[i] *= randomValue;
			tmsScore[i] = Math.floor(tmsScore[i] * 100) / 100;
			if (tmsScore[i] != 0) {
				attrs = new TextAttributes(Color.MAGENTA);
				cn.setTextAttributes(attrs);// yazý rengini consola uyguluyoruz
				System.out.println("t" + (i + 1) + " score = " + tmsScore[i]);
				attrs = new TextAttributes(Color.WHITE);
				cn.setTextAttributes(attrs);// yazý rengini consola uyguluyoruz
				isNoTeam = false;
			}
		}

		if (isNoTeam) {
			System.out.println("Winner: No winner. Prize transferred to the next week");
			switch (gameName) {
			case 'c':
				chessPrize += chessPrize;
				break;
			case 'r':
				runPrize += runPrize;
				break;
			case 's':
				sumoPrize += sumoPrize;
				break;
			case 'p':
				pingPongPrize += pingPongPrize;
				break;
			}
		} else {
			int winner = findWinner(tmsScore);
			if (winner >= 0) {
				attrs = new TextAttributes(Color.GREEN);
				cn.setTextAttributes(attrs);// yazý rengini consola uyguluyoruz
				System.out.println("Winner: Team " + winner);
				attrs = new TextAttributes(Color.WHITE);
				cn.setTextAttributes(attrs);// yazý rengini consola uyguluyoruz
				switch (gameName) {
				case 'c':
					t[winner - 1].setCredits(t[winner - 1].getCredits() + chessPrize);
					chessPrize = 200;/* kazanan olduysa prize resetlenir */
					break;
				case 'r':
					t[winner - 1].setCredits(t[winner - 1].getCredits() + runPrize);
					runPrize = 200;
					break;
				case 's':
					t[winner - 1].setCredits(t[winner - 1].getCredits() + sumoPrize);
					sumoPrize = 250;
					break;
				case 'p':
					t[winner - 1].setCredits(t[winner - 1].getCredits() + pingPongPrize);
					pingPongPrize = 250;
					break;
				}

			}
		}
	}

	public void reduceDurabilty() {
		for (int i = 0; i < t.length; i++) {
			if (t[i].getRobotT() != null) {
				for (int j = 0; j < t[i].getRobotT().length; j++) {
					if (t[i].getRobotT()[j] != null) {
						if (t[i].getRobotT()[j].getMdl()[0].getDurability() > 0) {
							t[i].getRobotT()[j].getMdl()[0]
									.setDurability(t[i].getRobotT()[j].getMdl()[0].getDurability() - 2);
						}
						if (t[i].getRobotT()[j].getMdl()[1].getDurability() > 0) {
							t[i].getRobotT()[j].getMdl()[1]
									.setDurability(t[i].getRobotT()[j].getMdl()[1].getDurability() - 2);
						}
						if (t[i].getRobotT()[j].getMdl()[2].getDurability() > 0) {
							t[i].getRobotT()[j].getMdl()[2]
									.setDurability(t[i].getRobotT()[j].getMdl()[2].getDurability() - 2);
						}
						if (t[i].getRobotT()[j].getMdl()[3].getDurability() > 0) {
							t[i].getRobotT()[j].getMdl()[3]
									.setDurability(t[i].getRobotT()[j].getMdl()[3].getDurability() - 2);
						}

					}
				}
			}
		}
	}

	public boolean isRobot(String rbt) {/* robot ise true döndürür */
		for (int i = 0; i < 9; i++) {
			if (rbt.equals("r" + (i + 1)))/* eðer robot ismi girdiyse */
			{
				return true;
			}
		}
		return false;
	}

	/* metod'da biraz sorun var sonra bak!!! */
	public boolean isGame(String gm) {
		if (gm.substring(0, 1).equals("c") || gm.substring(0, 1).equals("r") || gm.substring(0, 1).equals("s")
				|| gm.substring(0, 1).equals("p")) {
			for (int i = 0; i < 9; i++) {
				if (Integer.parseInt(gm.substring(1, 2)) == (i + 1)) {
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public void error() {
		attrs = new TextAttributes(Color.RED);
		cn.setTextAttributes(attrs);
		System.out.println("There is a error, please follow the guidelines./help");
		attrs = new TextAttributes(Color.WHITE);
		cn.setTextAttributes(attrs);
	}

	public boolean isModule(
			String mdl) {/* module(ar1,tr2 gibi) kontrolu yapar */
		for (int i = 0; i < 6; i++) {
			if (mdl.equals("tr" + (i + 1))) {
				return true;
			} else if (mdl.equals("hd" + (i + 1))) {
				return true;
			} else if (mdl.equals("lg" + (i + 1))) {
				return true;
			} else if (mdl.equals("ar" + (i + 1))) {
				return true;
			}
		}
		return false;
	}

	public boolean isSameInventory(String inv1, String inv2, String inv3, String inv4) {
		String[] arrInv = new String[] { inv1, inv2, inv3, inv4 };
		for (int i = 0; i < arrInv.length; i++) {
			for (int j = 0; j < arrInv.length; j++) {
				if (arrInv[i].equals(arrInv[j]) && i != j) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isInventory(String invName) {/*
												 * bu metod m01,m02,m03 mu diye
												 * kontrol yapar degilse false
												 */

		for (int i = 0; i < t[0].getInventory().length; i++) {
			if (i < 9) {
				if (invName.equals("m0" + (i + 1))) {
					return true;
				}
			} else {
				if (invName.equals("m" + (i + 1))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isSameModule(String mdl1, String mdl2, String mdl3, String mdl4) {
		/*
		 * bu robot oluþtururken modulleri karþýlaþtýrýr ayný tip alýnmasýný
		 * onler
		 */
		String[] arrMdl = new String[] { mdl1, mdl2, mdl3, mdl4 };
		for (int i = 0; i < arrMdl.length; i++) {
			for (int j = 0; j < arrMdl.length; j++) {
				if (arrMdl[i].equals(arrMdl[j]) && i != j) {
					return true;
				}
			}
		}
		return false;

	}

	public int calInventoryNo(String str) {
		for (int i = 0; i < t[0].getInventory().length; i++) {
			if (i < 9) {
				if (str.equals("m0" + (i + 1))) {
					return i;
				}
			} else {
				if (str.equals("m" + (i + 1))) {
					return i;
				}
			}
		}
		return -2;
	}

	public void controlOfFinish() {
		for (int i = 0; i < t.length; i++) {
			if (t[i].getCredits() >= 10000 && t[i].countOfRobot() >= 6) {
				setFinish(true);
				System.out.println("Congratulations! Team " + (i + 1));
				break;
			}
		}
	}

	public void calPrize() {
		chessPrize = chessPrize + calCountRegTeam('c') * 25;
		runPrize = runPrize + calCountRegTeam('r') * 30;
		sumoPrize = sumoPrize + calCountRegTeam('s') * 35;
		pingPongPrize = pingPongPrize + calCountRegTeam('p') * 40;

	}

	public int calCountRegTeam(char gameName) {
		int count = 0;
		for (int i = 0; i < t.length; i++) {
			if (t[i].getRobotT() != null) {
				for (int j = 0; j < t[i].getRobotT().length; j++) {
					if (t[i].getRobotT()[j] != null && t[i].getRobotT()[j].getRegGameNo() != 0) {
						if (t[i].getRobotT()[j].getRegGameName() == gameName) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	public void resetDifferentGame() {
		for (int i = 0; i < differentGames.length; i++) {
			differentGames[i] = "";
		}
	}

	/*
	 * girilen oyun differentGame in içinde yoksa ekler 3 haftada bir buna göre
	 * kontrol yapacaðýz
	 */
	public void controlOfDifferentGame() {
		for (int i = 0; i < t.length; i++) {
			if (t[i].getRobotT() != null) {
				for (int j = 0; j < t[i].getRobotT().length; j++) {
					if (t[i].getRobotT()[j] != null && t[i].getRobotT()[j].getRegGameNo() != 0) {
						if (!controlSameGame(t[i].getRobotT()[j].getRegGameName(), i)) {
							differentGames[i] += t[i].getRobotT()[j].getRegGameName();
						}
					}
				}
			}
		}
	}

	/*
	 * 3 haftalýk kýsýmlarý kaydederken ayný oyunlarý tekrar kayýt etmesini
	 * önleyeceðiz
	 */
	public boolean controlSameGame(char gameName, int teamNo) {
		if (differentGames != null) {
			if (differentGames[teamNo] != null) {
				for (int j = 0; j < differentGames[teamNo].length(); j++) {
					if (differentGames[teamNo].charAt(j) == gameName) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getLastCursorPositionY() {
		return lastCursorPositionY;
	}

	public void setLastCursorPositionY(int lastCursorPositionY) {
		this.lastCursorPositionY = lastCursorPositionY;
	}

	public void printHeader() {
		cn = Enigma.getConsole("Robot Game", 105, 55, false);
		attrs = new TextAttributes(Color.YELLOW);
		cn.setTextAttributes(attrs);// yazý rengini consola uyguluyoruz
		cn.getTextWindow().setCursorPosition(1, 1);
		System.out.print("Week:" + week + " Robot/Credit: ");
		for (int i = 0; i < 6; i++) {
			System.out
					.print("T" + (i + 1) + ":" + t[i].countOfRobot() + "/" + Math.floor(t[i].getCredits() * 100) / 100);
			System.out.print(" ");
		}
		cn.getTextWindow().setCursorPosition(1, 2);
		System.out.println("--- Team1: Modules ---");
		attrs = new TextAttributes(Color.CYAN);
		cn.setTextAttributes(attrs);
		t[0].listInventory();// Bizim modullerimizi gösterecektir
		attrs = new TextAttributes(Color.YELLOW);
		cn.setTextAttributes(attrs);
		for (int i = 0; i < 65; i++) {
			System.out.print("-");
		}
		cn.getTextWindow().setCursorPosition(1, 8);
		System.out.print("--- Team1: Robots ---");
		for (int i = 0; i < 66; i++) {
			System.out.print("-");
		}
		System.out.println();
		attrs = new TextAttributes(Color.CYAN);
		cn.setTextAttributes(attrs);
		t[0].listRobots();
		attrs = new TextAttributes(Color.YELLOW);
		cn.setTextAttributes(attrs);
		cn.getTextWindow().setCursorPosition(1, 20);
		System.out.print("--- Games (Registering) ---");
		for (int i = 0; i < 59; i++) {
			System.out.print("-");
		}
		attrs = new TextAttributes(Color.CYAN);
		cn.setTextAttributes(attrs);
		/* kayit olunan oyunlarý listele bu kýsýmda */
		cn.getTextWindow().setCursorPosition(1, 21);
		System.out.print("Chess:" + chessPrize + "\t\tRun:" + runPrize + "\t\tSumo:" + sumoPrize + "\t\tPingPong:"
				+ pingPongPrize);
		cn.getTextWindow().setCursorPosition(1, 23);
		System.out.print("Team1: ");
		t[0].listRegisters();
		attrs = new TextAttributes(Color.YELLOW);
		cn.setTextAttributes(attrs);
		cn.getTextWindow().setCursorPosition(1, 25);
		for (int i = 0; i < 87; i++) {
			System.out.print("-");
		}
		cn.getTextWindow().setCursorPosition(0, lastCursorPositionY);
	}

	public void clearScreen() {// sadece header kýsmýný temizleyecek böylelikle
								// önceki yazýlanlarý görecez
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 90; j++) {
				cn.getTextWindow().setCursorPosition(j, i);
				System.out.print(" ");
			}

		}
	}
}
