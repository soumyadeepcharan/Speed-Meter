-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

-keep class kotlin.coroutines.Continuation
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
-dontwarn kotlinx.coroutines.**

-keep class androidx.datastore.*.** { *; }

-keepclassmembers class com.sdcgroup.speedmeter.data.Speed { *; }
-keepclassmembers class com.sdcgroup.speedmeter.data.HumanSpeed { *; }

-keep class com.sdcgroup.speedmeter.R$id { *; }
-keep class com.sdcgroup.speedmeter.R$layout { *; }

-keep public class com.sdcgroup.speedmeter.MainActivity
-keep public class com.sdcgroup.speedmeter.service.SpeedMonitorService

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-repackageclasses ''
-allowaccessmodification
