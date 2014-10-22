AspectJ Android Example
=======================

#### Dependency
* https://github.com/uPhyca/gradle-android-aspectj-plugin
* https://github.com/nostra13/Android-Universal-Image-Loader
* http://www.eclipse.org/aspectj/

#### Running
You can open the project with Android Studio and run

or

Run with Gradle on commandline
```
./gradlew assemble installDebug
```
#### Aspects
###### AuthenticationAspect
 Basic usage of aspect for auth-based method call
###### CachingAspect
 Fetch an image and implemented simple caching using aspect
###### ExceptionHandlingAspect
 Catch all exception and drive your application to specific errors
###### LoggingAspect
 Logging all actions
###### ProfilingAspect
 Measure elapsed time for async call and fetching image
