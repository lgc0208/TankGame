package myTankGame6;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

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
	boolean pause = false;
	
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
				e.printStackTrace();
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
	boolean pause = false;
	Vector<Shot> ss = new Vector<Shot>();
	
	public Hero(int x, int y)
	{
		//super()从子类中调用父类的构造方法
		super(x, y);
	}
	
	//开火
	public void shotEnemy()
	{
		if(!pause)
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
	boolean pause = false;
	
	//定义一个向量，访问到面板上所有敌人坦克
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	
	//定义一个向量存放敌人的子弹
	Vector<Shot> ss = new Vector<Shot>();
	//敌人添加子弹应该在刚刚创建坦克和敌人的坦克子弹死亡之后
	
	public EnemyTank(int x, int y)
	{
		super(x, y);
	}
	
	//得到GamePanel的敌人坦克向量
	public void setEts(Vector<EnemyTank> v)
	{
		this.ets = v;
	}
	
	//是否碰到了其他坦克
	public boolean isTouchOtherTank()
	{
		boolean b = false;
		
		switch(this.direct)
		{
		case 0:
			//向上
			//取出所有敌人坦克
			for(int i = 0; i < ets.size(); i++)
			{
				EnemyTank et = ets.get(i);
				//如果取出的坦克不是正在执行的坦克本身
				if(et != this)
				{
					//若敌人的方向是向下/向上
					if(et.direct == 0 || et.direct == 2)
					{
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 22
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 30)
						{
							return true;
						}
						if(this.getX() + 22 >= et.getX() && this.getX() + 22 <= et.getX() + 22
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 30)
						{
							return true;
						}
					}
					if(et.direct == 3 || et.direct == 1)
					{
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 30
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 22)
						{
							return true;
						}
						if(this.getX() + 22 >= et.getX() && this.getX() + 22 <= et.getX() + 30
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 22)
						{
							return true;
						}
					}
				}
			}
			break;
		case 1:
			//向右
			//取出所有敌人坦克
			for(int i = 0; i < ets.size(); i++)
			{
				EnemyTank et = ets.get(i);
				//如果取出的坦克不是正在执行的坦克本身
				if(et != this)
				{
					//若敌人的方向是向下/向上
					if(et.direct == 0 || et.direct == 2)
					{
						//上点
						if(this.getX() + 30 >= et.getX() && this.getX() + 30 <= et.getX() + 22
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 30)
						{
							return true;
						}
						//下点
						if(this.getX() + 30 >= et.getX() && this.getX() + 30 <= et.getX() + 22
								&& this.getY() + 22 >= et.getY() && this.getY() + 22 <= et.getY() + 30)
						{
							return true;
						}
					}
					if(et.direct == 3 || et.direct == 1)
					{
						if(this.getX() + 30 >= et.getX() + 30 && this.getX() + 30 <= et.getX() + 30
								&& this.getY() >= et.getY() && this.getY() + 22 <= et.getY() + 22)
						{
							return true;
						}
						if(this.getX() + 30 >= et.getX() && this.getX() + 30 <= et.getX() + 30
								&& this.getY() + 22 >= et.getY() && this.getY() + 22 <= et.getY() + 22)
						{
							return true;
						}
					}
				}
			}
			break;
		case 2:
			//向下
			//取出所有敌人坦克
			for(int i = 0; i < ets.size(); i++)
			{
				EnemyTank et = ets.get(i);
				//如果取出的坦克不是正在执行的坦克本身
				if(et != this)
				{
					//若敌人的方向是向下/向上
					if(et.direct == 0 || et.direct == 2)
					{
						//左点
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 22
								&& this.getY() + 30 >= et.getY() && this.getY() + 30 <= et.getY() + 30)
						{
							return true;
						}
						//右点
						if(this.getX() + 22 >= et.getX() && this.getX() + 22 <= et.getX() + 22
								&& this.getY() + 30 >= et.getY() && this.getY() + 30 <= et.getY() + 30)
						{
							return true;
						}
					}
					if(et.direct == 3 || et.direct == 1)
					{
						if(this.getX() >= et.getX() + 30 && this.getX() <= et.getX() + 30
								&& this.getY() + 30 >= et.getY() && this.getY() + 30 <= et.getY() + 22)
						{
							return true;
						}
						if(this.getX() + 22 >= et.getX() && this.getX() + 22 <= et.getX() + 30
								&& this.getY() + 30 >= et.getY() && this.getY() + 30 <= et.getY() + 22)
						{
							return true;
						}
					}
				}
			}
			break;
		case 3:
			//向左
			//取出所有敌人坦克
			for(int i = 0; i < ets.size(); i++)
			{
				EnemyTank et = ets.get(i);
				//如果取出的坦克不是正在执行的坦克本身
				if(et != this)
				{
					//若敌人的方向是向下/向上
					if(et.direct == 0 || et.direct == 2)
					{
						//上一点
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 22
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 30)
						{
							return true;
						}
						//下一点
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 22
								&& this.getY() + 22 >= et.getY() && this.getY() + 22 <= et.getY() + 30)
						{
							return true;
						}
					}
					if(et.direct == 3 || et.direct == 1)
					{
						if(this.getX() >= et.getX() + 30 && this.getX() <= et.getX() + 30
								&& this.getY() >= et.getY() && this.getY() <= et.getY() + 22)
						{
							return true;
						}
						if(this.getX() >= et.getX() && this.getX() <= et.getX() + 30
								&& this.getY() + 22 >= et.getY() && this.getY() + 22 <= et.getY() + 22)
						{
							return true;
						}
					}
				}
			}
			break;
		}
		
		return b;
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
					if(y > 0 && !this.isTouchOtherTank())
						y -= speed;
					else 
					{
						this.direct = 1;
						break;
					}
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
					if(x < 400 && !this.isTouchOtherTank())
						x += speed;
					else 
					{
						this.direct = 3;
						break;
					}
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
					if(y < 300 && !this.isTouchOtherTank())
						y += speed;
					else
					{
						this.direct = 0;
						break;
					}
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
					if(x > 0 && !this.isTouchOtherTank())
						x -= speed;
					else 
					{
						this.direct = 1;
						break;
					}
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
				if(isLive == true && !pause)
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
			
			if(!pause)
			{
				//让坦克随机产生一个新的方向
				this.direct = (int)(Math.random() * 4);
			}
			
			//判断敌人坦克是否死亡
			if(this.isLive == false)
			{
				//让坦克死亡后退出线程
				break;
			}
		}
		}
	}

//播放声音
class playMusic extends Thread
{
	private String filename;
	public playMusic(String filename)
	{
		this.filename = filename;
	}
	public void run() 
	{

		File soundFile = new File(filename);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		//这是缓冲
		byte[] abData = new byte[1024];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}
}