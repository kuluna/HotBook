# app
-keep class jp.kuluna.hotbook.models.** { *; }

# general
-dontwarn javax.annotation.**

# kotlin
-dontwarn kotlin.reflect.jvm.internal.**

# okhttp
-dontwarn okhttp3.**
-dontwarn okio.**

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
