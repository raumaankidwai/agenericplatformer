package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

import framework.*;

// x, y = top-left corner of AABB of spike
public class Spike extends GameObject {
	public static Color DEFAULT_COLOR = Color.GRAY;
	
	public Spike () {}
	
	public Spike (World world, double scale, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		addFixture(new Triangle(
			new Vector2(x + 0.01, y - 0.5),
			new Vector2(x + 0.49, y - 0.5),
			new Vector2(x + 0.25, y - 0.01)
		));
		setMass(MassType.INFINITE);
		
		world.addBody(this);
	}
	
	public Spike (World world, double scale, Color color, double x, double y) {
		super(scale, color);
		
		addFixture(new Triangle(
			new Vector2(x + 0.01, y - 0.5),
			new Vector2(x + 0.49, y - 0.5),
			new Vector2(x + 0.25, y - 0.01)
		));
		setMass(MassType.INFINITE);
		
		world.addBody(this);
	}
}