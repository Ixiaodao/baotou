package image.utils;


import com.ixiaodao.search.image.utils.FindImgUtils;
import com.ixiaodao.search.image.utils.ToolsUtils;

import java.awt.*;

public class KeyUtils {

    private static final Robot robot = FindImgUtils.robot;

    public static void key (int key) {
        robot.keyPress(key);
        ToolsUtils.sleep(50);
        robot.keyRelease(key);
    }

    public static void key (int one, int two) {
        robot.keyPress(one);
        robot.delay(10);
        robot.keyPress(two);
        robot.delay(20);
        robot.keyRelease(two);
        robot.delay(10);
        robot.keyRelease(one);

    }
}
