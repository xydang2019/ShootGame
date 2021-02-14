package com.bzyd.shoot;

import java.util.Random;

/**敌飞机类，继承FlyingObject类，实现Enemy接口*/
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 2;
	/**实现获取分数的方法*/
	@Override
	public int getSroce() {
		return 5;
	}

	public Airplane() {
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		Random r = new Random();
		y = -height;
		x = r.nextInt(ShootGame.WIDTH-width);
	}
	@Override
	public void step() {//移动
		y += speed;
	}
	@Override
	public boolean outOfBounds() {
		return y>ShootGame.HEIGHT;
	}
}
