package fr.axicer.furryattack.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.font.FontType;

public class Configuration {
	
	private static final String CONFIG_READ_PATH_ERROR_MSG = "ERROR IN PATH READING !";
	private static final String CONFIG_READ_VALUE_ERROR_MSG = "ERROR IN VALUE READING !";
	
	private static final String CONFIG_WRITE_PATH_FORCE_ERROR_MSG = "ERROR IN PATH WRITING ! Force writing is disabled !";
	private static final String CONFIG_WRITE_PATH_VALUE_ERROR_MSG = "ERROR IN PATH WRITING !";
	
	private JSONObject root;
	private Logger logger;
	
	public Configuration() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.root = new JSONObject();
	}
	public Configuration(File f) throws FileNotFoundException {
		this(new FileInputStream(f));
	}
	public Configuration(String path) {
		this(Configuration.class.getResourceAsStream(path));
	}
	public Configuration(InputStream stream) {
		this();
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
	
	public String getString(String path, String replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_MSG+"\" for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof String)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_MSG+"\" for path:"+path);
						return replace;
					}
					return (String) val;
				}else if(cur instanceof JSONObject) {
					Object val = (String)((JSONObject)cur).get(realNode);
					if(!(val instanceof String)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_MSG+"\" for path:"+path);
						return replace;
					}
					return (String) val;
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_MSG+"\" for path: "+path);
					return replace;
				}
			}
		}
		return null;
	}
	public int getInt(String path, int replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Long) && !(val instanceof Integer)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path: "+path);
						return replace;
					}
					if(val instanceof Long)return Math.toIntExact((long) val);
					else return (int) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof Long) && !(val instanceof Integer)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path: "+path);
						return replace;
					}
					if(val instanceof Long)return Math.toIntExact((long) val);
					else return (int) val;
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}
		}
		return 0;
	}
	public float getFloat(String path, float replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path:"+path);
						return replace;
					}
					return ((Double)val).floatValue();
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path:"+path);
						return replace;
					}
					return ((Double)val).floatValue();
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}
		}
		return 0;
	}
	public double getDouble(String path, double replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path:"+path);
						return replace;
					}
					return (double) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+replace+"\" for path:"+path);
						return replace;
					}
					return (double) val;
				}else {
					logger.warn("Error while reading path ! setting value to \""+replace+"\" for path: "+path);
					return replace;
				}
			}
		}
		return 0;
	}
	public boolean getBoolean(String path, boolean replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \"false\" for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Boolean)) {
						logger.warn("Error while getting value of path ! setting value to \"false\" for path:"+path);
						return replace;
					}
					return (boolean) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof Boolean)) {
						logger.warn("Error while getting value of path ! setting value to \"false\" for path:"+path);
						return replace;
					}
					return (boolean) val;
				}else {
					logger.warn("Error while reading path ! setting value to \"false\" for path: "+path);
					return replace;
				}
			}
		}
		return false;
	}
	public JSONObject getJSONObject(String path, JSONObject replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof JSONObject)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return replace;
					}
					return (JSONObject) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof JSONObject)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return replace;
					}
					return (JSONObject) val;
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return replace;
				}
			}
		}
		return null;
	}
	public JSONArray getJSONArray(String path, JSONArray replace) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return replace;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof JSONArray)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return replace;
					}
					return (JSONArray) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof JSONArray)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return replace;
					}
					return (JSONArray) val;
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return replace;
				}
			}
		}
		return null;
	}
	
	public JSONObject getRoot() {
		return this.root;
	}
	
	@SuppressWarnings("unchecked")
	public void setString(String path, String value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setInt(String path, int value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setFloat(String path, float value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setDouble(String path, double value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setBoolean(String path, boolean value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setJSONObject(String path, JSONObject value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setJSONArray(String path, JSONArray value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].length() != 1 ? nodes[i].replaceFirst("\\[", "") : nodes[i];
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(nodes[i]));
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].length() != 1 && nodes[i].startsWith("[")) {
								nObj = new JSONArray();
							}else {
								nObj = new JSONObject();
							}
							((JSONObject) cur).put(realNode, nObj);
							cur = nObj;
						}else {
							logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
						}
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}else {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null || forceCreation) {
						((JSONObject)cur).put(realNode, value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(val != null || forceCreation) {
						((JSONArray)cur).set(Integer.parseInt(realNode), value);
					}else {
						logger.warn(CONFIG_WRITE_PATH_FORCE_ERROR_MSG+" at "+path);
					}
				}else {
					logger.warn(CONFIG_WRITE_PATH_VALUE_ERROR_MSG+" at "+path);
				}
			}
		}
	}
	
	public boolean save(File f) {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(f);
			stream.write(root.toJSONString().getBytes());
			return true;
		} catch (IOException e) {
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
		return false;
	}
	
	public Color getColor(String path, Color replaceValue) {
		JSONArray colorRoot = getJSONArray(path, null);
		if(colorRoot != null) {
			int r = getInt(path+".0",0);
			int g = getInt(path+".1",0);
			int b = getInt(path+".2",0);
			int a = getInt(path+".3",0);
			return new Color(r, g, b, a);
		}
		return replaceValue;
	}
	
	@SuppressWarnings("unchecked")
	public void setColor(String path, Color value, boolean forcecreation) {
		JSONArray array = new JSONArray();
		array.add(value.x);
		array.add(value.y);
		array.add(value.z);
		array.add(value.w);
		setJSONArray(path, array, forcecreation);
	}
	
	public FontType getFontType(String path, FontType replaceValue) {
		FontType type =  FontType.getFontType(getString(path , null));
		return type == null ? replaceValue : type;
	}
	
	public void setFontType(String path, FontType value, boolean forcecreation) {
		setString(path, value.getName(), forcecreation);
	}
	
	public Vector3f getVector3f(String path, Vector3f replaceValue) {
		JSONArray colorRoot = getJSONArray(path, null);
		if(colorRoot != null) {
			float x = getFloat(path+".0", 0);
			float y = getFloat(path+".1", 0);
			float z = getFloat(path+".2", 0);
			return new Vector3f(x, y, z);
		}
		return replaceValue;
	}
	
	@SuppressWarnings("unchecked")
	public void setVector3f(String path, Vector3f value, boolean forcecreation) {
		JSONArray array = new JSONArray();
		array.add(value.x);
		array.add(value.y);
		array.add(value.z);
		setJSONArray(path, array, forcecreation);
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
