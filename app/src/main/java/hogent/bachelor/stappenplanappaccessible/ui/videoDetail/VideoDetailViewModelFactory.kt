package hogent.bachelor.stappenplanappaccessible.ui.videoDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Video

class VideoDetailViewModelFactory(private val dataSource1: Video, val app: Application)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VideoDetailViewModel::class.java)){
            return VideoDetailViewModel(dataSource1, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class...")
    }
}

