package hogent.bachelor.stappenplanappaccessible.ui.stapDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import com.google.firebase.firestore.Query
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class StapDetailViewModel(stap: Stap, stappenplan: Stappenplan, stappenplanDao: StappenplanDao, app: Application): AndroidViewModel(app){
    private val TAG = "STAP_DETAIL_VIEWMODEL"
    private val app = app
    var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var stappenplanRepository = StappenplanRepository(stappenplanDao)

    private var _stap = MutableLiveData<Stap>()
    val stap : MutableLiveData<Stap> = _stap

    private var _images = MutableLiveData<List<Image>>()
    val images : MutableLiveData<List<Image>> = _images

    val numberAndName : String = stap.volgnummer.toString() + ". " + stap.stapNaam
    private var stapId = stap.id
    //private var stappenplanId = stappenplan.id

    init {
        _stap.value = stap
    }

    fun getImageUrlsFromStap(): Query {
        return firestoreRepository.getImageUrlsFromStap(stapId.toString())
    }

    fun deleteImageFromStap(image: Image) {
        firestoreRepository.deleteImage(image.id)
    }

    fun getVideoFromStap(): Query {
        return firestoreRepository.getVideoUrlFromStap(stapId.toString())
    }

    fun updateAantalImagesFromStap(aantal: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.updateAantalImagesFromStap(stapId, aantal)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateAantalVideosFromStap(aantal: Int){
        coroutineScope.launch {
            try {
                stappenplanRepository.updateAantalVideosFromStap(stapId, aantal)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}