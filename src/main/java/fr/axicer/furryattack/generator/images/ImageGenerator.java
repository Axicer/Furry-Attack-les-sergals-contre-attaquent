package fr.axicer.furryattack.generator.images;

import java.awt.image.BufferedImage;

import fr.axicer.furryattack.generator.AbstractGenerator;

public abstract class ImageGenerator extends AbstractGenerator{
	public abstract BufferedImage generate();
}
