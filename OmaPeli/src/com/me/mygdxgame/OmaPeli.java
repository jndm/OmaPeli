package com.me.mygdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.mygdxgame.mysql.TopListaDAO;
import com.me.mygdxgame.screens.MainMenuScreen;

public class OmaPeli extends Game {
	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	public BitmapFont font;
	public ShapeRenderer sr;
	public final float width = 800, height = 480;
	public final float ASPECT_RATIO = 800/480;
	public TopListaDAO dao;
	
	@Override
	public void create() {
		dao = new TopListaDAO();
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		font.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	
}
