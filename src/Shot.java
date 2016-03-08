
public class Shot {

	private Ship sender;
	private Ship target;
	private String type;
	
		
	/**
	 * typeIN should be a tuple, not a string
	 * 
	 * @param shipIN	the ship being shot at
	 * @param damageIN	the amount of damage
	 * @param senderIN	the shooter
	 * @param typeIN	the type of damage
	 */
	public Shot(Ship shipIN, Ship senderIN, String typeIN){
		target = shipIN;
		sender = senderIN;
		type = typeIN;
	}
	
	public Ship getTarget(){
		return target;
	}
	
	public Ship getSender(){
		return sender;
	}
	
	public String getType(){
		return type;
	}
	
}
