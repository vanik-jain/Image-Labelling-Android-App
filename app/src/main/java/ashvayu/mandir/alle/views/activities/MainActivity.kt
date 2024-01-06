package ashvayu.mandir.alle.views.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ashvayu.mandir.alle.R
import ashvayu.mandir.alle.databinding.ActivityMainBinding
import ashvayu.mandir.alle.views.fragments.InfoFragment
import ashvayu.mandir.alle.views.fragments.ShareFragment

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  var currentUri: Uri? = null

  private val shareFragment by lazy { ShareFragment() }
  private val infoFragment by lazy { InfoFragment() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    replaceFragment(shareFragment)
    binding.bottomNavigation.setOnItemSelectedListener { item ->
      handleMenuItemSelection(item.itemId)
    }
  }

  private fun handleMenuItemSelection(itemId: Int): Boolean {
    return when (itemId) {
      R.id.share -> {
        replaceFragment(shareFragment)
        true
      }
      R.id.info -> {
        replaceFragment(infoFragment)
        true
      }
      else -> false
    }
  }

  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.container, fragment)
      commit()
    }
  }
}
