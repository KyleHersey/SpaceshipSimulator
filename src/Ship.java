import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * All the ships on the screen must extend this abstract class
 *
 */
public abstract class Ship {

	static int count = 0;	//currently used for ship ID generation
	
	public int shipID;		//ship ID
	public int team;		//which team/color
	
	public Coordinates coords;
	
	public int radius;	//derived from size
	
	public int killstreak;	//how many kills
	public int health;		//health remaining
	public int damage;		//damage per shot
	public int range;		//how far I can shoot
	public double speed;		//how fast I am.
	public int avoidRadius;		//how far I want to be from other ships
	
	
	//this is used by other classes to ensure that we lose pointers to this
	//in order to help out garbage collection
	public boolean destroyed;
	
	//all ships must...
	/**
	 * have the option to fly
	 * 
	 * @param m		the model
	 */
	abstract void moveLogic(Model m);
	
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
	public Ship(Model m){
		count++;
	}
	
	public void setStartPoint(Ship s, Model m){
		Random rand = new Random();
		
		
		int startX;
		if(team == 1){
			startX = 20 + rand.nextInt(20);
		} else{
			startX = m.drawingFrame.getSize().width - 20 - rand.nextInt(20);
		}
		int startY = rand.nextInt(m.drawingFrame.getSize().height);
		s.coords = new Coordinates(startX, startY);
		
		
		//s.coords = new Coordinates(rand.nextInt(m.drawingFrame.getWidth()), rand.nextInt(m.drawingFrame.getHeight()));
	}
	
	public void move(Model m){
		moveLogic(m);
		checkAllCollisions(m);
	}
	
	public double getDistanceEdges(Ship s){
		return getDistance(s) - radius - s.radius;
	}
	
	/**
	 * should be changed to calculate from edge to edge
	 * 
	 * @param s		the ship to find the distance to
	 * @return		the distance to the target ship
	 */
	public double getDistance(Ship s){
		return Math.sqrt(Math.pow(Math.abs(s.coords.getCoordX() - this.coords.getCoordX()),2) + Math.pow(Math.abs(s.coords.getCoordY() - this.coords.getCoordY()),2)); 
	}
	
	public int getDistanceSquared(Ship s){
		return ((s.coords.getCoordX() - this.coords.getCoordX())*(s.coords.getCoordX() - this.coords.getCoordX())) + 
				((s.coords.getCoordY() - this.coords.getCoordY())*(s.coords.getCoordY() - this.coords.getCoordY())); 
	}
	
	public void checkAllCollisions(Model m){
		for(int i = 0; i < m.shipList.size(); i++){
			if(!m.shipList.get(i).equals(this)){
				if(checkCollision(m.shipList.get(i))){
					
					m.collisionKills +=2;
					//System.out.println(	m.shipList.get(i).team + " collided with " + this.team);
					
					Ship bonked = m.shipList.get(i);
					
					if(this.radius < bonked.radius){
						m.destroyShip(this, false);
						bonked.health -= this.health;
					} else{
						m.destroyShip(bonked, false);
						this.health -= bonked.health;
					}
					
					//m.destroyShip(m.shipList.get(i), false);
					//m.destroyShip(this, false);
					
				}
			}
		}
	}
	
	private boolean checkCollision(Ship s){
		//if they are closer to eachother on either axis than the sum of their radii, check
		if((Math.abs(s.coords.getCoordX() - this.coords.getCoordX()) < (s.radius + this.radius) || Math.abs(s.coords.getCoordY() - this.coords.getCoordY()) < (s.radius + this.radius))){
			if(getDistance(s) <= s.radius + this.radius){
				return true;
			}
		}
		return false;
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
	 * 
	 * should this be here? its just a utility
	 * 
	 * @param s		the ship to move toward
	 */
	public void moveToward(Ship s){
		int distX = s.coords.getCoordX() - this.coords.getCoordX();
		int distY = s.coords.getCoordY() - this.coords.getCoordY();
		
		double angle = Math.atan2(distX, distY);
		this.coords.addToX(Math.sin(angle) * speed);
		this.coords.addToY(Math.cos(angle) * speed);
	}
	
	void moveAwayFrom(Ship s){
		int distX = s.coords.getCoordX() - this.coords.getCoordX();
		int distY = s.coords.getCoordY() - this.coords.getCoordY();
		
		double angle = Math.atan2(distX, distY);
		this.coords.addToX(Math.sin(angle) * speed * -1);
		this.coords.addToY(Math.cos(angle) * speed * -1);
	}
	
	public void keepAtRange(int goal, Ship s){
		if(getDistanceSquared(s) > goal * goal){
			moveToward(s);
		} else{
			moveAwayFrom(s);
		}
	}
	
	public Ship getClosestEnemy(Model m){
		Ship closest = null;
		int distance = Integer.MAX_VALUE;
		
		for(int i = 0; i < m.shipList.size(); i++){
			if(m.shipList.get(i).team != this.team && getDistanceSquared(m.shipList.get(i)) < distance && !m.shipList.get(i).equals(this)){		//this can be made cleaner, should it work
				closest = m.shipList.get(i);
				distance = getDistanceSquared(m.shipList.get(i));
			}
		}
		
		return closest;
	}
}
