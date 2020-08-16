/*
 *	 坦克游戏5.0.0
 *	1.画出坦克。
 *	2.我的坦克可以上下左右移动
 *	3.按下J键发射子弹，子弹连发，最多同时存在5颗
 *	4.当我的坦克击中敌人坦克时，敌人坦克爆炸并消失
 *	5.敌人坦克可以随意移动
 *	6.我方和敌方坦克在规定区域移动
 *	7.当敌人的坦克击中我方坦克时，我方坦克爆炸
 *	8.具有开始和结束提示界面
 */
package myTankGame5;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class  MyTankGame5 extends JFrame{

	InitialPanel ip = null;
	GamePanel mp = null;
	public static void main(String[] args) {
		MyTankGame5 mtg = new MyTankGame5();
	}
	
	
	//构造函数
	public MyTankGame5()
	{
		ip = new InitialPanel();
		this.add(ip);
		this.setTitle("坦克大战5.0.0_LGC_2020.08");
		this.setSize(414, 337);
		this.setLocationRelativeTo(null); //居中显示
		this.setResizable(false); //不可改变窗体大小
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mp = new GamePanel();
		//启动mp线程
		Thread t = new Thread(mp);
		t.start();

		this.add(mp);
		//注册监听
		this.addKeyListener(mp);
		this.setTitle("坦克大战_LGC_2020.08");
		this.setSize(414, 337);
		this.setLocationRelativeTo(null); //居中显示
		this.setResizable(false); //不可改变窗体大小
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}

//初始提示面板
class InitialPanel extends JPanel 
{
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.red);
		g.setFont(new Font("楷体", Font.BOLD, 30));
		g.drawString("游戏规则", 140, 70);
		g.setFont(new Font("楷体", Font.BOLD, 20));
		g.drawString("  该游戏为坦克大战游戏，击败所有敌人", 10, 120);
		g.drawString("即可获胜。", 10, 150);
		g.drawString("  使用WSAD进行移动，J键攻击，ESC退出", 10, 180);
		g.drawString("  提示：您一次最多只能发射5颗子弹", 10, 210);
		g.drawString("  游戏将在5秒后开始，祝您游戏愉快！", 10, 240);
	}
}

//游戏面板
class GamePanel extends JPanel implements KeyListener, Runnable
{

	
	Hero hero = null; //定义一个我的坦克
	Vector<EnemyTank> ets = new Vector<EnemyTank>(); //定义敌人的坦克组
	Vector<Bomb> bombs = new Vector<Bomb>(); //定义一个炸弹集合
	
	int enemySize = 5; //定义敌人个数
	
	//定义3张图片，3张图片组成一颗炸弹
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	//构造函数
	public GamePanel()
	{
		hero = new Hero(100, 200); //初始化自己的坦克
		//初始化敌人的坦克
		for(int i = 0; i < enemySize; i++)
		{
			EnemyTank et = new EnemyTank((i + 1) * 50, 0); //创建一个敌人的坦克对象
			et.setColor(0);
			et.setDirect(2);
			//启动敌人坦克
			Thread t = new Thread(et);
			t.start();
			
			Shot s = new Shot(et.getX() + 10, et.getY() + 30, 2); //给敌人坦克添加一颗子弹
			et.ss.add(s); //将子弹加入给敌人
			Thread t2 = new Thread(s);
			t2.start();
			
			ets.add(et); //加入向量组
		}
		//初始化图片
		image1 = Toolkit.getDefaultToolkit().getImage("images/bomb1.png");
		image2 = Toolkit.getDefaultToolkit().getImage("images/bomb2.png");
		image3 = Toolkit.getDefaultToolkit().getImage("images/bomb3.png");
	}
	
