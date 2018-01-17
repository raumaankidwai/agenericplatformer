package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

import framework.*;

// x, y = top-left corner of platform
public class Platform extends GameObject {
	public static Color DEFAULT_COLOR = Color.CYAN;
	
	public Platform () {}
	
	public Platform (World world, double scale, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		addFixture(new Rectangle(0.5, 0.5));
		setMass(MassType.INFINITE);
		
		translate(x + 0.25, y - 0.25);
		
		world.addBody(this);
	}
	
	public Platform (World world, double scale, double width, double height, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		addFixture(new Rectangle(width, height));
		setMass(MassType.INFINITE);
		
		translate(x + width / 2, y - height / 2);
		
		world.addBody(this);
	}
	
	public Platform (World world, double scale, Color color, double x, double y) {
		super(scale, color);
		
		addFixture(new Rectangle(0.5, 0.5));
		setMass(MassType.INFINITE);
		
		translate(x + 0.25, y - 0.25);
		
		world.addBody(this);
	}
	
	public Platform (World world, double scale, Color color, double width, double height, double x, double y) {
		super(scale, color);
		
		addFixture(new Rectangle(width, height));
		setMass(MassType.INFINITE);
		
		translate(x + width / 2, y - height / 2);
		
		world.addBody(this);
	}
}