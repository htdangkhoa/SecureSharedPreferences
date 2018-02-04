# SecureSharedPreferences [![](https://jitpack.io/v/htdangkhoa/SecureSharedPreferences.svg)](https://jitpack.io/#htdangkhoa/SecureSharedPreferences)

This project is a one of solutions to encrypt data using Shared Preferences.
  
## Installation
Add the JitPack repository to your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency:
```gradle
dependencies {
	compile 'com.github.htdangkhoa:SecureSharedPreferences:1.1.0'
}
```

## Usage
```java
/**
* Initialize secure shared preferences.
**/
new Prefs()
    .setContext(this)
    .setPassword("password")
    .setFilename("example_filename")
    .build();
    
/**
* Easy to use.
**/
Prefs.putString("hello", "world");
String s = Prefs.getString("hello", null);

/**
* Save list.
**/
List<CustomObject> customObjects = new ArrayList<>();
customObjects.add(...);
Prefs.putListObject("list", customObjects);

List<Object> objects = Prefs.getListObject("list", CustomObject.class, null);
List<CustomObject> list = (List<CustomObject>)(List<?>) new ArrayList<>(objects); 
```

## XML using Standard Android SharedPreferences
```xml
<map>
    <string name="hello">world</string>
</map>
```
## XML using Secure Shared Preferences
```xml
<map>
	<string name="ssvYPxb3mE+ebytZKoRIuMqrwn0kIFasENYlY/P6Muw=">uij9OxpVOB+N9mvLketIjQ==:dfYsnMagXw4DDeqmA2EnXocPJu8nsVp9WP2Zj0k+JUc=:XjVBOMAViOv4WEZme2+bbw==</string>
</map>
```
