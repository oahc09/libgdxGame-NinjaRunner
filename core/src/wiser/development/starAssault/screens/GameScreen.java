package wiser.development.starAssault.screens;
import wiser.development.starAssault.controller.BobController;
import wiser.development.starAssault.controller.ObjectController;
import wiser.development.starAssault.controller.SkeletonController;
import wiser.development.starAssault.model.Bob.BobState;
import wiser.development.starAssault.model.World;
import wiser.development.starAssault.utils.Assets;
import wiser.development.starAssault.view.WorldRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen, GestureListener, InputProcessor{

	public enum GameState{
		READY, RUNNING, PAUSED, LEVEL_END, GAME_OVER ;
	}
	public static final float CAMERA_WIDTH = 10f;
	public static final float CAMERA_HEIGHT = 7f;
	
	Game game;
	public OrthographicCamera cam;
	private World world;
	private WorldRenderer renderer;
	private BobController	controller;
	private SkeletonController skeletonController;
	private ObjectController objectController;
	private int width, height;
	SpriteBatch batcher;
	public GameState gameState;
	Rectangle nextLevelBounds;
	Rectangle retryBounds;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	Rectangle ninjaInfo;
	Vector3 touchPoint;
	String numThrowingStars;
	GestureDetector gestureDetector ;
	String message;
	int directionPointer= -1;
	int actionPointer=-1;
	int levelNumber;
	long time_on_screen;
	
	float down_x, down_y , initpos_y, initpos_x, initpan_x, initpan_y, delta_x, delta_y;


	public GameScreen(Game game, int levelNum){
		touchPoint = new Vector3();
		this.game =game;
		gameState=GameState.READY;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(0,0, 0);
		this.cam.update();
		this.levelNumber=levelNum;
		batcher = new SpriteBatch();
		 nextLevelBounds= new Rectangle(this.cam.position.x + CAMERA_WIDTH/8  , this.cam.position.y- 3*CAMERA_HEIGHT/8, CAMERA_WIDTH/4 ,CAMERA_HEIGHT/5);
		retryBounds= new Rectangle(this.cam.position.x - 3*CAMERA_WIDTH/8  , this.cam.position.y- 3*CAMERA_HEIGHT/8, CAMERA_WIDTH/4 ,CAMERA_HEIGHT/5);

		pauseBounds = new Rectangle( this.cam.position.x +  CAMERA_WIDTH/2 -1f,this.cam.position.y+CAMERA_HEIGHT/2  -1f, 2f, 2f);
		resumeBounds = new Rectangle( this.cam.position.x - CAMERA_WIDTH/2, this.cam.position.y , CAMERA_WIDTH, CAMERA_HEIGHT/2);
		quitBounds = new Rectangle( this.cam.position.x -  CAMERA_WIDTH/2 ,this.cam.position.y -CAMERA_HEIGHT/2 ,CAMERA_WIDTH, CAMERA_HEIGHT/2);
		ninjaInfo = new Rectangle(  cam.position.x - CAMERA_WIDTH/2 +1f, cam.position.y +CAMERA_HEIGHT/2 -1f, 1f,1f);
		
		world = new World(levelNum);
		renderer = new WorldRenderer(world, false, this, batcher);
        controller = new BobController(world, this);
        skeletonController = new SkeletonController(world, this);
        objectController = new ObjectController(world, this);
        InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(20, 0.1f, 1.1f, 0.1f, this);
       
        im.addProcessor(gd);
        im.addProcessor(this);
        
        Gdx.input.setInputProcessor(im);
     }
	
	

	

 
	public void setGameState(GameState state){
		gameState=state;
		
	}
	public void update (float deltaTime) {
		
		switch (gameState) {
		case READY:
			updateReady(deltaTime);
			break;
		case RUNNING:
			updateRunning(deltaTime);
			break;
		case PAUSED:
			updatePaused();
			break;
		case LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		default: updateReady(deltaTime);
		}
		this.cam.update();
	}
	private void updateReady(float delta){
		//reset ie release all controls in order to not continue anythingleft over from bob's previos life
		controller.climbDownReleased();
		controller.jumpReleased();
		controller.climbReleased();
		controller.punchReleased();
		controller.throwReleased();
		controller.leftReleased();
		controller.rightReleased();
		controller.getBob().setVelocity(new Vector2(0,0));
		gameState= GameState.RUNNING;
		
	}
	private void updatePaused() {
		Gdx.gl.glClearColor(0.7f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		resumeBounds.set( this.cam.position.x - CAMERA_WIDTH/2, this.cam.position.y  , CAMERA_WIDTH, CAMERA_HEIGHT/2);
		quitBounds.set(  this.cam.position.x - CAMERA_WIDTH/2, this.cam.position.y -CAMERA_HEIGHT/2 , CAMERA_WIDTH, CAMERA_HEIGHT/2);

		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameState = GameState.RUNNING;
				return;
			}

			if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameState = GameState.GAME_OVER;
				game.setScreen(new MainMenuScreen(game));
				
				return;
			}
		}
		
	}

	private void updateRunning(float delta){
		Gdx.gl.glClearColor(0.7f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.justTouched()) {
			cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameState = GameState.PAUSED;
				return;
			}
		}
		// update rectangle bounds for pause button and pause menu 
		pauseBounds.set(  this.cam.position.x + CAMERA_WIDTH/2 -1f , this.cam.position.y +CAMERA_HEIGHT/2 -1f  , 2f, 2f);
		objectController.update(delta);
		skeletonController.update(delta);
		controller.update(delta); 
		renderer.render();

	}
	private void updateLevelEnd(){
		if (Gdx.input.justTouched()) {
			game.setScreen(new LevelCompleteScreen(game, levelNumber));
		}	
	}
	private void updateGameOver(){
		if ((time_on_screen + 40  <=  System.currentTimeMillis()) ||Gdx.input.justTouched()) {
			game.setScreen(new GameOverScreen(game, levelNumber));
		}		
	}
	
	public void draw(){
		switch (gameState) {
		case READY:
			presentReady();
			break;
		case RUNNING:
			presentRunning();
			break;
		case PAUSED:
			presentPaused();
			break;
		case LEVEL_END:
			presentLevelEnd();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		
	}
	private void presentLevelEnd() {
		batcher.enableBlending();
		batcher.draw(Assets.levelComplete,  this.cam.position.x - 3*CAMERA_WIDTH/8  , this.cam.position.y, 3*CAMERA_WIDTH/4 ,2*CAMERA_HEIGHT/5);
		batcher.disableBlending();
	}

	private void presentReady() {
		// TODO Auto-generated method stub
		
	}

	private void presentRunning() {
		batcher.draw(Assets.pause,  cam.position.x + CAMERA_WIDTH/2 -1f, cam.position.y +CAMERA_HEIGHT/2 -1f, 1f,1f);
		for(int i=0; i<=controller.getBob().getThrowingStars()-1; ++i){
			batcher.draw(Assets.ninjaStars,  this.cam.position.x - CAMERA_WIDTH/2 +(0.5f*i) , this.cam.position.y +CAMERA_HEIGHT/2 -0.5f, 0.5f,0.5f);
		}

	}
	private void presentPaused() {
		batcher.draw(Assets.pause, cam.position.x -CAMERA_WIDTH/2, cam.position.y -CAMERA_HEIGHT/2,CAMERA_WIDTH, CAMERA_HEIGHT);

		
	}

	private void presentGameOver() {
		time_on_screen= System.currentTimeMillis();
		controller.getBob().setState(BobState.DEAD);
		batcher.enableBlending();
		///replace this with Ninja has Reached his desmisetem.
		batcher.draw(Assets.gameOver,this.cam.position.x - 3*CAMERA_WIDTH/8, cam.position.y , CAMERA_WIDTH*3 /4 , 2*CAMERA_HEIGHT/5 );
		batcher.disableBlending();		
	}

	@Override
	public void render(float delta) {		
		update(delta);
		batcher.begin();
		draw();
		batcher.end();
	}

 @Override
	public void show() {

	}
@Override
	public void resize(int width, int height) {
		//renderer.setSize(width, height);
		this.width=width;
		this.height=height;
	}
 
@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

@Override
	public void pause() {
	//	if (gameState == gameState.RUNNING) gameState = gameState.PAUSED;
//	 	controller.setIdle();
	}

@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}
//* InputProcessor methods ***************************//


