#include <iostream>
#include <jni.h>

#ifdef _WIN32
#define EXPORT __declspec(dllexport)
#else
#define EXPORT
#endif

extern "C" {
    EXPORT JNIEXPORT void JNICALL
    Java_se_hirt_memoryleaks_RunLeak_sayHello(JNIEnv *env, jclass clazz) {
#ifdef _WIN32
      std::cout << "Native leaks provided by library on Windows!" << std::endl;
#elif __APPLE__
      std::cout << "Native leaks provided by library on macOS!" << std::endl;
#else
      std::cout << "Native leaks provided by library on Linux!" << std::endl;
#endif
    }

    EXPORT JNIEXPORT void JNICALL
    Java_se_hirt_memoryleaks_NativeLeaks_leakGlobalHandle(JNIEnv *env, jclass clazz, jint count) {
      if (count <= 0) {
        std::cout << "Argument for leakGlobalHandle must be positive. It was " << count << "." << std::endl;
        return;
      }
      for (int i = 0; i < count; i++) {
        // Create a new string object
        jstring str = env->NewStringUTF("This is a leaked global reference");

        // Create a global reference to the string without releasing it
        jobject global_ref = env->NewGlobalRef(str);

        // We're not storing the global_ref anywhere or releasing it,
        // so this will leak memory each time the function is called
      }
      std::cout << "Leaked " << count << " global handle(s)" << std::endl;
    }

    EXPORT JNIEXPORT void JNICALL
    Java_se_hirt_memoryleaks_NativeLeaks_leakMallocedMemory(JNIEnv *env, jclass clazz, jint bytes) {
      if (bytes <= 0) {
        std::cout << "Argument for leakMallocedMemory must be positive. It was " << bytes << "." << std::endl;
        return;
      }

      // Allocate memory without freeing it
      void* leaked_memory = malloc(bytes);

      if (leaked_memory == nullptr) {
        std::cout << "Failed to allocate " << bytes << " bytes." << std::endl;
        return;
      }

      std::cout << "Leaked " << bytes << " bytes of malloc'd memory" << std::endl;
    }
}
