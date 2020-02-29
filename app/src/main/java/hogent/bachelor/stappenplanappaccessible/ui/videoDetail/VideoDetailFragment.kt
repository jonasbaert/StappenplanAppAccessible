package hogent.bachelor.stappenplanappaccessible.ui.videoDetail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentVideoBinding
import hogent.bachelor.stappenplanappaccessible.domain.Video
import hogent.bachelor.stappenplanappaccessible.ui.imageDetail.ImageDetailFragmentArgs
import hogent.bachelor.stappenplanappaccessible.ui.imageDetail.ImageDetailViewModel
import hogent.bachelor.stappenplanappaccessible.ui.imageDetail.ImageDetailViewModelFactory
import hogent.bachelor.stappenplanappaccessible.ui.stappenplanDetail.StappenplanDetailFragmentDirections
import kotlinx.android.synthetic.main.fragment_stappenplan_detail.*
import kotlinx.android.synthetic.main.fragment_video.*

class VideoDetailFragment : Fragment(){
    private val TAG = "VIDEO_DETAIL_FRAGMENT"

    private lateinit var viewModel: VideoDetailViewModel

    private var mediaController: MediaController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentVideoBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_video, container, false)

        val app = requireNotNull(this.activity).application
        val video = VideoDetailFragmentArgs.fromBundle(arguments!!).video

        val viewModelFactory = VideoDetailViewModelFactory(video, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(VideoDetailViewModel::class.java)
        binding.viewModel = viewModel

        binding.titleOfVideo.text = video.naam

        mediaController = MediaController(context)
        binding.videoFromDatabase.setMediaController(mediaController)
        var uri = Uri.parse(video.videoUrl)
        binding.videoFromDatabase.setVideoURI(uri)
        binding.videoFromDatabase.requestFocus()
        binding.videoFromDatabase.start()
        binding.progressBar.visibility = View.VISIBLE
        binding.videoFromDatabase.setOnPreparedListener {mp ->
            mp.start()
            mp.setOnVideoSizeChangedListener { mp, _, _ ->
                binding.progressBar.visibility = View.GONE
                mp.start()
            }
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_video.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_back -> this.findNavController().popBackStack()
                R.id.action_remove -> {

                }
            }
            true
        }
    }
}