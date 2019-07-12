# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

set(LATIN_IME_JNI_SRC_FILES
        com_android_inputmethod_keyboard_ProximityInfo.cpp
        com_android_inputmethod_latin_BinaryDictionary.cpp
        com_android_inputmethod_latin_BinaryDictionaryUtils.cpp
        com_android_inputmethod_latin_DicTraverseSession.cpp
        jni_common.cpp)

add_library(jni_latinime SHARED ${LATIN_IME_JNI_SRC_FILES})

target_link_libraries(jni_latinime PRIVATE latinime_core)