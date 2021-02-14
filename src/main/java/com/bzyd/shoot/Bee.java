package com.bzyd.shoot;

import java.util.Random;

/**蜜蜂类，继承FlyingObject，实现Award接口*/
public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;//x方向移动速度
	private int ySpeed = 2;//y方向移动速度
	private int awardType;//奖励类型

	@Override
	public int getType() {
		return awardType;
	}

	public Bee() {
		image = ShootGame.bee;//继承来的image属性就是ShootGame类中加载得到的静态Bee的图片
		width = image.getWidth();//蜜蜂的宽度
		height = image.getHeight();//蜜蜂的高度
		Random r = new Random();
		y = -height;//y坐标赋值
		x = r.nextInt(ShootGame.WIDTH - width);//x坐标赋值
		awardType = r.nextInt(2);//给奖励类型赋值
	}

	@Override
	public void step() {
		x += xSpeed;//x方向移动
		y += ySpeed;//y方向移动
		if (x > ShootGame.WIDTH - width) {//如果到达右边界就向左移动
			xSpeed = -1;
		}
		if (x < 0) {//如果到达左边界就向右移动
			xSpeed = 1;
		}
		
	}
	@Override
	public boolean outOfBounds() {
		return y > ShootGame.HEIGHT;
	}
}
