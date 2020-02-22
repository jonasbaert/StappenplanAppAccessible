package hogent.bachelor.stappenplanappaccessible.ui.modifyStappenplan

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao

class ModifyStappenplanViewModelFactory(private val dataSource: Stappenplan, private val dataSource2: StappenplanDao, private val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ModifyStappenplanViewModel::class.java)){
            return ModifyStappenplanViewModel(dataSource, dataSource2, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}