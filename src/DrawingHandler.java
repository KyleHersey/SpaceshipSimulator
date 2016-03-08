import java.awt.Color;
import java.awt.Graphics;

/**
 * This is where the Frame outsources its drawing tasks
 *
 */
public class DrawingHandler {

	DrawingHandler(){}
	
	/**
	 * This command draws everything that should be displayed onto the Panel
	 * 
	 * @param g		the graphics object
	 * @param m		the model
	 */
	public void drawIt(Graphics g, Model m){
		
		//draw all the ships.
		for(int i = 0 ; i < m.shipList.size(); i++){
			//ships know their own color. This should be changed to come from the model/factory
			m.shipList.get(i).drawOnGraphics(g);
		}
		
		//draw all the lasers
		g.setColor(new Color(111,207,78));
		for(int i = 0 ; i < m.shotList.size(); i++){
			g.drawLine(	m.shotList.get(i).getSender().xCoord, 
						m.shotList.get(i).getSender().yCoord, 
						m.shotList.get(i).getTarget().xCoord,
						m.shotList.get(i).getTarget().yCoord);
		}
		
		//words at the bottom
		String red = "Red: " + m.getReds();
		String blue = "Blue: " + m.getBlues();
		String ks = "Highest Killstreak: " + m.highestKillstreak;
		g.setColor(Color.BLACK);
		g.drawChars(red.toCharArray(), 0, red.length(), 50, 525);
		g.drawChars(blue.toCharArray(), 0, blue.length(), 150, 525);
		g.drawChars(ks.toCharArray(), 0, ks.length(), 250, 525);
	}
	
}
