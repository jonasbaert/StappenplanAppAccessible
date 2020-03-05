package hogent.bachelor.stappenplanappaccessible.ui.modifyStappenplan

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentModifyStappenplanBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import hogent.bachelor.stappenplanappaccessible.utils.extensions.hideKeyboard
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.fragment_modify_stappenplan.*
import java.lang.Exception

class ModifyStappenplanFragment : Fragment(){
    private val TAG = "MODIFY_STAPPENPLAN"
    private lateinit var viewModel: ModifyStappenplanViewModel
    private lateinit var plan: Stappenplan

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentModifyStappenplanBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_modify_stappenplan, container, false)

        val app = requireNotNull(this.activity).application

        val stappenplan = ModifyStappenplanFragmentArgs.fromBundle(arguments!!).stappenplan

        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = ModifyStappenplanViewModelFactory(stappenplan, dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ModifyStappenplanViewModel::class.java)
        binding.viewmodel = viewModel

        plan = stappenplan

        if(plan.naam.isBlank()){
            binding.titleNaam.text = "Nieuw stappenplan"
        }
        else{
            binding.titleNaam.text = "Pas stappenplan aan"
        }

        binding.editNaam.setText(plan.naam)
        binding.editBeschrijving.setText(plan.beschrijving)
        Log.d(TAG, "Id = ${plan.id}")

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun updateOrAddStappenplan(stappenplan: Stappenplan){
        if(!stappenplan.isAlreadyInDb){
            stappenplan.isAlreadyInDb = true
            viewModel.saveNewStappenplan(stappenplan)
        }
        else{
            viewModel.updateStappenplan(stappenplan)
        }
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_modify_stappenplan.setOnNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.action_back -> this.findNavController().navigate(
                    ModifyStappenplanFragmentDirections.actionBackToStartFragment())
                R.id.action_accept -> {
                    try {
                        var naam = edit_naam.text
                        var beschrijving = edit_beschrijving.text

                        if(naam.isBlank() && beschrijving.isBlank()){
                            showToast("Naam en beschrijving moeten ingevuld zijn!")
                        }
                        else if (naam.isBlank()){
                            showToast("Naam moet ingevuld zijn!")
                        }
                        else if (beschrijving.isBlank()){
                            showToast("Beschrijving moet ingevuld zijn!")
                        }
                        else if (naam.toString() == plan.naam && beschrijving.toString() == plan.beschrijving){
                            showToast("Wijzig eerst iets aan het stappenplan of keer terug")
                        }
                        else {
                            plan.naam = naam.toString()
                            plan.beschrijving = beschrijving.toString()
                            plan.stappen = emptyList()
                            updateOrAddStappenplan(plan)
                            this.findNavController().navigate(ModifyStappenplanFragmentDirections.actionBackToStartFragment())
                            hideKeyboard()
                        }
                    }catch (e: Exception){
                        Log.d(TAG, "Exception : " + e.message)
                    }
                }
            }
            true
        }
    }

    private fun showToast(text: String){
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, custom_toast_container)
        val txt: TextView = layout.findViewById(R.id.toast_text)

        txt.text = text
        with (Toast(requireContext())) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }
}