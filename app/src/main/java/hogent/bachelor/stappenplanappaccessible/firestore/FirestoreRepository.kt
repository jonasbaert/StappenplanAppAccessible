package hogent.bachelor.stappenplanappaccessible.firestore

import android.util.Log
import hogent.bachelor.stappenplanappaccessible.domain.Image
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import hogent.bachelor.stappenplanappaccessible.domain.Video

class FirestoreRepository {
    var TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()

    fun saveImage(image: Image){
        firestoreDB.collection("imagesplus").add(image)
            .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID : ${documentReference.id}")
        }
            .addOnFailureListener{ e ->
                Log.w(TAG, "Error adding document ", e)
            }
    }

    fun getImageUrlsFromStap(stapId: String): Query{
        return firestoreDB.collection("imagesplus").whereEqualTo("stapId", stapId)
    }

    fun deleteImage(imageId: String){
        firestoreDB.collection("imagesplus").document(imageId).delete()
    }

    fun updateImage(image: Image) {
        firestoreDB.collection("imagesplus").document(image.id).set(image)
    }

    fun addUploadRecordToDb(uri: String, stapId: String, naam: String, altText: String): Task<DocumentReference>{
        val data = HashMap<String, Any>()
        data["imageUrl"] = uri
        data["stapId"] = stapId
        data["naam"] = naam
        data["altText"] = altText

        var docRef = firestoreDB.collection("imagesplus")
        return docRef.add(data)
    }

    fun getVideoUrlFromStap(stapId: String): Query {
        return firestoreDB.collection("videosplus").whereEqualTo("stapId", stapId)
    }

    fun deleteVideo(videoId: String){
        firestoreDB.collection("videosplus").document(videoId).delete()
    }

    fun updateVideo(video: Video) {
        firestoreDB.collection("videosplus").document(video.id).set(video)
    }

    fun addUploadVidRecordToDb(uri: String, stapId: String, naam: String): Task<DocumentReference>{
        val data = HashMap<String, Any>()
        data["videoUrl"] = uri
        data["stapId"] = stapId
        data["naam"] = naam

        var docRef = firestoreDB.collection("videosplus")
        return docRef.add(data)
    }
}