package core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dyn4j.dynamics.World;

import framework.*;
import objects.*;

// x, y = top-left corner of level
public class PlatformerLevel {
	// Default class mapping
	public static List<Class<? extends GameObject>> DEFAULT_MAP = Arrays.asList(
		Air.class,
		Platform.class,
		NWJPlatform.class,
		Spike.class,
		Checkpoint1.class,
		Checkpoint2.class,
		Spring.class
	);
	
	// Checkpoints
	public Checkpoint1 checkpoint1;
	public Checkpoint2 checkpoint2;
	
	// Properties
	private int[][] rawLevelData;
	private List<Class<? extends GameObject>> map;
	private ArrayList<ArrayList<Class<? extends GameObject>>> levelData = new ArrayList<ArrayList<Class<? extends GameObject>>>();
	
	private String levelText;
	
	// Constructors
	public PlatformerLevel () {}
	
	public PlatformerLevel (int[][] rawLevelData, String levelText) {
		init(rawLevelData, DEFAULT_MAP, levelText);
	}
	
	public PlatformerLevel (int[][] rawLevelData, List<Class<? extends GameObject>> map, String levelText) {
		init(rawLevelData, map, levelText);
	}
	
	// Initialize level data
	public ArrayList<ArrayList<Class<? extends GameObject>>> init (int[][] rawLevelData, List<Class<? extends GameObject>> map, String levelText) {
		setRawLevelData(rawLevelData);
		setMap(map);
		setLevelText(levelText);
		
		for (int i = 0; i < rawLevelData.length; i ++) {
			int[] row = rawLevelData[i];
			
			levelData.add(new ArrayList<Class<? extends GameObject>>());
			
			for (int j = 0; j < row.length; j ++) {
				levelData.get(i).add(map.get(row[j]));
			}
		}
		
		return levelData;
	}
	
	// Add level to world
	public void add (World world, double scale, double x, double y) {
		for (int i = 0; i < levelData.size(); i ++) {
			ArrayList<Class<? extends GameObject>> row = levelData.get(i);
			
			for (int j = 0; j < row.size(); j ++) {
				Class<? extends GameObject> el = row.get(j);
				
				if (el == Air.class) {
					continue;
				}
				
				if (el == Checkpoint1.class) {
					checkpoint1 = new Checkpoint1(world, scale, x + j * 0.5, y - i * 0.5);
					continue;
				}
				
				if (el == Checkpoint2.class) {
					checkpoint2 = new Checkpoint2(world, scale, x + j * 0.5, y - i * 0.5);
					continue;
				}
				
				try {
					el.getConstructor(World.class, double.class, double.class, double.class).newInstance(world, scale, x + j * 0.5, y - i * 0.5);
				} catch (Exception e) {}
			}
		}
	}
	
	public int[][] getRawLevelData () {
		return rawLevelData;
	}
	
	public List<Class<? extends GameObject>> getMap () {
		return map;
	}
	
	public ArrayList<ArrayList<Class<? extends GameObject>>> getLevelData () {
		return levelData;
	}
	
	public String getLevelText () {
		return levelText;
	}
	
	public void setRawLevelData (int[][] rawLevelData) {
		this.rawLevelData = rawLevelData;
	}
	
	public void setMap (List<Class<? extends GameObject>> map) {
		this.map = map;
	}
	
	public void setLevelData (ArrayList<ArrayList<Class<? extends GameObject>>> levelData) {
		this.levelData = levelData;
	}
	
	public void setLevelText (String levelText) {
		this.levelText = levelText;
	}
}