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

        //Init viewModel
        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = StartViewModelFactory(dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartViewModel::class.java)
        binding.viewModel = viewModel

        //Manager + adapter
        val manager = LinearLayoutManager(activity)
        binding.stappenplanList.layoutManager = manager

        val adapter = StappenplanRecyclerAdapter(PlanListener {
            stappenplan -> viewModel.onStappenplanClicked(stappenplan)
        })

        object : SwipeHelp(activity, binding.stappenplanList, false) {
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder?, underlayButtons: MutableList<UnderlayButton>?) {

                //adding first button
                underlayButtons?.add(
                    SwipeHelp.UnderlayButton("", AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete_black_24dp),
                        Color.parseColor("#FF3C30"), 50, 40, 50, 40, Color.parseColor("#ffffff"),

                        UnderlayButtonClickListener { pos: Int ->
                            //Perform click operation on button1 at given pos
                            viewModel.deleteStappenplan(adapter!!.returnItem(pos))
                            adapter!!.notifyItemRemoved(pos)
                        }
                    ))
                underlayButtons?.add(
                    SwipeHelp.UnderlayButton("", AppCompatResources.getDrawable(requireContext(), R.drawable.ic_create_black_24dp),
                        Color.parseColor("#FF9502"), 50, 40, 50, 40, Color.parseColor("#ffffff"),

                        UnderlayButtonClickListener { pos: Int ->
                            //Perform click operation on button2 at given pos
                            viewModel.onModifyStappenplanClicked(adapter!!.returnItem(pos))
                        }
                    ))
            }
        }

        binding.stappenplanList.adapter = adapter

        viewModel.plannen.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.navigateToModifyStappenplan.observe(this, Observer { stappenplan ->
            stappenplan?.let {
                this.findNavController().navigate(
                    StartFragmentDirections
                    .actionStartFragmentToModifyStappenplanFragment(stappenplan))
                viewModel.onModifyStappenlanNavigated()
            }
        })

        viewModel.navigateToStappenplanDetail.observe(this, Observer { stappenplan ->
            stappenplan?.let {
                this.findNavController().navigate(
                    StartFragmentDirections
                    .actionStartFragmentToStappenplanDetailFragment(stappenplan))
                viewModel.onStappenplanNavigated()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.action_to_modifyFragment).isVisible = true

        super.onCreateOptionsMenu(menu, inflater)
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



