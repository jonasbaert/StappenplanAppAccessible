package hogent.bachelor.stappenplanappaccessible.ui.stappenplanDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class StappenplanDetailViewModel(stappenplan: Stappenplan, stappenplanDao: StappenplanDao, app: Application) : AndroidViewModel(app){
    private val TAG = "STAPPENPLAN_DETAIL_VM"
    private val app = app
    //var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var stappenplanRepository = StappenplanRepository(stappenplanDao)

    private val stappenplanId = stappenplan.id

    val stappen : LiveData<List<Stap>> = stappenplanRepository.getStappenFromStappenplan(stappenplanId)

    private var _stappenplan = MutableLiveData<Stappenplan>()
    val stappenplan : MutableLiveData<Stappenplan> = _stappenplan

    private val _navigateToStapDetail = MutableLiveData<Stap>()
    val navigateToStapDetail: LiveData<Stap> get() = _navigateToStapDetail

    private val _navigateToModifyStap = MutableLiveData<Stap>()
    val navigateToModifyStap: LiveData<Stap> get() = _navigateToModifyStap

    init {
        _stappenplan.value = stappenplan
    }

    fun onStapClicked(stap: Stap){
        _navigateToStapDetail.value = stap
    }

    fun onStapNavigated(){
        _navigateToStapDetail.value = null
    }

    fun onModifyStapClicked(stap: Stap){
        _navigateToModifyStap.value = stap
    }

    fun  onModifyStapNavigated(){
        _navigateToModifyStap.value = null
    }

    fun resetCheckboxes(){
        coroutineScope.launch {
            try {
                stappenplanRepository.resetStappenFromStappenplan(stappenplanId)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    //Delete a stap
    fun deleteStap(stap: Stap){
        coroutineScope.launch {
            try {
                stappenplanRepository.deleteStap(stap)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun changeVolgnummersByDelete(volgnummer: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.changeVolgnummersByDelete(volgnummer, stappenplanId)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}