package ashvayu.mandir.alle.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import ashvayu.mandir.alle.databinding.GalleryItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class ThumbnailAdapter(
  private val podImagesList: List<Uri>, private val width: Int
) : PagerAdapter() {

  override fun getCount(): Int {
    return podImagesList.size
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return `object` == view
  }

  override fun getPageTitle(position: Int): CharSequence {
    return "Item ${position + 1}"
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val binding =
      GalleryItemBinding.inflate(LayoutInflater.from(container.context), container, false)
    initGalleryItem(binding, position, container)
    return binding.root
  }

  private fun initGalleryItem(binding: GalleryItemBinding, position: Int, container: ViewGroup) {
    with(binding) {
      Glide.with(binding.imgGallery).load(podImagesList[position])
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).override(width, width)).into(binding.imgGallery)
      imgGallery.setZoom(0F)
      container.addView(root)
    }
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }
}
