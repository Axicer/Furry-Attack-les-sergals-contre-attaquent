package fr.axicer.furryattack.util.config.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.axicer.furryattack.util.config.Configuration;

public class Language extends Configuration{
	
	public Language(String name) {
		super("/lang/"+name+".json");
	}
	
	public String getRawText(String path) {
		return getString(path);
	}
	
	public String getText(String path, String replaceIfNull) {
		String str = getRawText(path);
		str = str == null ? replaceIfNull : str;
		return str;
	}
	
	public String getTranslation(String path, String replaceIfNull, String... values) {
		String rawString = getText(path, replaceIfNull);
		
		String[] globalPartsSplitted = rawString.split("[@][^ ]{1,}");
		Pattern globalPattern = Pattern.compile("[@][^ ]{1,}");
		Matcher globalMatcher = globalPattern.matcher(rawString);
		StringBuilder globalBuilder = new StringBuilder();
		globalBuilder.append(globalPartsSplitted[0]);
		int globalPartsId = 1;
		while(globalMatcher.find()) {
			globalBuilder.append(getRawText(globalMatcher.group().substring(1)));
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
			builder.append(values[Integer.parseInt(matcher.group().substring(1))]);
			if(partsId < partsSplitted.length)builder.append(partsSplitted[partsId]);
			partsId++;
		}
		return builder.toString();
	}
}
