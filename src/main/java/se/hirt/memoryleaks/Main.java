package se.hirt.memoryleaks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	private final static String LIBRARY_NAME = "memleak";

	static {
		NativeUtils.loadLibrary(LIBRARY_NAME);
	}

	private native void sayHello();

	public static void main(String[] args) {
		new Main().sayHello();
	}
}
