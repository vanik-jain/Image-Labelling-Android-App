package ashvayu.mandir.alle.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ashvayu.mandir.alle.R
import ashvayu.mandir.alle.databinding.ItemHorizontalStripBinding
import ashvayu.mandir.alle.utils.hideView
import ashvayu.mandir.alle.utils.showView
import com.bumptech.glide.Glide

class GalleryAdapter(
  private val thumbnailList: List<Uri>,
  private val mActivityCommunicator: ActivityCommunicator,
  private var mSelectedIndex: Int = 0
) : RecyclerView.Adapter<GalleryAdapter.ReviewGalleryViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewGalleryViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_horizontal_strip, parent, false)
    return ReviewGalleryViewHolder(view)
  }

  override fun getItemCount(): Int = thumbnailList.size

  override fun onBindViewHolder(holder: ReviewGalleryViewHolder, position: Int) {
    holder.bind(thumbnailList[position], position)
  }

  fun setCurrentItem(position: Int) {
    mSelectedIndex = position
    notifyItemChanged(position)
  }

  inner class ReviewGalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val itemBinding = ItemHorizontalStripBinding.bind(view)

    fun bind(image: Uri?, position: Int) {
      itemBinding.run {
        videoIcon.hideView()
        Glide.with(itemView.context).load(image).into(ivReviewImage)
        if (position == mSelectedIndex) {
          flBorder.showView()
        } else {
          flBorder.hideView()
        }
        root.setOnClickListener {
          mSelectedIndex = position
          mActivityCommunicator.changeSelectedItem(position)
          notifyItemChanged(position)
        }
      }
    }
  }

  interface ActivityCommunicator {
    fun changeSelectedItem(position: Int)
    fun getVideoId(url: String?): String?
  }
}
