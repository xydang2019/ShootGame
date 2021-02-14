package com.bzyd.shoot;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 飞机大战
 */
public class ShootGame extends JPanel {//继承面板类
	public static final int WIDTH = 400;//面板宽
	public static final int HEIGHT = 654;//面板高
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	
	public static BufferedImage background;//背景图片
	public static BufferedImage start;//游戏开始图片
	public static BufferedImage airplane;//敌飞机图片
	public static BufferedImage bee;//蜜蜂图片
	public static BufferedImage bullet;//子弹图片
	public static BufferedImage hero0;//英雄机图片1
	public static BufferedImage hero1;//英雄机图片2
	public static BufferedImage pause;//暂停图片
	public static BufferedImage gameover;//游戏结束图片
	
	private FlyingObject[] flyings = {};//敌机蜜蜂数组
	private Bullet[] bullets = {};//子弹数组
	private Hero hero = new Hero();//英雄机
	private Timer timer;//定时器
	private int intervel = 10;//时间间隔10毫秒
	private int score = 0;//计分
	private int state;//游戏状态

	/**加载图片*/
	static {
		try {
			//ImageIO.read(URL input)，得到BufferedImage对象
			//类对象.getResource("文件名")，在当前类的包下查找文件，得到URL对象
			background = ImageIO.read(ShootGame.class.getResource("/background.png"));
			start = ImageIO.read(ShootGame.class.getResource("/start.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("/airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("/bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("/bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("/hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("/hero1.png"));
			pause = ImageIO.read(ShootGame.class.getResource("/pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("/gameover.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 随机生成飞行物
	 * @return 飞行物对象
	 */
	public static FlyingObject nextOne() {
		Random r = new Random();
		int type = r.nextInt(20);//[0,20)
		if (type == 0) {
			return new Bee();
		}else {
			return new Airplane();
		}
	}


	/**重构paint方法（绘画组件，是Jpanel的父类JComponent中的方法）*/
	@Override
	public void paint(Graphics g) {
		g.drawImage(background,0,0,null);//画背景图
		paintHero(g);//画英雄机
		paintBullets(g);//画子弹
		paintFlyingObjects(g);//画飞行物（敌机和蜜蜂）
		paintScore(g);//画分数和命
		paintState(g);//画游戏状态
	}

	/**画英雄机*/
	public void paintHero(Graphics g) {
		g.drawImage(hero.getImage(),hero.getX(),hero.getY(),null);
	}

	/**画子弹*/
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImage(),b.getX(),b.getY(),null);
		}
	}

	/**画飞行物*/
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(),f.getX(),f.getY(),null);
		}
	}

	/**画分数和命数*/
	public void paintScore(Graphics g) {
		int x = 10;
		int y = 25;
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
		g.setColor(new Color(0x3A3B3B));//设置画笔颜色
		g.setFont(font);//设置字体
		g.drawString("SCORE: "+score,x,y);//画分数
		y += 20;
		g.drawString("LIFE: "+hero.getLife(), x, y);//画命
	}

	/**画游戏状态*/
	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	/**实现飞行物入场：即每调用该方法40次就随机生成一个飞行物放入flyings数组中*/
	int flyEnterIndex = 0;//飞行物入场计数
	public void enterAction() {
		flyEnterIndex++;
		if (flyEnterIndex%40 == 0) {//400毫秒=10*40
			FlyingObject obj = nextOne();//随机生成一个飞行物
			flyings = Arrays.copyOf(flyings,flyings.length+1);//扩容
			flyings[flyings.length-1] = obj;//放在最后
		}
	}

	/**所有飞行物：敌机、蜜蜂、子弹、英雄机的移动*/
	public void stepAction() {
		/**飞行物移动*/
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			f.step();
		}
		/**子弹移动*/
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			b.step();
		}
		/**英雄机移动*/
		hero.step();
	}

