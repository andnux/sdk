### 快速集成：
- **Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.andnux:ChunLin:0.0.11'
    or
    implementation 'com.github.andnux.ChunLin:utils:0.0.11'
    implementation 'com.github.andnux.ChunLin:glide:0.0.11'
    implementation 'com.github.andnux.ChunLin:ui:0.0.11'
    implementation 'com.github.andnux.ChunLin:adapter:0.0.11'
    implementation 'com.github.andnux.ChunLin:mvvm:0.0.11'
    implementation 'com.github.andnux.ChunLin:zbar:0.0.11'
    implementation 'com.github.andnux.ChunLin:mvp:0.0.11'
    implementation 'com.github.andnux.ChunLin:http:0.0.11'
    implementation 'com.github.andnux.ChunLin:base:0.0.11'
    implementation 'com.github.andnux.ChunLin:compat:0.0.11'
    implementation 'com.github.andnux.ChunLin:web:0.0.11'
}
```