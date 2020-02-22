package hogent.bachelor.stappenplanappaccessible.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import hogent.bachelor.stappenplanappaccessible.persistence.entities.*

@Dao
interface StappenplanDao{

    @Query("SELECT * FROM stappenplan ORDER BY naam")
    fun getAllStappenplannen(): LiveData<List<StappenplanAllStap>>

    @Query("SELECT * FROM stap WHERE stappenplanId = :stappenplanId ORDER BY volgnummer")
    fun getAllStappenFromStappenplan(stappenplanId: Long): LiveData<List<StapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStappenplanWithStappen(stappenplan: StappenplanEntity, stappen: List<StapEntity>)

    @Insert
    fun insertStap(stap: StapEntity)

    @Query("Update stap SET isGedaan = 'false' WHERE stappenplanId = :stappenplanId")
    fun updateStappenFromStappenplan(stappenplanId: Long)

    @Query("UPDATE Stap SET volgnummer = volgnummer + 1 WHERE stappenplanId = :planId AND volgnummer >= :volgnummer")
    fun updateStappenWithVolgnummerGreaterThan(volgnummer: Int, planId: Long)

    @Query("UPDATE Stap SET volgnummer = volgnummer - 1 WHERE stappenplanId = :planId AND volgnummer <= :volgnummer")
    fun updateStappenWithVolgnummerSmallerThan(volgnummer: Int, planId: Long)

    @Query("UPDATE Stap SET volgnummer = volgnummer - 1 WHERE stappenplanId = :planId AND volgnummer >= :volgnummer")
    fun updateStappenWithVolgnummerGreaterThanByDelete(volgnummer: Int, planId: Long)

    @Query("UPDATE stap SET volgnummer = volgnummer - 1 WHERE stappenplanId = :planId AND volgnummer > :volgnummerOud AND volgnummer <= :volgnummerNieuw")
    fun updateStappenWithVolgnummersBetween(volgnummerOud: Int, volgnummerNieuw: Int, planId: Long)

    @Query("UPDATE stap SET volgnummer = volgnummer + 1  WHERE stappenplanId = :planId AND volgnummer < :volgnummerOud AND volgnummer >= :volgnummerNieuw")
    fun updateStappenWithVolgnummersBetweenIfSmaller(volgnummerOud: Int, volgnummerNieuw: Int, planId: Long)

    @Query("UPDATE stappenplan SET naam = :naam, beschrijving = :beschrijving WHERE id = :stappenplanId")
    fun updateStappenplan(naam: String, beschrijving: String, stappenplanId: Long)

    @Query("UPDATE stap SET stapNaam = :stapNaam, volgnummer = :volgnummer, uitleg = :uitleg, aantalImages = :aantalImages, aantalVideos = :aantalVideos WHERE id = :stapId")
    fun updateStap(stapNaam: String, volgnummer: Int, uitleg: String, aantalImages: Int, aantalVideos: Int, stapId: Long)

    @Query("SELECT MAX(volgnummer) FROM stap WHERE stappenplanId = :stappenplanId")
    fun getLastVolgnummerInList(stappenplanId: Long): Int

    @Query("SELECT MIN(volgnummer) FROM stap WHERE stappenplanId = :stappenplanId")
    fun getFirstVolgnummerInList(stappenplanId: Long): Int

    @Query("SELECT volgnummer FROM stap WHERE stappenplanId = :stappenplanId AND volgnummer = :volgnummer")
    fun getVolgnummerFromStap(stappenplanId: Long, volgnummer: Int) : Int?

    @Delete
    fun deleteStappenplan(stappenplan: StappenplanEntity)

    @Delete
    fun deleteStap(stap: StapEntity)

    @Query("UPDATE stap SET isGedaan = :boolean WHERE id = :stapId")
    fun updateIsGedaanFromStap(stapId: Long, boolean: Boolean)

    @Query("UPDATE stap SET aantalImages = :aantalImages WHERE id = :stapId")
    fun updateAantalImagesFromStap(stapId: Long, aantalImages: Int)

    @Query("UPDATE stap SET aantalImages = :aantalVideos WHERE id = :stapId")
    fun updateAantalVideosFromStap(stapId: Long, aantalVideos: Int)
}