	/**实现英雄机射击，即每调用该方法30次就发射一次子弹，并将发射的子弹存储到bullets数组中*/
	int shootIndex = 0;//射击计数
	public void shootAction() {
		shootIndex++;
		if (shootIndex%30 == 0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length);//扩容
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);//追加
		}
	}

	/**子弹与飞行物碰撞检测*/
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			bang(bullets[i]);//所有子弹逐个检测
		}
	}
	private void bang(Bullet bullet) {
		int index = -1;//击中的飞行物的索引
		for (int i = 0; i < flyings.length; i++) {//逐个飞行物检测
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) {//判断是否击中
				index = i;//记录被击中的飞行物的索引
				break;//如果当前子弹击中了，就结束循环，外循环判断下一枚子弹
			}
		}
		if (index != -1) {//如果与击中的飞行物
			FlyingObject one = flyings[index];//记录被击中的飞行物
			FlyingObject temp = flyings[index];//与最后一个交换
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = temp;
			flyings = Arrays.copyOf(flyings,flyings.length-1);//缩容，删除最后一个被击中的
			//检测被击中的飞行物的类型，如果是敌机就算分，如果是蜜蜂就给予奖励（增命或者双倍火力）
			if (one instanceof Enemy) {
				Enemy e = (Enemy)one;
				score += e.getSroce();
			}
			if (one instanceof Award) {
				Award a = (Award)one;
				int type = a.getType();
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
		}
	}

	/**删除越界飞行物及子弹*/
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		//用来存储活着的飞行物
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (! f.outOfBounds()) {
				flyingLives[index] = f;//不越界的留着
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);//将不越界的飞行物留着
		
		index = 0;//重置为0
		Bullet[] bulletLives = new Bullet[bullets.length];
		//用来存储活着的子弹
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (! b.outOfBounds()) {
				bulletLives[index] = b;//不越界的留着
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);//将不越界的子弹留着
	}

	/**判断游戏是否结束*/
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			int index = -1;
			if (hero.hit(obj)) {//检测英雄机与飞行物是否碰撞
				hero.subtractLife();
				hero.setDoubleFire(0);
				index = i;
			}
			if (index != -1) {
				FlyingObject temp = flyings[index];
				flyings[index] = flyings[flyings.length-1];
				flyings[flyings.length-1] = temp;
				flyings = Arrays.copyOf(flyings,flyings.length-1);
			}
		}
		return hero.getLife() <= 0;
	}

	/**检查游戏是否已经结束*/
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER;
		}
	}

	/**实现10毫秒入场一个飞机或者蜜蜂，并使所有飞行物移动一步，最后重画页面*/
	public void action() {//启动执行代码
		//鼠标监听事件
		MouseAdapter event = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {//鼠标移动事件
				if (state == RUNNING) {//运行时移动英雄机
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {//鼠标进入事件
				if (state == PAUSE) {//暂停时运行
					state = RUNNING;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {//鼠标退出事件
				if (state != GAME_OVER) {
					state = PAUSE;//游戏未结束，则设置为暂停
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {//鼠标点击事件
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER://游戏结束，重新开始
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					hero = new Hero();
					score = 0;
					state = START;
					break;
				}
			}
		};
		this.addMouseListener(event);//处理鼠标点击操作
		this.addMouseMotionListener(event);//处理鼠标滑动操作
		
		timer = new Timer();
		timer.schedule(new TimerTask() {//使用匿名内部类创建一个任务TimerTask
			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction();//飞行物入场（实例方法可以直接调用实例方法）
					stepAction();//走一步
					shootAction();//射击
					bangAction();//检测击中
					outOfBoundsAction();//删除越界飞行物与子弹
					checkGameOverAction();//检查游戏是否结束
				}
				repaint();//重画页面，调用paint()方法
			}
		}, intervel, intervel);//安排指定的任务从指定的延迟后开始进行重复的固定延迟执行。
	}


	/**主方法*/
	public static void main(String[] args) {
		JFrame frame = new JFrame("飞机大战");//创建游戏窗口实例
		ShootGame shootGame = new ShootGame();//面板对象
		frame.add(shootGame);//添加面板到窗口
		frame.setSize(WIDTH, HEIGHT);//设置窗口初始大小
		frame.setAlwaysOnTop(true);//初始位置总是在最上面
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口就结束进程（必须）
		frame.setLocationRelativeTo(null);//设置窗口初始位置
		frame.setVisible(true);//设置窗体是否显示
		shootGame.action();//启动执行
	}
}
