# java-memoryleaks

This project contains examples of various kinds of memory leaks.

## Building the Project

The project can simulate various different kinds of leaks that can occur in a Java program, including various kinds of
native memory leaks. It contains a native library to cause these leaks, and for this to properly build, a c compiler
must be set up. How to do this varies with the platform.

On Windows, with Visual Studio installed, you can set the proper path by running:

```bat
"C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"   
```

Once everything is properly setup, it should be possible to build it using:

```bash
mvn clean package
```

## Running the Leaks

To run the default leak, simply run:

```bash
java -Xmx32m -jar target\java-memoryleaks-1.0-SNAPSHOT.jar
```

To list the available leaks, run:

```bash
java -Xmx32m -jar target\java-memoryleaks-1.0-SNAPSHOT.jar -help
```

To run the default leak with the flight recorder and allowing [JDK Mission Control](https://github.com/openjdk/jmc) to
use local attach (and to get reference chains for the OldObjectSample events etc) run:

```bash
java -Xmx32m -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:StartFlightRecording=disk=true,settings=profile,dumponexit=true,name=Leak,jdk.OldObjectSample#cutoff=30s,filename=recording.jfr -Djdk.attach.allowAttachSelf=true -jar target\java-memoryleaks-1.0-SNAPSHOT.jar
```

Then connect with JDK Mission Control and dump the whole recording after half a minute or so.
