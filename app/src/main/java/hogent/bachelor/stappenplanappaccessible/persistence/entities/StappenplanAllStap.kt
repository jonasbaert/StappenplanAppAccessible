package hogent.bachelor.stappenplanappaccessible.persistence.entities

import androidx.room.Embedded
import androidx.room.Relation
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan

class StappenplanAllStap {
    @Embedded
    var stappenplan : StappenplanEntity? = null

    @Relation(parentColumn = "id", entityColumn = "stappenplanId", entity = StapEntity::class)
    var stappen : List<StapEntity> = emptyList()
}

fun StappenplanAllStap.stappenplanEntityToDomain(): Stappenplan {
    return Stappenplan(
        id = stappenplan!!.id,
        naam = stappenplan!!.naam,
        beschrijving = stappenplan!!.beschrijving,
        isAlreadyInDb = stappenplan!!.isAlreadyInDb,
        stappen = stappen.stapEntityToDomain() //One-to-many
    )
}

fun List<StapEntity>.stapEntityToDomain(): List<Stap>{
    return map {
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