package fr.axicer.furryattack.util.lang;

import fr.axicer.furryattack.generator.config.MainConfigGenerator;
import fr.axicer.furryattack.generator.lang.LangGenerator;
import fr.axicer.furryattack.util.Constants;

/**
 * Language manager
 * @author Axicer
 *
 */
public class LanguageManager {
	
	/**
	 * Default language name
	 */
	public final String DEFAULT_LANGUAGE_STRING = "en_US";
	/**
	 * Default language instance
	 */
	private Language DEFAULT_LANGUAGE;
	/**
	 * Actual language instance
	 */
	private Language ACTUAL_LANGUAGE;
	
	/**
	 * Create this manager<br>
	 * Instanciate both actual and default language
	 */
	public LanguageManager() {
		DEFAULT_LANGUAGE = LangGenerator.generate(DEFAULT_LANGUAGE_STRING);
		ACTUAL_LANGUAGE = LangGenerator.generate(Constants.MAIN_CONFIG.getString(MainConfigGenerator.LANG_PATH, DEFAULT_LANGUAGE_STRING));
	}
	
	/**
	 * Get the default Language instance
	 * @return {@link Language} instance
	 */
	public Language getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}
	
	/**
	 * Get the actual Language instance
	 * @return {@link Language} instance
	 */
	public Language getActualLanguage() {
		return ACTUAL_LANGUAGE;
	}
	
	/**
	 * Set the actual Language instance
	 * @param l {@link Language} to set actual
	 */
	public void setActualLanguage(Language l) {
		ACTUAL_LANGUAGE = l;
	}
}
