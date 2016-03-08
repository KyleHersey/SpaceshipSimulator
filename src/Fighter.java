
public class Fighter extends Frigate{
	
	Carrier parent;
	
	boolean docked;
	boolean returning;

	public Fighter(int teamIN, Model m, Carrier parentIN) {
		super(teamIN, m);

		docked = true;
		parent = parentIN;
		avoidRadius = 8;
		speed *= 1.3;
	}

	@Override
	void shoot(Model m){
		if(!returning && !docked){
			if(target != null && this.getDistanceEdges(target) < range){
				m.handleShot(new Shot(target, this, "Laser"));
			} else {
				Ship closestEnemy = getClosestEnemy(m);
				if(closestEnemy != null){
					if(getDistanceEdges(closestEnemy) < range){
						m.handleShot(new Shot(closestEnemy, this, "Laser"));
					}
				}
			}
		}
	}
	
	@Override
	void moveLogic(Model m){
		
		if(!returning && !docked){
			super.moveLogic(m);
		} else if(returning){
			moveToward(parent);
		}
	}
	
	
}
