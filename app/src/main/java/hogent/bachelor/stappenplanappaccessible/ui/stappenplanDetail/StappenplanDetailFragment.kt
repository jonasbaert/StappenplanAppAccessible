package hogent.bachelor.stappenplanappaccessible.ui.stappenplanDetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentStappenplanDetailBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.utils.extensions.SwipeHelp
import hogent.bachelor.stappenplanappaccessible.utils.extensions.SwipeHelp.UnderlayButtonClickListener
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import kotlinx.android.synthetic.main.fragment_stappenplan_detail.*

class StappenplanDetailFragment : Fragment(){
    private val TAG = "STAPPENPLAN_DETAIL"
    private lateinit var viewModel: StappenplanDetailViewModel
    private lateinit var stappenplan: Stappenplan

    private lateinit var adapter: StapRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentStappenplanDetailBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_stappenplan_detail, container, false)
        val app = requireNotNull(this.activity).application
        val stappenplan = StappenplanDetailFragmentArgs.fromBundle(arguments!!).stappenplan

        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = StappenplanDetailViewModelFactory(stappenplan, dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StappenplanDetailViewModel::class.java)
        binding.viewModel = viewModel

        this.stappenplan = stappenplan

        val manager = LinearLayoutManager(activity)
        binding.stapList.layoutManager = manager

        adapter = StapRecyclerAdapter(StappenListener {
            stap -> viewModel.onStapClicked(stap)
        }, dataSource)

        binding.stapList.adapter = adapter

        viewModel.stappen.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        object : SwipeHelp(activity, binding.stapList, false) {
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder?, underlayButtons: MutableList<UnderlayButton>?) {

                //adding first button
                underlayButtons?.add(
                    SwipeHelp.UnderlayButton("", AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete_black_24dp),
                        Color.parseColor("#FF3C30"), 50, 40, 50, 40, Color.parseColor("#ffffff"),

                        UnderlayButtonClickListener { pos: Int ->
                            //Perform click operation on button1 at given pos
                            viewModel.changeVolgnummersByDelete(adapter!!.returnItem(pos).volgnummer)
                            viewModel.deleteStap(adapter!!.returnItem(pos))
                            adapter!!.notifyItemRemoved(pos)
                        }
                    ))
                underlayButtons?.add(
                    SwipeHelp.UnderlayButton("", AppCompatResources.getDrawable(requireContext(), R.drawable.ic_create_black_24dp),
                        Color.parseColor("#FF9502"),  50, 40, 50, 40, Color.parseColor("#ffffff"),

                        UnderlayButtonClickListener { pos: Int ->
                            //Perform click operation on button2 at given pos
                            viewModel.onModifyStapClicked(adapter!!.returnItem(pos))
                        }
                    ))
            }
        }

        viewModel.navigateToStapDetail.observe(this, Observer { stap ->
            stap?.let {
                this.findNavController().navigate(
                    StappenplanDetailFragmentDirections
                    .actionStappenplanDetailFragmentToStapDetailFragment(stap, stappenplan))
                viewModel.onStapNavigated()
            }
        })

        viewModel.navigateToModifyStap.observe(this, Observer { stap ->
            stap?.let {
                Log.d(TAG, "id van stap: ${stap.id}")
                this.findNavController().navigate(
                    StappenplanDetailFragmentDirections
                    .actionStappenplanDetailFragmentToModifyStapFragment(stap, stappenplan))
                viewModel.onModifyStapNavigated()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_stappenplan_detail.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_back -> this.findNavController().navigate(
                    StappenplanDetailFragmentDirections.actionStappenplanDetailFragmentToStartFragment()
                )
               R.id.action_edit ->
                this.findNavController().navigate(
                    StappenplanDetailFragmentDirections.actionStappenplanDetailFragmentToModifyStappenplanFragment(stappenplan)
                )
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_multiple, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //get item id to handle item clicks
        val id = item.itemId

        //Handle item clicks
        if(id == R.id.action_to_modifyFragment){
            viewModel.onModifyStapClicked(Stap(0, "", 0, "", false, false, 0, 0))
        }
        if(id == R.id.action_to_resetCheckedStappen){
            viewModel.resetCheckboxes()
            adapter.notifyDataSetChanged()
        }

        return super.onOptionsItemSelected(item)
    }

}