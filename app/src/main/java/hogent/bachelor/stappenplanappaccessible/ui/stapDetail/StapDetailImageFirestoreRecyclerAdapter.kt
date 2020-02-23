package hogent.bachelor.stappenplanappaccessible.ui.stapDetail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hogent.bachelor.stappenplanappaccessible.databinding.ImageListContentBinding
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import hogent.bachelor.stappenplanappaccessible.domain.Video

class StapDetailImageFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Image>, var stapId: String, val clickListener: ImageListener):
    FirestoreRecyclerAdapter<Image, StapDetailImageFirestoreRecyclerAdapter.StapDetailViewHolder>(options){

    var itemcount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StapDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ImageListContentBinding.inflate(layoutInflater, parent, false)
        return StapDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StapDetailViewHolder, position: Int, image: Image) {
        Log.d("ADAPTER_IMAGES", "Url = ${image.imageUrl}")
        if(image.id.isBlank()){
            var id = snapshots.getSnapshot(position).id
            image.id = id
            image.stapId = stapId
            FirestoreRepository().updateImage(image)
        }
        holder.setStapImage(image, clickListener)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        itemcount = itemCount
    }

    inner class StapDetailViewHolder internal constructor(private val binding: ImageListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun setStapImage(item: Image, clickListener: ImageListener) {
            binding.image = item
            binding.clickListener = clickListener
            binding.imageName.text = item.naam
        }
    }
}


class ImageListener(val clickListener: (image: Image) -> Unit){
    fun onClick(image: Image) = clickListener(image)
}