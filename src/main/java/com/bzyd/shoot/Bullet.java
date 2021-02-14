package com.bzyd.shoot;
/**子弹类，属于飞行物，继承FlyingObject类*/
public class Bullet extends FlyingObject {
	private int speed = 3;//子弹速度
	public Bullet(int x,int y) {
		this.x = x;
		this.y = y;
		image = ShootGame.bullet;
	}
	@Override
	public void step() {
		y -= speed;//子弹向上移动
	}
	@Override
	public boolean outOfBounds() {
		return y < -height;
	}
}
