package image.utils;


import java.awt.*;

/**
 * <h2></h2>
 * description:
 * @author wenbiao.jin
 * @version 1.0
 * @since 2021/6/30 20:48
 */
public class ColorUtil {
	public static Color hexToColor(String colorStr){
		return intToColor(hexToInt(colorStr));
	}
	public static int hexToInt(String hex){
		return Integer.valueOf(hex,16);
	}
	public static Color intToColor(int value){
		return new Color(value);
	}
}
