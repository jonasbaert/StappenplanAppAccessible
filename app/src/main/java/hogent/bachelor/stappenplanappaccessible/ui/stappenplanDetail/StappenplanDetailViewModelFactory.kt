package hogent.bachelor.stappenplanappaccessible.ui.stappenplanDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import java.lang.IllegalArgumentException

class StappenplanDetailViewModelFactory(private val dataSource: Stappenplan, private val dataSource2: StappenplanDao, private val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StappenplanDetailViewModel::class.java)){
            return StappenplanDetailViewModel(dataSource, dataSource2, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}