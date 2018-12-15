package fr.axicer.furryattack.map;

public interface TileDecoder {
	
	/**
	 * Get the boolean status of a tile
	 * @param tile int the tile to check
	 * @param pos the position of the bit
	 */
	public default boolean getBit(long tile, int pos) {
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
	public default int getNbits(long tile, int n, int pos) {
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
