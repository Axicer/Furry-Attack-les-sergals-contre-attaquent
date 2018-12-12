package fr.axicer.furryattack.map;


public class BitmapUtil {
	/**
	 * Get the first bit sets to 1 starting from LSB
	 * @param val int value to check
	 * @return the first bit sets to 1 starting from LSB
	 */
	public static int getFirstbitOnFromLSB(long val) {
		for(int b = 0 ; b < 8*8 ; b+=8) {
			//get the octet
			byte by = (byte)((val >> b) & 0xFF);
			//check for a 1
			for(int i = 0 ; i < 8 ; i++) {
				//if it's a 1
				if(isBitSet(by, i)) {
					return b+i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Get bits set to 1 inside a int
	 * @param b int to count bits from
	 * @return the amount of bits set to 1
	 */
	public static int getEnabledBitsCount(long val) {
		int count = 0;
		//count for each octet
		for(int b = 0 ; b < 8*8 ; b+=8) {
			//get the octet
			byte by = (byte)((val >> b) & 0xFF);
			//check for each bits
			for(int i = 0 ; i < 8 ; i++) {
				count += isBitSet(by, i) ? 1 : 0;
			}
		}
		return count;
	}
	
	/**
	 * Check for the nth bit to be 1
	 * @param b the octet to check
	 * @param n the nth bits to check to 
	 * @return true if 1,  0 else
	 */
	public static boolean isBitSet(byte b, int n){
	    return ((b >> n) & 1) != 0;
	}
}
