package hogent.bachelor.stappenplanappaccessible.ui.stappenplannen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao

class StappenplannenViewModelFactory(private val dataSource: StappenplanDao, private val application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StappenplannenViewModel::class.java)) {
            return StappenplannenViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}