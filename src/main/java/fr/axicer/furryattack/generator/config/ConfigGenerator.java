package fr.axicer.furryattack.generator.config;

import fr.axicer.furryattack.generator.Generator;
import fr.axicer.furryattack.util.config.Configuration;

public abstract class ConfigGenerator extends Generator{
	public abstract Configuration generate();
}
