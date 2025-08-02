package image.utils;

import com.ixiaodao.search.image.model.CoordBean;
import com.ixiaodao.search.image.model.RgbImageComparerBean;
import com.ixiaodao.search.image.utils.CollectionUtils;
import com.ixiaodao.search.image.utils.ColorUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2></h2>
 * description:
 * @author wenbiao.jin
 * @version 1.0
 * @since 2021/7/4 17:46
 */
public class FindImgUtils {

	public static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static int SIM_ACCURATE_VERY = 0;

	public static Map<String, BufferedImage> bufferedImageList = new HashMap<>(10);

	public static CoordBean searchImg (int x, int y, int width, int height, File fIle, int num) {
		// 截图
		BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(x, y, width, height));
		// 待搜索的图片
		BufferedImage resourceImage = getResourceImage(fIle);
		if (resourceImage == null) {
			return null;
		}
		List<CoordBean> list = imageSearch(screenCapture, resourceImage, num);
		List<CoordBean> resultLiST = new ArrayList<CoordBean>();
		for (CoordBean c : list) {
			c.setX(x + c.getX());
			c.setY(y + c.getY());
			resultLiST.add(c);
		}
		if (com.ixiaodao.search.image.utils.CollectionUtils.isEmpty(list)) {
			return null;
		}
		return resultLiST.get(0);
	}

	public static CoordBean searchImg (int x, int y, int width, int height, BufferedImage resourceImage, int num) {
		// 截图
		BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(x, y, width, height));
		// 待搜索的图片
		if (resourceImage == null) {
			return null;
		}
		List<CoordBean> list = imageSearch(screenCapture, resourceImage, num);
		List<CoordBean> resultLiST = new ArrayList<>();
		for (CoordBean c : list) {
			c.setX(x + c.getX());
			c.setY(y + c.getY());
			resultLiST.add(c);
		}
		if (com.ixiaodao.search.image.utils.CollectionUtils.isEmpty(list)) {
			return null;
		}
		return resultLiST.get(0);
	}

	public static CoordBean searchImg (int x, int y, int width, int height, BufferedImage screenCapture, BufferedImage resourceImage, int num) {
		// 截图
		// 待搜索的图片
		if (resourceImage == null) {
			return null;
		}
		List<CoordBean> list = imageSearch(screenCapture, resourceImage, num);
		List<CoordBean> resultLiST = new ArrayList<>();
		for (CoordBean c : list) {
			c.setX(x + c.getX());
			c.setY(y + c.getY());
			resultLiST.add(c);
		}
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return resultLiST.get(0);
	}

	private static BufferedImage getResourceImage(File file){
		BufferedImage localImg = bufferedImageList.get(file.getName());
		if(localImg != null){
			return localImg;
		}

		try {
			java.io.InputStream inputStream = new FileInputStream(file);
			localImg = ImageIO.read(inputStream);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		if(localImg == null){
			return null;
		}else{
			bufferedImageList.put(file.getName(), localImg);
			return localImg;
		}
	}

	private static List<CoordBean> imageSearch(BufferedImage sourceImage, BufferedImage searchImage, int sim){
		List<CoordBean> list = new ArrayList<>();

		RgbImageComparerBean pxSource = getPX(sourceImage);
		RgbImageComparerBean pxSearch = getPX(searchImage);

		int[][] px = pxSource.getColorArray();	//原图的像素点
		int[][] pxS = pxSearch.getColorArray();	//要搜索的目标图的像素点
		int pxSXMax = pxSearch.getImgWidth()-1;	//要搜索的图的像素点的数组最大x下标
		int pxSYMax = pxSearch.getImgHeight()-1;	//要搜索的图的像素点的数组最大y下标
		int xSearchEnd = pxSource.getImgWidth()-pxSearch.getImgWidth();	//可搜索的x坐标的原图像素数组下标+1
		int ySearchEnd = pxSource.getImgHeight()-pxSearch.getImgHeight();	//可搜索的y坐标的原图像素数组下标+1
		//要搜索的图片的纵横中心点的像素大小，非坐标
		int contentSearchX = 1;
		int contentSearchY = 1;

		//根据sim计算最小像素匹配率
		double pxPercent = 0.99f;
		if(sim>0){
			//RGB的模糊率／4为最小的像素匹配率，大于这个匹配率，为图像识别成功
			pxPercent = ((double)sim/255)/4;
		}

		for (int x = 0; x < xSearchEnd; x++) {
			for (int y = 0; y < ySearchEnd; y++) {

				boolean contrast = false;	//对比，是否通过

				//如果使用的精确搜索（SIM_ACCURATE、SIM_ACCURATE_VERY），则匹配图像的四个角的点跟中心点
				if(sim < 32){
					//首先比较图片四个角的四个点，如果四个点比较通过，则进行下一轮比较
					if(colorCompare(px[x][y], pxS[0][0], sim)){
						//要搜索的图左上坐标在原图匹配成功
						int pxX = x+pxSearch.getImgWidth()-1;	//原图要搜索的，定位搜索图右上坐标x下标
						if(colorCompare(px[pxX][y], pxS[pxSXMax][0], sim)){
							//要搜索的图右上坐标在原图匹配成功
							int pxY = y+pxSearch.getImgHeight()-1;	//原图要搜索的，定位搜索图右上坐标x下标
							if(colorCompare(px[x][pxY], pxS[0][pxSYMax], sim)){
								//要搜索的图左下坐标在原图匹配成功
								if(colorCompare(px[pxX][pxY], pxS[pxSXMax][pxSYMax], sim)){
									//要搜索的图右下坐标在原图匹配成功

									//进行要搜索的图片的中心点比较
									//计算中心点坐标
									if(pxSXMax>2){
										contentSearchX = (int) Math.ceil(pxSXMax >> 1);
									}
									if(pxSYMax>2){
										contentSearchY = (int) Math.ceil(pxSYMax >> 1);
									}
									if(colorCompare(px[x+contentSearchX][y+contentSearchY], pxS[contentSearchX][contentSearchY], sim)){
										//要搜索的图的中心点坐标在原图上匹配成功
										contrast = true;
									}
								}
							}
						}
					}
				}else{
					//两个模糊搜索不搜索四角＋中心点
					contrast = true;
				}

				//模糊搜索的大模糊不进行纵横十字搜索
				if(sim < 62){
					//如果图片四角点对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之横向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图纵向中心，X横向像素条
						int ySour = y+contentSearchY;
						for (int i = 0; i < pxSearch.getImgWidth(); i++) {
							if(colorCompare(px[x+i][ySour], pxS[i][contentSearchY], sim)){
								yes++;
							}
						}
						if((yes/pxSearch.getImgWidth())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}

					//如果以上对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之纵向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图横向为中心，Y纵向像素条
						int xSour = x+contentSearchX;
						for (int i = 0; i < pxSearch.getImgHeight(); i++) {
							if(colorCompare(px[xSour][y+i], pxS[contentSearchX][i], sim)){
								yes++;
							}
						}

						if((yes/pxSearch.getImgHeight())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}
				}else{
					//大模糊搜索不进行纵横十字搜索
					contrast = true;
				}

				//进行整个目标图片的像素扫描
				if(contrast){
					int yes = 0;
					for (int xS = 0; xS < pxSearch.getImgWidth(); xS++) {
						for (int yS = 0; yS < pxSearch.getImgHeight(); yS++) {
							if(colorCompare(px[x+xS][y+yS], pxS[xS][yS], sim)){
								yes++;
							}
						}
					}
					if((yes/pxSearch.getPxCount())>pxPercent){
						CoordBean coord = new CoordBean();
						coord.setX(x);
						coord.setY(y);
						list.add(coord);
					}
				}
			}
		}

		return list;
	}
	private static CoordBean imageSearchOnce(BufferedImage sourceImage, BufferedImage searchImage, int sim){
		RgbImageComparerBean pxSource = getPX(sourceImage);
		RgbImageComparerBean pxSearch = getPX(searchImage);

		int[][] px = pxSource.getColorArray();	//原图的像素点
		int[][] pxS = pxSearch.getColorArray();	//要搜索的目标图的像素点
		int pxSXMax = pxSearch.getImgWidth()-1;	//要搜索的图的像素点的数组最大x下标
		int pxSYMax = pxSearch.getImgHeight()-1;	//要搜索的图的像素点的数组最大y下标
		int xSearchEnd = pxSource.getImgWidth()-pxSearch.getImgWidth();	//可搜索的x坐标的原图像素数组下标+1
		int ySearchEnd = pxSource.getImgHeight()-pxSearch.getImgHeight();	//可搜索的y坐标的原图像素数组下标+1
		//要搜索的图片的纵横中心点的像素大小，非坐标
		int contentSearchX = 1;
		int contentSearchY = 1;

		//根据sim计算最小像素匹配率
		double pxPercent = 0.99f;
		if(sim>0){
			//RGB的模糊率／4为最小的像素匹配率，大于这个匹配率，为图像识别成功
			pxPercent = ((double)sim/255)/4;
		}

		for (int x = 0; x < xSearchEnd; x++) {
			for (int y = 0; y < ySearchEnd; y++) {

				boolean contrast = false;	//对比，是否通过

				//如果使用的精确搜索（SIM_ACCURATE、SIM_ACCURATE_VERY），则匹配图像的四个角的点跟中心点
				if(sim < 32){
					//首先比较图片四个角的四个点，如果四个点比较通过，则进行下一轮比较
					if(colorCompare(px[x][y], pxS[0][0], sim)){
						//要搜索的图左上坐标在原图匹配成功
						int pxX = x+pxSearch.getImgWidth()-1;	//原图要搜索的，定位搜索图右上坐标x下标
						if(colorCompare(px[pxX][y], pxS[pxSXMax][0], sim)){
							//要搜索的图右上坐标在原图匹配成功
							int pxY = y+pxSearch.getImgHeight()-1;	//原图要搜索的，定位搜索图右上坐标x下标
							if(colorCompare(px[x][pxY], pxS[0][pxSYMax], sim)){
								//要搜索的图左下坐标在原图匹配成功
								if(colorCompare(px[pxX][pxY], pxS[pxSXMax][pxSYMax], sim)){
									//要搜索的图右下坐标在原图匹配成功

									//进行要搜索的图片的中心点比较
									//计算中心点坐标
									if(pxSXMax>2){
										contentSearchX = (int) Math.ceil(pxSXMax >> 1);
									}
									if(pxSYMax>2){
										contentSearchY = (int) Math.ceil(pxSYMax >> 1);
									}
									if(colorCompare(px[x+contentSearchX][y+contentSearchY], pxS[contentSearchX][contentSearchY], sim)){
										//要搜索的图的中心点坐标在原图上匹配成功
										contrast = true;
									}
								}
							}
						}
					}
				}else{
					//两个模糊搜索不搜索四角＋中心点
					contrast = true;
				}

				//模糊搜索的大模糊不进行纵横十字搜索
				if(sim < 62){
					//如果图片四角点对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之横向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图纵向中心，X横向像素条
						int ySour = y+contentSearchY;
						for (int i = 0; i < pxSearch.getImgWidth(); i++) {
							if(colorCompare(px[x+i][ySour], pxS[i][contentSearchY], sim)){
								yes++;
							}
						}
						if((yes/pxSearch.getImgWidth())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}

					//如果以上对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之纵向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图横向为中心，Y纵向像素条
						int xSour = x+contentSearchX;
						for (int i = 0; i < pxSearch.getImgHeight(); i++) {
							if(colorCompare(px[xSour][y+i], pxS[contentSearchX][i], sim)){
								yes++;
							}
						}

						if((yes/pxSearch.getImgHeight())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}
				}else{
					//大模糊搜索不进行纵横十字搜索
					contrast = true;
				}

				//进行整个目标图片的像素扫描
				if(contrast){
					int yes = 0;
					for (int xS = 0; xS < pxSearch.getImgWidth(); xS++) {
						for (int yS = 0; yS < pxSearch.getImgHeight(); yS++) {
							if(colorCompare(px[x+xS][y+yS], pxS[xS][yS], sim)){
								yes++;
							}
						}
					}
					if((yes/pxSearch.getPxCount())>pxPercent){
						CoordBean coord = new CoordBean();
						coord.setX(x);
						coord.setY(y);
						return coord;
					}
				}
			}
		}

		return null;
	}

	public static CoordBean imageSearchMohu(BufferedImage sourceImage, BufferedImage searchImage, int sim){

		RgbImageComparerBean pxSource = getPX(sourceImage);
		RgbImageComparerBean pxSearch = getPX(searchImage);

		int[][] px = pxSource.getColorArray();	//原图的像素点
		int[][] pxS = pxSearch.getColorArray();	//要搜索的目标图的像素点
		int pxSXMax = pxSearch.getImgWidth()-1;	//要搜索的图的像素点的数组最大x下标
		int pxSYMax = pxSearch.getImgHeight()-1;	//要搜索的图的像素点的数组最大y下标
		int xSearchEnd = pxSource.getImgWidth()-pxSearch.getImgWidth();	//可搜索的x坐标的原图像素数组下标+1
		int ySearchEnd = pxSource.getImgHeight()-pxSearch.getImgHeight();	//可搜索的y坐标的原图像素数组下标+1
		//要搜索的图片的纵横中心点的像素大小，非坐标
		int contentSearchX = 1;
		int contentSearchY = 1;

		//根据sim计算最小像素匹配率
		double pxPercent = 0.99f;
		if(sim>0){
			//RGB的模糊率／4为最小的像素匹配率，大于这个匹配率，为图像识别成功
			pxPercent = ((double)sim/255)/4;
		}

		for (int x = 0; x < xSearchEnd; x++) {
			for (int y = 0; y < ySearchEnd; y++) {

				boolean contrast = false;	//对比，是否通过

				//如果使用的精确搜索（SIM_ACCURATE、SIM_ACCURATE_VERY），则匹配图像的四个角的点跟中心点
				if(sim < 32){
					//首先比较图片四个角的四个点，如果四个点比较通过，则进行下一轮比较
					if(colorCompare(px[x][y], pxS[0][0], sim)){
						//要搜索的图左上坐标在原图匹配成功
						int pxX = x+pxSearch.getImgWidth()-1;	//原图要搜索的，定位搜索图右上坐标x下标
						if(colorCompare(px[pxX][y], pxS[pxSXMax][0], sim)){
							//要搜索的图右上坐标在原图匹配成功
							int pxY = y+pxSearch.getImgHeight()-1;	//原图要搜索的，定位搜索图右上坐标x下标
							if(colorCompare(px[x][pxY], pxS[0][pxSYMax], sim)){
								//要搜索的图左下坐标在原图匹配成功
								if(colorCompare(px[pxX][pxY], pxS[pxSXMax][pxSYMax], sim)){
									//要搜索的图右下坐标在原图匹配成功

									//进行要搜索的图片的中心点比较
									//计算中心点坐标
									if(pxSXMax>2){
										contentSearchX = (int) Math.ceil(pxSXMax >> 1);
									}
									if(pxSYMax>2){
										contentSearchY = (int) Math.ceil(pxSYMax >> 1);
									}
									if(colorCompare(px[x+contentSearchX][y+contentSearchY], pxS[contentSearchX][contentSearchY], sim)){
										//要搜索的图的中心点坐标在原图上匹配成功
										contrast = true;
									}
								}
							}
						}
					}
				}else{
					//两个模糊搜索不搜索四角＋中心点
					contrast = true;
				}

				//模糊搜索的大模糊不进行纵横十字搜索
				if(sim < 62){
					//如果图片四角点对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之横向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图纵向中心，X横向像素条
						int ySour = y+contentSearchY;
						for (int i = 0; i < pxSearch.getImgWidth(); i++) {
							if(colorCompare(px[x+i][ySour], pxS[i][contentSearchY], sim)){
								yes++;
							}
						}
						if((yes/pxSearch.getImgWidth())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}

					//如果以上对比通过，进而进行要搜索的图片的纵横中心点，十字形像素条的比较之纵向像素条比较
					if(contrast){
						int yes = 0;

						//计算以搜索图横向为中心，Y纵向像素条
						int xSour = x+contentSearchX;
						for (int i = 0; i < pxSearch.getImgHeight(); i++) {
							if(colorCompare(px[xSour][y+i], pxS[contentSearchX][i], sim)){
								yes++;
							}
						}

						if((yes/pxSearch.getImgHeight())>pxPercent){
							contrast = true;
						}else{
							contrast = false;
						}
					}
				}else{
					//大模糊搜索不进行纵横十字搜索
					contrast = true;
				}

				//进行整个目标图片的像素扫描
				if (contrast) {

					CoordBean coord = new CoordBean();
					coord.setX(x);
					coord.setY(y);
					return coord;
				}
			}
		}
		return null;
	}

	public static RgbImageComparerBean getPX(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int minx = bufferedImage.getMinX();
		int miny = bufferedImage.getMinY();

		RgbImageComparerBean rgb = new RgbImageComparerBean();
		int[][] colorArray = new int[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				colorArray[i][j] = bufferedImage.getRGB(i, j);
			}
		}
		rgb.setColorArray(colorArray);
		return rgb;
	}

	public static boolean colorCompare(int pxSource, int pxSearch, int sim) {
		if (sim == SIM_ACCURATE_VERY) {
			return pxSearch == pxSource;
		} else {
			Color sourceRgb = com.ixiaodao.search.image.utils.ColorUtil.intToColor(pxSource);
			Color searchRgb = ColorUtil.intToColor(pxSearch);
			return FindImgUtils.colorCompare(sourceRgb, searchRgb, sim);
		}
	}

	public static boolean colorCompare(Color color1, Color color2, int sim) {
		return Math.abs(color1.getRed() - color2.getRed()) <= sim
				&& Math.abs(color1.getGreen() - color2.getGreen()) <= sim
				&& Math.abs(color1.getBlue() - color2.getBlue()) <= sim;
	}

	public static CoordBean searchImgOnce(int startX, int startY, int width, int height, BufferedImage searchImg, int sim) {
		// 截图
		BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(startX, startY, width, height));
		CoordBean coordBean = imageSearchOnce(screenCapture, searchImg, sim);
		if (coordBean == null) {
			return null;
		}
		coordBean.setX(startX + coordBean.getX());
		coordBean.setY(startY + coordBean.getY());
		return coordBean;
	}

	public static CoordBean searchImgOnce(int startX, int startY, BufferedImage sourceImg, BufferedImage searchImg, int sim) {
		CoordBean coordBean = imageSearchOnce(sourceImg, searchImg, sim);
		if (coordBean == null) {
			return null;
		}
		coordBean.setX(startX + coordBean.getX());
		coordBean.setY(startY + coordBean.getY());
		return coordBean;
	}

}