@Override
public boolean keyDown(int keycode) {
	if (keycode==Keys.UP)
		controller.climbPressed();
	else if (keycode == Keys.DOWN)
		controller.downPressed();
	else if (keycode == Keys.LEFT)
		controller.leftPressed();
	else if (keycode == Keys.RIGHT)
		controller.rightPressed();
	else if (keycode == Keys.Z){
		controller.bobJump();
	message="Jump was pressed";
	Gdx.app.log("INFO", message);
	}
	else if (keycode == Keys.C)
		controller.throwPressed();
	else if (keycode == Keys.X)
		controller.punchPressed();
	return true;
}

@Override
public boolean keyUp(int keycode) {
	if(keycode==Keys.UP){
		controller.climbReleased();
	}else if (keycode == Keys.DOWN)
		controller.climbDownReleased();
	else if (keycode == Keys.LEFT)
		controller.leftReleased();
	else if (keycode == Keys.RIGHT)
		controller.rightReleased();
	else if (keycode == Keys.Z)
		controller.jumpReleased();
	else if (keycode == Keys.X)
		controller.punchReleased();
	else if (keycode == Keys.C)
		controller.throwReleased();
	else if (keycode == Keys.D)
		renderer.setDebug(!renderer.isDebug());
	return true;
}

@Override
public boolean keyTyped(char character) {
	// TODO Auto-generated method stub
	message= "keyTyped was detected";
	Gdx.app.log("INFO", message);
	return false;
	
}


