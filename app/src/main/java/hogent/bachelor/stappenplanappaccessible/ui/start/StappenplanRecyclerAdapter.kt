package hogent.bachelor.stappenplanappaccessible.ui.start

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hogent.bachelor.stappenplanappaccessible.databinding.StappenplanListContentBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan

class StappenplanRecyclerAdapter(val clickListener: PlanListener) : ListAdapter<Stappenplan, StappenplanRecyclerAdapter.ViewHolder>(StappenplanDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    fun returnItem(position: Int): Stappenplan{
        return getItem(position)
    }

    class ViewHolder private constructor(val binding: StappenplanListContentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Stappenplan, clickListener: PlanListener) {
            binding.stappenplan = item
            binding.clickListener = clickListener
            binding.naamString.text = item.naam
            binding.beschrijvingString.text = item.beschrijving.substring(0, 40) + "..."
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StappenplanListContentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class StappenplanDiffCallback: DiffUtil.ItemCallback<Stappenplan>() {
    override fun areItemsTheSame(oldItem: Stappenplan, newItem: Stappenplan): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stappenplan, newItem: Stappenplan): Boolean {
        return oldItem == newItem
    }
}


class PlanListener(val clickListener: (stappenplan: Stappenplan) -> Unit){
    fun onClick(stappenplan: Stappenplan) = clickListener(stappenplan)
}