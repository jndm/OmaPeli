package com.me.mygdxgame.elements;

import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Piikki {
	
	private float x, y, width, height;
	private Texture piikitTile;
	private Sprite piikki;

	public Piikki(float x, float y, float leveys, float korkeus) {
        this.x = x;
        this.y = y;
        this.width = leveys;
        this.height = korkeus;
        piikitTile = new Texture(Gdx.files.internal("piikit.png"));
        piikitTile.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        piikki = new Sprite(piikitTile, 32, 32);
    }

    public Sprite getPiikki(){
    	return piikki;
    }

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

    
}
