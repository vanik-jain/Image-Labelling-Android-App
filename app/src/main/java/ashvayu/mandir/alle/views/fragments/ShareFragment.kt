package ashvayu.mandir.alle.views.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Insets
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import ashvayu.mandir.alle.views.activities.MainActivity
import ashvayu.mandir.alle.adapter.GalleryAdapter
import ashvayu.mandir.alle.adapter.ThumbnailAdapter
import ashvayu.mandir.alle.databinding.FragmentShareBinding
import ashvayu.mandir.alle.utils.hideView
import ashvayu.mandir.alle.utils.isListNotNullOrEmpty
import ashvayu.mandir.alle.utils.showView

class ShareFragment : Fragment(),
  GalleryAdapter.ActivityCommunicator {

  private val selectedImages = mutableListOf<Uri>()
  private lateinit var binding: FragmentShareBinding // Assuming FragmentGalleryBinding is used here

  companion object {
    private const val REQUEST_CODE_MULTI_IMAGE = 1002 // Define your request code
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = FragmentShareBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      myToolbar.setNavigationOnClickListener {
        activity?.finish()
      }
      btOpenGallery.setOnClickListener {
        openGalleryForMultipleImages()
      }
    }
  }

  private fun setThumbnailAdapter(reviewGalleryAdapter: GalleryAdapter) {
    with(binding.rvThumbnails) {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
      adapter = reviewGalleryAdapter
    }
  }

  private fun openGalleryForMultipleImages() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    startActivityForResult(intent, REQUEST_CODE_MULTI_IMAGE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_MULTI_IMAGE && resultCode == Activity.RESULT_OK) {
      selectedImages.clear()
      data?.clipData?.let { clipData ->
        for (i in 0 until clipData.itemCount) {
          val imageUri = clipData.getItemAt(i).uri
          selectedImages.add(imageUri)
        }
        processSelectedImage()
      }
    }
  }

  private fun WindowManager.currentWindowMetricsPointCompat(): Point {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      val windowInsets = currentWindowMetrics.windowInsets
      var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
      windowInsets.displayCutout?.run {
        insets = Insets.max(
          insets, Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
        )
      }
      val insetsWidth = insets.right + insets.left
      val insetsHeight = insets.top + insets.bottom
      Point(
        currentWindowMetrics.bounds.width() - insetsWidth,
        currentWindowMetrics.bounds.height() - insetsHeight
      )
    } else {
      Point().apply {
        @Suppress("DEPRECATION") defaultDisplay.getSize(this)
      }
    }
  }

  private fun processSelectedImage() {
    with(binding) {
      btOpenGallery.hideView()
      rvThumbnails.showView()
      selectedImages.isListNotNullOrEmpty({
        val imageAdapter =
          ThumbnailAdapter(it, activity?.windowManager?.currentWindowMetricsPointCompat()?.x ?: 0)
        viewpager.adapter = imageAdapter
        piImages.attachToPager(binding.viewpager)
        viewpager.currentItem = 0
        (activity as? MainActivity)?.currentUri = selectedImages.getOrNull(0)
        val reviewGalleryAdapter = GalleryAdapter(it, this@ShareFragment, 0).also {
          setThumbnailAdapter(it)
        }
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
          override fun onPageScrollStateChanged(state: Int) { // No implementation required
          }

          override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
          ) { // No implementation required
          }

          override fun onPageSelected(position: Int) {
            (activity as? MainActivity)?.currentUri = selectedImages.getOrNull(position)
            reviewGalleryAdapter.setCurrentItem(position)
            val mWrapContentLinearLayoutManager = rvThumbnails.layoutManager as LinearLayoutManager
            if (mWrapContentLinearLayoutManager.findFirstCompletelyVisibleItemPosition() > position || mWrapContentLinearLayoutManager.findLastCompletelyVisibleItemPosition() < position) {
              mWrapContentLinearLayoutManager.scrollToPosition(position)
            }
          }
        })
      }, {
        activity?.finish()
      })
    }
  }

  override fun changeSelectedItem(position: Int) {
    binding.viewpager.currentItem = position
  }

  override fun getVideoId(url: String?): String? = null

}

//
//  override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View? {
//    val rootView = inflater.inflate(R.layout.fragment_share, container, false)
//
//    val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
//    recyclerView.layoutManager = LinearLayoutManager(activity)
//    recyclerView.adapter = screenshotAdapter
//
//    val imageLabeler =
//      ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
//
//    // Fetch screenshots from the device
//    val screenshotFiles =
//      File(requireContext().getExternalFilesDir(null), "Pictures").listFiles()
//    screenshotFiles?.forEach { file ->
//      val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//      val inputImage = InputImage.fromBitmap(bitmap, 0)
//
//      // Perform text recognition on the screenshot
//      textRecognizer.process(inputImage).addOnSuccessListener { result ->
//        val description = result.text
//
//        // Image labeling
//        imageLabeler.process(inputImage).addOnSuccessListener { labels ->
//          // Add the screenshot to the adapter
//          screenshotAdapter.addScreenshot(Screenshot(file.name, description, labels))
//        }.addOnFailureListener { e ->
//          Log.e("ShareFragment", "Error getting image labels", e)
//        }
//      }.addOnFailureListener { e ->
//        Log.e("ShareFragment", "Error getting OCR text", e)
//      }
//    }
//    return rootView
//  }
//}
