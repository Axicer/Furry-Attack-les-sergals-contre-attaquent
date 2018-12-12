package fr.axicer.furryattack.map;

public final class TileDecoder {
	
	/*--------------------------- SIMPLE METHODS -------------------------------*/
	
	public static boolean isSolid(long tile) {
		//solid is pos 0
		return getBit(tile, 0);
	}
	
	public static int getHeal(long tile) {
		//return the 3 bits of heal at pos 35
		return getNbits(tile, 3, 35);
	}
	
	public static int getColor(long tile) {
		//return the first 4 octets
		return getNbits(tile, 4*8, 0);
	}
	
	/*----------------------------- RAW METHODS -----------------------------------*/
	
	/**
	 * Get the boolean status of a tile
	 * @param tile int the tile to check
	 * @param pos the position of the bit
	 */
	public static boolean getBit(long tile, int pos) {
		long mask = 1L << pos;
		return ((tile & mask) >> pos) == 1;
	}
	
	/**
	 * Get the n bits from pos
	 * @param tile the tile to get from
	 * @param n the number of bits to read
	 * @param pos the position to start reading
	 * @return the n bits
	 */
	public static int getNbits(long tile, int n, int pos) {
		int working = 0;
		//iterate trough each bits
		for(int i = 0 ; i <= n ; i++) {
			//get the i th mask
			long mask = 1L << (i+pos);
			working = (int)(tile & ~mask);
		}
		return working >> n;
	}
	
}
