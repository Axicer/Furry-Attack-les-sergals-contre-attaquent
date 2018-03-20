package fr.axicer.furryattack.util.events;

public abstract class AbstractEvent {

	private static int ID_INC = 0;
	
	private int id;
	
	public AbstractEvent() {
		this.id = ID_INC++;
	}

	public int getId() {
		return id;
	}
	
}
