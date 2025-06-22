#include <jni.h>
#include <string>
#include <libdjxlx/djxlx.h>

extern "C"
JNIEXPORT jint JNICALL
Java_com_mgenware_djxlku_DjxlKu_djxlmain(JNIEnv *env, jobject thiz, jobjectArray argv) {
    int argc = env->GetArrayLength(argv);
    typedef char *pchar;
    pchar *argvPtr = new pchar[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring)env->GetObjectArrayElement(argv, i); //A Java string
        const char *pjc = env->GetStringUTFChars(js, nullptr); //A pointer to a Java-managed char buffer
        size_t jslen = strlen(pjc);
        argvPtr[i] = new char[jslen + 1]; //Extra char for the terminating null
        strcpy(argvPtr[i],
               pjc); //Copy to *our* buffer. We could omit that, but IMHO this is cleaner. Also, const correctness.
        env->ReleaseStringUTFChars(js, pjc);
    }

    int res = djxl_main(argc, argvPtr);

    // Clean up.
    for (i = 0; i < argc; i++)
        delete[] argvPtr[i];
    delete[] argvPtr;
    return res;
}