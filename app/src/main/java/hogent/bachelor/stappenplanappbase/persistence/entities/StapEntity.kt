package hogent.bachelor.stappenplanappbase.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import hogent.bachelor.stappenplanappbase.domain.Stap

@Entity(tableName = "Stap")
data class StapEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var stapNaam: String,
    var volgnummer: Int,
    var uitleg: String,
    var isGedaan: Boolean,
    var isAlInDb: Boolean,
    var aantalImages: Int,
    var aantalVideos: Int,
    var stappenplanId: Long = 0L
)