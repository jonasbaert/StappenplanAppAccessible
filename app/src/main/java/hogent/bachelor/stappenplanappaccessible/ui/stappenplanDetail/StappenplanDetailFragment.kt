package hogent.bachelor.stappenplanappaccessible.ui.stappenplanDetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentStappenplanDetailBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
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
                R.id.action_remove -> {
                   showDialog()
                }
            }
            true
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_multiple, menu)

        var menuItem1 = menu.getItem(0).subMenu[0]
        var s = SpannableString(menuItem1.title)
        s.setSpan(ForegroundColorSpan(R.color.colorPrimaryDark), 0, s.length, 0)
        s.setSpan(RelativeSizeSpan(1.4f), 0, s.length, 0)
        s.setSpan(StyleSpan(Typeface.BOLD), 0, s.length, 0)
        menuItem1.title = s

        var menuItem2 = menu.getItem(0).subMenu[1]
        var s2 = SpannableString(menuItem2.title)
        s2.setSpan(ForegroundColorSpan(R.color.colorPrimaryDark), 0, s2.length, 0)
        s2.setSpan(RelativeSizeSpan(1.4f), 0, s2.length, 0)
        s2.setSpan(StyleSpan(Typeface.BOLD), 0, s2.length, 0)
        menuItem2.title = s2

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

    private fun showDialog(){
        var dialog : Dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setTitle("Wil je doorgaan met verwijderen?")
        var text = dialog.findViewById<TextView>(R.id.dialog_text)
        text.text = "Als je op verwijderen drukt, dan wordt het stappenplan met naam " + stappenplan.naam + " definitief verwijderd..."
        var image = dialog.findViewById<ImageView>(R.id.dialog_image)
        image.setImageResource(R.drawable.ic_delete_red_24dp)

        var okButton = dialog.findViewById<Button>(R.id.dialog_button_ok)
        var annuleerButton = dialog.findViewById<Button>(R.id.dialog_button_annuleer)

        okButton.setOnClickListener {
            // Verwijder
            dialog.dismiss()
            viewModel.deleteStappenplan(stappenplan)
            this.findNavController().popBackStack()
        }

        annuleerButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}