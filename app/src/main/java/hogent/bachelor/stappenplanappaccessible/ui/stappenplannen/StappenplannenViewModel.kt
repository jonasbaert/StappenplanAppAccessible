package hogent.bachelor.stappenplanappaccessible.ui.stappenplannen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class StappenplannenViewModel(stappenplanDao: StappenplanDao, val app: Application) : AndroidViewModel(app) {
    val TAG = "STAPPENPLANNEN_VIEW_MODEL"
    //var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val stappenplanRepository = StappenplanRepository(stappenplanDao)
    val plannen = stappenplanRepository.stappenplannen

    private var _stappenplannen = MutableLiveData<List<Stappenplan>>()
    val savedStappenplannen : LiveData<List<Stappenplan>> get() = _stappenplannen

    private val _navigateToStappenplanDetail = MutableLiveData<Stappenplan>()
    val navigateToStappenplanDetail: LiveData<Stappenplan> get() = _navigateToStappenplanDetail

    private val _navigateToModifyStappenplan = MutableLiveData<Stappenplan>()
    val navigateToModifyStappenplan: LiveData<Stappenplan> get() = _navigateToModifyStappenplan

    fun onStappenplanClicked(stappenplan: Stappenplan){
        _navigateToStappenplanDetail.value = stappenplan
    }

    fun onStappenplanNavigated(){
        _navigateToStappenplanDetail.value = null
    }

    fun onModifyStappenplanClicked(stappenplan: Stappenplan){
        _navigateToModifyStappenplan.value = stappenplan
    }

    fun  onModifyStappenlanNavigated(){
        _navigateToModifyStappenplan.value = null
    }

    //Get all stappenplannen
    /*fun getQueryForStappenplannen() : Query {
        return firestoreRepository.getQueryForStappenplannen()
    }*/

    //Delete a stappenplan
    fun deleteStappenplan(stappenplan: Stappenplan){
        /*firestoreRepository.deleteStappenplan(stappenplan).addOnFailureListener {
            Log.e(TAG, "Failed to delete Stappenplen")
        }*/
        coroutineScope.launch {
            try {
                stappenplanRepository.deleteStappenplan(stappenplan)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


}