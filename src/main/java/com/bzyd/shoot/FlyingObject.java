package com.bzyd.shoot;

import java.awt.image.BufferedImage;

/**飞行物类，英雄机、敌飞机、蜜蜂、子弹都继承此类*/
public abstract class FlyingObject {
	protected int x;//x坐标
	protected int y;//y坐标
	protected int width;//宽度
	protected int height;//高度
	protected BufferedImage image;//图片，表示贴图

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * 飞行物移动一步（体现多态性）
	 */
	public abstract void step();

	/**
	 * 检查飞行物（敌机、蜜蜂）是否被子弹击中
	 * bee.x < bullet.x < bee.x + width
	 * &&
	 * bee.y < bullet.y < bee.y + height
	 * @param bullet 子弹对象
	 * @return true 表示被击中了
	 */
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x;//子弹x坐标
		int y = bullet.y;//子弹y坐标
		return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
	}

	/**
	 * 检查是否越界（体现多态性）
	 * @return true 出界与否
	 */
	public abstract boolean outOfBounds();
}
