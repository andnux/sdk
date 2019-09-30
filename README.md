### 快速集成：
- **Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- **Step 2.** Add the dependency
```groovy
dependencies {
    implementation 'com.github.andnux:sdk:0.0.14'
    or
    implementation 'com.github.andnux.sdk:language:0.0.14'
    implementation 'com.github.andnux.sdk:ui:0.0.14'
    implementation 'com.github.andnux.sdk:compat:0.0.14'
    implementation 'com.github.andnux.sdk:skin:0.0.14'
    implementation 'com.github.andnux.sdk:net:0.0.14'
    implementation 'com.github.andnux.sdk:zbarui:0.0.14'
    implementation 'com.github.andnux.sdk:adapter:0.0.14'
    implementation 'com.github.andnux.sdk:base:0.0.14'
    implementation 'com.github.andnux.sdk:volley:0.0.14'
    implementation 'com.github.andnux.sdk:utils:0.0.14'
    implementation 'com.github.andnux.sdk:mvp:0.0.14'
    implementation 'com.github.andnux.sdk:sqlite:0.0.14'
    implementation 'com.github.andnux.sdk:json:0.0.14'
    implementation 'com.github.andnux.sdk:web:0.0.14'
    implementation 'com.github.andnux.sdk:mvvm:0.0.14' 
}
```