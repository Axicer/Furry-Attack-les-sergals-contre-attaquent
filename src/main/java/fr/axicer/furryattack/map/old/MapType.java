package fr.axicer.furryattack.map.old;

/**
 * All type of map available
 * @author Axicer
 *
 */
public enum MapType {
	/**
	 * Classic map, nothing more inside
	 */
	CLASSIC(ClassicMap.class);
	
	/**
	 * Map type class to construct later
	 */
	private Class<? extends AbstractMap> invokedMapClass;
	
	/**
	 * Constructor of a map type
	 * @param invokedMapClass
	 */
	private MapType(Class<? extends AbstractMap> invokedMapClass) {
		this.invokedMapClass = invokedMapClass;
	}
	
	/**
	 * get the invoked Map class
	 * @return {@link Class}
	 */
	public Class<? extends AbstractMap> getInvokedMapClass(){
		return invokedMapClass;
	}
	
	/**
	 * Get the map type from a string
	 * @param input {@link String} input string
	 * @return {@link MapType} corresponding {@link MapType} or {@code mapType.CLASSIC} if not found
	 */
	public static MapType getMapTypeFromString(String input){
		for(MapType type : values()) {
			if(type.name().equals(input))return type;
		}
		return MapType.CLASSIC;
	}
}
