import java.awt.Color;
import java.awt.Graphics;

/**
 * All the ships on the screen must extend this abstract class
 *
 */
public abstract class Ship {

	static int count = 0;	//currently used for ship ID generation
	
	public int shipID;		//ship ID
	public int team;		//which team/color
	
	public int xCoord;		//x-coordinate
	public int yCoord;		//y-coordinate
	
	public int killstreak;	//how many kills
	public int health;		//health remaining
	public int damage;		//damage per shot
	public int range;		//how far I can shoot
	public int speed;		//how fast I am.
	
	//this is used by other classes to ensure that we lose pointers to this
	//in order to help out garbage collection
	public boolean destroyed;
	
	//all ships must...
	/**
	 * have the option to fly
	 * 
	 * @param m		the model
	 */
	abstract void move(Model m);
	
	/**
	 * have the option to shoot
	 * 
	 * @param m		the model
	 */
	abstract void shoot(Model m);
	
	/**
	 * draw themselves.
	 * 
	 * Should this go in the drawing handler?
	 * 
	 * @param g		the graphics object of the panel
	 */
	abstract void drawOnGraphics(Graphics g);
	
	/**
	 * have the option for their death to trigger something
	 * 
	 * @param m		the model
	 */
	abstract void onDeath(Model m);
	
	/**
	 * all ship constructors must call this with super(). HOW DO I ENFORCE THIS?
	 */
	public Ship(){
		count++;
	}
	
	/**
	 * should be changed to calculate from edge to edge
	 * 
	 * @param s		the ship to find the distance to
	 * @return		the distance to the target ship
	 */
	public double getDistance(Ship s){
		return Math.sqrt(Math.pow(Math.abs(s.xCoord - this.xCoord),2) + Math.pow(Math.abs(s.yCoord - this.yCoord),2)); 
	}
	
	/**
	 * @return
	 */
	public Color getColor(){
		Color rColor = null;
		switch(team){
			case 1: rColor = Color.RED; break;
			case 2: rColor = Color.BLUE; break;
		}
		return rColor;
	}
	
	/**
	 * allows a ship to make a beeline towards another ship
	 * currently ships cannot travel except at 0, 45, and 90 degree angles
	 * 
	 * should this be here? its just a utility
	 * 
	 * @param s		the ship to move toward
	 */
	public void moveToward(Ship s){
		
		int distX = Math.abs(s.xCoord - this.xCoord);
		int distY = Math.abs(s.yCoord - this.yCoord);
		
		if(Math.abs(distX) > Math.abs(distY)){
			if(s.xCoord > this.xCoord){
				this.xCoord += speed;
			} else{
				this.xCoord -= speed;
			}
		} else{
			if(s.yCoord > this.yCoord){
				this.yCoord += speed;
			} else{
				this.yCoord -= speed;
			}
		}
	}
	
}
