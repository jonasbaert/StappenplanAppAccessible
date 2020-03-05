package hogent.bachelor.stappenplanappaccessible.ui.videoDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Video
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class VideoDetailViewModel(video: Video, stap: Stap, stappenplanDao: StappenplanDao, app: Application) : AndroidViewModel(app){
    var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var stappenplanRepository = StappenplanRepository(stappenplanDao)

    private var _video = MutableLiveData<Video>()
    val video : MutableLiveData<Video> = _video

    private var stapId = stap.id

    init {
        _video.value = video
    }

    fun deleteVideoFromStap(video: Video) {
        firestoreRepository.deleteVideo(video.id)
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

