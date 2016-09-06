# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.easynetwork.weather.bean.** {*;}

#-keep class com.easynetwork.ad.bean.** {*;}

#-keep class com.easynetwork.ad.manager.**

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#-----------EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#-----------ormLite
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** {*;}

#-----------okio.Okio.**
#-dontwarn okio.Okio.**
#-keep class okio.Okio.** {*;}
-dontwarn java.nio.file.**
-keep class java.nio.file.**
-dontwarn org.codehaus.**
-keep class org.codehaus.**

#-----------多盟
-dontwarn cn.domob.android.**
-keep class cn.domob.android.** {*;}
#-----------百度
-keep public class com.baidu.appx.**
-keep public class com.baidu.appx.** { *; }
#-keepattributes *Annotation*
-keepattributes *Exceptions*
#-keepattributes Signature
#-----------腾讯
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}
#-----------京东
-keep class com.jd.**



#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

#-keep public class * extends android.view.View{
    #*** get*();
    #void set*(***);
#    public &lt;init&gt;(android.content.Context);
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet);
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet, int);
#}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#-keepclasseswithmembers class * {
    #public &lt;init&gt;(android.content.Context, android.util.AttributeSet);
    #public &lt;init&gt;(android.content.Context, android.util.AttributeSet, int);
#}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------
