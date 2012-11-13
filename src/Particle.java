import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;


public class Particle {
	public State state, previousState;
	private float mass;
	private HashMap<String, Vector2> forces;
	private int ticks;
	
	public Particle(Vector2 r, Vector2 v, float m)
	{
		state = new State(r, v);
		previousState = state;
		mass = m;
		forces = new HashMap<String, Vector2>();
		forces.put("gravity", new Vector2(0.0f, 9.81f));
	}
	
	public void tick(float t, float dt)
	{
		ticks++;
		if(ticks > 60)
		{
			removeForce("random");
		}
		previousState = state;
		integrate(state, t, dt);
	}
	
	public void draw(Graphics g, double alpha)
	{
		g.drawRect((int)Math.round(state.r.x*alpha + previousState.r.x*(1.0-alpha)), (int)Math.round(state.r.y*alpha + previousState.r.y*(1.0-alpha)), 5, 5);
	}
	
	public void addForce(String name, Vector2 force)
	{
		forces.put(name, force);
	}
	public void removeForce(String name)
	{
		if(forces.containsKey(name))
			forces.remove(name);
	}
	
	private Derivative evaluate(final State initial, float t, float dt, final Derivative derivative)
	{
		State state = new State();
		state.r = initial.r.add(derivative.dr.dotProduct(dt));
		state.v = initial.v.add(derivative.dv.dotProduct(dt));
		
		Derivative output = new Derivative();
		output.dr = state.v;
		output.dv = acceleration(state, t+dt);
		return output;
	}
	private Vector2 acceleration(final State state, float t)
	{
		Vector2 result = new Vector2();
		Iterator<Vector2> iterator = forces.values().iterator();
		while(iterator.hasNext())
		{
			result = result.add(iterator.next());
		}
		iterator = forces.values().iterator();
		return result;
		
	}
	public Vector2 getNetForce()
	{
		Vector2 result = new Vector2();
		Iterator<Vector2> iterator = forces.values().iterator();
		while(iterator.hasNext())
		{
			result = result.add(iterator.next());
		}
		return result;
	}
	private void integrate(State state, float t, float dt)
	{
		Derivative a = evaluate(state, t, 0.0f, new Derivative());
		Derivative b = evaluate(state, t, dt*0.5f, a);
		Derivative c = evaluate(state, t, dt*0.5f, b);
		Derivative d = evaluate(state, t, dt, c);
		
		Vector2 dxdt = (a.dr.add((b.dr.add(c.dr)).dotProduct(2.0f).add(d.dr))).dotProduct(1.0f/6.0f);
		Vector2 dvdt = (a.dv.add((b.dv.add(c.dv)).dotProduct(2.0f).add(d.dv))).dotProduct(1.0f/6.0f);
		
		state.r = state.r.add(dxdt.dotProduct(dt));
		state.v = state.v.add(dvdt.dotProduct(dt));
	}
}

class State {
	public Vector2 r; //Position
	public Vector2 v; //Velocity
	
	public State()
	{
		r = new Vector2(0, 0);
		v = new Vector2(0, 0);
	}
	public State(Vector2 r, Vector2 v)
	{
		this.r = r;
		this.v = v;
	}
}

class Derivative
{
	public Vector2 dr; //Velocity
	public Vector2 dv; //Acceleration
	
	public Derivative()
	{
		dr = new Vector2(0, 0);
		dv = new Vector2(0, 0);
	}
	public Derivative(Vector2 dr, Vector2 dv)
	{
		this.dr = dr;
		this.dv = dv;
	}
}