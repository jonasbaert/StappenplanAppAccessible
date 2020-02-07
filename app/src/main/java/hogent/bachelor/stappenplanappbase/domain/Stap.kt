package hogent.bachelor.stappenplanappbase.domain

import hogent.bachelor.stappenplanappbase.persistence.entities.StapEntity
import hogent.bachelor.stappenplanappbase.persistence.entities.StappenplanEntity
import java.io.Serializable

data class Stap(var id: Long, var stapNaam: String, var volgnummer: Int, var uitleg: String, var isGedaan: Boolean, var isAlInDb: Boolean, var aantalImages: Int, var aantalVideos: Int) : Serializable {
    constructor() : this(0, "", 0, "", false, false, 0, 0)
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

fun Stap.stapDomainToDB() : StapEntity {
    return StapEntity(
        id = this.id,
        stapNaam = this.stapNaam,
        volgnummer = this.volgnummer,
        uitleg = this.uitleg,
        isGedaan = this.isGedaan,
        aantalImages = this.aantalImages,
        aantalVideos = this.aantalVideos,
        isAlInDb = this.isAlInDb
    )
}