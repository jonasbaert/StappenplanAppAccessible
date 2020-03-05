package hogent.bachelor.stappenplanappaccessible.ui.stapDetail

import android.app.Dialog
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentStapDetailBinding
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hogent.bachelor.stappenplanappaccessible.domain.Video
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import kotlinx.android.synthetic.main.fragment_stap_detail.*

class StapDetailFragment : Fragment(){
    private val TAG = "STEP_DETAIL_FRAGMENT"
    private lateinit var viewModel: StapDetailViewModel
    private lateinit var stappenplan: Stappenplan
    private lateinit var stap: Stap

    private var adapterImage: StapDetailImageFirestoreRecyclerAdapter? = null
    private var adapterVideo: StapDetailVideoFirestoreRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentStapDetailBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_stap_detail, container, false)

        val app = requireNotNull(this.activity).application

        val stap = StapDetailFragmentArgs.fromBundle(arguments!!).stap
        val stappenplan = StapDetailFragmentArgs.fromBundle(arguments!!).stappenplan

        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = StapDetailViewModelFactory(stap, stappenplan, dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StapDetailViewModel::class.java)
        binding.viewmodel = viewModel

        this.stappenplan = stappenplan
        this.stap = stap

        val manager = LinearLayoutManager(activity)
        binding.imageList.layoutManager = manager
        val options = FirestoreRecyclerOptions.Builder<Image>()
                .setQuery(viewModel.getImageUrlsFromStap(), Image::class.java).build()

        adapterImage = StapDetailImageFirestoreRecyclerAdapter(options, stap.id.toString(), ImageListener {
            image -> viewModel.onImageClicked(image)
        })

        binding.imageList.adapter = adapterImage

        viewModel.navigateToImageDetail.observe(this, Observer { image ->
            image?.let {
                this.findNavController().navigate(
                    StapDetailFragmentDirections
                        .actionStapDetailFragmentToImageDetailFragment(image, stap))
                    viewModel.onImageNavigated()

            }
        })

        if (stap.aantalImages == 0) {
            binding.linLayImages.visibility = View.GONE
        } else {
            binding.linLayImages.visibility = View.VISIBLE
        }

        val manager2 = LinearLayoutManager(activity)

        binding.videoList.layoutManager = manager2
        val optionsVideo = FirestoreRecyclerOptions.Builder<Video>()
            .setQuery(viewModel.getVideoFromStap(), Video::class.java).build()

        adapterVideo = StapDetailVideoFirestoreRecyclerAdapter(optionsVideo, stap, VideoListener {
            video -> viewModel.onVideoClicked(video)
        })

        binding.videoList.adapter = adapterVideo

        viewModel.navigateToVideoDetail.observe(this, Observer { video ->
            video?.let {
                this.findNavController().navigate(
                    StapDetailFragmentDirections
                        .actionStapDetailFragmentToVideoDetailFragment(video, stap))
                viewModel.onVideoNavigated()
            }
        })

        if(stap.aantalVideos == 0){
            binding.linLayVideos.visibility = View.GONE
        }
        else{
            binding.linLayVideos.visibility = View.VISIBLE
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapterImage!!.startListening()
        adapterVideo!!.startListening()

        bottom_navigation_stap_detail.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_back ->
                    this.findNavController().navigate(
                        StapDetailFragmentDirections.actionBackToStappenplanDetailFragment(stappenplan)
                    )
                R.id.action_edit -> {
                    this.findNavController().navigate(StapDetailFragmentDirections.actionStapDetailFragmentToModifyStapFragment(stap, stappenplan))
                }
                R.id.action_remove -> showDialog()
            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        if(adapterImage != null){
            adapterImage!!.stopListening()
        }

        if(adapterVideo != null){
            adapterVideo!!.stopListening()
        }
    }

    private fun showDialog(){
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setTitle("Wil je doorgaan met verwijderen?")
        var text = dialog.findViewById<TextView>(R.id.dialog_text)
        text.text = "Als je op verwijderen drukt, dan wordt de afbeelding met naam " + stap.stapNaam + " definitief verwijderd..."
        var image = dialog.findViewById<ImageView>(R.id.dialog_image)
        image.setImageResource(R.drawable.ic_delete_red_24dp)

        var okButton = dialog.findViewById<Button>(R.id.dialog_button_ok)
        var annuleerButton = dialog.findViewById<Button>(R.id.dialog_button_annuleer)

        okButton.setOnClickListener {
            // Verwijder
            dialog.dismiss()
            viewModel.deleteStap(stap)
            this.findNavController().popBackStack()
        }

        annuleerButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}