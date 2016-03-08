import java.awt.Graphics;
import java.util.Random;


/**
 * Generic ship.
 * Fastish speed, short range, low damage, low health
 *
 * need to add size, and draw circle/manage collisions based upon it
 */
public class Grunt extends Ship{
	
	Ship target;	//current target ship
	
	/**
	 * @param teamIN	The team for the ship. Currently, Red = 1, Blue = 2. 
	 * 
	 * Future direction involves factory pattern for ship creation, inputting team, Shiptype.
	 */
	public Grunt(int teamIN){
		super();
		Random rand = new Random();
				
		team = teamIN;
		xCoord = rand.nextInt(500);		//current placement is random. add to Ship class
		yCoord = rand.nextInt(500);
		
		//these should be derived from final variables (representing starting stats).
		//Base ship stats
		speed = 1;
		health = 200;
		damage = 5;
		range = 12;
		
		//not yet sure best way to ID ships. this works for now
		shipID = count;
	}

	
	/* (non-Javadoc)
	 * @see Ship#move(Model)
	 * 
	 * Current behavior is to detect the closest enemy ship. Pursue it until it is destroyed.
	 * 
	 */
	@Override
	void move(Model m) {
		Ship closest = null;
		double distance = Integer.MAX_VALUE;
		
		if(target == null || target.destroyed){
			for(int i = 0; i < m.shipList.size(); i++){
				if(m.shipList.get(i).team != this.team && getDistance(m.shipList.get(i)) < distance && !m.shipList.get(i).equals(this)){		//this can be made cleaner, should it work
					closest = m.shipList.get(i);
					distance = getDistance(m.shipList.get(i));
				}
			}
			
			target = closest;
		}
		
		//BUG WORKAROUND HERE. If game ends, crash when no targets remain.
		if(this.target == null){
			System.out.println("Game Over");
			return;
		}

		//if target is out of range, move towards it
		if(this.getDistance(target) >= range){
			this.moveToward(target);
		}
		
		//collision check <3
		//probably should be part of a section of ship abstract. ships should always collide, based upon size.
		for(int i = 0; i < m.shipList.size(); i++){
			if(!m.shipList.get(i).equals(this)){
				if(this.getDistance(m.shipList.get(i)) <= 4){
					
					m.collisionKills +=2;
					System.out.println(	m.shipList.get(i).shipID + " collided with " + this.shipID + 
										"\tLaser Kills: " + m.laserKills + "\tCollisions: " + 
										m.collisionKills);
					
					m.destroyShip(m.shipList.get(i), false);
					m.destroyShip(this, false);
					
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see Ship#shoot(Model)
	 * 
	 * currently wrong but works. Model should handle whether shots hit or not,
	 * evaluating range, etc. This allows more cool things later, such as shields
	 * 
	 */
	@Override
	void shoot(Model m) {
		if((target != null) && (getDistance(target) < range)){
			m.handleShot(new Shot(target, damage, this, "Laser"));
		}
	}

	/* (non-Javadoc)
	 * @see Ship#drawOnGraphics(java.awt.Graphics)
	 * 
	 * draws the ship onto the graphics. Grunt is an circle for now
	 * 
	 * can be expanded to draw a ship once I feel the need to make it look nice
	 */
	@Override
	void drawOnGraphics(Graphics g) {
		g.setColor(getColor());
		g.drawOval(xCoord - 2, yCoord - 2, 4, 4);
	}
	
	/* (non-Javadoc)
	 * @see Ship#onDeath(Model)
	 * 
	 * this triggers on death.
	 * 
	 * grunt should not do anything special on death.
	 * commented code is remnants of death explosion testing
	 */
	@Override
	void onDeath(Model m){
		m.shipList.remove(this);
		destroyed = true;
		/* explosion on death
		for(int i = 0 ; i < m.shipList.size(); i++){
			if(this.getRange(m.shipList.get(i)) < 50){
				m.handleShot(new Shot(m.shipList.get(i), 20, this, "Explosion"));
			}
		}
		*/
	}
	
}
