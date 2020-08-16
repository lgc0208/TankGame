package myTankGame5;

import java.util.Vector;

//炸弹类
class Bomb
{
	//定义炸弹的坐标
	int x, y;
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

	int life = 9;
	boolean isLive = true;
	
	public Bomb(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	//减少生命值
	public void lifeDown()
	{
		if(life > 0)
		{
			life--;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else
		{
			this.isLive = false;
		}
	}
}

//子弹类
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed = 3;
	
	//是否还活着
	boolean isLive = true;
	
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
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
	
	//射击位置
	public Shot(int x, int y, int direct)
	{
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
	
	//Runnable接口函数重写
	public void run()
	{
		while(true)
		{
			
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			switch(direct)
			{
			case 0:
				//上
				this.y -= this.speed;
				break;
			case 1:
				//右
				this.x += this.speed;
				break;
			case 2:
				//下
				this.y += this.speed;
				break;
			case 3:
				//左
				this.x -= this.speed;
				break;
			}
			//System.out.println("子弹坐标：x=" + this.getX() + " y=" + this.getY());
			
			//子弹死亡
			//判断该子弹是否碰到边缘
			if(x < 0 || y < 0 || x > 400 || y > 300)
			{
				this.isLive = false;
				break;
			}
		}
	}
}

//坦克类
class Tank
{
	//坦克的横坐标
	int x = 0;
	//坦克的纵坐标
	int y = 0;
	//坦克方向
	//0表示上，1表示右，2表示下，3表示左
	int direct = 0;
	//坦克的速度
	int speed = 1;
	//坦克的颜色
	int color;
	boolean isLive = true;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
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
		
	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	
}

//我的坦克
class Hero extends Tank
{
	//子弹
	Shot s = null;
	Vector<Shot> ss = new Vector<Shot>();
	
	public Hero(int x, int y)
	{
		//super()从子类中调用父类的构造方法
		super(x, y);
	}
	
	//开火
	public void shotEnemy()
	{
		switch(this.direct)
		{
		case 0:
			//创建一颗子弹
			s = new Shot(x + 9, y, this.direct);
			//把子弹加入到向量
			ss.add(s);
			break;
		case 1:
			s = new Shot(x + 30, y + 9, this.direct);
			ss.add(s);
			break;
		case 2:
			s = new Shot(x + 9, y + 30, this.direct);
			ss.add(s);
			break;
		case 3:
			s = new Shot(x, y + 9, this.direct);
			ss.add(s);
			break;
		}
		
		//启动子弹线程
		Thread t = new Thread(s);
		t.start();
	}
	
	//坦克向上移动
	public void moveUp()
	{
		if(this.y > 0)
			this.y -= this.speed;
	}
	
	//坦克向右移动
	public void moveRight()
	{
		if(this.x < 400 - 30)
			this.x += this.speed;
	}
	
	//坦克向下移动
	public void moveDown()
	{
		if(this.y < 300 - 30)
			this.y += this.speed;
	}
	
	//坦克向左移动
	public void moveLeft()
	{
		if(this.x > 0)
			this.x -= this.speed;
	}
}

//敌人的坦克 做成线程类
class EnemyTank extends Tank implements Runnable
{
	int times = 0;
	
	//定义一个向量存放敌人的子弹
	Vector<Shot> ss = new Vector<Shot>();
	//敌人添加子弹应该在刚刚创建坦克和敌人的坦克子弹死亡之后
	
	public EnemyTank(int x, int y)
	{
		super(x, y);
	}
	
	public void run()
	{
		while(true)
		{	
			switch(this.direct)
			{
			case 0:
				//坦克正在向上移动
				for(int i = 0; i < 30; i++)
				{
					if(y > 0)
						y -= speed;
					else this.direct = 1;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case 1:
				//向右
				for(int i = 0; i < 30; i++)
				{
					if(x < 400)
						x += speed;
					else this.direct = 3;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case 2:
				//向下
				for(int i = 0; i < 30; i++)
				{
					if(y < 300)
						y += speed;
					else this.direct = 0;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case 3:
				//向左
				for(int i = 0; i < 30; i++)
				{
					if(x > 0)
						x -= speed;
					else this.direct = 1;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			}
			
			this.times++;
			if(times % 2 == 0)
			{
				if(isLive == true)
				{
					//★这里的4表示同时可以发射4颗子弹
					if(ss.size() < 4)
					{
						Shot s = null;
						//没有子弹，此时添加子弹
						switch(direct)
						{
						case 0:
							//创建一颗子弹
							s = new Shot(getX() + 9, getY(), direct);
							//把子弹加入到向量
							ss.add(s);
							break;
						case 1:
							s = new Shot(getX() + 30, getY() + 9, direct);
							ss.add(s);
							break;
						case 2:
							s = new Shot(getX() + 9, getY() + 30, direct);
							ss.add(s);
							break;
						case 3:
							s = new Shot(getX(), getY() + 9, direct);
							ss.add(s);
							break;
						}
						//启动子弹线程
						Thread t = new Thread(s);
						t.start();
					}
				}
			}
			
			//让坦克随机产生一个新的方向
			this.direct = (int)(Math.random() * 4);
			
			//判断敌人坦克是否死亡
			if(this.isLive == false)
			{
				//让坦克死亡后退出线程
				break;
			}
		}
		}
	}

