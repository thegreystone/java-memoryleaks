package se.hirt.memoryleaks;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeUtils {
	/**
	 * Convenience method to make it possible to load native libraries that exist in a jar.
	 * @param libraryName the name of the library to load.
	 * @throws IOException if there was a problem writing the library out to temp from the jar, or reading it back again.
	 */
	public static void loadLibraryFromJar(String libraryName) throws IOException {
		String osName = System.getProperty("os.name").toLowerCase();
		String osArch = System.getProperty("os.arch").toLowerCase();
		String libExtension = osName.contains("win") ? ".dll" : osName.contains("mac") ? ".dylib" : ".so";
		String libFullName = libraryName + libExtension;

		Logger.getLogger(NativeUtils.class.getName()).log(Level.INFO, "Attempting to load library " + libFullName + " from jar");

		try (InputStream in = Main.class.getResourceAsStream("/" + libFullName)) {
			if (in == null) {
				Logger.getLogger(NativeUtils.class.getName()).log(Level.SEVERE, "Could not find library " + libFullName + " in the jar");
				throw new RuntimeException("Library " + libFullName + " not found in JAR");
			}

			File temp = File.createTempFile(libraryName, libExtension);
			temp.deleteOnExit();

			try (OutputStream out = new FileOutputStream(temp)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
			System.load(temp.getAbsolutePath());
			Logger.getLogger(NativeUtils.class.getName()).log(Level.INFO, "Library " + libFullName + " successfully loaded");
		}
	}

	/**
	 * Will first attempt to load the library from the library path, then attempt to load it from within the jar.
	 * @param libraryName
	 */
	public static void loadLibrary(String libraryName) {
		try {
			Logger.getLogger(NativeUtils.class.getName()).log(Level.INFO, "Attempting to load library " + libraryName);
			System.loadLibrary(libraryName);
			Logger.getLogger(NativeUtils.class.getName()).log(Level.INFO, "Loaded library " + libraryName);
		} catch (UnsatisfiedLinkError e) {
			Logger.getLogger(NativeUtils.class.getName()).log(Level.SEVERE, "Failed to load library " + libraryName);
			try {
				NativeUtils.loadLibraryFromJar(libraryName);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

	}

}
