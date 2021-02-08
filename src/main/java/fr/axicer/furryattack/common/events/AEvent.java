package fr.axicer.furryattack.common.events;

/**
 * An event
 */
public abstract class AEvent {

	private static int ID_INC = 0;
	
	private final int id;
	
	public AEvent() {
		this.id = ID_INC++;
	}

	public int getId() {
		return id;
	}
}
