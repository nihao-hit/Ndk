//
// Created by nihao on 2018/4/26.
//
#include <jni.h>

JNIEXPORT jdouble JNICALL
Java_com_example_nihao_ndk_CalculatorHelper_add(JNIEnv *env, jobject instance, jdouble num1,
                                                jdouble num2) {

    // TODO
    return (num1 + num2);

}
JNIEXPORT jdouble JNICALL
Java_com_example_nihao_ndk_CalculatorHelper_sub(JNIEnv *env, jobject instance, jdouble num1,
                                                jdouble num2) {

    // TODO
    return (num1 - num2);

}
JNIEXPORT jdouble JNICALL
Java_com_example_nihao_ndk_CalculatorHelper_mul(JNIEnv *env, jobject instance, jdouble num1,
                                                jdouble num2) {

    // TODO
    return (num1 * num2);

}
JNIEXPORT jdouble JNICALL
Java_com_example_nihao_ndk_CalculatorHelper_div(JNIEnv *env, jobject instance, jdouble num1,
                                                jdouble num2) {

    // TODO
    return (num1 / num2);

}