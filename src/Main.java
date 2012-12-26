import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;


public class Main extends JFrame implements Runnable, MouseListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private static final float gravitationalConstant = 1.0f/5972.0f;
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private double t, dt, alpha;
	private double currentTime, accumulator;
	private Particle planet, satellite;
	private ArrayList<Point> points;
	private int red, green, blue;
	
	public Main(String title)
	{
		super(title);
		buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = buffer.getGraphics();
		t = 0;
		dt = 1.0/60.0;
		accumulator = 0.0;
		
		planet = new Particle(new Vector2(250.0f, 250.0f), new Vector2(0.0f, 0.0f), (float)6983000000.0f);
		satellite = new Particle(new Vector2(250.0f, 150.0f), new Vector2(100.0f, 0.0f), 1.0f);
		
		addMouseListener(this);
		addKeyListener(this);
		
		points = new ArrayList<Point>();
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
				double r = Math.sqrt(Math.pow(satellite.state.r.x-planet.state.r.x, 2) + Math.pow(satellite.state.r.y-planet.state.r.y, 2));
				double forceMagnitude = gravitationalConstant*satellite.getMass()*planet.getMass()/(r*r);
				Vector2 forceDirection = new Vector2((float)(planet.state.r.x-satellite.state.r.x), (float)(planet.state.r.y-satellite.state.r.y)).getUnitVector();
				Vector2 force = forceDirection.dotProduct((float)forceMagnitude);
				satellite.removeForce("gravity");
				satellite.addForce("gravity", force);
				satellite.tick((float)t, (float)dt);
				planet.tick((float)t, (float)dt);
				t += dt;
				accumulator -= dt;
				points.add(new Point((int)satellite.state.r.x, (int)satellite.state.r.y));
			}
			
			alpha = accumulator / dt;
			
			repaint();
		}
	}
	public void paint(Graphics g)
	{
		bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
		planet.draw(bufferGraphics, alpha);
		satellite.draw(bufferGraphics, alpha);
		red += green/255;
		green += blue/255;
		green = green%256;
		blue = blue%256;
		bufferGraphics.setColor(new Color(red, green, blue));
		blue+=5;
		for(int i = 0; i < points.size()-1; i++)
		{
			bufferGraphics.drawLine(points.get(i).x+4, points.get(i).y+4, points.get(i+1).x+4, points.get(i+1).y+4);
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
		int keyCode = e.getKeyCode();
		/*if(keyCode == KeyEvent.VK_SPACE)
		{
			planet.addForce("thrust", satellite.state.v.getUnitVector().dotProduct(100.0f));
		}
		if(keyCode == KeyEvent.VK_UP)
		{
			//planet.addForce("up", new Vector2(0, -6983000000.0f));
			planet.state.v = new Vector2(0.0f, -100.0f);
		}
		if(keyCode == KeyEvent.VK_DOWN)
		{
			//planet.addForce("down", new Vector2(0, 6983000000.0f));
			planet.state.v = new Vector2(0.0f, 100.0f);
		}
		if(keyCode == KeyEvent.VK_LEFT)
		{
			//planet.addForce("left", new Vector2(-6983000000.0f, 0));
			planet.state.v = new Vector2(-100.0f, 0.0f);
		}
		if(keyCode == KeyEvent.VK_RIGHT)
		{
			//planet.addForce("right", new Vector2(6983000000.0f, 0));
			planet.state.v = new Vector2(100.0f, 0.0f);
		}
		*/
		if(keyCode == KeyEvent.VK_R)
		{
			satellite.state.r = new Vector2(250.0f, 150.0f);
			satellite.state.v = new Vector2(100.0f, 0.0f);
			planet.state.r = new Vector2(250.0f, 250.0f);
			planet.state.v = new Vector2(0.0f, 0.0f);
			points.clear();
		}
		if(keyCode == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_SPACE)
		{
			//planet.removeForce("thrust");
		}
		planet.state.v = new Vector2(0.0f, 0.0f);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void mousePressed(MouseEvent e)
	{
		planet.state.r = new Vector2(e.getX(), e.getY());
	}
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
