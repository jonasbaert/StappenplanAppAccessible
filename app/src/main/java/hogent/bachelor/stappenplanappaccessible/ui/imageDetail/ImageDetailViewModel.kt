package hogent.bachelor.stappenplanappaccessible.ui.imageDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.firestore.FirestoreRepository
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ImageDetailViewModel(image: Image, stap: Stap, stappenplanDao: StappenplanDao, app: Application) : AndroidViewModel(app){
    var firestoreRepository = FirestoreRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    var stappenplanRepository = StappenplanRepository(stappenplanDao)

    private var _image = MutableLiveData<Image>()
    val image : MutableLiveData<Image> = _image

    private var stapId = stap.id

    init {
        _image.value = image
    }

    fun deleteImageFromStap(image: Image) {
        firestoreRepository.deleteImage(image.id)
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
}