package hogent.bachelor.stappenplanappbase.ui.stappenplanDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hogent.bachelor.stappenplanappbase.databinding.StapListContentBinding
import hogent.bachelor.stappenplanappbase.databinding.StappenplanListContentBinding
import hogent.bachelor.stappenplanappbase.domain.Stap
import hogent.bachelor.stappenplanappbase.domain.Stappenplan
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappbase.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.*
import java.lang.Exception

class StapRecyclerAdapter(val clickListener: StappenListener, val stappenplanDao: StappenplanDao) : ListAdapter<Stap, StapRecyclerAdapter.ViewHolder>(StapDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, stappenplanDao)
    }

    fun returnItem(position: Int): Stap{
        return getItem(position)
    }

    class ViewHolder private constructor(val binding: StapListContentBinding): RecyclerView.ViewHolder(binding.root) {
        private var viewModelJob = Job()
        private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)

        fun bind(item: Stap, clickListener: StappenListener, stappenplanDao: StappenplanDao) {
            var stappenplanRepository = StappenplanRepository(stappenplanDao)
            binding.stap = item
            binding.clickListener = clickListener
            binding.stapNaamString.text = item.volgnummer.toString() + ". " + item.stapNaam
            binding.checkboxIsGedaan.isChecked = item.isGedaan
            binding.checkboxIsGedaan.setOnClickListener {
                //To room db
                coroutineScope.launch {
                    try {
                        if(!item.isGedaan) {
                            stappenplanRepository.veranderIsGedaan(item.id, true)
                        }
                        else{
                            stappenplanRepository.veranderIsGedaan(item.id, false)
                        }
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StapListContentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class StapDiffCallback: DiffUtil.ItemCallback<Stap>() {
    override fun areItemsTheSame(oldItem: Stap, newItem: Stap): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stap, newItem: Stap): Boolean {
        return oldItem == newItem
    }
}


class StappenListener(val clickListener: (stap: Stap) -> Unit){
    fun onClick(stap: Stap) = clickListener(stap)
}