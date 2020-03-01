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
    private var firebaseStore : FirebaseStorage? = null
    private var photoRef : StorageReference? = null

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
                        .actionStapDetailFragmentToImageDetailFragment(image))
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
                        .actionStapDetailFragmentToVideoDetailFragment(video))
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

    /*
    private fun getItemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                viewHolder2: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
              *//*  val pos = viewHolder.adapterPosition
                adapterImage!!.notifyItemRemoved(pos)
                firebaseStore = FirebaseStorage.getInstance()
                photoRef = firebaseStore!!.getReferenceFromUrl(adapterImage!!.getItem(pos).imageUrl)
                if(photoRef != null) {
                    photoRef!!.delete().addOnSuccessListener {
                        viewModel.updateAantalImagesFromStap(stap.aantalImages - 1)
                        viewModel.deleteImageFromStap(adapterImage!!.getItem(pos))
                        Toast.makeText(
                            requireContext(),
                            "Succesvol verwijderd uit db",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Probleem bij verwijderen uit db",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    viewModel.deleteImageFromStap(adapterImage!!.getItem(pos))
                    Toast.makeText(
                        requireContext(),
                        "Succesvol verwijderd uit db",
                        Toast.LENGTH_SHORT
                    ).show()
                }*//*

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                var colorBackground = Color.parseColor("#FF3C30")
                var deleteIcon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_white_24dp)!!
                val inWidth = deleteIcon.intrinsicWidth
                val inHeight = deleteIcon.intrinsicHeight
                val background = ColorDrawable()
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                background.color = colorBackground
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                val iconTop = itemView.top + (itemHeight - inHeight) / 2
                val iconMargin = (itemHeight - inHeight) / 2
                val (iconLeft, iconRight) = getIconPositionHorizontal(itemView, iconMargin, dX, inWidth)
                val iconBottom = iconTop + inHeight

                Log.d("Position", "Left: $iconLeft Top: $iconTop Right: $iconRight Bottom: $iconBottom")

                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

            private fun getIconPositionHorizontal(itemView: View, iconMargin: Int, dX: Float, inWidth: Int): Pair<Int, Int> {
                val iconLeft: Int
                val iconRight: Int

                // swiping from left to right
                if (dX > 0) {
                    iconLeft = itemView.left + iconMargin
                    iconRight = itemView.left + iconMargin + inWidth
                } else {
                    iconLeft = itemView.right - iconMargin - inWidth
                    iconRight = iconLeft + inWidth
                }

                return Pair(iconLeft, iconRight)
            }
        }
    }
    */

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
        var dialog : Dialog = Dialog(requireContext())
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