	//重写paint函数
	public void paint(Graphics g)
	{
		super.paint(g); //调用父类构造方法
		g.fillRect(0, 0, 400, 300);
		
		//画出自己的坦克
		if(hero.isLive == true)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.direct, 0);
		}
		
		//画出敌人的坦克
		for(int i = 0; i < ets.size(); i++)
		{
			EnemyTank et = ets.get(i);
			if(et.isLive == true)
			{
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
				//再画出敌人的子弹
				for(int j = 0; j < et.ss.size(); j++)
				{
					//取出子弹
					Shot enemyShot = et.ss.get(j);
					if(enemyShot.isLive == true)
					{
						g.setColor(Color.YELLOW);
						g.draw3DRect(enemyShot.getX(), enemyShot.getY(), 1, 1, false);
					}
					else
					{
						//若敌人的坦克死亡，就从Vector去掉
						et.ss.remove(enemyShot);
					}
				}
			}
			else
			{
				ets.remove(et);
			}
		}
		
		//从ss中取出每颗子弹并画出
		for(int i = 0; i < hero.ss.size(); i++)
		{
			//取出一颗子弹
			Shot myShot = hero.ss.get(i);
			
			//画出自己的子弹
			if(myShot != null && myShot.isLive == true)
			{
				g.setColor(Color.CYAN);
				g.draw3DRect(myShot.getX(), myShot.getY(), 1, 1, false);
			}
			
			if(myShot.isLive == false)
			{
				//从向量中擦除该子弹
				hero.ss.remove(myShot);
			}
		}
		
		//画出爆炸效果
		for(int i = 0; i < bombs.size(); i++)
		{
			//取出一颗炸弹
			Bomb b = bombs.get(i);
			
			if(b.life > 6)
			{
				g.drawImage(image1, b.getX() - 4, b.getY() - 1, 30, 30, this);
			}
			else if(b.life > 3)
			{
				g.drawImage(image2, b.getX() - 4, b.getY() - 1, 30, 30, this);
			}
			else
			{
				//精修，使第一个爆炸的坦克炸的优雅
				if(i == 0)
					g.drawImage(image2, b.getX() - 4, b.getY() - 1, 30, 30, this);
				else
					g.drawImage(image3, b.getX() - 4, b.getY() - 1, 30, 30, this);
			}
			//让b的生命值减小
			b.lifeDown();
			//若炸弹生命值为0，就把该炸弹bombs向量去掉
			if(b.life == 0)
			{
				bombs.remove(b);
			}
		}
		
		if(hero.isLive == false)
		{
			g.setColor(Color.red);
			g.setFont(new Font("楷体", Font.BOLD, 30));
			g.drawString("游戏结束，您失败了！", 50, 130);
			g.setFont(new Font("楷体", Font.BOLD, 20));
			g.drawString("按下ESC键即可退出游戏", 80, 180);
		}
		if(ets.size() == 0)
		{
			g.setColor(Color.red);
			g.setFont(new Font("楷体", Font.BOLD, 30));
			g.drawString("游戏结束，您胜利了！", 50, 160);
			g.setFont(new Font("楷体", Font.BOLD, 20));
			g.drawString("按下ESC键即可退出游戏", 80, 180);
		}
		
	}
	
	//判断我的子弹是否击中敌人坦克
	public void hitEnemyTank()
	{
		for(int i = 0; i < hero.ss.size(); i++)
		{
			//取出子弹
			Shot myShot = hero.ss.get(i);
			
			if(myShot.isLive == true)
			{
				//取出每一个敌人的坦克与之进行判断
				for(int j = 0; j < ets.size(); j++)
				{	
					EnemyTank et = ets.get(j); //取出坦克	
					if(et.isLive == true)
					{
						this.hitTank(myShot, et);
					}
				}
			}
		}
	}
	
	//我是否被子弹击中
	public void hitMe()
	{
		for(int i = 0; i < this.ets.size(); i++)
		{
			EnemyTank et = ets.get(i); //取出坦克
			
			//取出每一颗子弹
			for(int j = 0; j < et.ss.size(); j++)
			{
				Shot enemyShot = et.ss.get(j); //取出子弹
				if(hero.isLive == true)
				{
					this.hitTank(enemyShot, hero);
				}
			}
		}
	}
	
	//判断子弹是否击中敌人的坦克
	public void hitTank(Shot s, Tank et)
	{
		//判断该坦克的方向
		switch(et.getDirect())
		{
		//向上或者向下
		case 0:
		case 2:
			//击中某一辆坦克
			if(s.getX() < et.getX() + 20 && s.getX() > et.getX()
					&& s.getY() > et.getY() && s.getY() < et.getY() + 30)
			{
				s.isLive = false;
				et.isLive = false;
				//创建一颗炸弹，放入vector
				Bomb b = new Bomb(et.getX(), et.getY());
				bombs.add(b);
			}
			break;
			
		//向左或者向右
		case 1:
		case 3:
			if(s.getX() > et.getX() && s.getX() < et.getX() + 30
					&& s.getY() > et.getY() && s.getY() < et.getY() + 20)
			{
				s.isLive = false;
				et.isLive = false;
				//创建一颗炸弹，放入vector
				Bomb b = new Bomb(et.getX(), et.getY());
				bombs.add(b);
			}
			break;
		}
	}
	
	//画出坦克的函数
	public void drawTank(int x, int y, Graphics g, int direct, int type)
	{
		//判断坦克类型
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.YELLOW);
			break;
		}
		
		//判断坦克方向
		switch(direct)
		{
		//向上
		case 0:
			//1.画出左边的矩形
			g.fill3DRect(x, y, 6, 30, false);
			//2.画出右边的矩形
			g.fill3DRect(x + 15, y, 6, 30, false);
			//3.画出中间的矩形
			g.fill3DRect(x + 5, y + 6, 10, 20, false);
			//4.画出圆形
			g.fillOval(x + 5, y + 10, 10, 10);
			//5.画出线
			g.drawLine(x + 10, y + 15, x + 11, y);
			break;
		
		//向右
		case 1:
			//画出上面的矩形
			g.fill3DRect(x, y, 30, 6, false);
			//画出下面的矩形
			g.fill3DRect(x, y + 15, 30, 6, false);
			//画出中间的矩形
			g.fill3DRect(x + 6, y + 5, 20, 10, false);
			//画出圆形
			g.fillOval(x + 10, y + 5, 10, 10);
			//画出线
			g.drawLine(x + 15, y + 10, x + 30, y + 10);
			break;
			
		//向下
		case 2:
			//1.画出左边的矩形
			g.fill3DRect(x, y, 6, 30, false);
			//2.画出右边的矩形
			g.fill3DRect(x + 15, y, 6, 30, false);
			//3.画出中间的矩形
			g.fill3DRect(x + 5, y + 6, 10, 20, false);
			//4.画出圆形
			g.fillOval(x + 5, y + 10, 10, 10);
			//5.画出线
			g.drawLine(x + 10, y + 15, x + 10, y + 30);
			break;
		
		//向左
		case 3:
			//画出上面的矩形
			g.fill3DRect(x, y, 30, 6, false);
			//画出下面的矩形
			g.fill3DRect(x, y + 15, 30, 6, false);
			//画出中间的矩形
			g.fill3DRect(x + 6, y + 5, 20, 10, false);
			//画出圆形
			g.fillOval(x + 10, y + 5, 10, 10);
			//画出线
			g.drawLine(x + 15, y + 10, x, y + 10);
			break;
		}
	}

	//按键操作
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			//设置我的坦克的方向
			this.hero.setDirect(0);
			this.hero.moveUp();
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_D)
		{
			//向右
			this.hero.setDirect(1);
			this.hero.moveRight();
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
		{
			//向下
			this.hero.setDirect(2);
			this.hero.moveDown();
		}
		else if(e.getKeyCode() == KeyEvent.VK_A)
		{
			//向左
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		//判断玩家是否按下J键开火
		if(e.getKeyCode() == KeyEvent.VK_J)
		{
			if(this.hero.ss.size() < 5)
			{
				//开火
				this.hero.shotEnemy();
			}
		}
		
		//判断玩家是否选择按下ESC退出游戏
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			//退出游戏
			System.exit(0);
		}
		
		
		this.repaint(); //必须重新绘制Panel
		
	}

	
	
	public void run()
	{
		//每隔50ms重画
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			//判断我的子弹是否击中敌人坦克
			this.hitEnemyTank();
			
			//判断自身的坦克是否被击中
			this.hitMe();
			
			//重绘
			this.repaint();
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}

