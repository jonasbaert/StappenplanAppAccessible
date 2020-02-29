package hogent.bachelor.stappenplanappaccessible.ui.imageDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentImageBinding
import hogent.bachelor.stappenplanappaccessible.databinding.ImageListContentBinding
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_stappenplan_detail.*

class ImageDetailFragment : Fragment() {
    private val TAG = "IMAGE_DETAIL_FRAGMENT"

    private lateinit var viewModel: ImageDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentImageBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_image, container, false)

        val app = requireNotNull(this.activity).application
        val image = ImageDetailFragmentArgs.fromBundle(arguments!!).image

        val viewModelFactory = ImageDetailViewModelFactory(image, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ImageDetailViewModel::class.java)
        binding.viewModel = viewModel

        binding.titleOfImage.text = image.naam
        if(image.altText != null || image.altText != "") {
            binding.altImage.text = image.altText
        }

        Glide.with(requireContext()).load(image.imageUrl).into(binding.imageFromDb)

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_image.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_back -> this.findNavController().popBackStack()
                R.id.action_remove -> {

                }
            }
            true
        }
    }
}