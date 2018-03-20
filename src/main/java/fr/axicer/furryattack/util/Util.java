package fr.axicer.furryattack.util;

public class Util {
	
	public static <T> boolean contains(T[] args, T val) {
		for(int i = 0 ; i < args.length ; i++) {
			if(args[i].equals(val)) {
				return true;
			}
		}
		return false;
	}
	
}
