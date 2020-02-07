package hogent.bachelor.stappenplanappbase.domain

import hogent.bachelor.stappenplanappbase.persistence.entities.StappenplanEntity
import java.io.Serializable

data class Stappenplan(var id: Long, var naam: String, var beschrijving: String, var stappen: List<Stap>, var isAlreadyInDb: Boolean) : Serializable {
    constructor() : this(0, "", "", emptyList(), false)

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

fun Stappenplan.stappenplanDomainToDB() : StappenplanEntity {
    return StappenplanEntity(
        id = this.id,
        naam = this.naam,
        beschrijving = this.beschrijving,
        isAlreadyInDb = this.isAlreadyInDb
    )
}