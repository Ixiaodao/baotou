package image.utils;


import com.ixiaodao.search.image.utils.FindImgUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @since 2021/11/29 8:05
 */
public class MouseUtils {

	public static final Robot robot = FindImgUtils.robot;
	

	public static void move (int x, int y) {
		robot.mouseMove(x, y);
	}



	public static void moveAndClick (int x, int y, int time) {
		robot.mouseMove(x, y);
		robot.delay(time);
		MouseUtils.click();
	}

	public static void click() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(50);
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
	}
}
