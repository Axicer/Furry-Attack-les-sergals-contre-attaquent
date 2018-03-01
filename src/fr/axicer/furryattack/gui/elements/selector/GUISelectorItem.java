package fr.axicer.furryattack.gui.elements.selector;

public class GUISelectorItem<T> {
	
	private String name;
	private T val;
	
	public GUISelectorItem(T val, String name) {
		this.val = val;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getVal() {
		return val;
	}

	public void setVal(T val) {
		this.val = val;
	}
	
	
}
