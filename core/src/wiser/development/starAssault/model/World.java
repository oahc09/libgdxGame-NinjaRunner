package wiser.development.starAssault.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

	public class World {

	 /** Our player controlled hero **/
	 Bob bob;
	 /** A world has a level through which Bob needs to go through **/
	 Level level;
	 
	 
		
		/** The collision boxes **/
		Array<Rectangle> collisionRects = new Array<Rectangle>();

	
	 // Getters -----------
	 
	public Array<Rectangle> getCollisionRects() {
			return collisionRects;
	}
	public Level getLevel() {
		return level;
	}

	 public Bob getBob() {
	  return bob;
	 }	

	 /** Return only the blocks that need to be drawn **/

	 
		public List<Block> getDrawableBlocks( int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<Block> blocks = new ArrayList<Block>();
			Block block;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					block = level.getBlocks()[col][row];
					if (block != null) {
						blocks.add(block);
					}
				}
			}
			return blocks;
		}

		public List<Climable> getDrawableClimable( int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<Climable> climables = new ArrayList<Climable>();
			Climable climable;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					climable = level.getClimables()[col][row];
					if (climable != null) {
						climables.add(climable);
					}
				}
			}
			return climables;
		}
	 // --------------------
		public List<Spring> getDrawableSprings (int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<Spring> springs = new ArrayList<Spring>();
			Spring spring;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					spring = level.getSprings()[col][row];
					if (spring != null) {
						springs.add(spring);
					}
				}
			}
			return springs;
		}
		public List<SpeedPad> getDrawableSpeedPads (int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<SpeedPad> speedPads = new ArrayList<SpeedPad>();
			SpeedPad speedPad;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					speedPad= level.getSpeedPads()[col][row];
					if (speedPad != null) {
						speedPads.add(speedPad);
					}
				}
			}
			return speedPads;
		}
		public List<Fire> getDrawableFire( int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<Fire> fires = new ArrayList<Fire>();
			Fire fire;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					fire = level.getFires()[col][row];
					if (fire != null) {
						fires.add(fire);
					}
				}
			}
			return fires;
		}
		public ArrayList<FireBall> getDrawableFireBall( int cameraWidth, int cameraHeight) {
			return level.getFireBalls();
		}
		public ArrayList<Platform> getDrawablePlatforms( int cameraWidth, int cameraHeight) {
			return level.getPlatforms();
		}
		public ArrayList<Skeleton> getDrawableSkeletons( int cameraWidth, int cameraHeight) {
			return level.getSkeletons();
		}
	 
		public List<NinjaStars> getDrawableNinjaStars( int cameraWidth, int cameraHeight) {
			int x = (int) (bob.getPosition().x - cameraWidth);
			int y = (int) (bob.getPosition().y - cameraHeight);
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			int x2 = x + 2 * cameraWidth;
			int y2 = y + 2 * cameraHeight;
			if (x2 > level.getWidth()) {
				x2 = level.getWidth() - 1;
			}
			if (y2 > level.getHeight()) {
				y2 = level.getHeight() - 1;
			}
			
			List<NinjaStars> NinjaStars = new ArrayList<NinjaStars>();
			NinjaStars NinjaStar;
			for (int col = x; col <= x2; col++) {
				for (int row = y; row <= y2; row++) {
					NinjaStar = level.getNinjaStar()[col][row];
					
					if (NinjaStar != null) {
						NinjaStars.add(NinjaStar);
					}
				}
			}
			ArrayList<NinjaStars> thrownStars = new ArrayList<NinjaStars>();
			thrownStars= level.getThrownStars();
			Iterator<NinjaStars> starIt =  thrownStars.iterator();
			while(starIt.hasNext()){
				NinjaStar=starIt.next();
//				if(( (float)x <= NinjaStar.getPosition().x)&&( (float)x2 >= NinjaStar.getPosition().x) 
//					&& ( (float)y <= NinjaStar.getPosition().y)&& ( (float)y2 <= NinjaStar.getPosition().y)) 
//				{
				NinjaStars.add(NinjaStar);	
//				}
			}
			return NinjaStars;
		}
	 public World(int levelNum) {
		 createLevel(levelNum);
	 }

	 private void createLevel(int levelNum) {
			level = new Level(levelNum);
			bob = new Bob (level.getBobStart());
		}

	}