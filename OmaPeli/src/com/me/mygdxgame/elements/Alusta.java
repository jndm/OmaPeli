package com.me.mygdxgame.elements;

import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Alusta extends Rectangle{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4353285105070195936L;
	private Texture metalliTile;
	private Sprite metalli;

	public Alusta(float x, float y, float leveys, float korkeus) {
        this.x = x;
        this.y = y;
        this.width = leveys;
        this.height = korkeus;
        metalliTile = new Texture(Gdx.files.internal("metallitile.png"));
        metalliTile.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        metalli = new Sprite(metalliTile, 32, 32);
    }
    
	public boolean contains(Point2D.Float pt){
		return this.contains(pt.x, pt.y);
	}
	
    public void liiku(float nopeusY, float nopeusX, boolean kameraPaikallaanX, boolean kameraPaikallaanY) {
        /*if(!kameraPaikallaanY)
            this.y -= nopeusY;
        if(!kameraPaikallaanX)
            this.x += nopeusX;
        */
    }
    public Sprite getMetalli(){
    	return metalli;
    }
}