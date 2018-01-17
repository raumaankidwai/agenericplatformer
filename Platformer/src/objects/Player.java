package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

import framework.*;

// x, y = midpoint of bottom edge of player
public class Player extends GameObject {
	public static Color DEFAULT_COLOR = Color.GREEN;
	
	public Player () {}
	
	public Player (World world, double scale, double x, double y) {
		super(scale, DEFAULT_COLOR);
		
		addFixture(new Rectangle(0.5, 0.5));
		
		setMass(MassType.NORMAL);
		
		translate(x, y + 0.25);
		
		world.addBody(this);
	}
	
	public Player (World world, double scale, Color color, double x, double y) {
		super(scale, color);
		
		addFixture(new Rectangle(0.5, 0.5));
		setMass(MassType.NORMAL);
		
		translate(x, y + 0.25);
		
		world.addBody(this);
	}
}