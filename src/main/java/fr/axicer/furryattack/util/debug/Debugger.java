package fr.axicer.furryattack.util.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class used to debug or count time in the console
 * @author Axicer
 *
 */
public class Debugger {
	
	private static long count = 0L;
	private static boolean shouldCount = false;
	private static final Logger logger = LoggerFactory.getLogger(Debugger.class);
	
	public static void startCounting() {
		count = 0L;
		shouldCount = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(shouldCount) {
					count++;
					try {
						Thread.sleep(1L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public static void endCounting() {
		shouldCount = false;
		logger.info("Duration = "+count+" milliseconds");
	}
}
