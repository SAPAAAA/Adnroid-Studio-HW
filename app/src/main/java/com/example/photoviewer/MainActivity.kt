package com.example.photoviewer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.photoviewer.data.entities.Image
import com.example.photoviewer.ui.theme.PhotoViewerTheme
import com.example.photoviewer.utils.ImageUtils.createImageUri
import com.example.photoviewer.viewmodel.PhotoViewModel
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    
    private lateinit var photoViewModel: PhotoViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        
        photoViewModel = ViewModelProvider(this)[PhotoViewModel::class.java]
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoViewerTheme {
                PhotoGalleryApp(
                    photoViewModel = photoViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoThumbnail(image: Image, onClick: () -> Unit, onLongPress: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = image.uri),
            contentDescription = image.title,
            modifier = Modifier.fillMaxSize()
        )
        
        // Conditionally display the Star icon if the image is favorited
        if (image.isFavorite) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Favorited",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                tint = Color.Yellow
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoGrid(images: List<Image>, onPhotoClick: (Image) -> Unit, onPhotoLongPress: (Image) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(images.size) { index ->
            PhotoThumbnail(
                image = images[index],
                onClick = { onPhotoClick(images[index]) },
                onLongPress = { onPhotoLongPress(images[index]) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoDetail(
    image: Image,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    canNavigateNext: Boolean,
    canNavigatePrevious: Boolean,
    onPhotoLongPress: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    val transformState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                // Apply the transformable modifier to handle gestures
                .transformable(state = transformState)
                // Handle horizontal drag for navigation
                .pointerInput(canNavigateNext, canNavigatePrevious) {
                    detectHorizontalDragGestures { _, dragAmount ->
                         if (abs(dragAmount) > 10) {
                             offset = Offset.Zero
                         }
                        if (dragAmount < -50 && canNavigateNext) {
                            onNavigateNext()
                        } else if (dragAmount > 50 && canNavigatePrevious) {
                            onNavigatePrevious()
                        }
                    }
                }
                // Handle long press for options
                .combinedClickable(
                    onClick = {},
                    onLongClick = onPhotoLongPress
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter(image.uri),
                contentDescription = image.title,
                modifier = Modifier.fillMaxSize()
            )
            
            // Conditionally display the Star icon if the image is favorited
            if (image.isFavorite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorited",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.Yellow
                )
            }
        }
        
        IconButton(
            onClick = { scale = 1f; offset = Offset.Zero; onNavigatePrevious() }, // Reset zoom on nav
            modifier = Modifier.align(Alignment.CenterStart),
            enabled = canNavigatePrevious
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Photo")
        }
        IconButton(
            onClick = { scale = 1f; /* offset = Offset.Zero; */ onNavigateNext() }, // Reset zoom on nav
            modifier = Modifier.align(Alignment.CenterEnd),
            enabled = canNavigateNext
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Photo")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryApp(photoViewModel: PhotoViewModel = viewModel()) {
    val photos by photoViewModel.photos.collectAsState()
    val navController = rememberNavController()
    val context = LocalContext.current // Get context for permission checks and Uri creation
    
    var selectedImage by remember { mutableStateOf<Image?>(null) }
    var showDeleteFavoriteDialog by remember { mutableStateOf(false) }
    var showSourceChoiceDialog by remember { mutableStateOf(false) } // State for the new dialog
    var currentRoute by remember { mutableStateOf("grid") }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) } // To store Uri for camera capture
    
    // --- Launcher for Picking Multiple Images from Storage ---
    val multiplePhotosPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris.forEach { uri ->
                // Persist permission if needed for long-term access (optional)
                // val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                // context.contentResolver.takePersistableUriPermission(uri, flag)
                photoViewModel.addPhotoFromStorage(uri)
            }
        }
    )
    
    // --- Launcher for Taking a Picture ---
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let {
                    photoViewModel.addPhotoFromCamera(it)
                    tempCameraUri = null // Clear the temp uri
                }
            } else {
                // Handle failure or cancellation (e.g., show a Toast)
                tempCameraUri = null // Clear the temp uri even on failure
            }
        }
    )
    
    // --- Launcher for Requesting Camera Permission ---
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, create URI and launch camera
                tempCameraUri = createImageUri(context)
                tempCameraUri?.let { takePictureLauncher.launch(it) }
                
            } else {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )
    
    // --- Function to launch camera after checking permission ---
    fun launchCameraWithPermissionCheck() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                // Permission already granted
                tempCameraUri = createImageUri(context)
                tempCameraUri?.let { takePictureLauncher.launch(it) }
            }
            else -> {
                // Request permission
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route ?: "grid"
        }
    }
    
    Scaffold(
        floatingActionButton = {
            if (currentRoute == "grid") {
                FloatingActionButton(
                    onClick = {
                        // Show the source choice dialog instead of launching picker directly
                        showSourceChoiceDialog = true
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Photo",
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "grid",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "grid") {
                PhotoGrid(
                    images = photos,
                    onPhotoClick = { selectedPhoto ->
                        val index = photos.indexOf(selectedPhoto)
                        // Ensure index is valid before navigating
                        if (index != -1) {
                            navController.navigate("detail/$index")
                        }
                    },
                    onPhotoLongPress = { photo ->
                        selectedImage = photo
                        showDeleteFavoriteDialog = true
                    }
                )
            }
            composable("detail/{photoIndex}") { backStackEntry ->
                val photoIndex = backStackEntry.arguments?.getString("photoIndex")?.toIntOrNull()
                
                if (photoIndex != null && photoIndex >= 0 && photoIndex < photos.size) {
                    val photo = photos[photoIndex]
                    
                    // Calculate if navigation is possible
                    val canNavigateNext = photoIndex < photos.lastIndex
                    val canNavigatePrevious = photoIndex > 0
                    
                    PhotoDetail(
                        image = photo,
                        // Pass the boolean flags
                        canNavigateNext = canNavigateNext,
                        canNavigatePrevious = canNavigatePrevious,
                        
                        // Pass lambdas, use empty lambda {} when navigation is disabled
                        onNavigateNext = if (canNavigateNext) {
                            {
                                val nextIndex = photoIndex + 1
                                navController.navigate("detail/$nextIndex") {
                                    popUpTo("detail/$photoIndex") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        } else { {} }, // Pass empty lambda instead of null
                        
                        onNavigatePrevious = if (canNavigatePrevious) {
                            {
                                val prevIndex = photoIndex - 1
                                navController.navigate("detail/$prevIndex") {
                                    popUpTo("detail/$photoIndex") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        } else { {} },
                        // Provide the implementation for the new parameter
                        onPhotoLongPress = {
                            selectedImage = photo // Set the current photo as selected
                            showDeleteFavoriteDialog = true // Show the dialog
                        }
                        
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
        
        // --- Source Choice Dialog ---
        if (showSourceChoiceDialog) {
            AlertDialog(
                onDismissRequest = { showSourceChoiceDialog = false },
                title = { Text("Add Photo From") },
                text = { Text("Choose a source for your new photo.") },
                confirmButton = {
                    TextButton(onClick = {
                        showSourceChoiceDialog = false
                        // Launch Camera with permission check
                        launchCameraWithPermissionCheck()
                    }) {
                        Text("Camera")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showSourceChoiceDialog = false
                        // Launch Storage Picker
                        multiplePhotosPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Text("Storage")
                    }
                }
            )
        }
        
        
        // --- Delete/Favorite Dialog ---
        if (showDeleteFavoriteDialog && selectedImage != null) {
            AlertDialog(
                onDismissRequest = { showDeleteFavoriteDialog = false },
                title = { Text("Photo Options") },
                text = { Text("What would you like to do with this photo?") },
                confirmButton = {
                    TextButton(onClick = {
                        selectedImage?.let { photoViewModel.favoritePhoto(it.id) }
                        showDeleteFavoriteDialog = false
                        selectedImage = null // Clear selection
                    }) {
                        Text(if (selectedImage?.isFavorite == true) "Unfavorite" else "Favorite")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        selectedImage?.let { photoViewModel.deletePhoto(it.id) }
                        showDeleteFavoriteDialog = false
                        selectedImage = null // Clear selection
                    }) {
                        Text("Delete")
                    }
                }
            )
        }
    }
}