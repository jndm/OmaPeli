package com.me.mygdxgame.screens;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.mygdxgame.OmaPeli;
import com.me.mygdxgame.elements.Alusta;
import com.me.mygdxgame.elements.Ammus;
import com.me.mygdxgame.elements.Maali;
import com.me.mygdxgame.elements.Pelihahmo;
import com.me.mygdxgame.elements.Piikki;
import com.me.mygdxgame.elements.Vihollinen;

public class GameScreen implements Screen{
	private final OmaPeli game;

	private Texture hpImage;
	private OrthographicCamera camera;
	
	private final int MAALISSA=1, PAUSE=2, PELISSA=0;
	private int tila;
	
	private float painovoima;
    private ArrayList<Alusta> alustat;
    private Pelihahmo pelihahmo;
    private Array<Piikki> piikit;
    private Array<Vihollinen> viholliset;
    private boolean kameraPaikallaanX, kameraPaikallaanY;
    private long lastPressProcessed;
    private long lastEnemyTime;
	private DecimalFormat df;
	private float stateTime;
	private TextureRegion nykyinenHahmoFrame;
	private boolean hyppyPainettu;
	private Texture tausta;
	private final float KENTAN_LEVEYS = 2048;
	private final float KENTAN_KORKEUS = 1024;
	private long palautumisAika;
	private Maali maali;
	private boolean maalissa;
	private long aika;
	private int sadasOsaSek, kymmenesOsaSek, sek, kymSek;
	private String lapiMenonKesto;

