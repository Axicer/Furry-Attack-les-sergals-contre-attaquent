package fr.axicer.furryattack.util.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
	
	private static final String CONFIG_READ_PATH_ERROR_MSG = "ERROR IN PATH READING !";
	private static final int CONFIG_READ_PATH_ERROR_ID = -1;
	private static final String CONFIG_READ_VALUE_ERROR_MSG = "ERROR IN VALUE READING !";
	private static final int CONFIG_READ_VALUE_ERROR_ID = -2;
	
	private JSONObject root;
	private Logger logger;
	
	public Configuration(String path) {
		this(Configuration.class.getResourceAsStream(path));
	}
	public Configuration(InputStream stream) {
		logger = LoggerFactory.getLogger(this.getClass());
		JSONParser parser = new JSONParser();
		String rawJSON = getStringFromInputStream(stream);
		try {
			root = (JSONObject) parser.parse(rawJSON);
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			if(stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getString(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_MSG+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_MSG;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof String)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_MSG+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_MSG;
					}
					return (String) val;
				}else if(cur instanceof JSONObject) {
					Object val = (String)((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof String)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_MSG+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_MSG;
					}
					return (String) val;
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_MSG+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_MSG;
				}
			}
		}
		return null;
	}
	public int getInt(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof Long)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return Math.toIntExact((long) val);
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof Long)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return Math.toIntExact((long) val);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}
		}
		return 0;
	}
	public float getFloat(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return ((Double)val).floatValue();
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return ((Double)val).floatValue();
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}
		}
		return 0;
	}
	public double getDouble(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return (double) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return (double) val;
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}
		}
		return 0;
	}
	public boolean getBoolean(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to \"false\" for path: "+path);
					return false;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof Boolean)) {
						logger.warn("Error while getting value of path ! setting value to \"false\" for path:"+path);
						return false;
					}
					return (boolean) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof Boolean)) {
						logger.warn("Error while getting value of path ! setting value to \"false\" for path:"+path);
						return false;
					}
					return (boolean) val;
				}else {
					logger.warn("Error while reading path ! setting value to \"false\" for path: "+path);
					return false;
				}
			}
		}
		return false;
	}
	public JSONObject getJONObject(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(nodes[i]));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(nodes[i]);
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return null;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(!(val instanceof JSONObject)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return null;
					}
					return (JSONObject) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(nodes[i]);
					if(!(val instanceof JSONObject)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return null;
					}
					return (JSONObject) val;
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return null;
				}
			}
		}
		return null;
	}
	
	public JSONObject getRoot() {
		return this.root;
	}
	
	public void setString(String path, String value) {
		//TODO
	}
	
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return root.toJSONString();
	}
}
