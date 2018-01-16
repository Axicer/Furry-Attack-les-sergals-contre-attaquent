package fr.axicer.furryattack.util.font;

import java.awt.Font;

public enum FontType {
	CONSOLAS("Consolas", Font.PLAIN, 50),
	ARIAL("Arial", Font.PLAIN, 50);
	
	private Font f;
	
	private FontType(String name, int style, int size) {
		this.f = new Font(name, style, size);
	}

	public Font getFont() {
		return f;
	}
	
}
