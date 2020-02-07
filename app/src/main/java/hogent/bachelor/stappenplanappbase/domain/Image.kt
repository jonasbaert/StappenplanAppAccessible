package hogent.bachelor.stappenplanappbase.domain

import java.io.Serializable

class Image(var id: String, var imageUrl: String, var stapId: String) : Serializable {
    constructor() : this("", "", "")
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

