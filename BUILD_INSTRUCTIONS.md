# تعليمات البناء - Aronium POS

هذا الدليل يوضح كيفية بناء وتشغيل تطبيق Aronium POS من الكود المصدري.

## 📋 المتطلبات المسبقة

### 1. تثبيت Java Development Kit (JDK)
```bash
# تحقق من إصدار Java
java -version

# يجب أن يكون الإصدار 17 أو أحدث
```

### 2. تحميل وتثبيت Android Studio
- احميل Android Studio من [developer.android.com](https://developer.android.com/studio)
- ثبت Android Studio مع SDK
- تأكد من تثبيت Android SDK Build-Tools 34.0.0

### 3. إعداد Android SDK
```bash
# إضافة متغيرات البيئة (Linux/Mac)
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Windows
set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\emulator
set PATH=%PATH%;%ANDROID_HOME%\tools
set PATH=%PATH%;%ANDROID_HOME%\tools\bin
set PATH=%PATH%;%ANDROID_HOME%\platform-tools
```

## 🔧 إعداد المشروع

### 1. استنساخ المشروع
```bash
git clone <repository-url>
cd aronium-pos
```

### 2. إنشاء ملف local.properties
```bash
# نسخ القالب
cp local.properties.template local.properties

# تعديل المسار ليشير إلى Android SDK
echo "sdk.dir=/path/to/your/Android/Sdk" > local.properties
```

### 3. منح صلاحيات التنفيذ
```bash
# Linux/Mac
chmod +x gradlew

# Windows
# لا حاجة لإجراء إضافي
```

## 🛠️ البناء

### طريقة 1: استخدام Android Studio

1. افتح Android Studio
2. اختر "Open an existing project"
3. اختر مجلد المشروع
4. انتظر حتى تكتمل مزامنة Gradle
5. اضغط على زر "Run" أو Shift+F10

### طريقة 2: استخدام سطر الأوامر

#### بناء تطبيق Debug
```bash
# Windows
gradlew assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

#### بناء تطبيق Release
```bash
# Windows
gradlew assembleRelease

# Linux/Mac
./gradlew assembleRelease
```

#### تشغيل الاختبارات
```bash
# Windows
gradlew test

# Linux/Mac
./gradlew test
```

#### فحص الكود (Lint)
```bash
# Windows
gradlew lint

# Linux/Mac
./gradlew lint
```

## 📱 التثبيت والتشغيل

### طريقة 1: تثبيت مباشر عبر ADB
```bash
# تأكد من تمكين "خيارات المطور" و "تصحيح USB"
adb install app/build/outputs/apk/debug/app-debug.apk
```

### طريقة 2: استخدام Gradle
```bash
# Windows
gradlew installDebug

# Linux/Mac
./gradlew installDebug
```

### طريقة 3: استخدام المحاكي

1. افتح Android Studio
2. Tools → AVD Manager
3. إنشاء جهاز افتراضي جديد أو استخدام موجود
4. تشغيل المحاكي
5. تشغيل التطبيق من Android Studio

## 🔍 استكشاف المشاكل الشائعة

### 1. خطأ "SDK location not found"
```bash
# إنشاء ملف local.properties
echo "sdk.dir=/path/to/your/Android/Sdk" > local.properties
```

### 2. خطأ "Gradle sync failed"
```bash
# تنظيف وإعادة البناء
./gradlew clean
./gradlew build
```

### 3. خطأ "Failed to install APK"
```bash
# إلغاء تثبيت الإصدار السابق
adb uninstall com.aronium.pos
# إعادة التثبيت
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 4. خطأ "Java version"
- تأكد من استخدام JDK 17 أو أحدث
- تحديث متغير JAVA_HOME

### 5. خطأ "Build Tools version"
- افتح Android Studio
- Tools → SDK Manager
- تأكد من تثبيت Build Tools الإصدار المطلوب

## 🔧 إعدادات البناء المتقدمة

### تخصيص Build Variants
```gradle
android {
    buildTypes {
        debug {
            debuggable true
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
        }
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
}
```

### إعداد التوقيع (Signing)
```gradle
android {
    signingConfigs {
        release {
            storeFile file("your-release-key.keystore")
            storePassword "your-store-password"
            keyAlias "your-key-alias"
            keyPassword "your-key-password"
        }
    }
}
```

## 📊 معلومات البناء

### ملفات الإخراج
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`
- **تقارير Lint**: `app/build/reports/lint-results.html`
- **تقارير الاختبارات**: `app/build/reports/tests/`

### إحصائيات البناء
- **حجم APK تقريبي**: 15-25 MB
- **وقت البناء**: 2-5 دقائق (حسب قوة الجهاز)
- **API المستهدف**: 34
- **أدنى API مدعوم**: 24

## 🚀 النشر

### إعداد Play Store
1. إنشاء keystore للتوقيع
2. بناء release APK موقع
3. اختبار APK على أجهزة مختلفة
4. رفع إلى Google Play Console

### إعداد GitHub Actions
```yaml
# .github/workflows/android.yml موجود بالفعل
# يقوم ببناء التطبيق تلقائياً عند push
```

## 🔧 أدوات مساعدة

### سكريبت البناء السريع
```bash
#!/bin/bash
# build.sh
echo "بناء Aronium POS..."
./gradlew clean
./gradlew assembleDebug
echo "تم الانتهاء! APK متاح في app/build/outputs/apk/debug/"
```

### سكريپت التثبيت السريع
```bash
#!/bin/bash
# install.sh
./gradlew installDebug
adb shell am start -n com.aronium.pos/.MainActivity
echo "تم تثبيت وتشغيل التطبيق!"
```

## 📝 ملاحظات مهمة

1. **المساحة المطلوبة**: تأكد من وجود 5 GB مساحة حرة
2. **اتصال الإنترنت**: مطلوب لتحميل dependencies
3. **صلاحيات المطور**: فعل "Developer Options" و "USB Debugging"
4. **إصدار Gradle**: لا تحدث إعدادات Gradle بدون داع

## 🆘 الحصول على المساعدة

إذا واجهت مشاكل:

1. راجع [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
2. ابحث في [Issues](https://github.com/your-repo/issues)
3. افتح issue جديد مع تفاصيل المشكلة
4. راسلنا على: support@aroniumpos.com

---

**نجاح البناء يعني أن جميع الملفات صحيحة ومتوافقة! 🎉**