/// all below methods are for touch based game play
@Override
public boolean fling(float velocityX, float velocityY, int button) {

	return false;
}

@Override
public boolean touchDown(int x, int y, int pointer, int button) {

	if (x < width / 3 && y > height / 2) {
		directionPointer= pointer;
		if(x< width/6){		
			controller.leftPressed();
			message=" driection TOUCH DOWN POINTER IS: " + pointer  + "the action pointer -> " +actionPointer +"directionPointer->"+ directionPointer;
			Gdx.app.log("INFO", message);	

		}else{
			controller.rightPressed();	
			message=" direction TOUCH DOWN POINTER IS: " + pointer  + "the action pointer -> " +actionPointer +"directionPointer->"+ directionPointer;
			Gdx.app.log("INFO", message);	
		//	return true;
			
		}		
		
	}else{
		actionPointer= pointer;
		down_x=x;
		down_y=y;
		message= "Pointer value is" + actionPointer;
	}

	message="TOUCH DOWN POINTER IS: " + pointer  + "the action pointer -> " +actionPointer +"directionPointer->"+ directionPointer;
	Gdx.app.log("INFO", message);	
	return false;
	
}

@Override
public boolean touchUp(int x, int y, int pointer, int button) {

	if ( pointer == directionPointer) {			
		controller.leftReleased();
		controller.rightReleased();	
		directionPointer=-1;
		//return false;				
	}
	if (x < width / 3 && y > height / 2) {
	
		controller.leftReleased();
		controller.rightReleased();	
		directionPointer=-1;
		message= "TOUCHUP pointer is ->"+ pointer + "actionPointer->"+ actionPointer + "directionPointer-> "+ directionPointer;
		Gdx.app.log("INFO", message);
			//return true;				
	}
	
	message= "touchUP was detected";
	Gdx.app.log("INFO", message);
	return false;
	
}


@Override
public boolean touchDragged(int x, int y, int pointer) {
	return false;
}

// @Override
public boolean touchMoved(int x, int y) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean scrolled(int amount) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean mouseMoved(int screenX, int screenY) {

	// TODO Auto-generated method stub
	return false;
}
/// neew to figure out how to integrate swipes into game properly


@Override
public boolean touchDown(float x, float y, int pointer, int button) {
return false;
}

@Override
public boolean tap(float x, float y, int count, int button) {
//	message= "tap was detected";
//	Gdx.app.log("INFO", message);
	if (pauseBounds.contains(x, y)) {
		Assets.playSound(Assets.clickSound);
		gameState = GameState.PAUSED;
		return false;
	}
	if(x >width / 3 && y > height / 2){
		controller.bobPunch();
	}
	return false;
}

@Override
public boolean longPress(float x, float y) {

	return false;
}


@Override
public boolean pan(float x, float y, float deltaX, float deltaY) {
	
	//get initial touchpint of a pan 
	initpan_x= x;
	initpan_y=y;
//	deltapan_x=deltaX;
//	deltapan_y=deltaY;
//	message= "pan was detected initpan_x="+initpan_x +" initpan_y="+ initpan_y + " delta_x =" + deltaX + " delta_y=" +deltaY ;
//	Gdx.app.log("INFO", message);
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean panStop(float x, float y, int pointer, int button) {
	message="swipe not processed";
	if( pointer==actionPointer){
		delta_x= Math.abs(down_x -x);
		delta_y= Math.abs(down_y - y);
		if (delta_x < delta_y ){
			// this is an up or down value 
			if(down_x > x){
				controller.bobPunch();
				message = "SWIPE DOWNWARD " ;			
			}else {
				// swipe up 
				controller.bobJump();
				message = "SWIPE UPWARD " ;
			}			
		}else{
			if(down_y< y){
				controller.throwPressed();
				message=" SWIPE RIGHT";
				//swipe right ward 
			}else{
				controller.throwPressed();
				message=" SWIPE LEFT";
			   // swipe left 	
			}
		}
	}
	Gdx.app.log("INFO", message);
	message="panstop pointer is : " + pointer  + "the action pointer-> " +actionPointer + "directionPointer->" + directionPointer;
	Gdx.app.log("INFO", message);
	actionPointer=-1;
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean zoom(float initialDistance, float distance) {

	// TODO Auto-generated method stub
	return false;
}

public void makeVibrate(){
	Gdx.input.vibrate(100);
}
@Override
public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
		Vector2 pointer1, Vector2 pointer2) {
	// TODO Auto-generated method stub
	return false;
}


}
