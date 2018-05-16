package fr.axicer.furryattack.gui.elements.selector;

/**
 * A selector item (a tuple with a name and a T value)
 * @author Axicer
 *
 * @param <T>
 */
public class GUISelectorItem<T> {
	
	/**
	 * item name
	 */
	private String name;
	/**
	 * corresponding value
	 */
	private T val;
	
	/**
	 * Constructor of a {@link GUISelectorItem}
	 * @param val T value to set
	 * @param name {@link String} corresponding name
	 */
	public GUISelectorItem(T val, String name) {
		this.val = val;
		this.name = name;
	}

	/**
	 * Get the name of this item
	 * @return {@link String} name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this item
	 * @param name {@link String} name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of the item
	 * @return T value
	 */
	public T getVal() {
		return val;
	}

	/**
	 * Set the value of the item
	 * @param val T value
	 */
	public void setVal(T val) {
		this.val = val;
	}
	
	
}
