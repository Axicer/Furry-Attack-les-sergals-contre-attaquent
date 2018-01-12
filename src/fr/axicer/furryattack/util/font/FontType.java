package fr.axicer.furryattack.util.font;

public enum FontType {
	CONSOLAS("/font/consolas.png", 16, 16);
	
	private String texturePath;
	private int columns, rows;
	
	private FontType(String texturePath, int columns, int rows) {
		this.texturePath = texturePath;
		this.columns = columns;
		this.rows = rows;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}
	
}
