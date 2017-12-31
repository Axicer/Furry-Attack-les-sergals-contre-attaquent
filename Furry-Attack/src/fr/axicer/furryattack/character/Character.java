package fr.axicer.furryattack.character;

import fr.axicer.furryattack.util.Color;

public class Character {
	
	private Species race;
	private Color primaryColor;
	private Color secondaryColor;
	private String expression;
	//TODO coup special
	
	public Character(Species race, Color primary, Color secondary, String expression) {
		this.race = race;
		this.primaryColor = primary;
		this.secondaryColor = secondary;
		this.expression = expression;
	}

	public Species getRace() {
		return race;
	}

	public void setRace(Species race) {
		this.race = race;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
