package se.hirt.memoryleaks;

import java.util.Arrays;

public class LeakConfiguration {
	public enum LeakType {
		NATIVE_GLOBAL_HANDLE,
		NATIVE_MALLOC
	}

	private LeakType leakType = LeakType.NATIVE_GLOBAL_HANDLE;
	private int leaksPerBatch = 1000;
	private int pauseInMsBetweenBatches = 250;
	private int totalNumberOfBatches = Integer.MAX_VALUE;
	private int bytesPerLeak = 1024;

	public static LeakConfiguration parseArguments(String[] args) {
		LeakConfiguration config = new LeakConfiguration();

		if (args.length == 0) {
			return config;
		}

		if (args[0].equals("-help")) {
			printHelp();
			System.exit(0);
		}

		try {
			config.leakType = LeakType.valueOf(args[0].toUpperCase());

			switch (config.leakType) {
			case NATIVE_GLOBAL_HANDLE:
				config.parseOptionalArgs(args, 1, 3);
				break;
			case NATIVE_MALLOC:
				config.parseOptionalArgs(args, 1, 4);
				break;
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid leak type or arguments. Use -help for usage information.");
			System.exit(1);
		}

		return config;
	}

	private void parseOptionalArgs(String[] args, int startIndex, int maxArgs) {
		for (int i = startIndex; i < Math.min(args.length, startIndex + maxArgs); i++) {
			try {
				int value = Integer.parseInt(args[i]);
				switch (i - startIndex) {
				case 0:
					leaksPerBatch = value;
					break;
				case 1:
					pauseInMsBetweenBatches = value;
					break;
				case 2:
					totalNumberOfBatches = value;
					break;
				case 3:
					if (leakType == LeakType.NATIVE_MALLOC) {
						bytesPerLeak = value;
					}
					break;
				}
			} catch (NumberFormatException e) {
				System.err.println("Invalid argument: " + args[i] + ". Expected an integer.");
				System.exit(1);
			}
		}
	}

	private static void printHelp() {
		System.out.println("Usage: java -jar java-memoryleaks.jar [LEAK_TYPE] [ARGS...]");
		System.out.println("Available leak types:");
		System.out.println("  NATIVE_GLOBAL_HANDLE [leaksPerBatch] [pauseInMsBetweenBatches] [totalNumberOfBatches]");
		System.out.println("    - leaksPerBatch: Number of leaks per batch (default: 1000)");
		System.out.println("    - pauseInMsBetweenBatches: Pause in ms between batches (default: 250)");
		System.out.println("    - totalNumberOfBatches: Total number of batches (default: Integer.MAX_VALUE)");
		System.out.println("  NATIVE_MALLOC [leaksPerBatch] [pauseInMsBetweenBatches] [totalNumberOfBatches] [bytesPerLeak]");
		System.out.println("    - leaksPerBatch: Number of leaks per batch (default: 1000)");
		System.out.println("    - pauseInMsBetweenBatches: Pause in ms between batches (default: 250)");
		System.out.println("    - totalNumberOfBatches: Total number of batches (default: Integer.MAX_VALUE)");
		System.out.println("    - bytesPerLeak: Number of bytes to leak per malloc call (default: 1024)");
		System.out.println("\nAll arguments are optional. You can specify just the leak type to use defaults, or provide as many arguments as you want to override the defaults.");
	}

	// Getters
	public LeakType getLeakType() { return leakType; }
	public int getLeaksPerBatch() { return leaksPerBatch; }
	public int getPauseInMsBetweenBatches() { return pauseInMsBetweenBatches; }
	public int getTotalNumberOfBatches() { return totalNumberOfBatches; }
	public int getBytesPerLeak() { return bytesPerLeak; }
}
