# Moviespot
A movie collection android application that allows users to view a list of movies, view detailed informaation of a selected movie, and add a movie to their favorite collection and mange favorite movies

### Prerequisites

What things you need to run the project

```
Android Studio 3.5 or higher
Android SDK
Android Emulator or Android Phone with USB Debugging allowed
Android version 6.0 or higher
```

### Dependencies Used

Dependencies used are already included in the build.gradle(app level) file.
Dependencies will be downloaded automatically when opening the project for the first time. with internet connection of course

```
//Retrofit
    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
    implementation 'com.squareup.retrofit2:retrofit:2.4.0'
    
//Picasso
    implementation 'com.squareup.picasso:picasso:2.71828'

//Circular imaging
    implementation 'de.hdodenhof:circleimageview:3.0.0'

//Flexbox for wrapping items in a layout
    implementation 'com.google.android:flexbox:2.0.1'
```

### Issues Faced

When developing this software, i faced TWO problems from the HTTP response i was receiving.

#### 1.
First was the spelling of one single element in the JSON object. instead of the object being named `videos`, it was named `vidoes`. and that took me a long time to figure out what was really generating an error when running the project.

### 2.
The Second issue was the Page description. Information that was supposed to let me know if i was fetching from the last page or first page were always set to same value across every page.
```
"pageable":"INSTANCE",
"totalPages":1,
"totalElements":10,
"last":true,
"first":true,
"sort":{"sorted":false,"unsorted":true,"empty":true},
"numberOfElements":10,
"size":10,
"number":0,
"empty":false
```

the elements `first` and `last` were both set to `true` in every page i checked.
also `totalPages` was not really telling me the right number of pages available.
