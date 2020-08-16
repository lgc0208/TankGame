/*
 *	 ̹����Ϸ5.0.0
 *	1.����̹�ˡ�
 *	2.�ҵ�̹�˿������������ƶ�
 *	3.����J�������ӵ����ӵ����������ͬʱ����5��
 *	4.���ҵ�̹�˻��е���̹��ʱ������̹�˱�ը����ʧ
 *	5.����̹�˿��������ƶ�
 *	6.�ҷ��͵з�̹���ڹ涨�����ƶ�
 *	7.�����˵�̹�˻����ҷ�̹��ʱ���ҷ�̹�˱�ը
 *	8.���п�ʼ�ͽ�����ʾ����
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
	
	
	//���캯��
	public MyTankGame5()
	{
		ip = new InitialPanel();
		this.add(ip);
		this.setTitle("̹�˴�ս5.0.0_LGC_2020.08");
		this.setSize(414, 337);
		this.setLocationRelativeTo(null); //������ʾ
		this.setResizable(false); //���ɸı䴰���С
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mp = new GamePanel();
		//����mp�߳�
		Thread t = new Thread(mp);
		t.start();

		this.add(mp);
		//ע�����
		this.addKeyListener(mp);
		this.setTitle("̹�˴�ս_LGC_2020.08");
		this.setSize(414, 337);
		this.setLocationRelativeTo(null); //������ʾ
		this.setResizable(false); //���ɸı䴰���С
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}

//��ʼ��ʾ���
class InitialPanel extends JPanel 
{
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.red);
		g.setFont(new Font("����", Font.BOLD, 30));
		g.drawString("��Ϸ����", 140, 70);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString("  ����ϷΪ̹�˴�ս��Ϸ���������е���", 10, 120);
		g.drawString("���ɻ�ʤ��", 10, 150);
		g.drawString("  ʹ��WSAD�����ƶ���J��������ESC�˳�", 10, 180);
		g.drawString("  ��ʾ����һ�����ֻ�ܷ���5���ӵ�", 10, 210);
		g.drawString("  ��Ϸ����5���ʼ��ף����Ϸ��죡", 10, 240);
	}
}

//��Ϸ���
class GamePanel extends JPanel implements KeyListener, Runnable
{

	
	Hero hero = null; //����һ���ҵ�̹��
	Vector<EnemyTank> ets = new Vector<EnemyTank>(); //������˵�̹����
	Vector<Bomb> bombs = new Vector<Bomb>(); //����һ��ը������
	
	int enemySize = 5; //������˸���
	
	//����3��ͼƬ��3��ͼƬ���һ��ը��
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	//���캯��
	public GamePanel()
	{
		hero = new Hero(100, 200); //��ʼ���Լ���̹��
		//��ʼ�����˵�̹��
		for(int i = 0; i < enemySize; i++)
		{
			EnemyTank et = new EnemyTank((i + 1) * 50, 0); //����һ�����˵�̹�˶���
			et.setColor(0);
			et.setDirect(2);
			//��������̹��
			Thread t = new Thread(et);
			t.start();
			
			Shot s = new Shot(et.getX() + 10, et.getY() + 30, 2); //������̹�����һ���ӵ�
			et.ss.add(s); //���ӵ����������
			Thread t2 = new Thread(s);
			t2.start();
			
			ets.add(et); //����������
		}
		//��ʼ��ͼƬ
		image1 = Toolkit.getDefaultToolkit().getImage("images/bomb1.png");
		image2 = Toolkit.getDefaultToolkit().getImage("images/bomb2.png");
		image3 = Toolkit.getDefaultToolkit().getImage("images/bomb3.png");
	}
	
	//��дpaint����
	public void paint(Graphics g)
	{
		super.paint(g); //���ø��๹�췽��
		g.fillRect(0, 0, 400, 300);
		
		//�����Լ���̹��
		if(hero.isLive == true)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.direct, 0);
		}
		
		//�������˵�̹��
		for(int i = 0; i < ets.size(); i++)
		{
			EnemyTank et = ets.get(i);
			if(et.isLive == true)
			{
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
				//�ٻ������˵��ӵ�
				for(int j = 0; j < et.ss.size(); j++)
				{
					//ȡ���ӵ�
					Shot enemyShot = et.ss.get(j);
					if(enemyShot.isLive == true)
					{
						g.setColor(Color.YELLOW);
						g.draw3DRect(enemyShot.getX(), enemyShot.getY(), 1, 1, false);
					}
					else
					{
						//�����˵�̹���������ʹ�Vectorȥ��
						et.ss.remove(enemyShot);
					}
				}
			}
			else
			{
				ets.remove(et);
			}
		}
		
		//��ss��ȡ��ÿ���ӵ�������
		for(int i = 0; i < hero.ss.size(); i++)
		{
			//ȡ��һ���ӵ�
			Shot myShot = hero.ss.get(i);
			
			//�����Լ����ӵ�
			if(myShot != null && myShot.isLive == true)
			{
				g.setColor(Color.CYAN);
				g.draw3DRect(myShot.getX(), myShot.getY(), 1, 1, false);
			}
			
			if(myShot.isLive == false)
			{
				//�������в������ӵ�
				hero.ss.remove(myShot);
			}
		}
		
		//������ըЧ��
		for(int i = 0; i < bombs.size(); i++)
		{
			//ȡ��һ��ը��
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
				//���ޣ�ʹ��һ����ը��̹��ը������
				if(i == 0)
					g.drawImage(image2, b.getX() - 4, b.getY() - 1, 30, 30, this);
				else
					g.drawImage(image3, b.getX() - 4, b.getY() - 1, 30, 30, this);
			}
			//��b������ֵ��С
			b.lifeDown();
			//��ը������ֵΪ0���ͰѸ�ը��bombs����ȥ��
			if(b.life == 0)
			{
				bombs.remove(b);
			}
		}
		
		if(hero.isLive == false)
		{
			g.setColor(Color.red);
			g.setFont(new Font("����", Font.BOLD, 30));
			g.drawString("��Ϸ��������ʧ���ˣ�", 50, 130);
			g.setFont(new Font("����", Font.BOLD, 20));
			g.drawString("����ESC�������˳���Ϸ", 80, 180);
		}
		if(ets.size() == 0)
		{
			g.setColor(Color.red);
			g.setFont(new Font("����", Font.BOLD, 30));
			g.drawString("��Ϸ��������ʤ���ˣ�", 50, 160);
			g.setFont(new Font("����", Font.BOLD, 20));
			g.drawString("����ESC�������˳���Ϸ", 80, 180);
		}
		
	}
	
	//�ж��ҵ��ӵ��Ƿ���е���̹��
	public void hitEnemyTank()
	{
		for(int i = 0; i < hero.ss.size(); i++)
		{
			//ȡ���ӵ�
			Shot myShot = hero.ss.get(i);
			
			if(myShot.isLive == true)
			{
				//ȡ��ÿһ�����˵�̹����֮�����ж�
				for(int j = 0; j < ets.size(); j++)
				{	
					EnemyTank et = ets.get(j); //ȡ��̹��	
					if(et.isLive == true)
					{
						this.hitTank(myShot, et);
					}
				}
			}
		}
	}
	
	//���Ƿ��ӵ�����
	public void hitMe()
	{
		for(int i = 0; i < this.ets.size(); i++)
		{
			EnemyTank et = ets.get(i); //ȡ��̹��
			
			//ȡ��ÿһ���ӵ�
			for(int j = 0; j < et.ss.size(); j++)
			{
				Shot enemyShot = et.ss.get(j); //ȡ���ӵ�
				if(hero.isLive == true)
				{
					this.hitTank(enemyShot, hero);
				}
			}
		}
	}
	
	//�ж��ӵ��Ƿ���е��˵�̹��
	public void hitTank(Shot s, Tank et)
	{
		//�жϸ�̹�˵ķ���
		switch(et.getDirect())
		{
		//���ϻ�������
		case 0:
		case 2:
			//����ĳһ��̹��
			if(s.getX() < et.getX() + 20 && s.getX() > et.getX()
					&& s.getY() > et.getY() && s.getY() < et.getY() + 30)
			{
				s.isLive = false;
				et.isLive = false;
				//����һ��ը��������vector
				Bomb b = new Bomb(et.getX(), et.getY());
				bombs.add(b);
			}
			break;
			
		//�����������
		case 1:
		case 3:
			if(s.getX() > et.getX() && s.getX() < et.getX() + 30
					&& s.getY() > et.getY() && s.getY() < et.getY() + 20)
			{
				s.isLive = false;
				et.isLive = false;
				//����һ��ը��������vector
				Bomb b = new Bomb(et.getX(), et.getY());
				bombs.add(b);
			}
			break;
		}
	}
	
	//����̹�˵ĺ���
	public void drawTank(int x, int y, Graphics g, int direct, int type)
	{
		//�ж�̹������
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.YELLOW);
			break;
		}
		
		//�ж�̹�˷���
		switch(direct)
		{
		//����
		case 0:
			//1.������ߵľ���
			g.fill3DRect(x, y, 6, 30, false);
			//2.�����ұߵľ���
			g.fill3DRect(x + 15, y, 6, 30, false);
			//3.�����м�ľ���
			g.fill3DRect(x + 5, y + 6, 10, 20, false);
			//4.����Բ��
			g.fillOval(x + 5, y + 10, 10, 10);
			//5.������
			g.drawLine(x + 10, y + 15, x + 11, y);
			break;
		
		//����
		case 1:
			//��������ľ���
			g.fill3DRect(x, y, 30, 6, false);
			//��������ľ���
			g.fill3DRect(x, y + 15, 30, 6, false);
			//�����м�ľ���
			g.fill3DRect(x + 6, y + 5, 20, 10, false);
			//����Բ��
			g.fillOval(x + 10, y + 5, 10, 10);
			//������
			g.drawLine(x + 15, y + 10, x + 30, y + 10);
			break;
			
		//����
		case 2:
			//1.������ߵľ���
			g.fill3DRect(x, y, 6, 30, false);
			//2.�����ұߵľ���
			g.fill3DRect(x + 15, y, 6, 30, false);
			//3.�����м�ľ���
			g.fill3DRect(x + 5, y + 6, 10, 20, false);
			//4.����Բ��
			g.fillOval(x + 5, y + 10, 10, 10);
			//5.������
			g.drawLine(x + 10, y + 15, x + 10, y + 30);
			break;
		
		//����
		case 3:
			//��������ľ���
			g.fill3DRect(x, y, 30, 6, false);
			//��������ľ���
			g.fill3DRect(x, y + 15, 30, 6, false);
			//�����м�ľ���
			g.fill3DRect(x + 6, y + 5, 20, 10, false);
			//����Բ��
			g.fillOval(x + 10, y + 5, 10, 10);
			//������
			g.drawLine(x + 15, y + 10, x, y + 10);
			break;
		}
	}

	//��������
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(0);
			this.hero.moveUp();
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_D)
		{
			//����
			this.hero.setDirect(1);
			this.hero.moveRight();
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
		{
			//����
			this.hero.setDirect(2);
			this.hero.moveDown();
		}
		else if(e.getKeyCode() == KeyEvent.VK_A)
		{
			//����
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		//�ж�����Ƿ���J������
		if(e.getKeyCode() == KeyEvent.VK_J)
		{
			if(this.hero.ss.size() < 5)
			{
				//����
				this.hero.shotEnemy();
			}
		}
		
		//�ж�����Ƿ�ѡ����ESC�˳���Ϸ
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			//�˳���Ϸ
			System.exit(0);
		}
		
		
		this.repaint(); //�������»���Panel
		
	}

	
	
	public void run()
	{
		//ÿ��50ms�ػ�
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			//�ж��ҵ��ӵ��Ƿ���е���̹��
			this.hitEnemyTank();
			
			//�ж������̹���Ƿ񱻻���
			this.hitMe();
			
			//�ػ�
			this.repaint();
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}

