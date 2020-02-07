package hogent.bachelor.stappenplanappbase.ui.stapDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappbase.domain.Stap
import hogent.bachelor.stappenplanappbase.domain.Stappenplan
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao
import java.lang.IllegalArgumentException

class StapDetailViewModelFactory(private val dataSource1: Stap, private val dataSource2: Stappenplan, private val dataSource3: StappenplanDao, val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StapDetailViewModel::class.java)){
            return StapDetailViewModel(dataSource1, dataSource2, dataSource3, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}