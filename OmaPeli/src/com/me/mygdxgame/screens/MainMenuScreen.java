package com.me.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.me.mygdxgame.OmaPeli;

public class MainMenuScreen implements Screen{
	
	private final OmaPeli game;
	private OrthographicCamera camera;
	
	public MainMenuScreen(OmaPeli game){
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "WELCOME TO MAIN MENU!!! ", 100, 150);
		game.font.draw(game.batch, "Klikkaa mistä vain aloittaaksesi!", 100, 100);
		game.font.draw(game.batch, "Paina H nähdäksesi toplistat!", 100, 68);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
		
		if(Gdx.input.isKeyPressed(Keys.H)){
			game.setScreen(new TopListaScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
