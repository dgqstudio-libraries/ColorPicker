# ColorPicker

This is a custom component for Android that allows you to select a custom color.

## Requeriments

- API 24 or more.

## Setup dependencies

### Gradle

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.dgqstudio-libraries:ColorPicker:1.0.0'
}
```

### Maven

```xml
<!-- <repositories> section of pom.xml -->
<repository>
    <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>

<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.dgqstudio-libraries</groupId>
    <artifactId>ColorPicker</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```xml
<com.dgqstudio.colorpicker.ColorPicker
    android:id="@+id/colorPicker"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

</com.dgqstudio.colorpicker.ColorPicker>
```

```kt
// Get color programatically
val selectedColor = colorPicker.getSelectedColorHex()
```

## ColorPicker attributes

| Attribute              | Format          | Default value |
|------------------------|-----------------|---------------|
| initialColor           | reference/color | null          |
| showColorPickedPointer | boolean         | true          |
