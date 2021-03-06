package hogent.bachelor.stappenplanappaccessible.ui.modifyStap

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao

class ModifyStapViewModelFactory(private val dataSource1: Stap, private val dataSource2: Stappenplan, private val dataSource3: StappenplanDao, private val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ModifyStapViewModel::class.java)){
            return ModifyStapViewModel(dataSource1, dataSource2, dataSource3, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}