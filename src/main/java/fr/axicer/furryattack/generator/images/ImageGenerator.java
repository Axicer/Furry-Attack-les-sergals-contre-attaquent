package fr.axicer.furryattack.generator.images;

import java.awt.image.BufferedImage;

import fr.axicer.furryattack.generator.Generator;

public abstract class ImageGenerator extends Generator{
	public abstract BufferedImage generate();
}
