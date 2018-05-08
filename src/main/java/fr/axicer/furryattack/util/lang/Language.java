package fr.axicer.furryattack.util.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.axicer.furryattack.util.config.Configuration;

public class Language extends Configuration{
	
	public static final String TRANSLATION_GOLBAL_PATH = "translation.";
	public static final String NULL_GLOBAL_VARIABLE_MSG = "[null global variable]";
	public static final String NULL_ARGUMENT_VARIABLE_MSG = "[null argument variable]";
	
	public Language(String name) {
		super("/lang/"+name+".json");
	}
	
	public String getRawText(String path) {
		return getString(path, null);
	}
	
	public String getText(String path, String replaceIfNull) {
		String str = getRawText(path);
		str = str == null ? replaceIfNull : str;
		return str;
	}
	
	public String getTranslation(String path, String replaceIfNull, String... values) {
		String rawString = getText(TRANSLATION_GOLBAL_PATH+path, replaceIfNull);
		
		String[] globalPartsSplitted = rawString.split("[@][^ ]{1,}");
		Pattern globalPattern = Pattern.compile("[@][^ ]{1,}");
		Matcher globalMatcher = globalPattern.matcher(rawString);
		StringBuilder globalBuilder = new StringBuilder();
		globalBuilder.append(globalPartsSplitted[0]);
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
}
