package hogent.bachelor.stappenplanappaccessible.ui.start

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao

class StartViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}