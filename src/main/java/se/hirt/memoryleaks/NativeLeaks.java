package se.hirt.memoryleaks;

public class NativeLeaks {
	/**
	 * Will leak a specified number of global handles.
	 *
	 * @param count
	 * 		the number of handles to leak.
	 */
	public static native void leakGlobalHandle(int count);

	public static native void leakMallocedMemory(int count);
}
