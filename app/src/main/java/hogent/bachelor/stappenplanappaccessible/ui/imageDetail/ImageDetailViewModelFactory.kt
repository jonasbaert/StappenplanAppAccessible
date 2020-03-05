package hogent.bachelor.stappenplanappaccessible.ui.imageDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Image
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao

class ImageDetailViewModelFactory(private val dataSource1: Image, private val dataSource2: Stap, private val stappenplanDao: StappenplanDao, val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageDetailViewModel::class.java)){
            return ImageDetailViewModel(dataSource1, dataSource2, stappenplanDao, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}