	public GameScreen(final OmaPeli game) {
		this.game = game;
		hyppyPainettu = false;
		maalissa = false;

		hpImage = new Texture(Gdx.files.internal("hp.png"));
		hpImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tausta = new Texture(Gdx.files.internal("tausta.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		tila = PELISSA;
		alustaMaailma();
		df = new DecimalFormat("#.#");		
	}

	@Override
	public void render(float delta) {
		
		switch(tila){
			case PELISSA:
				Gdx.gl.glClearColor(0, 0, 0.4f, 1);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				paivitaKamera();
				
				stateTime += Gdx.graphics.getDeltaTime(); //lasketaan animaatioita varten aikaa
				
				//Virallinen SpriteBatch, jolla piirretään debugtekstit ja pelihahmoanimaatio
				game.batch.setProjectionMatrix(camera.combined);
				game.batch.begin();
				game.batch.draw(tausta,0,0);
				game.batch.draw(pelihahmo.getNykyinenFrame(), pelihahmo.getX(), pelihahmo.getY());
				
				for(Alusta alusta : alustat){
					if(alusta.width >= alusta.height){
						for(int i=0; i<alusta.width/32;i++)
							game.batch.draw(alusta.getMetalli(), alusta.x+(i*32), alusta.y, 32, 32);
					}
					else{
						for(int i=0; i<alusta.height/32;i++)
							game.batch.draw(alusta.getMetalli(), alusta.x, alusta.y+(i*32), 32, 32);
					}
				}
				
				for(Piikki piikki : piikit){
					for(int i=0; i<piikki.getWidth()/32;i++){
						game.batch.draw(piikki.getPiikki(), piikki.getX()+(i*32), piikki.getY(), 32, 32);
					}
				}
				game.font.draw(game.batch, "vY: "+df.format(pelihahmo.getLiikeY())+"", pelihahmo.getX(), pelihahmo.getY());
				game.font.draw(game.batch, "vX: "+df.format(pelihahmo.getLiikeX()), pelihahmo.getX(), pelihahmo.getY()-16);
				game.font.draw(game.batch, "v: "+pelihahmo.vasemmassaSeinassa(), pelihahmo.getX(), pelihahmo.getY()-32);
				game.font.draw(game.batch, "o: "+pelihahmo.oikeassaSeinassa(), pelihahmo.getX(), pelihahmo.getY()-48);
				game.font.draw(game.batch, "lattia: "+pelihahmo.isGrounded(), pelihahmo.getX(), pelihahmo.getY()-64);
				game.font.draw(game.batch, "x: "+pelihahmo.getX(), pelihahmo.getX(), pelihahmo.getY()-80);
				game.font.draw(game.batch, "y: "+pelihahmo.getY(), pelihahmo.getX(), pelihahmo.getY()-96);
				game.batch.end();
				
				
				//Hudia varten luotu peliluokassa oma batch, jota ei aseteta seuraamaan cameran matriisia(?) jolloin sen x on aina 0-800 ja y 0-480 (toimii kuin meidän itse kirjoittama kamera eli siirtää esineitä hahmon mukaan)
				game.hudBatch.begin();
		        for(int i = 0; i < pelihahmo.getHp(); ++i) game.hudBatch.draw(hpImage, 5+i*20, 480-hpImage.getHeight()-5);
		        game.font.draw(game.hudBatch, "fps: "+Gdx.graphics.getFramesPerSecond(), 760 ,480-16);
		        game.font.draw(game.hudBatch, laskeAika(), 700 ,480-32);
		        game.hudBatch.end();
				
		        //ShapeRenderer jolla piirretään alustat ja viholliset
				game.sr.setProjectionMatrix(camera.combined);	
				game.sr.begin(ShapeType.Line);
				game.sr.rect(pelihahmo.getX(), pelihahmo.getY(), pelihahmo.getHalkaisija(), pelihahmo.getHalkaisija());
				game.sr.rect(maali.getX(), maali.getY(), maali.getLEVEYS(), maali.getKORKEUS());
				Iterator<Ammus> ammusIte = pelihahmo.getAmmukset().iterator();
		        while(ammusIte.hasNext()) {
		        	Ammus ammus = (Ammus)ammusIte.next();
		        	game.sr.circle(ammus.getX(), ammus.getY(), ammus.getHalkaisija()/2.0f);
		        }
		        game.sr.end();
		
				game.sr.begin(ShapeType.Filled);
		
				Iterator<Vihollinen> vihuIte = viholliset.iterator();
				while(vihuIte.hasNext()){
		        	Vihollinen vihu = vihuIte.next();
					game.sr.rect(vihu.x, vihu.y, vihu.width, vihu.height, new Color(Color.BLUE), new Color(Color.RED), new Color(Color.BLUE), new Color(Color.RED));
				}
		        game.sr.end();
		        
				kasitteleNappaimet();
		
				paivitaMaailma();
				break;
		
			case MAALISSA:
				game.setScreen(new MainMenuScreen(game));
				game.dao.lisaaTulos("seppo", lapiMenonKesto);
				dispose();
				break;
				
			case PAUSE:
	}
}

	private void kasitteleNappaimet() {
		if(Gdx.input.isKeyPressed(Keys.SPACE) && !hyppyPainettu){
        	pelihahmo.hyppaa();
        	hyppyPainettu=true;
        }
		else if(!Gdx.input.isKeyPressed(Keys.SPACE))hyppyPainettu=false;
		
        if(Gdx.input.isKeyPressed(Keys.Z)){
            if(TimeUtils.millis() - lastPressProcessed > 100) {
                pelihahmo.ammu();
                lastPressProcessed = TimeUtils.millis();
            }
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)){
	        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
	            pelihahmo.liikuX(1, stateTime);
	        }
	        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
	            pelihahmo.liikuX(-1, stateTime);
	        }
        }else{
        	pelihahmo.liikuX(0, stateTime);
        }
        if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
        	pelihahmo.sprinttaa(true);
        }
        else pelihahmo.sprinttaa(false);
        
        if(Gdx.input.isKeyPressed(Keys.Z) && !pelihahmo.getDash())
        	pelihahmo.dash();
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
	
	private void paivitaMaailma(){
        ArrayList<Ammus> ammukset = pelihahmo.getAmmukset();
   
        Iterator<Vihollinen> iteVihu = viholliset.iterator();
        while(iteVihu.hasNext()){
            Vihollinen vihu = iteVihu.next();
            vihu.liiku(painovoima, pelihahmo.getLiikeY(), pelihahmo.getLiikeX(), kameraPaikallaanX, kameraPaikallaanY);   //Vihollisten liikutus
        }
        
        Iterator<Ammus> ammusIte = ammukset.iterator();
        while(ammusIte.hasNext()){
            ammusIte.next().liiku(pelihahmo.getLiikeY(), pelihahmo.getLiikeX(), kameraPaikallaanX, kameraPaikallaanY);   //Ammusten liikutus
        }
        
        pelihahmo.liiku(painovoima, kameraPaikallaanX, kameraPaikallaanY); //Pelihahmon Liikutus
        
        // asetetaan hahmo alustalle jos mahdollista

        this.pelihahmo.yritaKayttaaAlustaa(alustat);
        for (Alusta alusta : alustat){
            iteVihu = viholliset.iterator();
            while(iteVihu.hasNext())
                iteVihu.next().yritaKayttaaAlustaa(alusta);
            
            ammusIte = ammukset.iterator();
            while(ammusIte.hasNext()){
                if(ammusIte.next().tarkistaTormays(alusta)){
                	ammusIte.remove();
                }
            }
        }
        
        iteVihu = viholliset.iterator();
        while(iteVihu.hasNext()){
            Vihollinen vihu = iteVihu.next();
            
            ListIterator<Ammus> iteAmmus = ammukset.listIterator();
            while(iteAmmus.hasNext()){
                if(iteAmmus.next().tarkistaTormaysVihu(vihu)){
                    //this.pelihahmo.poistaAmmus(iterator);
                    vihu.setHp(vihu.getHp()-1);
                    if(vihu.getHp()==0){
                        iteVihu.remove();
                    }
                    iteAmmus.remove();
                }
            }
            
            if(pelihahmo.tarkistaVihuTormays(vihu)){
                iteVihu.remove();
                pelihahmo.setHp(pelihahmo.getHp()-1);
            }
        }
        
        //tarkistetaan osuuko pelaaja piikkeihin
        for(Piikki piikki : piikit){
        	if(TimeUtils.millis() - palautumisAika > 1000 && pelihahmo.tarkistaPiikitTormays(piikki)){
        		pelihahmo.setHp(pelihahmo.getHp()-1);
        		palautumisAika = TimeUtils.millis();
        	}
        }
        
        //tarkistetaan osuuko pelaaja maaliin
        if(pelihahmo.tarkistaMaaliTormays(maali)){
        	tila = MAALISSA;
        }
        
        // nopeutetaan peliä 600 kierroksen välein (600 kierrosta on noin 
        // 10 sekuntia)
        if (TimeUtils.millis() - lastEnemyTime > 5000)
        	spawnEnemy();
	}
	
	private void spawnEnemy(){
		Rectangle vihollinen = new Vihollinen();
		vihollinen.x = MathUtils.random(alustat.get(0).x + alustat.get(0).width, alustat.get(1).x - 16);
		vihollinen.y = MathUtils.random(alustat.get(0).y + alustat.get(0).height, alustat.get(2).y + alustat.get(2).height);
		vihollinen.width = 16;
		vihollinen.height = 16;
		viholliset.add((Vihollinen)vihollinen);
		lastEnemyTime = TimeUtils.millis();
	}
	
	private void alustaMaailma(){
		painovoima = -15f;

        alustat = new ArrayList<Alusta>();
        
        alustat.add(new Alusta(1600, -20, 32, 640));
        alustat.add(new Alusta(1300, 72, 32, 600)); 
        alustat.add(new Alusta(0, 0, 32, 600));
        
        //kentän rajat (kenttä 2048x1048)
        alustat.add(new Alusta(2016, 32, 32 ,960));
        alustat.add(new Alusta(0, 32, 32, 960));
        alustat.add(new Alusta(0, 0, 2048, 32));
        alustat.add(new Alusta(0, 992, 2048, 32));
        
        alustat.add(new Alusta(64, 512, 512, 32));
        alustat.add(new Alusta(192, 384, 512, 32));
        alustat.add(new Alusta(64, 256, 512, 32));
        alustat.add(new Alusta(192, 128, 512, 32));
        viholliset = new Array<Vihollinen>();

        pelihahmo = new Pelihahmo(50, 32, 32);
        
        piikit = new Array<Piikki>();
        
        piikit.add(new Piikki(320,32,160,32));
        
        maali = new Maali(900, 32);
	}
	
	private void paivitaKamera(){
		if(pelihahmo.getY() < Gdx.graphics.getHeight()/2 && (pelihahmo.getX() < Gdx.graphics.getWidth()/2))	//vasen ala
			camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		else if(pelihahmo.getY() < Gdx.graphics.getHeight()/2 && pelihahmo.getX() > (KENTAN_LEVEYS-Gdx.graphics.getWidth()/2))	//oikea ala 
			camera.position.set(KENTAN_LEVEYS-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		else if(pelihahmo.getY() > KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2) && (pelihahmo.getX() < Gdx.graphics.getWidth()/2))	//vasen yla
			camera.position.set(Gdx.graphics.getWidth()/2, KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2), 0);
		else if(pelihahmo.getY() > KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2) && pelihahmo.getX() > (KENTAN_LEVEYS-Gdx.graphics.getWidth()/2))	//oikea yla
			camera.position.set(KENTAN_LEVEYS-Gdx.graphics.getWidth()/2, KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2), 0);
		else if(pelihahmo.getX() > (KENTAN_LEVEYS-Gdx.graphics.getWidth()/2))						//oikea
			camera.position.set(KENTAN_LEVEYS-Gdx.graphics.getWidth()/2, pelihahmo.getY(), 0);	
		else if(pelihahmo.getY() < Gdx.graphics.getHeight()/2)								//ala
			camera.position.set(pelihahmo.getX() ,Gdx.graphics.getHeight()/2, 0);		
		else if(pelihahmo.getX() < Gdx.graphics.getWidth()/2)								//vasen
			camera.position.set(Gdx.graphics.getWidth()/2 , pelihahmo.getY(), 0);	
		else if(pelihahmo.getY() > KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2))			//yla
			camera.position.set(pelihahmo.getX(), KENTAN_KORKEUS-(Gdx.graphics.getHeight()/2), 0);	
		else
			camera.position.set(pelihahmo.getX(),pelihahmo.getY() , 0);
		
		camera.update();

	}
	
	private String laskeAika(){
		if(System.currentTimeMillis() - aika > 10){
            sadasOsaSek++;
            if(sadasOsaSek == 10){
            	kymmenesOsaSek++;
            	sadasOsaSek = 0;
            	if(kymmenesOsaSek == 10){
            		sek++;
            		kymmenesOsaSek = 0;
            		if(sek==10){
                        kymSek++;
                        sek = 0;
                    }
            	}
            }
            aika = System.currentTimeMillis();
         }
		
		lapiMenonKesto = ""+kymSek+""+sek+":"+kymmenesOsaSek+""+sadasOsaSek;
		return lapiMenonKesto;
	}
}
