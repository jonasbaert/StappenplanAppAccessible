package hogent.bachelor.stappenplanappaccessible.ui.stapDetail

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hogent.bachelor.stappenplanappaccessible.databinding.ImageListContentBinding
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hogent.bachelor.stappenplanappaccessible.databinding.VideoListContentBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Video
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class StapDetailVideoFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Video>, var stap: Stap, var context: Context, var stappenplanDao: StappenplanDao):
    FirestoreRecyclerAdapter<Video, StapDetailVideoFirestoreRecyclerAdapter.StapDetailVideoViewHolder>(options){

    private var firestoreRepository = FirestoreRepository()
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private var stappenplanRepository = StappenplanRepository(stappenplanDao)
    private var firebaseStore : FirebaseStorage? = null
    private var videoRef : StorageReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StapDetailVideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VideoListContentBinding.inflate(layoutInflater, parent, false)
        return StapDetailVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StapDetailVideoViewHolder, position: Int, video: Video) {
        Log.d("ADAPTER_VIDEOS", "Url = ${video.videoUrl}")
        if(video.id.isBlank()){
            var id = snapshots.getSnapshot(position).id
            video.id = id
            video.stapId = stap.id.toString()
            FirestoreRepository().updateVideo(video)
        }

        holder.setStapVideo(video)

        holder.deleteVideo(position)
    }

    inner class StapDetailVideoViewHolder internal constructor(private val binding: VideoListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        var mediaController: MediaController? = null

        internal fun setStapVideo(item: Video) {
            binding.video = item
            Log.d("ADAPTER_IMAGES", "Url = ${item.videoUrl}")
            mediaController = MediaController(context)
            binding.videoFromDatabase.setMediaController(mediaController)
            var uri = Uri.parse(item.videoUrl)
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
        }

        fun deleteVideo(pos: Int){
            binding.btnDeleteVideo.setOnClickListener {
                var vid = getItem(pos)
                firebaseStore = FirebaseStorage.getInstance()
                videoRef = firebaseStore!!.getReferenceFromUrl(vid.videoUrl)
                if(videoRef != null){
                    videoRef!!.delete().addOnSuccessListener {
                        coroutineScope.launch {
                            try {
                                stappenplanRepository.updateAantalVideosFromStap(
                                    stap.id,
                                    stap.aantalVideos - 1
                                )
                            } catch (e: Exception){
                                e.printStackTrace()
                            }
                        }
                        firestoreRepository.deleteVideo(vid.id)
                        Toast.makeText(
                            context,
                            "Succesvol verwijderd uit db",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener{
                        Toast.makeText(
                            context,
                            "Probleem bij verwijderen uit db",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    coroutineScope.launch {
                        try {
                            stappenplanRepository.updateAantalVideosFromStap(
                                stap.id,
                                stap.aantalVideos - 1
                            )
                        } catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                    firestoreRepository.deleteVideo(vid.id)
                    Toast.makeText(
                        context,
                        "Succesvol verwijderd uit db",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}