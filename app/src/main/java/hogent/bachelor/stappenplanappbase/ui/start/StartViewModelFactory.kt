package hogent.bachelor.stappenplanappbase.ui.start

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappbase.persistence.DummyDataSource
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao

class StartViewModelFactory(private val dataSource: StappenplanDao, private val application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}