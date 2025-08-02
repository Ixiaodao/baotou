package image.model;


/**
 * 坐标系
 * @author 管雷鸣
 */
public class CoordBean {

	private int x;
	private int y;

	public CoordBean() {

	}

	public CoordBean(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 获取x坐标
	 * @return x坐标
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 获取y坐标
	 * @return y坐标
	 */
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("%s,%s", x, y);
	}

}
