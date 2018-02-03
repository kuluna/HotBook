# app
-keep class jp.kuluna.hotbook.models.** { *; }

# general
-dontwarn javax.annotation.**

# kotlin
-dontwarn kotlin.reflect.jvm.internal.**

# okhttp
-dontwarn okhttp3.**
-dontwarn okio.**

# picasso
-dontwarn com.squareup.okhttp.**

# retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
