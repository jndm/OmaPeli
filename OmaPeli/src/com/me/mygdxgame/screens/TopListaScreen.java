package com.me.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.OmaPeli;
import com.me.mygdxgame.mysql.TopListaDAO;
import com.me.mygdxgame.mysql.TopListaDTO;

public class TopListaScreen implements Screen{
	
	private final OmaPeli game;
	private OrthographicCamera camera;
	private TopListaDAO dao;
	private Array<Integer> rank; 
	private Array<String> nimi, aika;
	
	public TopListaScreen(OmaPeli game){
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		rank = new Array<Integer>();
		nimi = new Array<String>();
		aika = new Array<String>();
		
		lataaLista();
	}
	
	public void piirraTopLista(){
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		game.sr.setProjectionMatrix(camera.combined);
		
		//L‰pin‰kyvyys laitettu p‰‰lle ja piirret‰‰n neliˆ johon toplistat piirret‰‰n.
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		game.sr.begin(ShapeType.Filled);
		game.sr.setColor(0, 0, 1, .3f);
		game.sr.rect(32,32,game.width-64,game.height-64);
		game.sr.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		for(int i=0; i<nimi.size; i++){
			game.font.draw(game.batch, (i+1)+". "+nimi.get(i)+" "+aika.get(i), 64+(32*i), 416-(32*i));
		}
		
		game.font.draw(game.batch, "WELCOME TO MAIN MENU!!! ", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();
	}
	
	public void lataaLista(){
		TopListaDTO[] lista = game.dao.getTulokset();
		for(int i=0; i<lista.length; i++){
			nimi.add(lista[i].getNick());
			aika.add(lista[i].getAika());
		}		
	}
	
	@Override
	public void render(float delta) {	
		if (Gdx.input.isKeyPressed(Keys.M)) {
			game.setScreen(new MainMenuScreen(game));
			dispose();
		}
		
		piirraTopLista();
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
