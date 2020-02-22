package hogent.bachelor.stappenplanappaccessible.ui.start

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentStartBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.utils.extensions.SwipeHelp
import hogent.bachelor.stappenplanappaccessible.utils.extensions.SwipeHelp.UnderlayButtonClickListener
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import hogent.bachelor.stappenplanappaccessible.ui.stappenplannen.StappenplannenViewModel
import hogent.bachelor.stappenplanappaccessible.ui.stappenplannen.StappenplannenViewModelFactory


class StartFragment : Fragment() {
    val TAG = "START_FRAGMENT"

    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentStartBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_start, container, false)

        val app = requireNotNull(this.activity).application

        val viewModelFactory = StartViewModelFactory(app)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartViewModel::class.java)
        binding.viewModel = viewModel

        binding.btnToStappenplannen.setOnClickListener {
            this.findNavController().navigate(
                StartFragmentDirections
                    .actionStartFragmentToStappenplannenFragment())
        }

        binding.btnToCreateStappenplan.setOnClickListener {
            this.findNavController().navigate(
                StartFragmentDirections
                    .actionStartFragmentToModifyStappenplanFragment(Stappenplan(0, "", "", emptyList(), false)))
            viewModel.onModifyStappenlanNavigated()
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //get item id to handle item clicks
        val id = item.itemId

        //Handle item clicks
        if(id == R.id.action_to_modifyFragment){
            viewModel.onModifyStappenplanClicked(Stappenplan(0, "", "", emptyList(), false))
        }

        return super.onOptionsItemSelected(item)
    }

}



