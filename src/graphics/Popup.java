package graphics;

import java.awt.Color;
import java.util.ArrayList;

public class Popup {
	
	
	private final float FADE_OUT_TIME=1f;
	
	private String message;
	private Color color, currColor;
	private float stayTime;

	private int lifeCount=0;
	private boolean active=true;
	
	public static ArrayList<Popup> popups;
	
	public Popup(String message, Color color, float stayTime){
		this.message=message;
		this.color=color;
		this.stayTime=stayTime;
		
		this.currColor=color;
		
		if(popups==null)
			popups=new ArrayList<Popup>();
		popups.add(this);
	}
	
	public static void update(){
		if(popups==null)
			return;
		
		for(int i=popups.size()-1;i>=0;i--){
			if(popups.get(i).active)
				popups.get(i).updatePopup();
			else
				popups.remove(i);
		}
		
		//Send data to UI
		String[] ui=new String[popups.size()];
		Color[] colors = new Color[popups.size()];
		for(int i=0;i<popups.size();i++){
			ui[i]=popups.get(i).message;
			colors[i]=popups.get(i).currColor;
		}
		UI.popupUI.setPopups(ui, colors);
	}
	
	private void updatePopup(){
		lifeCount++;
		if(lifeCount>=stayTime*60){active=false; return;}
		
		if(lifeCount/60f<=FADE_OUT_TIME){//Fade in
			currColor=blend(color,new Color(Screen.defaultBackground),(lifeCount/60f)/(FADE_OUT_TIME));
			
		}else if(lifeCount/60f>=stayTime-FADE_OUT_TIME && lifeCount/60f<stayTime){//Fade out
			currColor=blend(color,new Color(Screen.defaultBackground),(float)(stayTime-(lifeCount/60f))/(FADE_OUT_TIME));
			
		}else{//Normal Update
			currColor=color;
		}
	}
	
	//Number of popups = X/Game.scale
	//public void render(){
		
	//}
	
	//Grab from screen, edit to work with alpha out of 1
	private Color blend(Color colorA,Color colorB,float alpha){
		Color c0 = colorA;
		Color c1 = colorB;
		
	    //double totalAlpha = c0.getAlpha() + c1.getAlpha();
	    double weight0 = alpha;//c0.getAlpha() / totalAlpha;
	    double weight1 = 1-alpha;//c1.getAlpha() / totalAlpha;

	    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
	    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
	    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
	    double a = Math.max(c0.getAlpha(), c1.getAlpha());
	    
	    return new Color((int) r, (int) g, (int) b, (int) a);
	}
	
}
