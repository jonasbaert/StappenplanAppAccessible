package hogent.bachelor.stappenplanappaccessible.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Stappenplan")
data class StappenplanEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var naam: String,
    var beschrijving: String,
    var isAlreadyInDb: Boolean
)