package se.hirt.memoryleaks;

public class RunLeak {
	private final static String LIBRARY_NAME = "memleak";

	static {
		NativeUtils.loadLibrary(LIBRARY_NAME);
	}

	private static native void sayHello();

	public static void main(String[] args) throws InterruptedException {
		LeakConfiguration config = LeakConfiguration.parseArguments(args);
		RunLeak.sayHello();

		switch (config.getLeakType()) {
		case NATIVE_GLOBAL_HANDLE:
			runNativeGlobalHandleLeak(config.getLeaksPerBatch(),
					config.getPauseInMsBetweenBatches(),
					config.getTotalNumberOfBatches());
			break;
		case NATIVE_MALLOC:
			runNativeMallocLeak(config.getLeaksPerBatch(),
					config.getPauseInMsBetweenBatches(),
					config.getTotalNumberOfBatches(),
					config.getBytesPerLeak());
			break;
		}
	}

	public static void runNativeGlobalHandleLeak(int leaksPerBatch, int pauseInMsBetweenBatches, int totalNumberOfBatches)
			throws InterruptedException {

		for (int i = 0; i < totalNumberOfBatches; i++) {
			NativeLeaks.leakGlobalHandle(leaksPerBatch);
			Thread.sleep(pauseInMsBetweenBatches);
			System.out.println("Total leaked objects " + (i + 1) * leaksPerBatch);
		}
	}

	public static void runNativeMallocLeak(int leaksPerBatch, int pauseInMsBetweenBatches, int totalNumberOfBatches, int bytesPerLeak)
			throws InterruptedException {

		for (int i = 0; i < totalNumberOfBatches; i++) {
			for (int j = 0; j < leaksPerBatch; j++) {
				NativeLeaks.leakMallocedMemory(bytesPerLeak);
			}
			Thread.sleep(pauseInMsBetweenBatches);
			System.out.println("Total leaked memory " + ((i + 1) * leaksPerBatch * bytesPerLeak) + " bytes");
		}
	}
}
