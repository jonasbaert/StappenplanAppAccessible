package hogent.bachelor.stappenplanappbase.persistence

import hogent.bachelor.stappenplanappbase.domain.Image
import hogent.bachelor.stappenplanappbase.domain.Stap
import hogent.bachelor.stappenplanappbase.domain.Stappenplan

class DummyDataSource {
    private var stappenplan1 = Stappenplan(
        id = 198,
        naam = "Aankleden van jezelf",
        beschrijving = "Dit is een stappenplan voor het aankleden van jezelf.",
        isAlreadyInDb = true,
        stappen = listOf(
            Stap(
                id = 111,
                stapNaam = "Eerste stap",
                volgnummer = 1,
                uitleg = "Dit is een eerste stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 1,
                aantalVideos = 0
            ),
            Stap(
                id = 123,
                stapNaam = "Tweede stap",
                volgnummer = 2,
                uitleg = "Dit is de tweede stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 0,
                aantalVideos = 0
            ),
            Stap(
                id = 101,
                stapNaam = "Derde stap",
                volgnummer = 3,
                uitleg = "Dit is de derde stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 0,
                aantalVideos = 0
            )
        )
    )

    private var stappenplan2 = Stappenplan(
        id = 156,
        naam = "Tafel zetten",
        beschrijving = "Dit is een stappenplan voor het zetten van een tafel.",
        isAlreadyInDb = true,
        stappen = listOf(
            Stap(
                id = 125,
                stapNaam = "Eerste stap",
                volgnummer = 1,
                uitleg = "Dit is een eerste stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 0,
                aantalVideos = 1
            ),
            Stap(
                id = 175,
                stapNaam = "Tweede stap",
                volgnummer = 2,
                uitleg = "Dit is de tweede stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 1,
                aantalVideos = 0
            ),
            Stap(
                id = 169,
                stapNaam = "Derde stap",
                volgnummer = 3,
                uitleg = "Dit is de derde stap",
                isGedaan = false,
                isAlInDb = true,
                aantalImages = 0,
                aantalVideos = 0
            )
        )
    )

    fun getStappenplannen() : List<Stappenplan> {
        var stappenplannen = mutableListOf<Stappenplan>()
        stappenplannen.addAll(listOf(stappenplan1, stappenplan2))
        return stappenplannen
    }

}