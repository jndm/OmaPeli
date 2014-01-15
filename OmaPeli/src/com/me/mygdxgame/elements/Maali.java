package com.me.mygdxgame.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Maali {
	private final float LEVEYS=32, KORKEUS=64;
	private float x,y;
	private Texture maaliTekstuuri;
	private Sprite maali;
	
	public Maali(float x, float y) {
        this.x = x;
        this.y = y;
        //maaliTekstuuri = new Texture(Gdx.files.internal("maali.png"));
        //maali = new Sprite(maaliTekstuuri, 32, 32);
    }

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Sprite getMaali() {
		return maali;
	}

	public float getLEVEYS() {
		return LEVEYS;
	}

	public float getKORKEUS() {
		return KORKEUS;
	}
	
	
}
