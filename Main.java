package robotGameNew;
import java.awt.Color;

import enigma.console.TextAttributes;

public class Main {
	public static Game gm;
	public static void main(String[] args) {
		 gm=new Game();
		while (!gm.isFinish()) {
			if (!gm.isPressPL()) {
			gm.printHeader();
			System.out.print("Command > ");
			gm.cn.setTextAttributes(new TextAttributes(Color.WHITE));
			String cmd = gm.cn.readLine();
			gm.Command(cmd);
			gm.setLastCursorPositionY(gm.cn.getTextWindow().getCursorY());
			}else {
				gm.setPressPL(false);
				
				System.out.print("Please press any key");
				gm.cn.readLine();
			}
			if (!gm.isPressPL()) {
				gm.clearScreen();
			}
		}
		gm.clearScreen();
		gm.printHeader();
		

	}

}

