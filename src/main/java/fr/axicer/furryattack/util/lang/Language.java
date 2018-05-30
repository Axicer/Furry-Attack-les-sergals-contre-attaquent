package fr.axicer.furryattack.util.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.axicer.furryattack.util.config.Configuration;

/**
 * A language configuration
 * @author Axicer
 *
 */
public class Language extends Configuration{
	
	/**
	 * Default translation path
	 */
	public static final String TRANSLATION_GOLBAL_PATH = "translation.";
	/**
	 * Null global variable replacement string
	 */
	public static final String NULL_GLOBAL_VARIABLE_MSG = "[null global variable]";
	/**
	 * Null argument replace string
	 */
	public static final String NULL_ARGUMENT_VARIABLE_MSG = "[null argument variable]";
	
	/**
	 * Create a new language instance from a lang name<br>
	 * 
	 * <i>Deprecated: this can't fail if the file is not found<br>
	 * use {@code LangGenerator.generate(name);} instead</i>
	 * @param name
	 */
	@Deprecated
	public Language(String name) {
		super("/lang/"+name+".json");
	}
	
	/**
	 * Get the text in the config of a path or path if not found<br>
	 * Deprecated:<i> this method does not transform global variable, use getTranslation instead</i>
	 * @param path {@link String} path
	 * @return {@link String} value
	 */
	@Deprecated
	public String getText(String path) {
		return getString(path, path);
	}
	
	/**
	 * Get the text of a string and use the replace value if the string is not found<br>
	 * Deprecated:<i> this method does not transform global variable, use getTranslation instead</i>
	 * @param path {@link String} path
	 * @param replaceIfNull {@link String} replacement value
	 * @return {@link String} value
	 */
	@Deprecated
	public String getText(String path, String replaceIfNull) {
		String str = getText(path);
		str = str == path ? replaceIfNull : str;
		return str;
	}
	
	/**
	 * Get the text of a string and use the replace value if not found<br>
	 * You can specify values to replace &0 to &n by the value of values[0] to values[n]
	 * @param path {@link String} path
	 * @param replaceIfNull {@link String} replacement value
	 * @param values {@link String}[] replacement arguments
	 * @return {@link String}
	 */
	public String getTranslation(String path, String replaceIfNull, String... values) {
		String rawString = getText(TRANSLATION_GOLBAL_PATH+path, replaceIfNull);
		
		String[] globalPartsSplitted = rawString.split("[@][^ ]{1,}");
		Pattern globalPattern = Pattern.compile("[@][^ ]{1,}");
		Matcher globalMatcher = globalPattern.matcher(rawString);
		StringBuilder globalBuilder = new StringBuilder();
		if(globalPartsSplitted.length != 0)globalBuilder.append(globalPartsSplitted[0]);
		int globalPartsId = 1;
		while(globalMatcher.find()) {
			globalBuilder.append(getTranslation(globalMatcher.group().substring(1), NULL_GLOBAL_VARIABLE_MSG, values));
			if(globalPartsId < globalPartsSplitted.length)globalBuilder.append(globalPartsSplitted[globalPartsId]);
			globalPartsId++;
		}
		
		//replacing &X variables to values[X] value
		String[] partsSplitted = globalBuilder.toString().split("[&][0-9]{1,}");
		Pattern pattern = Pattern.compile("[&][0-9]{1,}");
		Matcher matcher = pattern.matcher(globalBuilder.toString());
		StringBuilder builder = new StringBuilder();
		builder.append(partsSplitted[0]);
		int partsId = 1;
		while(matcher.find()) {
			int id = Integer.parseInt(matcher.group().substring(1));
			if(id < 0 || id > values.length) {
				builder.append(NULL_ARGUMENT_VARIABLE_MSG);
			}else {
				builder.append(values[id]);
			}
			if(partsId < partsSplitted.length)builder.append(partsSplitted[partsId]);
			partsId++;
		}
		return builder.toString();
	}
	
	/**
	 * Get the text of a string or the path himself if not found<br>
	 * You can specify values to replace &0 to &n by the value of values[0] to values[n]
	 * @param path {@link String} path
	 * @param values {@link String}[] replacement arguments
	 * @return {@link String}
	 */
	public String getTranslation(String path, String... values) {
		return getTranslation(path, path, values);
	}
}
