#include <jni.h>
#include <iostream>

#ifdef _WIN32
#define EXPORT __declspec(dllexport)
#else
#define EXPORT
#endif

extern "C" {
    EXPORT JNIEXPORT void JNICALL Java_se_hirt_memoryleaks_Main_sayHello(JNIEnv* env, jobject obj) {
        #ifdef _WIN32
        std::cout << "Hello, World from C++ on Windows!" << std::endl;
        #elif __APPLE__
        std::cout << "Hello, World from C++ on macOS!" << std::endl;
        #else
        std::cout << "Hello, World from C++ on Linux!" << std::endl;
        #endif
    }
}
