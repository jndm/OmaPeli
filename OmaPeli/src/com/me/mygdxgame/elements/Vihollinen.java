package com.me.mygdxgame.elements;

import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Vihollinen extends Rectangle{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1574282012645253619L;
	
	private float alkuX;
    private float alkuY;
    
    private int hp;
    private float liikeY;
    
    public Vihollinen(float xx, float yy, float leveys, float korkeus) {
        alkuX = xx;
        alkuY = yy;
        x = alkuX;
        y = alkuY;
        width = leveys;
        height = korkeus;
        liikeY = 0f;
        hp = 2;
    }

    public Vihollinen() {
    	this(0,0,0,0);
	}
    
    public void liiku(float painovoima, float hahmonLiikeY, float hahmonLiikeX, boolean kameraPaikallaanX, boolean kameraPaikallaanY) {
        liikeY = liikeY - (painovoima * Gdx.graphics.getDeltaTime());
        
        if(liikeY < -8) {
            liikeY = -8;
        }
        y -= liikeY;
        
        /*
        if(!kameraPaikallaanY)
            y -= hahmonLiikeY;
        
        if(!kameraPaikallaanX)
            x -= hahmonLiikeX;
        */
    }
    
    public void yritaKayttaaAlustaa(Alusta alusta) {
        Point2D.Float x1=new Point2D.Float(x, y);			//   |   |
        Point2D.Float x2=new Point2D.Float(x + width, y);	//  x1--x2
        
        if((alusta.contains(x1) || alusta.contains(x2)) && y + (Math.abs(liikeY) + 2) > alusta.getY() + alusta.height) {
            y = alusta.y + alusta.height;
            liikeY = 0;
        }
        
        Point2D.Float xx1 = new Point2D.Float(x, y + (height/2));
        Point2D.Float xx2 = new Point2D.Float(x + width, y + (height/2));
        
        if(alusta.contains(xx2)) {
            x = alusta.getX() - width - 1;
        }else if(alusta.contains(xx1)){
            x = alusta.x + alusta.width + 1;
        }
    }
    
	public boolean contains(Point2D.Float pt){
		return this.contains(pt.x, pt.y);
	}

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public float getLiikeY() {
        return liikeY;
    }
    
    public void setLiikeY(float liikeY) {
        this.liikeY=liikeY;
    }
    
    public Vihollinen getVihu(){
        return this;
    }
}