package com.me.mygdxgame.elements;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import java.awt.image.BufferedImage;
import com.badlogic.gdx.utils.TimeUtils;

public class Pelihahmo {
	
    private float alkuX;
    private float alkuY;
    private float x;
    private float y;
    
    private float leveys;
    private float korkeus;
    private int hyppynro;
    
    private int katseenSuunta;
    
    private int hp;
    private float liikeY;
    private float liikeX;
    private float kiihtyvyys;
    private final float kitka = 20f;
    private final float ivast = 10f;
    private final float seinaKitka = 10f;
    private boolean grounded, katossa;
    private boolean sprint;
    private boolean vasemmassaSeinassa, oikeassaSeinassa;
    
    private boolean dash, dashinJalkeenMaassa;
    private boolean voiDashataUudelleen;
    private long viimeDash;
    
    private static final int PYSTY_RIVIT = 4;
	private static final int POIKKI_RIVIT = 4;
	private Animation oikeakavely, vasenkavely, seisominen;
	private Texture animaatioSheet;
	private TextureRegion[] vasenKavelyFramet, oikeaKavelyFramet;
	private TextureRegion seisominenFrame, nykyinenFrame;
	

	private ArrayList<Ammus> ammukset;
    
    
    public Pelihahmo(float x, float y, float halkaisija) {
        this.alkuX = x;
        this.alkuY = y;
        
        this.x = alkuX;
        this.y = alkuY;
        this.leveys = halkaisija;
        this.korkeus = halkaisija;
        
        this.liikeY = 0;
        this.kiihtyvyys = 0;
        this.katseenSuunta=1;
        this.hp=5;
        this.ammukset = new ArrayList<Ammus>();
        sprint = false;
        
        //Ladataan animaatiota varten spriteSheet ja eritell‰‰n siit‰ k‰vely ja seisomis kuvat sek‰ luodaan niist‰ Animaatio-luokan ilmentym‰t
        animaatioSheet = new Texture(Gdx.files.internal("hahmo.png")); //Ladattavan kuvan leveyden ja korkeuden pit‰‰ olla 2 potenssi t. OpenGL
        TextureRegion[][] tmp = TextureRegion.split(animaatioSheet, animaatioSheet.getWidth()/PYSTY_RIVIT, animaatioSheet.getWidth()/POIKKI_RIVIT);
        vasenKavelyFramet = new TextureRegion[3];
        for(int i=0; i<3;i++){
        	vasenKavelyFramet[i] = tmp[1][i];
        }
        oikeaKavelyFramet = new TextureRegion[3];
        for(int i=0; i<3;i++){
        	oikeaKavelyFramet[i] = tmp[2][i];
        }
        seisominenFrame = new TextureRegion();
        seisominenFrame = tmp[0][1];
        
        vasenkavely = new Animation(0.20f, vasenKavelyFramet);
        oikeakavely = new Animation(0.20f, oikeaKavelyFramet);
        seisominen = new Animation(0.20f, seisominenFrame);
        
        nykyinenFrame = seisominenFrame;
        
    }
    
