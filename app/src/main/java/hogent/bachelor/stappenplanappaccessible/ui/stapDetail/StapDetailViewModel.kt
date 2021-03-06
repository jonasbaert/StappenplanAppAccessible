package hogent.bachelor.stappenplanappaccessible.ui.stapDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import com.google.firebase.firestore.Query
import hogent.bachelor.stappenplanappaccessible.domain.Video
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

    private var _stappenplan = MutableLiveData<Stappenplan>()
    val stappenplan : MutableLiveData<Stappenplan> = _stappenplan

    private var _stap = MutableLiveData<Stap>()
    val stap : MutableLiveData<Stap> = _stap

    private var _images = MutableLiveData<List<Image>>()
    val images : MutableLiveData<List<Image>> = _images

    private val _navigateToImageDetail = MutableLiveData<Image>()
    val navigateToImageDetail: LiveData<Image> get() = _navigateToImageDetail

    private val _navigateToVideoDetail = MutableLiveData<Video>()
    val navigateToVideoDetail: LiveData<Video> get() = _navigateToVideoDetail

    val numberAndName : String = "Stap " + stap.volgnummer.toString() + ". " + stap.stapNaam

    private var stappenplanId = stappenplan.id
    private var stapId = stap.id

    init {
        _stap.value = stap
        _stappenplan.value = stappenplan
    }

    fun getImageUrlsFromStap(): Query {
        return firestoreRepository.getImageUrlsFromStap(stapId.toString())
    }

    fun getVideoFromStap(): Query {
        return firestoreRepository.getVideoUrlFromStap(stapId.toString())
    }

    fun onImageClicked(image: Image) {
        _navigateToImageDetail.value = image
    }

    fun onImageNavigated(){
        _navigateToImageDetail.value = null
    }

    fun onVideoClicked(video: Video){
        _navigateToVideoDetail.value = video
    }

    fun onVideoNavigated(){
        _navigateToVideoDetail.value = null
    }

    //Delete a stap
    fun deleteStap(stap: Stap){
        coroutineScope.launch {
            try {
                stappenplanRepository.deleteStap(stap)
                stappenplanRepository.updateStapSizeFromStappenplan(stappenplan.value!!.stapSize - 1, stappenplanId)
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