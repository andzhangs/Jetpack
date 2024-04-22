#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_zs_android_jetpack_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}