    public Pelihahmo(float x, float y, float leveys, float korkeus) {
    	
        this.alkuX = x;
        this.alkuY = y;
        
        this.x = alkuX;
        this.y = alkuY;
        this.leveys = leveys;
        this.korkeus = korkeus;
        
        this.liikeY = 0;
        this.kiihtyvyys = 0;
        this.katseenSuunta=1;
        this.hp=5;
        this.ammukset = new ArrayList<Ammus>();
        
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHalkaisija() {
        return leveys;
    }

    public void liiku(float painovoima, boolean kameraPaikallaanX, boolean kameraPaikallaanY) {
        //Painovoima vaikuttaa kokoajan alasp‰in hahmoon jos ollaan sein‰ss‰ otetaan huomioon sein‰n kitka
    	//Painovoima ei vaikuta kun dashataan
    	if(!dash){
	    	if((vasemmassaSeinassa || oikeassaSeinassa ) && liikeY < 50)
	    		this.liikeY = this.liikeY + painovoima + seinaKitka;
	    	else
	    		this.liikeY = this.liikeY + painovoima;
    	}
    	
        if(!dash)
        	this.liikeX = this.liikeX + kiihtyvyys; //Kiihdytet‰‰n nopeutta		
        //Jos maassa v‰hennet‰‰n liikeX:st‰ kitka
        if(grounded){
	        if(merkki(liikeX) != merkki(liikeX-kitka)){		//jos liikeX etumerkki muuttuu miinukseksi kitkaa v‰hennett‰ess‰ liike pys‰htyy
	            liikeX=0;
	        }else{
	            this.liikeX = this.liikeX - merkki(liikeX)*kitka;	//jos liikett‰ viel‰ j‰ljell‰ v‰hennet‰‰n siit‰ kitkan arvo
	        }
        }
        //Jos ei maassa hidastuu liikeX ilmannopeus (joka on pienempi kuin kitka [no shit?])
        else{
	        if(merkki(liikeX) != merkki(liikeX-ivast)){		//jos liikeX etumerkki muuttuu miinukseksi ilmanvastusta v‰hennett‰ess‰ liike pys‰htyy
	            liikeX=0;
	        }else{
	            this.liikeX = this.liikeX - merkki(liikeX)*ivast;	//jos liikett‰ viel‰ j‰ljell‰ v‰hennet‰‰n siit‰ ilmanvastuksen arvo
	        }
        }
        if(!dash){
	        if(sprint){											//Jos hahmo sprinttaa (shift pohjassa) max nopeus on kovempi
	        	if(Math.abs(liikeX) > 500){
		            this.liikeX = merkki(liikeX)*500;
		        }
	        }
	        else{
		        if(Math.abs(liikeX) > 300){
		            this.liikeX = merkki(liikeX)*300;
		        }
	        }
        }
        else if(TimeUtils.millis() - viimeDash > 500)
        	dash = false;
        
        //lis‰t‰‰n liike hahmon y:n arvoon ja otetaan huomioon framen nopeus
        this.y = this.y + (this.liikeY * Gdx.graphics.getDeltaTime());
        
        if(this.liikeY < -500f) {	//max putoamisnopeus
            this.liikeY = -500f;
        }
        
        //lis‰t‰‰n liike hahmon x:n arvoon ja otetaan huomioon framen nopeus
        this.x = this.x + (liikeX * Gdx.graphics.getDeltaTime());

    }
    
    public void yritaKayttaaAlustaa(ArrayList<Alusta> alustat) {
        Point2D.Float x1=new Point2D.Float(x + 1, y);																		//  .-x6-------x5-.
        Point2D.Float x2=new Point2D.Float(x + leveys - 1, y);																//  x7			  x4
        Point2D.Float x3=new Point2D.Float(x + leveys, y + 1);																//  |		 	  |
        Point2D.Float x4=new Point2D.Float(x + leveys, y + korkeus-1);														//  |		      |
        Point2D.Float x5=new Point2D.Float(x + leveys-1, y + korkeus);														//  |             |
        Point2D.Float x6=new Point2D.Float(x + 1, y + korkeus);																//  |		  	  |
        Point2D.Float x7=new Point2D.Float(x, y + korkeus-1);																//  x8      	  x3
        Point2D.Float x8=new Point2D.Float(x, y + 1);																		//  '-x1-------x2-'
        
        vasemmassaSeinassa = false;
        oikeassaSeinassa = false;
        grounded = false;
        
        for(Alusta alusta : alustat){     	
	        if((alusta.contains(x1) || alusta.contains(x2)) && y + (Math.abs(liikeY*Gdx.graphics.getDeltaTime()) + 2) > alusta.y + alusta.height ) {		//lattia
	            y = alusta.y + alusta.height;																					//
	            if(liikeY < 0) {																									//
	                liikeY = 0;																									//
	            }																												//
	            hyppynro = 0;																									//
	            grounded = true;																								//
	            dashinJalkeenMaassa = true;
	        }
	        
	        if((alusta.contains(x3) || alusta.contains(x4)) && x + leveys - (Math.abs(liikeX*Gdx.graphics.getDeltaTime()) + 2) < alusta.x) {				//oikea sein‰
	            x = alusta.x - leveys;
	            liikeX = 0;
	            kiihtyvyys = 0;
	            oikeassaSeinassa = true;
	            dashinJalkeenMaassa = true;
	            dash = false;
	        }
	        
	        if((alusta.contains(x7) || alusta.contains(x8)) && x + (Math.abs(liikeX*Gdx.graphics.getDeltaTime()) + 2) > alusta.x + alusta.width ) {	//vasen sein‰
	            x = alusta.getX() + alusta.width;
	            liikeX = 0;
	            kiihtyvyys = 0;
	            vasemmassaSeinassa = true;
	            dashinJalkeenMaassa = true;
	            dash = false;
	        }  
	       	
	        if((alusta.contains(x5) || alusta.contains(x6)) && y + korkeus - (Math.abs(liikeY*Gdx.graphics.getDeltaTime()) + 2) < alusta.y ) {				//katto
	            y = alusta.getY() - this.korkeus;																				//
	            if(liikeY > 0) {			 																					//
	            	liikeY = 0;																									//
	            }																												//
	        }    
        }
    }
    
    public boolean tarkistaVihuTormays(Vihollinen vihu){
        Rectangle2D r1 = new Rectangle2D.Float(vihu.x, vihu.y, vihu.width, vihu.height);
        Rectangle2D r2 = new Rectangle2D.Float(x, y, leveys, korkeus);
        
        if(r1.intersects(r2)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean tarkistaPiikitTormays(Piikki piikki){
    	Rectangle2D r1 = new Rectangle2D.Float(piikki.getX(), piikki.getY(), piikki.getWidth(), piikki.getHeight());
        Rectangle2D r2 = new Rectangle2D.Float(x, y, leveys, korkeus);
        
        if(r1.intersects(r2)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean tarkistaMaaliTormays(Maali maali){
    	Rectangle2D r1 = new Rectangle2D.Float(maali.getX(), maali.getY(), maali.getLEVEYS(), maali.getKORKEUS());
        Rectangle2D r2 = new Rectangle2D.Float(x, y, leveys, korkeus);
        
        if(r1.intersects(r2)){
            return true;
        }else{
            return false;
        }
    }

    
    public void hyppaa() {
        if(hyppynro==0 && grounded){
            liikeY = 500;
            grounded = false;
            ++hyppynro;
        }
        else if(vasemmassaSeinassa && !grounded){
        	liikeY = 400;
        	liikeX = 500;
        	vasemmassaSeinassa = false;
        	katseenSuunta = 1;
        }
        else if(oikeassaSeinassa && !grounded){
        	liikeY = 400;
        	liikeX = -500;
        	oikeassaSeinassa = false;
        	katseenSuunta = -1;
        }
    }
    
    public void ammu(){
        ammukset.add(new Ammus(x, y, leveys, korkeus, katseenSuunta));
    }
    
    public void poistaAmmus(Ammus i){
        ammukset.remove(i);
    }
    
    public void liikuX(int k, float stateTime){
    	//Metodi kutsutaan kun painetaan liikkumisnappia (nuolin‰pp‰imet atm)
    	if(sprint)
    		kiihtyvyys = 50*k;
    	else 
    		kiihtyvyys = 35*k;
        
        if(k!=0)
            katseenSuunta=k;
        
        //Animaation framejen asetus katseensuunnan mukaan.
        if(k == -1){
        	nykyinenFrame = vasenkavely.getKeyFrame(stateTime, true);
        	oikeassaSeinassa = false;
        }
        else if(k == 1){
        	nykyinenFrame = oikeakavely.getKeyFrame(stateTime, true);
        	vasemmassaSeinassa=false;
        }
        else if(k == 0)
        	nykyinenFrame = seisominenFrame;
    }
    
    public void reset(){
        this.x=this.alkuX;
        this.y=this.alkuY;
    }
    
    public float getLiikeY() {
        return liikeY;
    }

    public float getLiikeX() {
        return liikeX;
    }
    
    public void setLiikeY(float liikeY) {
        this.liikeY=liikeY;
    }

    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public boolean isGrounded() {
		return grounded;
	}

    public ArrayList<Ammus> getAmmukset() {
        return ammukset;
    }
    
    private float merkki(float f){
        return (f == 0.0f || isNaN(f)) ? f : copySign(1.0f, f);
    }

	private static boolean isNaN(float f) {
	    return (f != f);
	}
	
	private static float copySign(float magnitude, float sign) {
	    return rawCopySign(magnitude, (isNaN(sign) ? 1.0f : sign));
	}

	private static float rawCopySign(float magnitude, float sign) {
	    return Float.intBitsToFloat((Float.floatToRawIntBits(sign)
	            & (FloatConsts.SIGN_BIT_MASK))
	            | (Float.floatToRawIntBits(magnitude)
	            & (FloatConsts.EXP_BIT_MASK
	            | FloatConsts.SIGNIF_BIT_MASK)));
	}

	static class FloatConsts {
	    public static final int SIGN_BIT_MASK = -2147483648;
	    public static final int EXP_BIT_MASK = 2139095040;
	    public static final int SIGNIF_BIT_MASK = 8388607;
	}
	
	public TextureRegion getNykyinenFrame(){
		return nykyinenFrame;
	}
	
	public void sprinttaa(boolean b){
		sprint = b;
	}
	
	public void dash(){
		if(TimeUtils.millis() - viimeDash > 1000 && dashinJalkeenMaassa && !oikeassaSeinassa && !vasemmassaSeinassa){
			voiDashataUudelleen = true;
		}
		
		if(voiDashataUudelleen){
			if(katseenSuunta == 1){
				dash = true;
				liikeX = 500;
				liikeY = 0;
			}
			else if(katseenSuunta == -1){
				dash = true;
				liikeX = -500;
				liikeY=0;
			}
			viimeDash = TimeUtils.millis();
			voiDashataUudelleen = false;
			dashinJalkeenMaassa = false;
		}
	}
	
	public boolean vasemmassaSeinassa(){
		return vasemmassaSeinassa;
	}

	public boolean oikeassaSeinassa() {
		return oikeassaSeinassa;
	}
	
	public boolean getDash(){
		return dash;
	}
}