package hogent.bachelor.stappenplanappaccessible.ui.videoDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Video

class VideoDetailViewModel(video: Video, app: Application) : AndroidViewModel(app){
    private var _video = MutableLiveData<Video>()
    val video : MutableLiveData<Video> = _video

    init {
        _video.value = video
    }
}

