package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

import framework.*;

// x, y = top-left corner of spring
public class Spring extends GameObject {
	public static Color DEFAULT_COLOR = Color.GRAY;
	
	public Spring () {}
	
	public Spring (World world, double scale, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		addFixture(new Rectangle(0.5, 0.25));
		setMass(MassType.INFINITE);
		
		translate(x + 0.25, y - 0.375);
		
		world.addBody(this);
	}
	
	public Spring (World world, double scale, Color color, double x, double y) {
		super(scale, color);
		
		addFixture(new Rectangle(0.5, 0.25));
		setMass(MassType.INFINITE);
		
		translate(x + 0.25, y - 0.375);
		
		world.addBody(this);
	}
}