package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricStaggeredTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.Random;
import java.util.zip.ZipEntry;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    TextureRegion down, up, right, left, stand, walkOpo, runLeft, zoombiewalk,
            zoombieStand, tree,rock;

    static final int WIDTH = 16;
    static final int HEIGHT = 16;

    static final int DRAW_WIDTH = WIDTH * 3;
    static final int DRAW_HEIGHT = HEIGHT * 3;

    float x, y, xv, yv, zombieX, zombieY;


    static final float MAX_VELOCITY = 100;
    static final float SPEED_VELOCITY = MAX_VELOCITY * 2;


    //boolean canJump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    boolean fast;

    static final int GRAVITY = -50;
    //boolean goUp,goDown,goLeft,goRight = true;
    Animation walk, walkLeft, zombie;
    float time;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;


    public MyGdxGame() {
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        Texture tiles = new Texture("tiles.png");

        //creates the figure
        TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
        down = grid[6][0];
        up = grid[6][1];
        right = grid[6][3];
        stand = grid[6][2];
        zoombiewalk = grid[6][7];
        zoombieStand = grid[6][6];
        rock = grid [7][5];
        tree = new TextureRegion(tiles,0,8,WIDTH,HEIGHT);


        walkOpo = new TextureRegion(stand);
        walkOpo.flip(true, false);
        runLeft = new TextureRegion(right);
        runLeft.flip(true, false);

        left = new TextureRegion(right);
        left.flip(true, false);
        walk = new Animation(0.2f, grid[6][2], grid[6][3]);
        walkLeft = new Animation(0.2f, walkOpo, runLeft);


        zombie = new Animation(0.2f, zoombieStand, zoombiewalk);

        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1/8f);
        camera = new OrthographicCamera();


    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime();
        move();

        TextureRegion img;


        if (yv > 0) {
            img = up;
        } else if (yv < 0) {
            img = down;
        } else if (xv > 0) {
            img = walk.getKeyFrame(time, true);
        } else if (xv < 0) {
            img = walkLeft.getKeyFrame(time, true);
        } else {
            img = stand;
        }


        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.setView(camera);
        camera.setToOrtho(true,50,50);
        renderer.render();

        batch.begin();
        batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);

        batch.draw(tree,300,400,DRAW_WIDTH,DRAW_HEIGHT);
        batch.draw(tree,500,200,DRAW_WIDTH,DRAW_HEIGHT);
        batch.draw(tree,25,300,DRAW_WIDTH,DRAW_HEIGHT);
        batch.draw(tree,30,100,DRAW_WIDTH,DRAW_HEIGHT);
        batch.end();
        zombie();
    }


    public void zombie() {
        zombieMove();
        Random random = new Random();
        //random.nextFloat(zombieX);
        zombieX ++;
        zombieY ++;

        TextureRegion img;
        img = zombie.getKeyFrame(time, true);
        batch.begin();
        batch.draw(img, zombieX, zombieY, DRAW_WIDTH, DRAW_HEIGHT);

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

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yv = MAX_VELOCITY;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            yv = SPEED_VELOCITY;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            yv = SPEED_VELOCITY * -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            xv = SPEED_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            xv = SPEED_VELOCITY * -1;
        }

        y += yv * Gdx.graphics.getDeltaTime();
        x += xv * Gdx.graphics.getDeltaTime();


        yv = decelerate(yv);
        xv = decelerate(xv);

        outOfBound();
    }

    public void outOfBound() {
        if (x < 0) {
            x = Gdx.graphics.getWidth();
        }
        if (x > Gdx.graphics.getWidth()) {
            x = 0;
        }

        if (y < 0) {
            y = Gdx.graphics.getHeight();

        }
        if (y > Gdx.graphics.getHeight()) {
            y = 0;
        }
    }

    public void zombieMove() {
        if (zombieX < 0) {
            zombieX = Gdx.graphics.getWidth();
        }
        if (zombieX > Gdx.graphics.getWidth()) {
            zombieX = 0;
        }

        if (zombieY < 0) {
            zombieY = Gdx.graphics.getHeight();

        }
        if (zombieY > Gdx.graphics.getHeight()) {
            zombieY = 0;
        }

    }


    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        map.dispose();
        renderer.dispose();
    }
}
