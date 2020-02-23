package hogent.bachelor.stappenplanappaccessible.ui.imageDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Image

class ImageDetailViewModelFactory(private val dataSource1: Image, val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageDetailViewModel::class.java)){
            return ImageDetailViewModel(dataSource1, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}


