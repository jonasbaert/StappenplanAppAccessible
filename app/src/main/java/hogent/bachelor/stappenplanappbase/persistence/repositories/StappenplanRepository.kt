package hogent.bachelor.stappenplanappbase.persistence.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import hogent.bachelor.stappenplanappbase.domain.*
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappbase.persistence.entities.stapEntityToDomain
import hogent.bachelor.stappenplanappbase.persistence.entities.stappenplanEntityToDomain

class StappenplanRepository(private val stappenplanDao: StappenplanDao) {
    val stappenplannen : LiveData<List<Stappenplan>> = Transformations.map(stappenplanDao.getAllStappenplannen()) {
        it?.let {
            it.map {
                it.stappenplanEntityToDomain()
            }
        }
    }

    fun getStappenFromStappenplan(stappenplanId: Long) : LiveData<List<Stap>> {
        return Transformations.map(stappenplanDao.getAllStappenFromStappenplan(stappenplanId)){
            it?.let {
                it.map {
                    Stap(
                        id = it.id,
                        stapNaam = it.stapNaam,
                        volgnummer = it.volgnummer,
                        uitleg = it.uitleg,
                        isGedaan = it.isGedaan,
                        isAlInDb = it.isAlInDb,
                        aantalImages = it.aantalImages,
                        aantalVideos = it.aantalVideos
                    )
                }
            }
        }
    }
    fun insertStappenplannenWithStappen(stappenplan: Stappenplan){
        stappenplanDao.insertStappenplanWithStappen(stappenplan.stappenplanDomainToDB(), emptyList())
    }

    fun updateStappenplan(stappenplan: Stappenplan){
        stappenplanDao.updateStappenplan(stappenplan.naam, stappenplan.beschrijving, stappenplan.id)
    }

    fun deleteStappenplan(stappenplan: Stappenplan){
        if (stappenplan.stappen.isNotEmpty()) {
            stappenplan.stappen.forEach {
                deleteStap(it)
            }
        }
        stappenplanDao.deleteStappenplan(stappenplan.stappenplanDomainToDB())
    }

    fun insertStap(stap: Stap, stappenplanId: Long){
        var s = stap.stapDomainToDB()
        s.stappenplanId = stappenplanId
        stappenplanDao.insertStap(s)
    }

    fun updateStap(stap: Stap){
        stappenplanDao.updateStap(stap.stapNaam, stap.volgnummer, stap.uitleg, stap.aantalImages, stap.aantalVideos, stap.id)
    }

    fun deleteStap(stap: Stap){
        stappenplanDao.deleteStap(stap.stapDomainToDB())
    }

    fun changeVolgnummerBetween(volgnummerOud: Int, volgnummerNieuw: Int, planId: Long){
        stappenplanDao.updateStappenWithVolgnummersBetween(volgnummerOud, volgnummerNieuw, planId)
    }

    fun changeVolgnummerBetweenIfSmaller(volgnummerOud: Int, volgnummerNieuw: Int, planId: Long){
        stappenplanDao.updateStappenWithVolgnummersBetweenIfSmaller(volgnummerOud, volgnummerNieuw, planId)
    }

    fun changeVolgnummersByDelete(volgnummer: Int, planId: Long){
        stappenplanDao.updateStappenWithVolgnummerGreaterThanByDelete(volgnummer, planId)
    }

    fun resetStappenFromStappenplan(stappenplanId: Long) {
        stappenplanDao.updateStappenFromStappenplan(stappenplanId)
    }

    fun veranderIsGedaan(stapId: Long, boolean: Boolean) {
        stappenplanDao.updateIsGedaanFromStap(stapId, boolean)
    }

    fun checkIfVolgnummerIsAvailable(planId: Long, volgnummer: Int): Boolean{
        return stappenplanDao.getVolgnummerFromStap(planId, volgnummer) == null
    }

    fun changeVolgnummersGreaterThan(volgnummerNieuw: Int, planId: Long) {
        stappenplanDao.updateStappenWithVolgnummerGreaterThan(volgnummerNieuw, planId)
    }

    fun updateAantalImagesFromStap(stapId: Long, aantal: Int) {
        stappenplanDao.updateAantalImagesFromStap(stapId, aantal)
    }

    fun updateAantalVideosFromStap(stapId: Long, aantal: Int) {
        stappenplanDao.updateAantalVideosFromStap(stapId, aantal)
    }

}

