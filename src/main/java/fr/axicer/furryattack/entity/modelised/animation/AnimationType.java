package fr.axicer.furryattack.entity.modelised.animation;

/**
 * Current animation available in the game
 * @author Axicer
 *
 */
public enum AnimationType {
    IDLE,
    WALK,
    JUMP,
    SNEAK;
	
	
	/**
	 * get an {@link AnimationType} from a given string
	 * @param name {@link String}
	 * @return the corresponding {@link AnimationType} if found or null if not found
	 */
	public static AnimationType fromString(String name) {
		for(AnimationType type : values()) {
			if(type.toString().equals(name.toUpperCase())){
				return type;
			}
		}
		return null;
	}
}