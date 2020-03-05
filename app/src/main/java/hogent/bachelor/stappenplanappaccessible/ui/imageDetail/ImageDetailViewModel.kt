package hogent.bachelor.stappenplanappaccessible.ui.imageDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Image

class ImageDetailViewModel(image: Image, app: Application) : AndroidViewModel(app){
    private var _image = MutableLiveData<Image>()
    val image : MutableLiveData<Image> = _image

    init {
        _image.value = image
    }
}