package fr.axicer.furryattack.entity.modelised.model;

import java.util.List;

import org.joml.Vector4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * parts holder
 * @author Axicer
 *
 */
public class PartHolder {

    private List<Part> parts;

    /**
     * Constructor of a part holder
     * Assuming that parts data a correct, if not then the holder will be empty
     * @param partJSON {@link JSONObject} to parse as parts
     */
    @SuppressWarnings("unchecked")
	public PartHolder(JSONObject partJSON) {
        JSONArray jsonpart = (JSONArray)partJSON.get("parts");
        jsonpart.forEach(obj->{
        	JSONObject jobj = (JSONObject)obj;
        	int id = (int) jobj.get("id");
        	JSONArray boundsjson = (JSONArray)jobj.get("bounds");
        	Vector4f bounds = new Vector4f((float)boundsjson.get(0), (float)boundsjson.get(1), (float)boundsjson.get(2), (float)boundsjson.get(3));
        	if(getPart(id) == null) {
        		Part p = new Part(id, bounds);
        		parts.add(p);
        	}
        });
    }

    /**
     * Get a specific part from id
     * @param id given int
     * @return {@link Part} instance if found null elsewhere
     */
    public Part getPart(int id) {
    	for(Part p : parts) {
    		if(p.getID()==id)return p;
    	}
    	return null;
    }

    
    /**
     * Hold a part
     * @param part {@link Part} to hold
     * @return {@link Boolean} true if added false elsewhere
     */
    public boolean holdPart(Part part) {
    	//check for the list to contains the part
    	if(!parts.contains(part)) {
    		//if not the check for same ID
    		for(Part p : parts) {
    			if(p.getID()==part.getID()) {
    				return false;
    			}
    		}
    		//if no part got the same ID then return the add
    		return parts.add(part);
    	}
    	//if it already contains the given part then return false
    	return false;
    }

    /**
     * Remove a specific part 
     * @param id id to delete
     * @return {@link Boolean} true if deleted false elsewhere
     */
    public boolean removePart(int id) {
    	//require part to delete
    	Part required = null;
    	//searching for a matching id
    	for(Part p : parts) {
    		//if ids matches
    		if(p.getID()==id) {
    			//set required to current part
    			required = p;
    			//exit loop
    			break;
    		}
    	}
    	//return false if not found else return the remove
    	if(required == null)return false;
    	return parts.remove(required);
    }

}