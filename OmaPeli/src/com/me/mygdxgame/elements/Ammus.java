package com.me.mygdxgame.elements;

import java.awt.geom.Point2D;

import com.badlogic.gdx.Gdx;

public class Ammus {
    
    private float x;
    private float y;;
    
    private float halkaisija;
    
    private float liikeX;
    
    public Ammus(float x, float y, float hahmLeveys, float hahmKorkeus, int k){
        this.halkaisija=5;
        
        this.x=x-(halkaisija/2)+((k+1)/2)*hahmLeveys;
        this.y=y+(hahmKorkeus)/2-(halkaisija/2);
        
        this.liikeX=200*k;
    }
    
    public void liiku(float hahmonLiikeY, float hahmonLiikeX, boolean kameraPaikallaanX, boolean kameraPaikallaanY) {
        this.x += liikeX*Gdx.graphics.getDeltaTime();
        /*
        if(!kameraPaikallaanX)
            this.x -= hahmonLiikeX;
        if(!kameraPaikallaanY)
            this.y -= hahmonLiikeY;
        */
    }

    public boolean tarkistaTormays(Alusta vihollinen) {
        Point2D.Float north = new Point2D.Float(x + (halkaisija/2.0f), y + halkaisija);
        Point2D.Float west = new Point2D.Float(x, y + (halkaisija/2.0f));
        Point2D.Float south = new Point2D.Float(x + (halkaisija/2.0f), y);
        Point2D.Float east = new Point2D.Float(x + halkaisija, y + (halkaisija/2.0f));
        if(vihollinen.contains(north) || vihollinen.contains(east)  || vihollinen.contains(south)  || vihollinen.contains(west)) {
            return true;
        }else{
            return false;
        }
    }
    
    public boolean tarkistaTormaysVihu(Vihollinen vihollinen){
        Point2D.Float north = new Point2D.Float(x + (halkaisija/2.0f), y + halkaisija);
        Point2D.Float west = new Point2D.Float(x, y + (halkaisija/2.0f));
        Point2D.Float south = new Point2D.Float(x + (halkaisija/2.0f), y);
        Point2D.Float east = new Point2D.Float(x + halkaisija, y + (halkaisija/2.0f));
        if(vihollinen.contains(north) || vihollinen.contains(east)  || vihollinen.contains(south)  || vihollinen.contains(west)) {
            return true;
        }else{
            return false;
        }
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHalkaisija() {
        return halkaisija;
    }
}
