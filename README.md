# djxl-ku-android

djxl (from libjxl) on Android.

- Min SDK: 26
- ABIs: arm64-v8a, x86_64
- The version of this library is the version of libjxl used to build it.

| iOS / macOS                                                  | Android                                                        |
|--------------------------------------------------------------|----------------------------------------------------------------|
| [djxl-ku-darwin](https://github.com/mgenware/djxl-ku-darwin) | [djxl-ku-android](https://github.com/mgenware/djxl-ku-android) |

## Installation

```kotlin
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation("com.mgenware.djxlku:djxlku:0.11.1")
}
```

## Usage

```kotlin
val djxlKu = DjxlKu();
// args: djxl arguments.
val args = arrayOf<String>(
    inputFile,
    outFile
)
val retCode = djxlKu.run(args)
```
