package hogent.bachelor.stappenplanappaccessible.ui.modifyStap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ModifyStapViewModel(stap: Stap, stappenplan: Stappenplan, stappenplanDao: StappenplanDao, app: Application): AndroidViewModel(app) {
    private val TAG = "MODIFY_STAP_VIEWMODEL"
    private val app = app
    var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var stappenplanRepository = StappenplanRepository(stappenplanDao)

    private var _stappenplan = MutableLiveData<Stappenplan>()
    val stappenplan : MutableLiveData<Stappenplan> = _stappenplan

    private var _stap = MutableLiveData<Stap>()
    val stap : MutableLiveData<Stap> = _stap

    private var planId = stappenplan.id
    private var stapId = stap.id

    init {
        _stappenplan.value = stappenplan
        _stap.value = stap

        Log.d(TAG, "Id van stap : $stap.id")
    }

    fun checkIfAvailable(volgnummer: Int) : Boolean{
        var bool : Boolean = false
        coroutineScope.launch {
            try {
                bool = stappenplanRepository.checkIfVolgnummerIsAvailable(planId, volgnummer)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        return bool
    }

    fun changeVolgnummersBetween(volgnummerOud: Int, volgnummerNieuw: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.changeVolgnummerBetween(volgnummerOud, volgnummerNieuw, planId)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun changeVolgnummersBetweenIfSmaller(volgnummerOud: Int, volgnummerNieuw: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.changeVolgnummerBetweenIfSmaller(volgnummerOud, volgnummerNieuw, planId)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun changeVolgnummersGreaterThan(volgnummerNieuw: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.changeVolgnummersGreaterThan(volgnummerNieuw, planId)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    //Checks last number from a list of stappen and then add 1 to it for the new number
    fun determineNumber() : Int{
        return getSize() + 1
    }

    fun getSize() : Int {
        var aantalStappen = 0
        if(stappenplan.value?.stappen != null){
            aantalStappen = stappenplan.value!!.stappen!!.size
            Log.d(TAG, "Aantal stappen = $aantalStappen")
        }
        return aantalStappen
    }

    fun saveNewStap(stap: Stap){
        coroutineScope.launch {
            try {
                stappenplanRepository.insertStap(stap, planId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateStap(stap: Stap){
        coroutineScope.launch {
            try {
                stappenplanRepository.updateStap(stap)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun addUploadRecordToDb(uri: String, naam: String, altText: String): Task<DocumentReference> {
        return firestoreRepository.addUploadRecordToDb(uri, stapId.toString(), naam, altText)
    }

    fun addUploadVidRecordToDb(uri: String, naam: String): Task<DocumentReference> {
        return firestoreRepository.addUploadVidRecordToDb(uri, stapId.toString(), naam)
    }
}