package com.example.imageloader

import ImageLoadListener
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.imageloader.databinding.ActivityMainBinding
import com.example.imageloader.loaders.ImageLoader
import com.example.imageloader.services.NotificationService
import com.example.imageloader.receivers.NetworkChangeReceiver
import com.example.imageloader.utils.NetworkUtils

class MainActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<Bitmap?>,     // Loader returns nullable Bitmap
    ImageLoadListener,                          // Listener for AsyncTask callbacks
    NetworkChangeReceiver.NetworkStateListener  // Listener for network changes
{
    private companion object {
        const val IMAGE_LOADER_ID = 101
        const val URL_KEY = "image_url"
        const val TAG = "MainActivity"
    }

    // View binding
    private lateinit var binding: ActivityMainBinding

    // Permission handling
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "Notification permission granted.")
                // Permission is granted. Start the service.
                startNotificationService()
            } else {
                Log.w(TAG, "Notification permission denied.")
                Toast.makeText(
                    this,
                    R.string.notification_permission_denied_message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Setup UI Listeners using View Binding ---
        binding.loadButton.setOnClickListener { loadImage() }

        // Set up button click listener
        binding.urlEditText.setOnEditorActionListener { _, actionId, _ ->
            // Handle "Done" action on the keyboard
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadImage()
                true // Consume the event
            } else {
                false
            }
        }
        
        // Check initial network state and update UI
        updateUIBasedOnConnectivity(NetworkUtils.isNetworkAvailable(this))
        // Register this Activity as the listener for network changes via the BroadcastReceiver
        NetworkChangeReceiver.setNetworkStateListener(this)
        
        // Check notification permission before starting the service (required for Android 13+)
        checkNotificationPermissionAndStartService()
        
        if (supportLoaderManager.getLoader<Bitmap?>(IMAGE_LOADER_ID) != null) {
            Log.d(TAG, "Reconnecting to existing loader ID: $IMAGE_LOADER_ID")
            // Show loading indicator as the loader is likely still running or finished
            binding.statusTextView.text = getString(R.string.status_loading)
            // initLoader will reconnect if it exists, otherwise onCreateLoader will be called
            supportLoaderManager.initLoader(IMAGE_LOADER_ID, null, this)
        } else {
            Log.d(TAG, "No existing loader found for ID: $IMAGE_LOADER_ID")
            binding.statusTextView.text = getString(R.string.status_idle) // Set initial state
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        NetworkChangeReceiver.setNetworkStateListener(null)
    }
    
    // --- Notification Permission & Service Start ---
    private fun checkNotificationPermissionAndStartService() {
        // Runtime permission check required only for Android 13 (API 33) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                // Check if permission is already granted
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    Log.i(TAG, "Notification permission already granted.")
                    startNotificationService() // Proceed to start the service
                }
                // Check if we should show an explanation (user denied previously but didn't check "Don't ask again")
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Log.i(TAG, "Showing rationale for notification permission.")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) // Request after rationale (example)
                }
                // Permission has not been asked yet or user checked "Don't ask again"
                else -> {
                    Log.i(TAG, "Requesting notification permission.")
                    // Request the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No runtime permission needed for notifications below Android 13
            Log.i(TAG, "Notification permission not required (Below Android 13).")
            startNotificationService()
        }
    }

    private fun startNotificationService() {
        Log.d(TAG, "Attempting to start NotificationService.")
        val serviceIntent = Intent(this, NotificationService::class.java)
        try {
            startService(serviceIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start NotificationService: ${e.message}", e)
            Toast.makeText(this, "Could not start background service.", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun loadImage() {
        val url = binding.urlEditText.text.toString()

        if (TextUtils.isEmpty(url) || !Patterns.WEB_URL.matcher(url).matches()) {
            binding.statusTextView.text = getString(R.string.status_invalid_url)
            binding.imageView.setImageDrawable(null) // Clear image view
            Log.w(TAG, "Invalid URL entered.")
            return
        }

        if (!NetworkUtils.isNetworkAvailable(this)) {
            binding.statusTextView.text = getString(R.string.status_no_internet)
            binding.imageView.setImageDrawable(null) // Clear image view
            Log.w(TAG, "No internet connection.")
            return
        }

        // Initialize the loader
        binding.imageView.setImageDrawable(null) // Clear previous image
        binding.statusTextView.text = getString(R.string.status_loading) // Set loading status

        //
        Log.i(TAG, "Using AsyncTaskLoader to load image...")
        val args = Bundle().apply { putString(URL_KEY, url) }
        // restartLoader ensures the latest URL is used and handles existing loaders
        supportLoaderManager.restartLoader(IMAGE_LOADER_ID, args, this)
    }

    private fun updateUIBasedOnConnectivity(isConnected: Boolean) {
        runOnUiThread {
            Log.d(TAG, "Updating UI based on connectivity: $isConnected")
            binding.loadButton.isEnabled = isConnected
            if (!isConnected) {
                // Show "No internet" only if not already showing an error/loading state
                if (binding.statusTextView.text == getString(R.string.status_idle) ||
                    binding.statusTextView.text == getString(R.string.status_success)
                ) {
                    binding.statusTextView.text = getString(R.string.status_no_internet)
                }
            } else {
                // Restore idle status only if the current status is "No internet"
                if (binding.statusTextView.text == getString(R.string.status_no_internet)) {
                    binding.statusTextView.text = getString(R.string.status_idle)
                }
            }
        }
    }

    // Called when a new loader needs to be created
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Bitmap?> { // <-- Explicit return type Loader<Bitmap?>
        Log.d(TAG, "onCreateLoader called for ID: $id")
        binding.statusTextView.text = getString(R.string.status_loading)
        val url = args?.getString(URL_KEY) ?: ""
        return ImageLoader(this, url) // Returning your specific ImageLoader instance
    }

    // Called when a previously created loader has finished its load
    override fun onLoadFinished(loader: Loader<Bitmap?>, data: Bitmap?) {
        Log.d(TAG, "onLoadFinished called for loader ID: ${loader.id}. Data is null: ${data == null}")
        // Update the UI with the result (data will be null if loading failed)
        if (data != null) {
            binding.imageView.setImageBitmap(data)
            binding.statusTextView.text = getString(R.string.status_success)
        } else {
            // Show an error placeholder or message if data is null
            binding.imageView.setImageResource(android.R.drawable.ic_dialog_alert) // Example error icon
            binding.statusTextView.text = getString(R.string.status_error)
        }
    }

    // Called when a previously created loader is being reset, making its data unavailable
    override fun onLoaderReset(loader: Loader<Bitmap?>) {
        Log.d(TAG, "onLoaderReset called for loader ID: ${loader.id}")
        binding.imageView.setImageDrawable(null)
    }

    // --- ImageLoadListener Implementation (for AsyncTask) ---

    override fun onImageLoadStart() {
        // Ensure UI updates run on the main thread (though AsyncTask's onPreExecute is already there)
        runOnUiThread {
            Log.d(TAG, "AsyncTask: Load Start")
            binding.statusTextView.text = getString(R.string.status_loading)
            binding.imageView.setImageDrawable(null) // Clear previous image
        }
    }

    override fun onImageLoadSuccess(bitmap: Bitmap?) {
        runOnUiThread { // onPostExecute is on main thread, but runOnUiThread is safe practice
            Log.d(TAG, "AsyncTask: Load Finished. Bitmap is null: ${bitmap == null}")
            if (bitmap != null) {
                binding.imageView.setImageBitmap(bitmap)
                binding.statusTextView.text = getString(R.string.status_success)
            } else {
                Log.w(TAG, "AsyncTask: onImageLoaded called with null bitmap, calling onImageLoadFailed.")
                onImageLoadFailed() // Call failure handler
            }
        }
    }

    override fun onImageLoadFailed() {
        runOnUiThread { // onPostExecute is on main thread
            Log.e(TAG, "AsyncTask: Load Failed")
            binding.imageView.setImageResource(android.R.drawable.ic_dialog_alert) // Show error icon
            binding.statusTextView.text = getString(R.string.status_error)
        }
    }

    // --- NetworkStateListener Implementation ---

    override fun onNetworkAvailable() {
        Log.i(TAG, "Network Listener: Network Available")
        updateUIBasedOnConnectivity(true) // Update UI accordingly
    }

    override fun onNetworkUnavailable() {
        // Called from NetworkChangeReceiver when network is lost
        Log.w(TAG, "Network Listener: Network Unavailable")
        updateUIBasedOnConnectivity(false) // Update UI accordingly
    }
}