import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;


//test
public class Main extends JFrame implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private double t, dt, alpha;
	private double currentTime, accumulator;
	private ArrayList<Particle> particles;
	
	public Main(String title)
	{
		super(title);
		buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = buffer.getGraphics();
		t = 0;
		dt = 1.0/60.0;
		accumulator = 0.0;
		
		particles = new ArrayList<Particle>();
		for(int i = 0; i < 10; i++)
		{
			particles.add(new Particle(new Vector2(250, 250), new Vector2(0.0f, 0.0f), 1.0f));
			particles.get(i).addForce("random", new Vector2((float)(Math.random()-0.5)*500.0f, (float)(-Math.random())*250.0f));
		}
		
		addKeyListener(this);
	}
	public void start()
	{
		currentTime = (double)System.currentTimeMillis()/1000.0;
		new Thread(this).start();
	}
	public void run()
	{
		while(true)
		{
			double newTime = (double)System.currentTimeMillis()/1000.0;
			double frameTime = newTime - currentTime;
			if(frameTime > 0.25)
				frameTime = 0.25;
			currentTime = newTime;
			
			accumulator += frameTime;
			while(accumulator >= dt)
			{
				for(int i = 0; i < particles.size(); i++)
				{
					particles.get(i).tick((float)t, (float)dt);
				}
				t += dt;
				accumulator -= dt;
			}
			
			alpha = accumulator / dt;
			
			repaint();
		}
	}
	public void paint(Graphics g)
	{
		bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
		for(int i = 0; i < particles.size(); i++)
		{
			particles.get(i).draw(bufferGraphics, alpha);
		}
		g.drawImage(buffer, 0, 0, null);
	}
	public void update(Graphics g)
	{
		paint(g);
	}
	
	
	public static void main(String[] args)
	{
		Main main = new Main("Simulation");
		main.setSize(500, 500);
		main.setResizable(false);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		main.setVisible(true);
		main.start();
	}
	public void keyPressed(KeyEvent e) 
	{
		/*
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP)
		{
			particle.addForce("up", new Vector2(0, -15));
		}
		if(keyCode == KeyEvent.VK_DOWN)
		{
			particle.addForce("down", new Vector2(0, 15));
		}
		if(keyCode == KeyEvent.VK_LEFT)
		{
			particle.addForce("left", new Vector2(-15, 0));
		}
		if(keyCode == KeyEvent.VK_RIGHT)
		{
			particle.addForce("right", new Vector2(15, 0));
		}
		*/
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
		/*
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP)
		{
			particle.removeForce("up");
		}
		if(keyCode == KeyEvent.VK_DOWN)
		{
			particle.removeForce("down");
		}
		if(keyCode == KeyEvent.VK_LEFT)
		{
			particle.removeForce("left");
		}
		if(keyCode == KeyEvent.VK_RIGHT)
		{
			particle.removeForce("right");
		}
		*/
	}
	@Override
	public void keyTyped(KeyEvent e) {}
}
