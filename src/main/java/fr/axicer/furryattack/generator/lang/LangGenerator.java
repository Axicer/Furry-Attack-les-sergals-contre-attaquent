package fr.axicer.furryattack.generator.lang;

import fr.axicer.furryattack.generator.AbstractGenerator;
import fr.axicer.furryattack.util.lang.Language;

/**
 * Lang generator
 * @author Axicer
 *
 */
public class LangGenerator extends AbstractGenerator {
	
	/**
	 * Try to get a new {@link Language} instance from a lang name or return null if unavailable
	 * @param lang {@link String} the lang name
	 * @return {@link Language} instance or null
	 */
	@SuppressWarnings("deprecation")
	public static Language generate(String lang) {
		try {
			return new Language(lang);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
