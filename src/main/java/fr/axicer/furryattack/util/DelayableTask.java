package fr.axicer.furryattack.util;

public class DelayableTask extends Thread{
	
	private Runnable action;
	private long delay;
	
	public DelayableTask(Runnable action, long delay) {
		this.action = action;
		this.delay = delay;
	}
	
	@Override
	public void run() {
		super.run();
		long currentTime = System.currentTimeMillis();
		long expectedTime = currentTime+delay;
		while(currentTime < expectedTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
		}
		action.run();
	}

	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	
}
