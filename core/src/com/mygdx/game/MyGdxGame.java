package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion down, up, right, left, stand;

	static final int WIDTH = 16;
	static final int HEIGHT = 16;

	static final int DRAW_WIDTH = WIDTH*5;
	static final int DRAW_HEIGHT = HEIGHT*5;

	float x, y, xv, yv;

	static final float MAX_VELOCITY = 500;

	boolean canJump;

	static final float MAX_JUMP_VELOCITY = 2000;
	static final int GRAVITY = -50;
	boolean goUp,goDown,goLeft,goRight = true;


	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture tiles = new Texture("tiles.png");

		TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
		down = grid[6][0];
		 up = grid[6][1];
		 right = grid[6][3];
		 stand = grid [6][2];
		 left = new TextureRegion(right);
		 left.flip(true, false);



	}

	@Override
	public void render () {
		move();

		TextureRegion img;
		if ( yv > 0){
			img = up;

		}else if (yv<0){
			img = down;

		}else if(xv>0){
			img = right;
		}else if (xv<0){
			img =left;
		}else {
			img=stand;
		}


		Gdx.gl.glClearColor(0.5f, .5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, x, y, DRAW_WIDTH,DRAW_HEIGHT);
		batch.end();
	}

	float decelerate(float velocity) {
		float deceleration = 0.95f; // the closer to 1, the slower the deceleration
		velocity *= deceleration;
		if (Math.abs(velocity) < 1) {
			velocity = 0;
		}
		return velocity;
	}





	void move() {

		if (Gdx.input.isKeyPressed(Input.Keys.UP) ) {
			yv = MAX_VELOCITY;

		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

			yv = MAX_VELOCITY * -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = MAX_VELOCITY;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			xv = MAX_VELOCITY * -1;
		}

		y += yv * Gdx.graphics.getDeltaTime();
		x += xv * Gdx.graphics.getDeltaTime();


		yv = decelerate(yv);
		xv = decelerate(xv);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
