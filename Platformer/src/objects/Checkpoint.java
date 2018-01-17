package objects;
import java.awt.Color;

import framework.*;

public class Checkpoint extends GameObject {
	public Checkpoint () {}
	
	public Checkpoint (double scale) {
		super(scale);
	}
	
	public Checkpoint (Color color) {
		super(color);
	}
	
	public Checkpoint (double scale, Color color) {
		super(scale, color);
	}
}