package com.me.mygdxgame.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TopListaDAO {
	private Connection yhteys;
	    
    public TopListaDAO() {
    	String username = "root";
    	String password = "";
        try{
        	Class.forName("com.mysql.jdbc.Driver");
            yhteys = DriverManager.getConnection("jdbc:mysql://localhost:3306/lista", username, password);
        }catch(Exception e){
            e.printStackTrace();}
    }
    
    @Override
    protected void finalize(){
        try{
            if (yhteys!=null)yhteys.close();
        } catch(Exception e){}
    }
    
    public TopListaDTO[] getTulokset(){
        List<TopListaDTO> list = new ArrayList<TopListaDTO>();
        Statement omaStatement = null;
        ResultSet omaRs = null;
        try{
            omaStatement = yhteys.createStatement();
            PreparedStatement sqlSelect = yhteys.prepareStatement("SELECT * FROM level1 ORDER BY aika ASC");
            
            // suorita kysely ja ota vastaan tulokset
            omaRs = sqlSelect.executeQuery();
            while (omaRs.next()) {
                String aika = omaRs.getString("aika");
                String nick = omaRs.getString("nimi");         
                TopListaDTO lista = new TopListaDTO();
                lista.setAika(aika);
                lista.setNick(nick);
                list.add(lista);
            }
        }
        catch(Exception e){e.printStackTrace();}
        finally{
            try{
            if (omaRs!=null) omaRs.close();
            if (omaStatement!=null) omaStatement.close();
            } catch(Exception e){e.printStackTrace();}
        }
        TopListaDTO[] returnArray = new TopListaDTO[list.size()];
        return (TopListaDTO[])list.toArray(returnArray);
    }
	    
    public void lisaaTulos(String nick, String aika){
        Statement omaStatement = null;
        ResultSet omaRs = null;
        try{
            omaStatement = yhteys.createStatement();
            PreparedStatement sqlSelect = yhteys.prepareStatement("INSERT into level1 VALUES(?,?)");
            sqlSelect.setString(1, nick);
            sqlSelect.setString(2, aika);
            // suorita kysely ja ota vastaan tulokset
            sqlSelect.executeUpdate();
        }catch(Exception e){e.printStackTrace();}
        finally{
            try{
            if (omaRs!=null) omaRs.close();
            if (omaStatement!=null) omaStatement.close();
            } catch(Exception e){e.printStackTrace();}
        }
    }
}
