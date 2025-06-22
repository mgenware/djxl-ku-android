# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("djxlku")

set(JNI_LIBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/lib_include)

set(
        LIB_NAMES
        libdjxlx
)

foreach (lib_name ${LIB_NAMES})
    add_library(
            ${lib_name}
            SHARED
            IMPORTED)
    set_target_properties(
            ${lib_name}
            PROPERTIES
            IMPORTED_LOCATION
            ${JNI_LIBS_DIR}/${lib_name}.so)
endforeach ()

add_library(${CMAKE_PROJECT_NAME} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        djxlku.cpp)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log
        ${LIB_NAMES}
)