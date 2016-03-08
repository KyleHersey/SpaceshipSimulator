import java.util.ArrayList;

/**
 * This models the spaceship game.
 * 
 * Everything that is simulation logic happens in here
 *
 *
 *	Notes: 	Keeping track of ship destruction should me more heavily regulated. 
 *			This can be accomplished by making more things private to the model.
 *			(As it should be)
 *
 *			Maybe, each ship should move, then check for deaths, then allow shooting, then check for deaths.
 */
public class Model {
	
	int laserKills;				//deaths by laser
	int collisionKills;			//deaths by collision

	int highestKillstreak;		//most kills by a single ship
	final int startShips = 4;	//this many ships on each team at start
	
	ArrayList<Ship> shipList = new ArrayList<Ship>();	//list of ships
	
	//should be moved to shot class
	ArrayList<Shot> shotList = new ArrayList<Shot>();	//list of shots (for graphical purposes)
	
	/**
	 * Initializes the model with the starting ships
	 */
	public Model(){
		for(int i = 0 ; i < startShips; i ++){
			addGrunt(1);
			addGrunt(2);
		}
	}
	
	/**
	 * Happens as a defined interval of time
	 * Model performs a list of procedures each time it 'ticks'
	 */
	public void tick(){
		
		//clear the list of shots
		shotList = new ArrayList<Shot>();
		
		//tell each ship to move. If it survived that, make it shoot.
		for(int i = 0; i < shipList.size(); i++){
			Ship tempShip = shipList.get(i);
			tempShip.move(this);
			
			//check for boundary violation
			if(!(0 < tempShip.xCoord && tempShip.xCoord < 500 &&
				0 < tempShip.yCoord && tempShip.yCoord < 500)){
				destroyShip(tempShip, true);
			}
			
			if(shipList.contains(tempShip)){
				tempShip.shoot(this);
			}
		}
	}
	
	/**
	 * This method is called to keep the board populated with ships. The world is my fishtank.
	 * 
	 * This may need to be an addShip method, which makes use of a ship factory class
	 *
	 * @param team		team to make new grunts with
	 */
	private void addGrunt(int team){
		shipList.add(new Grunt(team));
	}
	
	/**
	 * This method should do more validation that the ship firing the shot is capable of firing the shot
	 * 
	 * @param s		the shot that the ship sent
	 */
	public void handleShot(Shot s){
		shotList.add(s);	//add to list to draw
		s.getTarget().health -= s.getDamage();	//apply damage (maybe use a method. abstract in ship class?)
		
		//if we destroyed the ship.
		if(s.getTarget().health <= 0){
			destroyShip(s.getTarget(), true);	//Note: can cause problems with death explosions enabled
			
			laserKills++;	//may become obsolete as more weapons come into play
			
			//print death statement (can be toggled w/o consequence)
			System.out.println(s.getTarget().shipID + " died via laser" + "\tLaser Kills: " + laserKills + "\tCollisions: " + 
					collisionKills);
			
			//handout experience to the ship (not sure if this is smart)
			s.getSender().damage *= 2;
			s.getSender().killstreak += 1;
			
			//check killstreak - why bother. introduction of different ships makes this less cool
			if(s.getSender().killstreak > highestKillstreak){
				highestKillstreak = s.getSender().killstreak;
			}
		}
	}
	
	/**
	 * Called when a ship needs to be destroyed.
	 * Adds ships to keep the simulation populated.
	 * 
	 * @param s				ship to destroy
	 * @param makeMore		whether or not to populate model
	 */
	public void destroyShip(Ship s, boolean makeMore){
		shipList.remove(s);
		s.onDeath(this);
		if(makeMore){
			for(int i = 0; i < 2; i++){
				addGrunt(s.team);
			}
		}
	}
	
	/**
	 * More of an admin tool I guess.
	 * This method removes the ship from the shiplist
	 * 
	 * @param s		the ship to be removed
	 */
	public void removeShip(Ship s){
		shipList.remove(s);
		s.destroyed = true;
	}
	
	/**
	 * This should probably be implemented in a different way to handle alllll the colors
	 * 
	 * @return	the number of red ships
	 */
	public int getReds(){
		int count = 0;
		for(int i = 0 ; i < shipList.size(); i++){
			if(shipList.get(i).team == 1){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * See getReds
	 * 
	 * @return	the number of blue ships
	 */
	public int getBlues(){
		return shipList.size() - getReds();
	}
}
