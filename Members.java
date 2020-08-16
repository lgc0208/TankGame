package myTankGame5;

import java.util.Vector;

//ը����
class Bomb
{
	//����ը��������
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

	//��������ֵ
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

//�ӵ���
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed = 3;
	
	//�Ƿ񻹻���
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
	
	//���λ��
	public Shot(int x, int y, int direct)
	{
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
	
	//Runnable�ӿں�����д
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
				//��
				this.y -= this.speed;
				break;
			case 1:
				//��
				this.x += this.speed;
				break;
			case 2:
				//��
				this.y += this.speed;
				break;
			case 3:
				//��
				this.x -= this.speed;
				break;
			}
			//System.out.println("�ӵ����꣺x=" + this.getX() + " y=" + this.getY());
			
			//�ӵ�����
			//�жϸ��ӵ��Ƿ�������Ե
			if(x < 0 || y < 0 || x > 400 || y > 300)
			{
				this.isLive = false;
				break;
			}
		}
	}
}

//̹����
class Tank
{
	//̹�˵ĺ�����
	int x = 0;
	//̹�˵�������
	int y = 0;
	//̹�˷���
	//0��ʾ�ϣ�1��ʾ�ң�2��ʾ�£�3��ʾ��
	int direct = 0;
	//̹�˵��ٶ�
	int speed = 1;
	//̹�˵���ɫ
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

//�ҵ�̹��
class Hero extends Tank
{
	//�ӵ�
	Shot s = null;
	Vector<Shot> ss = new Vector<Shot>();
	
	public Hero(int x, int y)
	{
		//super()�������е��ø���Ĺ��췽��
		super(x, y);
	}
	
	//����
	public void shotEnemy()
	{
		switch(this.direct)
		{
		case 0:
			//����һ���ӵ�
			s = new Shot(x + 9, y, this.direct);
			//���ӵ����뵽����
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
		
		//�����ӵ��߳�
		Thread t = new Thread(s);
		t.start();
	}
	
	//̹�������ƶ�
	public void moveUp()
	{
		if(this.y > 0)
			this.y -= this.speed;
	}
	
	//̹�������ƶ�
	public void moveRight()
	{
		if(this.x < 400 - 30)
			this.x += this.speed;
	}
	
	//̹�������ƶ�
	public void moveDown()
	{
		if(this.y < 300 - 30)
			this.y += this.speed;
	}
	
	//̹�������ƶ�
	public void moveLeft()
	{
		if(this.x > 0)
			this.x -= this.speed;
	}
}

//���˵�̹�� �����߳���
class EnemyTank extends Tank implements Runnable
{
	int times = 0;
	
	//����һ��������ŵ��˵��ӵ�
	Vector<Shot> ss = new Vector<Shot>();
	//���������ӵ�Ӧ���ڸոմ���̹�˺͵��˵�̹���ӵ�����֮��
	
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
				//̹�����������ƶ�
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
				//����
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
				//����
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
				//����
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
					//�������4��ʾͬʱ���Է���4���ӵ�
					if(ss.size() < 4)
					{
						Shot s = null;
						//û���ӵ�����ʱ�����ӵ�
						switch(direct)
						{
						case 0:
							//����һ���ӵ�
							s = new Shot(getX() + 9, getY(), direct);
							//���ӵ����뵽����
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
						//�����ӵ��߳�
						Thread t = new Thread(s);
						t.start();
					}
				}
			}
			
			//��̹���������һ���µķ���
			this.direct = (int)(Math.random() * 4);
			
			//�жϵ���̹���Ƿ�����
			if(this.isLive == false)
			{
				//��̹���������˳��߳�
				break;
			}
		}
		}
	}
