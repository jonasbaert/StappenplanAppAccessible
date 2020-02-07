package hogent.bachelor.stappenplanappbase.firestore

import android.util.Log
import hogent.bachelor.stappenplanappbase.domain.Image
import hogent.bachelor.stappenplanappbase.domain.Stap
import hogent.bachelor.stappenplanappbase.domain.Stappenplan
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import hogent.bachelor.stappenplanappbase.domain.Video

class FirestoreRepository {
    var TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()

    fun saveImage(image: Image){
        firestoreDB.collection("images").add(image)
            .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID : ${documentReference.id}")
        }
            .addOnFailureListener{ e ->
                Log.w(TAG, "Error adding document ", e)
            }
    }

    fun getImageUrlsFromStap(stapId: String): Query{
        return firestoreDB.collection("images").whereEqualTo("stapId", stapId)
    }

    fun deleteImage(imageId: String){
        firestoreDB.collection("images").document(imageId).delete()
    }

    fun updateImage(image: Image) {
        firestoreDB.collection("images").document(image.id).set(image)
    }

    fun addUploadRecordToDb(uri: String, stapId: String): Task<DocumentReference>{
        val data = HashMap<String, Any>()
        data["imageUrl"] = uri
        data["stapId"] = stapId

        var docRef = firestoreDB.collection("images")
        return docRef.add(data)
    }

    fun getVideoUrlFromStap(stapId: String): Query {
        return firestoreDB.collection("videos").whereEqualTo("stapId", stapId)
    }

    fun deleteVideo(videoId: String){
        firestoreDB.collection("videos").document(videoId).delete()
    }

    fun updateVideo(video: Video) {
        firestoreDB.collection("videos").document(video.id).set(video)
    }

    fun addUploadVidRecordToDb(uri: String, stapId: String): Task<DocumentReference>{
        val data = HashMap<String, Any>()
        data["videoUrl"] = uri
        data["stapId"] = stapId

        var docRef = firestoreDB.collection("videos")
        return docRef.add(data)
    }
}