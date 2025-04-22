import android.graphics.Bitmap

interface ImageLoadListener {
	fun onImageLoadStart()
	fun onImageLoadSuccess(bitmap: Bitmap?)
	fun onImageLoadFailed()
}