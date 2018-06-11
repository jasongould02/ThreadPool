package jcg.pool.core;

import java.util.concurrent.PriorityBlockingQueue;

public class ThreadPool implements Runnable {

	Thread thread = null;
	
	private boolean running = false;
	private final int poolSize;
	
	private Worker workerList[] = null;
	private PriorityBlockingQueue<Runnable> taskList = null;
	
	public ThreadPool(int poolSize) {
		this.poolSize = poolSize;
		this.workerList = new Worker[poolSize];
		
		taskList = new PriorityBlockingQueue<Runnable>();
		
		for(int i = 0; i < poolSize; i++) {
			workerList[i] = new Worker(null);
		}
		
		
		this.start();
	}
	
	@Override
	public void run() {
		while (running) {
			for(Worker w : workerList) {
				if(w.getStatus() == WorkerStatus.STOPPING) {

				}
			}
		}
	}
	

	protected Runnable requestTask() throws InterruptedException {
		return taskList.take();
	}
	
	public synchronized void start() {
		thread = new Thread(this, "ThreadPool.Size:" + poolSize + "@" + (System.currentTimeMillis() / 1000) + "s");
		running = true;
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
		thread = null;
	}

	public synchronized void stop() {
		//thread.join();
		if(Thread.interrupted()) {
//			throw new InterruptedException(); //Is this the right way to do things?
			// or is this
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*
			 thread.interrupt();
			 */
		}
		thread = null;
		running = false;
	}
	

	
	private class Worker implements Runnable {

		//private ArrayList<Runnable> taskList = new ArrayList<Runnable>(10);
		private Runnable 		currentTask;
		private WorkerStatus 	status;
		
		public Worker(Runnable task) {
			currentTask = task;
		}
		
		@Override
		public void run() {
			while(status != WorkerStatus.STOPPING) {
				setStatus(WorkerStatus.WORKING);
				currentTask.run();
				setStatus(WorkerStatus.IDLE);
				currentTask = null;
				
				while(currentTask == null && getStatus() != WorkerStatus.STOPPING) { // Wait until there is a new task
					try {
						currentTask = getNextTask(); //ThreadPool.this.requestTask();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}
		
		private Runnable getNextTask() throws InterruptedException {
			return ThreadPool.this.requestTask(); // Class.this.method to get the enclosing class/object
		}
		
		public WorkerStatus getStatus() {
			return status;
		}
		
		
		protected void setStatus(WorkerStatus newStatus) {
			this.status = newStatus;
		}
		
	}
	
	private enum WorkerStatus {
		STARTING,
		STOPPING,
		WORKING,
		IDLE
	}
	
}
