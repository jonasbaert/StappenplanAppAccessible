package hogent.bachelor.stappenplanappaccessible.ui.start

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import hogent.bachelor.stappenplanappaccessible.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappaccessible.persistence.repositories.StappenplanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class StartViewModel(val app: Application) : AndroidViewModel(app) {
    val TAG = "START_VIEW_MODEL"

    private val _navigateToModifyStappenplan = MutableLiveData<Stappenplan>()
    val navigateToModifyStappenplan: LiveData<Stappenplan> get() = _navigateToModifyStappenplan

    fun onModifyStappenplanClicked(stappenplan: Stappenplan){
        _navigateToModifyStappenplan.value = stappenplan
    }

    fun  onModifyStappenlanNavigated(){
        _navigateToModifyStappenplan.value = null
    }

}