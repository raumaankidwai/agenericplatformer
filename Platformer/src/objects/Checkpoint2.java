package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

public class Checkpoint2 extends Checkpoint {
	public static Color DEFAULT_COLOR = Color.GRAY;
	
	public double x;
	public double y;
	
	public Checkpoint2 () {}
	
	public Checkpoint2 (World world, double scale, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		this.x = x;
		this.y = y;
		
		addFixture(new Triangle(
			new Vector2(x + 0.25, y),
			new Vector2(x + 0.25, y - 0.25),
			new Vector2(x + 0.49, y - 0.125)
		));
		addFixture(new Segment(
			new Vector2(x + 0.25, y - 0.25),
			new Vector2(x + 0.25, y - 0.49)
		));
		setMass(MassType.INFINITE);
		
		world.addBody(this);
	}
	
	public Checkpoint2 (World world, double scale, Color color, double x, double y) {
		super(scale, color);
		
		addFixture(new Triangle(
			new Vector2(x + 0.25, y),
			new Vector2(x + 0.25, y - 0.25),
			new Vector2(x + 0.49, y - 0.125)
		));
		addFixture(new Segment(
			new Vector2(x + 0.25, y - 0.25),
			new Vector2(x + 0.25, y - 0.49)
		));
		setMass(MassType.INFINITE);
		
		world.addBody(this);
	}
}