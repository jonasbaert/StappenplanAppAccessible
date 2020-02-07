package hogent.bachelor.stappenplanappbase.ui.modifyStappenplan

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappbase.domain.Stappenplan
import hogent.bachelor.stappenplanappbase.firestore.FirestoreRepository
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappbase.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ModifyStappenplanViewModel(stappenplan: Stappenplan, stappenplanDao: StappenplanDao, app: Application): AndroidViewModel(app) {
    private val TAG = "STAP_DETAIL_VIEWMODEL"
    private val app = app

    //var firestoreRepository = FirestoreRepository()
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val stappenplanRepository = StappenplanRepository(stappenplanDao)

    private var _stappenplan = MutableLiveData<Stappenplan>()
    val stappenplan : MutableLiveData<Stappenplan> = _stappenplan

    init {
        _stappenplan.value = stappenplan
    }


    //Update a stappenplan
    fun updateStappenplan(stappenplan: Stappenplan){
          /*  firestoreRepository.updateStappenplan(stappenplan)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }*/
        coroutineScope.launch {
            try {
                stappenplanRepository.updateStappenplan(stappenplan)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun saveNewStappenplan(stappenplan: Stappenplan){
/*        firestoreRepository.saveStappenplan(stappenplan)*/
        coroutineScope.launch {
            try {
                stappenplanRepository.insertStappenplannenWithStappen(stappenplan)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}