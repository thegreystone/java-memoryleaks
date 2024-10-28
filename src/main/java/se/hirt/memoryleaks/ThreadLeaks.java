package se.hirt.memoryleaks;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLeaks {
	private static final AtomicInteger threadCounter = new AtomicInteger(0);

	/**
	 * Creates a specified number of non-terminating threads that will leak memory.
	 * Each thread will continuously allocate and hold onto memory while performing
	 * "fake work" to simulate a realistic scenario.
	 *
	 * @param count the number of threads to leak
	 */
	public static void leakThreads(int count) {
		Random rnd = new Random();
		for (int i = 0; i < count; i++) {
			Thread leakyThread = new Thread(() -> {
				// Create some memory overhead per thread
				byte[] threadLocalBuffer = new byte[1024];

				while (!Thread.currentThread().isInterrupted()) {
					try {
						// Simulate some occasional work to make the thread consume CPU
						for (int j = 0; j < threadLocalBuffer.length; j++) {
							threadLocalBuffer[j] = (byte) (System.nanoTime() & 0xFF);
						}
						Thread.sleep(500 + rnd.nextInt(1000));
					} catch (InterruptedException e) {
						// Ignore interruptions, since, hey, evil
					}
				}
			}, "LeakyThread-" + threadCounter.incrementAndGet());

			// Set as daemon false to prevent proper cleanup
			leakyThread.setDaemon(false);
			leakyThread.start();
		}
		System.out.println("Leaked " + count + " thread(s)");
	}
}
