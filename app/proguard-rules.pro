# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
# }

-keepattributes Exceptions
-keepattributes SourceFile,LineNumberTable
-dontwarn com.squareup.picasso.**
-dontwarn org.jetbrains.anko.internals.**

## ref: http://126kr.com/article/610x7fk26ii
# Retrofit
-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.** *;
}
-keepclassmembers class * {
    @retrofit.** *;
}

# gson
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class cn.six.payx.entity.** { *; }

##---------------End: proguard configuration for Gson  ----------

-keep class android.support.** { *; }
-keep class com.nostra13.universalimageloader.** {*;}
-keep class kotlin.** {*;}
-keep class kotlin.Metadata { *; }
-keep class rx.** {*;}

# --- ---
# https://github.com/SnowdreamFramework/ToyBricks/blob/master/gradle/proguard/proguard-android-app-optimize.txt

# General Options
-verbose

# Suppress duplicate warning for system classes;  Blaze is passing android.jar
# to proguard multiple times.
-dontnote android.**
-dontnote dalvik.**
-dontnote com.android.**
-dontnote google.**
-dontnote com.google.**
-dontnote java.**
-dontnote javax.**
-dontnote junit.**
-dontnote org.apache.**
-dontnote org.json.**
-dontnote org.w3c.dom.**
-dontnote org.xml.sax.**
-dontnote org.xmlpull.v1.**


# Stop warnings about missing unused classes
-dontwarn android.**
-dontwarn dalvik.**
-dontwarn com.android.**
-dontwarn google.**
-dontwarn com.google.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn junit.**
-dontwarn org.apache.**
-dontwarn org.json.**
-dontwarn org.w3c.dom.**
-dontwarn org.xml.sax.**
-dontwarn org.xmlpull.v1.**

# Input/Output Options
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# Keep Options
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    <init>(...);
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Shrinking Options
# -dontshrink

# Optimization Options
# -dontoptimize
-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable,!class/unboxing/enum

# In the optimization step, ProGuard will then remove calls to such methods, if it can determine that the return values aren't used.

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#Specifies that the access modifiers of classes and class members may be broadened during processing.
#Counter-indication: you probably shouldn't use this option when processing code that is to be used as a library, since classes and class members that weren't designed to be public in the API may become public.
-allowaccessmodification

# Obfuscation Options
#-dontobfuscate
-dontusemixedcaseclassnames

#For app
#-keepattributes *Annotation*

# Preverification Options
# Preverification is irrelevant for the dex compiler and the Dalvik VM, so we can switch it off with the -dontpreverify option.
-dontpreverify