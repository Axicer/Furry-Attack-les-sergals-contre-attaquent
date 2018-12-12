package fr.axicer.furryattack.map;

import fr.axicer.furryattack.util.debug.Debugger;

public class TileEncoder {
	
	/*-------------------- SIMPLE METHODS ---------------------*/
	
	public static long setSolid(long tile, int value) {
		//solid is pos 0
		return setBit(tile, value, 0);
	}
	
	public static long setColor(long tile, int color) {
		//set the 4 first octets to the color starting from 0
		return setNbits(tile, color, 4*8, 0);
	}
	
	public static long setHeal(long tile, int heal) {
		//set the 3 bits value of heal inside the tile at pos 35
		return setNbits(tile, heal, 3, 35);
	}
	
	/* -------------------- RAW METHODS ----------------------*/
	
	/**
	 * Set the tile's n bit and set it's value to value
	 * @param tile the tile to modify
	 * @param value the value to set
	 * @param n the position of the bit
	 * @return the modified tile
	 */
	public static long setBit(long tile, int value, int n) {
		if(value != 0 && value != 1) {
			Debugger.debug("valeur non binaire !");
			return tile;
		}
		long mask = 1<<n;
		return (tile & ~mask) |  ((value << n) & mask);
	}
	
	/**
	 * Set the tile nth bits starting from LSB to pos bits to corresponding value inside value
	 * @param tile the tile to change
	 * @param value the value to set
	 * @param n the amount of bits to set
	 * @param pos the position of the first bit from LSB
	 * @return the modified tile
	 */
	public static long setNbits(long tile, int value, int n, int pos) {
		long working = tile;
		//iterate trough each bits
		for(int i = 0 ; i < n ; i++) {
			//get the i th mask
			long mask = 1L << (i+pos);
			working = (working & ~mask) |  ((((value >> i) & 1L)<< i+pos) & mask);
		}
		return working;
	}
	
}
