package jcg.pool.test;

import jcg.pool.core.ThreadPool;

public class Test {

	public static void main(String[] args) {
		ThreadPool pool = new ThreadPool(10);
		
		
		pool.queueTask(new Runnable() {
			@Override
			public void run() {
				System.out.println("hello world");
			}
		});
		
		pool.start();
		System.out.println("started");
		
		for(int i = 0; i < 50; i++ ) {
			pool.queueTask(new Runnable() {
				@Override
				public void run() {
					for(int i = 0; i <= 1e10; i++) {
						if(i == 1e8) {
							System.out.println("done " + Thread.currentThread().getName());
						}
					}
				}
			
			});
		}
	}
	
	
}
