package objects;
import java.awt.Color;

import org.dyn4j.dynamics.*;

// No-Wall-Jumping Platform
public class NWJPlatform extends Platform {
	public static Color DEFAULT_COLOR = Color.RED;
	
	public NWJPlatform () {}
	
	public NWJPlatform (World world, double scale, double x, double y) {
		super(world, scale, DEFAULT_COLOR, x, y);
	}
	
	public NWJPlatform (World world, double scale, double width, double height, double x, double y) {
		super(world, scale, DEFAULT_COLOR, width, height, x, y);
	}
	
	public NWJPlatform (World world, double scale, Color color, double x, double y) {
		super(world, scale, color, x, y);
	}
	
	public NWJPlatform (World world, double scale, Color color, double width, double height, double x, double y) {
		super(world, scale, color, x, y);
	}
}