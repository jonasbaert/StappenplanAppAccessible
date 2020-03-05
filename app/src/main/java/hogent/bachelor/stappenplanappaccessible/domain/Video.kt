package hogent.bachelor.stappenplanappaccessible.domain

import java.io.Serializable

data class Video(var id: String, var videoUrl: String, var stapId: String, var naam: String) : Serializable {
    constructor(): this("", "", "", "")
}