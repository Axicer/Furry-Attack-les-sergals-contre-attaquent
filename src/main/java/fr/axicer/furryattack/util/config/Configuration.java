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
	
	private static final String CONFIG_WRITE_PATH_FORCE_ERROR_MSG = "ERROR IN PATH WRITING ! Force writing is disabled !";
	private static final String CONFIG_WRITE_PATH_VALUE_ERROR_MSG = "ERROR IN PATH WRITING !";
	
	private JSONObject root;
	private Logger logger;
	
	public Configuration(File f) throws FileNotFoundException {
		this(new FileInputStream(f));
	}
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_MSG+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_MSG;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof String)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_MSG+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_MSG;
					}
					return (String) val;
				}else if(cur instanceof JSONObject) {
					Object val = (String)((JSONObject)cur).get(realNode);
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Long)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return Math.toIntExact((long) val);
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return ((Double)val).floatValue();
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \""+CONFIG_READ_PATH_ERROR_ID+"\" for path: "+path);
					return CONFIG_READ_PATH_ERROR_ID;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Double)) {
						logger.warn("Error while getting value of path ! setting value to \""+CONFIG_READ_VALUE_ERROR_ID+"\" for path:"+path);
						return CONFIG_READ_VALUE_ERROR_ID;
					}
					return (double) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to \"false\" for path: "+path);
					return false;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof Boolean)) {
						logger.warn("Error while getting value of path ! setting value to \"false\" for path:"+path);
						return false;
					}
					return (boolean) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
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
	public JSONObject getJSONObject(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return null;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof JSONObject)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return null;
					}
					return (JSONObject) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
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
	public JSONArray getJSONArray(String path) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONArray) {
					cur = ((JSONArray) cur).get(Integer.parseInt(realNode));
				}else if(cur instanceof JSONObject) {
					cur = ((JSONObject)cur).get(realNode);
				}else {
					logger.warn("Error while reading path ! setting value to null for path: "+path);
					return null;
				}
			}else {
				if(cur instanceof JSONArray) {
					Object val = ((JSONArray)cur).get(Integer.parseInt(realNode));
					if(!(val instanceof JSONArray)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return null;
					}
					return (JSONArray) val;
				}else if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(!(val instanceof JSONArray)) {
						logger.warn("Error while getting value of path ! setting value to null for path:"+path);
						return null;
					}
					return (JSONArray) val;
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
	
	@SuppressWarnings("unchecked")
	public void setString(String path, String value, boolean forceCreation) {
		String[] nodes = path.split("\\.");
		Object cur = root;
		for(int i = 0 ; i < nodes.length ; i++) {
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
			String realNode = nodes[i].replaceFirst("\\[", "");
			if(i < nodes.length-1) {
				if(cur instanceof JSONObject) {
					Object val = ((JSONObject)cur).get(realNode);
					if(val != null) {
						cur = val;
					}else {
						if(forceCreation) {
							Object nObj = null;
							if(nodes[i].startsWith("[")) {
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
							if(nodes[i].startsWith("[")) {
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
