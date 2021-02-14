package com.bzyd.shoot;

import java.awt.image.BufferedImage;
/**英雄机类，是飞行物，继承FlyingObject类*/
public class Hero extends FlyingObject {
	protected BufferedImage[] images = {};//表示Hero的贴图，有两张组成
	protected int index = 0;//两张图片进行交替显示的计数
	
	private int doubleFire;//双倍火力子弹数量
	private int life;//增命的数量
	
	public Hero() {
		life = 3;
		doubleFire = 0;
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		images = new BufferedImage[] {ShootGame.hero0,ShootGame.hero1};
		x = 150;
		y = 400;
	}

	@Override
	public void step() {
		if (images.length > 0) {
			image = images[index++/10%images.length];
		}
	}

	/**射击（产生子弹）*/
	public Bullet[] shoot() {
		int xStep = width/4;
		int yStep = 20;//子弹到飞机的位置
		if (doubleFire > 0) {//双倍火力
			Bullet[] bullets = new Bullet[2];//双倍火力一次发射2颗子弹
			bullets[0] = new Bullet(x+xStep, y-yStep);//子弹发射点1
			bullets[1] = new Bullet(x+3*xStep,y-yStep);//子弹发射点2
			doubleFire -= 2;//子弹数量不断减少
			return bullets;
		}else {//单倍火力
			Bullet[] bullets = new Bullet[1];//单倍火力一次发射1颗子弹
			bullets[0] = new Bullet(x+2*xStep, y-yStep);
			return bullets;
		}
	}

	/**鼠标移动（设定鼠标点为英雄的中心位置）*/
	public void moveTo(int x,int y) {
		this.x = x-width/2;//英雄机的x=鼠标的x-width/2
		this.y = y-height/2;//英雄机的y=鼠标的y-height/2
	}

	/**获取双倍火力*/
	public void addDoubleFire() {
		doubleFire += 40;
	}

	/**增命*/
	public void addLife() {
		life++;
	}

	/**获取英雄机的命数*/
	public int getLife() {
		return life;
	}
	
	@Override
	public boolean outOfBounds() {
		return false;
	}

	/**减命*/
	public void subtractLife() {
		life--;
	}

	/**重置双倍火力*/
	public void setDoubleFire(int doubleFire) {
		this.doubleFire = doubleFire;
	}

	/**检测英雄机是否与飞行物碰撞*/
	public boolean hit(FlyingObject other) {
		int x1 = other.x - this.width/2;
		int x2 = other.x + other.width + this.width/2;
		int y1 = other.y - this.height/2;
		int y2 = other.y + other.height + this.height/2;
		return this.x + this.width/2 > x1 && this.x + this.width/2 < x2
				&& this.y + this.height/2 > y1
				&& this.y + this.height/2 < y2;
	}
}
