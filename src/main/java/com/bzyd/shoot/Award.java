package com.bzyd.shoot;
/**表示奖励，提供奖励类型属性及获取奖励类型的方法，蜜蜂实现此方法*/
public interface Award {
	int DOUBLE_FIRE = 0;//0表示双倍火力
	int LIFE = 1;//1表示1条命
	/**获取奖励类型，0或1*/
	int getType();
}
