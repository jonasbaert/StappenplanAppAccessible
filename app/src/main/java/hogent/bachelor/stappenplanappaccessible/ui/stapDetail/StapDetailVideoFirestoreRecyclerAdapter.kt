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

class StapDetailVideoFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Video>, var stap: Stap, val clickListener: VideoListener):
    FirestoreRecyclerAdapter<Video, StapDetailVideoFirestoreRecyclerAdapter.StapDetailVideoViewHolder>(options){

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

        holder.setStapVideo(video, clickListener)
    }

    inner class StapDetailVideoViewHolder internal constructor(private val binding: VideoListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun setStapVideo(item: Video, clickListener: VideoListener) {
            binding.video = item
            binding.clickListener = clickListener
            binding.naamFromVideo.text = item.naam
        }
    }
}

class VideoListener(val clickListener: (video: Video) -> Unit){
    fun onClick(video: Video) = clickListener(video)
}