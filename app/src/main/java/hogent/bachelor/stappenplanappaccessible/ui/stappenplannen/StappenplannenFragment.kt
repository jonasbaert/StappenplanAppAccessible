package hogent.bachelor.stappenplanappaccessible.ui.stappenplannen

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentStappenplannenBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import kotlinx.android.synthetic.main.fragment_stappenplannen.*


class StappenplannenFragment : Fragment() {
    val TAG = "STAPPENPLANNEN_FRAGMENT"

    private lateinit var viewModel: StappenplannenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentStappenplannenBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_stappenplannen, container, false)

        val app = requireNotNull(this.activity).application

        //Init viewModel
        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = StappenplannenViewModelFactory(dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StappenplannenViewModel::class.java)
        binding.viewModel = viewModel

        //Manager + adapter
        val manager = LinearLayoutManager(activity)
        binding.stappenplanList.layoutManager = manager

        val adapter = StappenplanRecyclerAdapter(PlanListener {
            stappenplan -> viewModel.onStappenplanClicked(stappenplan)
        })

        /*object : SwipeHelp(activity, binding.stappenplanList, false) {
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder?, underlayButtons: MutableList<UnderlayButton>?) {

                //adding first button
                underlayButtons?.add(
                    SwipeHelp.UnderlayButton("", AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete_white_24dp),
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
        }*/

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
                    StappenplannenFragmentDirections
                    .actionStappenplannenFragmentToModifyStappenplanFragment(stappenplan))
                viewModel.onModifyStappenlanNavigated()
            }
        })

        viewModel.navigateToStappenplanDetail.observe(this, Observer { stappenplan ->
            stappenplan?.let {
                this.findNavController().navigate(
                    StappenplannenFragmentDirections
                    .actionStappenplannenFragmentToStappenplanDetailFragment(stappenplan))
                viewModel.onStappenplanNavigated()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_stappenplannen.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_back -> this.findNavController().navigate(
                    StappenplannenFragmentDirections.actionStappenplannenFragmentToStartFragment()
                )
                R.id.action_to_modifyFragment -> this.findNavController().navigate(
                    StappenplannenFragmentDirections.actionStappenplannenFragmentToModifyStappenplanFragment(
                        Stappenplan(0, "", "", emptyList(), false, 0)
                    )
                )
            }
            true
        }
    }
}



