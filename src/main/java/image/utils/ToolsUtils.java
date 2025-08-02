package image.utils;


import java.awt.*;

/**
 * @since 2021/9/20 12:33
 */
public class ToolsUtils {

	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	public static void sleep (long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
