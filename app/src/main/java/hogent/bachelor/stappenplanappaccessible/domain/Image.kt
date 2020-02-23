package hogent.bachelor.stappenplanappaccessible.domain

import java.io.Serializable

data class Image(var id: String, var imageUrl: String, var stapId: String, var naam: String, var altText: String) : Serializable {
    constructor() : this("", "", "", "", "")
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

