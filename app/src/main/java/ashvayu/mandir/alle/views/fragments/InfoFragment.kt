package ashvayu.mandir.alle.views.fragments

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ashvayu.mandir.alle.views.activities.MainActivity
import ashvayu.mandir.alle.databinding.FragmentInfoBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class InfoFragment : Fragment() {
  private lateinit var textRecognizer: TextRecognizer
  private lateinit var imageLabeler: ImageLabeler
  private lateinit var binding: FragmentInfoBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = FragmentInfoBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Initialize text recognizer and image labeler
    textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val options = ImageLabelerOptions.Builder()
      .setConfidenceThreshold(0.7f)
      .build()
    imageLabeler = ImageLabeling.getClient(options)

    val currentUri = (activity as? MainActivity)?.currentUri
    if (currentUri != null) {

      val bitmap = MediaStore.Images.Media.getBitmap(binding.ivPreview.context.contentResolver, currentUri)
      binding.ivPreview.setImageBitmap(bitmap)

      val bitmapN = (binding.ivPreview.drawable as BitmapDrawable).bitmap
      val image = InputImage.fromBitmap(bitmapN, 0)

        textRecognizer.process(image).addOnSuccessListener { result ->
            val description = result.text

            imageLabeler.process(image).addOnSuccessListener { labels ->
                var labelValue = ""
                labels.forEach {
                  labelValue = labelValue.plus(it.text+" ")
                }
                binding.tvLabels.text = labelValue
                binding.tvDescription.text = description
              }.addOnFailureListener { e ->
                Log.e("InfoFragment", "Error getting image labels", e)
              }
          }.addOnFailureListener { e ->
            Log.e("InfoFragment", "Error getting OCR text", e)
          }
    } else {
      Log.e("InfoFragment", "Current URI is null")
    }
  }
}


    //    // Assuming you have a bitmap initialized somewhere
//    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, (activity as? MainActivity)?.currentUri)
//    val inputImage = InputImage.fromBitmap(bitmap, 0)
//
//    // Process the image using ML Kit
//    textRecognizer.process(inputImage)
//      .addOnSuccessListener { result ->
//        val description = result.text
//
//        // Image labeling
//        imageLabeler.process(inputImage)
//          .addOnSuccessListener { labels ->
//            // Assuming you have screenshotAdapter initialized somewhere
//            val labelValue=""
//            labels.forEach{
//              labelValue.plus(it.text)
//            }
//            val res = "$labelValue + $description"
//            binding.tvLabels.text =  res
//          }
//          .addOnFailureListener { e ->
//            Log.e("InfoFragment", "Error getting image labels", e)
//          }
//      }
//      .addOnFailureListener { e ->
//        Log.e("InfoFragment", "Error getting OCR text", e)
//      }
//  }

