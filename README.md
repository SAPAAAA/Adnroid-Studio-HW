# Image Loader Android App (Kotlin Version)

This Android application demonstrates loading an image from a user-provided URL using various Android components and concepts, implemented natively in **Kotlin**. It fulfills the specific requirements of the assignment, including the use of `AsyncTask`, `AsyncTaskLoader`, `BroadcastReceiver`, `Service`, and `Notifications`.

## Features

* Load image from URL using `AsyncTask` (initial implementation).
* Load image from URL using `AsyncTaskLoader` (refactored implementation, handles configuration changes).
* Monitor network connectivity using a manifest-declared `BroadcastReceiver` (`CONNECTIVITY_ACTION`).
* Disable/enable image loading based on network status via a listener pattern.
* Display status messages (Loading, Success, Error, No Internet, Invalid URL).
* Run a background `Service` that periodically shows a `Notification`.
* Handles necessary `INTERNET`, `ACCESS_NETWORK_STATE`, and `POST_NOTIFICATIONS` permissions (runtime request for notifications).
* Written natively in Kotlin, leveraging features like null safety, lambdas, and objects/companion objects.
* Uses **View Binding** for safe and efficient access to UI elements.

**Important Note:** While this project uses `AsyncTask` and `AsyncTaskLoader` as required by the assignment, these APIs are **deprecated** and **not recommended** for modern Android development. The standard, preferred approach in Kotlin involves using **Coroutines**, **ViewModel**, and **LiveData/Flow** for managing background tasks, lifecycle awareness, and UI updates. Libraries like **Retrofit/Ktor** for networking and **Coil/Glide** for image loading significantly simplify these processes.

## How to Run

1.  Clone this repository.
2.  Open the project in Android Studio (ensure Kotlin plugin is up-to-date).
3.  Ensure `viewBinding { enabled = true }` is present in the `app/build.gradle` (or `.kts`) file's `android > buildFeatures` block.
4.  Let Gradle sync the project dependencies.
5.  Connect an Android device or start an emulator (API 21+).
6.  Build and run the application (Shift + F10 or Run -> Run 'app').
7.  **Grant Notification Permission:** If running on Android 13+, the app will request notification permission when first launched. Grant it to allow the background service notifications.
8.  Enter a valid image URL (e.g., `https://...`) into the text field.
9.  Click the "Load Image" button.
10. Observe the status messages and the loaded image (or error state).
11. Test network changes by turning Wi-Fi/Mobile Data on/off in the device/emulator settings. The load button should enable/disable accordingly.
12. Observe the background service notification appearing periodically (default: every 5 minutes). Click it to reopen the app.

## Implementation Details (Kotlin)

* **UI (`MainActivity.kt`, `activity_main.xml`)**:
    * `ConstraintLayout` defines the UI structure.
    * `MainActivity.kt` uses **View Binding** (`ActivityMainBinding`) for type-safe view access. It orchestrates UI events, network checks, permission requests, service starting, and initiates image loading via `AsyncTask` or `AsyncTaskLoader`.
* **Asynchronous Loading**:
    * `ImageLoadAsyncTask.kt`: Initial implementation using the deprecated `AsyncTask`. Communicates results via the `ImageLoadListener` interface. Includes deprecation warnings and notes recommending Coroutines.
    * `ImageLoader.kt`: Refactored implementation using `AsyncTaskLoader`, managed by `LoaderManager` in `MainActivity`. Handles configuration changes better than `AsyncTask`. Includes notes recommending ViewModel/LiveData/Flow/Coroutines.
* **Internet Connection Handling (`NetworkChangeReceiver.kt`, `NetworkUtils.kt`)**:
    * `NetworkUtils.kt`: Utility `object` to check network state using `ConnectivityManager`.
    * `NetworkChangeReceiver.kt`: Manifest-declared `BroadcastReceiver` listening for `CONNECTIVITY_ACTION`. Uses a static listener pattern (`NetworkStateListener` interface in `companion object`) to notify `MainActivity`. *Note: Dynamic registration or reactive patterns (LiveData/Flow) are generally preferred for UI updates based on connectivity.*
* **Service with Notification (`NotificationService.kt`, `NotificationUtils.kt`)**:
    * `NotificationService.kt`: A started `Service` using a `Handler` to post a delayed `Runnable` for periodic notification display. Includes notes recommending `WorkManager` for reliable background tasks.
    * `NotificationUtils.kt`: Utility `object` to create the notification channel (API 26+) and build/show notifications using `NotificationCompat`. Includes the runtime permission check logic within `showNotification`.
* **Permissions (`AndroidManifest.xml`, `MainActivity.kt`)**:
    * Manifest declares `INTERNET`, `ACCESS_NETWORK_STATE`, `POST_NOTIFICATIONS`, and potentially `FOREGROUND_SERVICE` (if adapted).
    * `MainActivity.kt` handles the runtime request for `POST_NOTIFICATIONS` on Android 13+ using `registerForActivityResult`.

## Code Style & Conventions
* Written following Kotlin coding conventions.
* Uses `val` where possible, explicit nullability (`?`), safe calls (`?.`), scope functions (`let`, `apply`), and lambdas.
* Includes KDoc comments explaining key components and highlighting assignment constraints versus modern best practices.
