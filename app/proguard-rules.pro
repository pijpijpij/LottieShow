# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Pierrejean\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more label, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


########################################################################################################################
## Retrolambda
########################################################################################################################
-dontwarn java.lang.invoke.*

########################################################################################################################
# Proguard bug includes the old HTTP library
########################################################################################################################
# We just ignore the warnmings
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

########################################################################################################################
# Dagger 2
########################################################################################################################
# That's a bug in Dagger 2.10 (see https://github.com/google/dagger/issues/645)
# CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue

########################################################################################################################
# RxJava
########################################################################################################################
# The rxjava library depends on sun.misc.Unsafe, which is unavailable on Android
# The rxjava team is aware of this, and mention in the docs that they only use
# the unsafe functionality if the platform supports it.
#  - ReactiveX/RxJava#1415 (comment)
#  - https://github.com/ReactiveX/RxJava/blob/1.x/src/main/java/rx/internal/util/unsafe/UnsafeAccess.java#L23
-dontwarn rx.internal.util.unsafe.**