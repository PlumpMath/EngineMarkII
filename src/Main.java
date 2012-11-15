import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class Main extends JFrame implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	private static final float gravitationalConstant = 1.0f/5972.0f;
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private double t, dt, alpha;
	private double currentTime, accumulator;
	private Particle planet, satellite;
	
	public Main(String title)
	{
		super(title);
		buffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = buffer.getGraphics();
		t = 0;
		dt = 1.0/60.0;
		accumulator = 0.0;
		
		planet = new Particle(new Vector2(250.0f, 250.0f), new Vector2(0.0f, 0.0f), (float)5983000000.0f);
		satellite = new Particle(new Vector2(250.0f, 150.0f), new Vector2(100.0f, 0.0f), 1.0f);
		
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
				double r = Math.sqrt(Math.pow(satellite.state.r.x-250, 2) + Math.pow(satellite.state.r.y-250, 2));
				double forceMagnitude = gravitationalConstant*satellite.getMass()*planet.getMass()/(r*r);
				Vector2 forceDirection = new Vector2((float)(250-satellite.state.r.x), (float)(250-satellite.state.r.y)).getUnitVector();
				Vector2 force = forceDirection.dotProduct((float)forceMagnitude);
				satellite.removeForce("gravity");
				satellite.addForce("gravity", force);
				satellite.tick((float)t, (float)dt);
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
		planet.draw(bufferGraphics, alpha);
		satellite.draw(bufferGraphics, alpha);
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
		if(keyCode == KeyEvent.VK_SPACE)
		{
			satellite.addForce("thrust", satellite.state.v.getUnitVector().dotProduct(100.0f));
		}
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_SPACE)
		{
			satellite.removeForce("thrust");
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
}
