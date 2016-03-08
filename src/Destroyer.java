import java.awt.Graphics;


public class Destroyer extends Ship{
	
	Ship target;
	
	final double startSpeed = 4;
	final int startHealth = 250;
	final int startDamage = 7;
	final int startRange = 30;
	final int size = 8;

	public Destroyer(int teamIN, Model m) {
		super(m);
		
		team = teamIN;
		
		setStartPoint(this, m);
		
		//Base ship stats
		speed = startSpeed;
		health = startHealth;
		damage = startDamage;
		range = startRange;
		radius = size/2;
		
		//not yet sure best way to ID ships. this works for now
		shipID = count;
	}
	
	

	@Override
	void moveLogic(Model m) {
		//attempt at collision avoidance with teammates [only for red]
				//an example of ships differing in behavior
				boolean avoidance = false;
				//if(this.team == 1){		//set for only team 1
					for(int i = 0; i < m.shipList.size(); i++){
						//if(m.shipList.get(i).team == this.team){		//set to avoid all ships
							if(!m.shipList.get(i).equals(this)){
								if(getDistanceSquared(m.shipList.get(i)) < ((radius + avoidRadius)*2*(radius + avoidRadius)*2)){
									moveAwayFrom(m.shipList.get(i));
									avoidance = true;
								}
							}
						//}
					//}
				}
				
				//if not in danger of collisions
				if(avoidance == false){
				
					Ship closest = null;
					double distance = Integer.MAX_VALUE;
					
					if(target == null || target.destroyed){
						for(int i = 0; i < m.shipList.size(); i++){
							if(m.shipList.get(i).team != this.team && getDistanceSquared(m.shipList.get(i)) < distance && !m.shipList.get(i).equals(this)){		//this can be made cleaner, should it work
								if(!(closest instanceof Frigate && !(m.shipList.get(i) instanceof Frigate))){
									closest = m.shipList.get(i);
									distance = getDistanceSquared(m.shipList.get(i));
								}
							}
						}
						
						target = closest;
					}
					
					//BUG WORKAROUND HERE
					if(this.target == null){
						m.gameOver = true;
						return;
					}
			
					//if target is out of range, move towards it
					if(this.getDistanceSquared(target) >= (range*range)){
						this.moveToward(target);
					}		
				}
	}

	@Override
	void shoot(Model m) {
		
		int shots = 0;
		
		for(int i = 0 ; shots < 3 && i < m.shipList.size() ; i++){
			if(m.shipList.get(i).team != this.team && getDistanceSquared(m.shipList.get(i)) < range*range){
				m.handleShot(new Shot(m.shipList.get(i),this,"Laser"));
				shots++;
			}
		}
	}

	@Override
	void drawOnGraphics(Graphics g) {
		g.setColor(getColor());
		g.drawOval(coords.getCoordX() - radius, coords.getCoordY() - radius, size, size);
		
	}

	@Override
	void onDeath(Model m) {
		m.shipList.remove(this);
		destroyed = true;
		
	}

}
