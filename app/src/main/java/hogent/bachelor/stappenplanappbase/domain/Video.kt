package hogent.bachelor.stappenplanappbase.domain

data class Video(var id: String, var videoUrl: String, var stapId: String) {
    constructor(): this("", "", "")
}