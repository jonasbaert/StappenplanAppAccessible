package hogent.bachelor.stappenplanappaccessible.ui.modifyStap

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import hogent.bachelor.stappenplanappaccessible.R
import hogent.bachelor.stappenplanappaccessible.databinding.FragmentModifyStapBinding
import hogent.bachelor.stappenplanappaccessible.domain.Stap
import hogent.bachelor.stappenplanappaccessible.domain.Stappenplan
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import hogent.bachelor.stappenplanappaccessible.persistence.StappenplanDatabase
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast_two.*
import kotlinx.android.synthetic.main.fragment_modify_stap.*
import java.io.IOException
import java.util.*

class ModifyStapFragment : Fragment(){
    private var TAG = "MODIFY_STAP_FRAGMENT"
    private lateinit var viewModel: ModifyStapViewModel
    private lateinit var stap : Stap
    private lateinit var stappenplan : Stappenplan

    private var isFotoToegevoegd: Boolean = false
    private var isVideoToegevoegd: Boolean = false

    private val PICK_IMAGE_REQUEST = 1
    private val PICK_VIDEO_REQUEST = 2
    private val PICK_AUDIO_REQUEST = 3

    private var filePath: Uri? = null
    private var filePath2: Uri? = null
    private var firebaseStore : FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentModifyStapBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_modify_stap, container, false)

        val app = requireNotNull(this.activity).application

        val stap = ModifyStapFragmentArgs.fromBundle(arguments!!).stap
        val stappenplan = ModifyStapFragmentArgs.fromBundle(arguments!!).stappenplan

        val dataSource = StappenplanDatabase.getInstance(app).stappenplanDao
        val viewModelFactory = ModifyStapViewModelFactory(stap, stappenplan, dataSource, app)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ModifyStapViewModel::class.java)
        binding.viewmodel = viewModel

        this.stap = stap
        this.stappenplan = stappenplan

        if(stap.stapNaam.isBlank()){
            binding.stapTitleNaam.text = "Nieuwe stap"
        }
        else{
            binding.stapTitleNaam.text = "Pas stap aan"
        }

        binding.editStapNaam.setText(stap.stapNaam)
        binding.editUitleg.setText(stap.uitleg)
        binding.editStapNummer.setText(stap.volgnummer.toString())

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        progressBar = binding.progressBar

        binding.btnUploadImage.setOnClickListener {
            launchGallery()
        }

        binding.btnUploadVideo.setOnClickListener {
            launchVideo()
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun updateOrAddStappenplan(stap: Stap){
        if(!stap.isAlInDb){
            stap.isAlInDb = true
            viewModel.saveNewStap(stap)
        }
        else{
            viewModel.updateStap(stap)
        }
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_modify_stap.setOnNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.action_back ->
                    this.findNavController().navigate(ModifyStapFragmentDirections.actionBackAgainToStappenplanDetailFragment(stappenplan))
                R.id.action_accept -> {
                    try {
                        var naam = edit_stap_naam.text
                        var uitleg = edit_uitleg.text
                        var volgnummer = edit_stap_nummer.text

                        if (naam.isBlank() && uitleg.isBlank()) {

                            showToast("Naam en uitleg moeten ingevuld zijn!")

                        } else if (naam.isBlank()) {

                            showToast("Naam moet ingevuld zijn")

                        } else if (uitleg.isBlank()) {

                            showToast("Meer uitleg moet ingevuld zijn")

                        } else if (naam.toString() == stap.stapNaam
                            && uitleg.toString() == stap.uitleg
                            && volgnummer.toString() == stap.volgnummer.toString()
                            && !isFotoToegevoegd && !isVideoToegevoegd) {

                            showToast("Wijzig eerst iets aan deze stap of keer terug")

                        } else if (volgnummer.toString().toInt() > viewModel.getSize() + 1) {

                            showToast("Geef een volgnummer tussen 1 en ${viewModel.getSize() + 1} in.")

                        } else if(naam.toString() != stap.stapNaam
                            || uitleg.toString() != stap.uitleg
                            || volgnummer.toString() != stap.volgnummer.toString()
                            || isFotoToegevoegd
                            || isVideoToegevoegd) {

                            stap.stapNaam = naam.toString()
                            stap.uitleg = uitleg.toString()

                            if(volgnummer.toString() != stap.volgnummer.toString()
                                || volgnummer.toString().toInt() == 0
                                || volgnummer.toString().isBlank()) {

                                var oudVolgnummer = stap.volgnummer
                                var nieuwVolgnummer = volgnummer.toString().toInt()

                                if (oudVolgnummer == 0 && nieuwVolgnummer > 0) {
                                    viewModel.changeVolgnummersGreaterThan(nieuwVolgnummer)
                                    stap.volgnummer = nieuwVolgnummer
                                } else if (nieuwVolgnummer == 0) {
                                    stap.volgnummer = viewModel.determineNumber()
                                } else if (nieuwVolgnummer.toString().isBlank()) {
                                    stap.volgnummer = viewModel.determineNumber()
                                } else if (!viewModel.checkIfAvailable(nieuwVolgnummer)) {
                                    if (oudVolgnummer == 1 && nieuwVolgnummer > 1) {
                                        viewModel.changeVolgnummersBetween(
                                            oudVolgnummer,
                                            nieuwVolgnummer
                                        )
                                        stap.volgnummer = nieuwVolgnummer
                                    } else if (oudVolgnummer < nieuwVolgnummer) {
                                        viewModel.changeVolgnummersBetween(
                                            oudVolgnummer,
                                            nieuwVolgnummer
                                        )
                                        stap.volgnummer = nieuwVolgnummer
                                    } else if (oudVolgnummer > nieuwVolgnummer) {
                                        viewModel.changeVolgnummersBetweenIfSmaller(
                                            oudVolgnummer,
                                            nieuwVolgnummer
                                        )
                                        stap.volgnummer = nieuwVolgnummer
                                    }
                                } else {
                                    stap.volgnummer = nieuwVolgnummer
                                }
                            }

                            updateOrAddStappenplan(stap)
                            isFotoToegevoegd = false
                            isVideoToegevoegd = false
                            this.findNavController().navigate(ModifyStapFragmentDirections.actionBackAgainToStappenplanDetailFragment(stappenplan))
                        }
                    }catch (e: Exception){
                        Log.w(TAG, "Exception : " + e.message)
                    }
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data == null || data.data == null) {
                    return
                }

                filePath = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        filePath
                    )
                    image_preview.setImageBitmap(bitmap)
                    uploadImage()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                if (data == null || data.data == null) {
                    return
                }

                filePath2 = data.data
                video_url_preview.text = filePath2.toString()
                uploadVideo()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun launchGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Kies een foto"), PICK_IMAGE_REQUEST)
    }

    private fun launchVideo(){
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Kies een video"), PICK_VIDEO_REQUEST)
    }

    private fun addUploadRecordToDb(uri: String, naam: String, altText: String){
        viewModel.addUploadRecordToDb(uri, naam, altText).addOnSuccessListener { documentReference ->
            showToastSuccess("Foto is opgeslagen in de databank")
        }
            .addOnFailureListener { e ->
                showToast("Error bij het opslaan naar de databank")
            }
    }

    private fun addUploadVidRecordToDb(uri: String, naam: String){
        viewModel.addUploadVidRecordToDb(uri, naam).addOnSuccessListener { documentReference ->
            showToastSuccess("Video is opgeslagen in de databank")
        }
            .addOnFailureListener { e ->
                showToast("Error bij het opslaan naar de databank")
            }
    }

    private fun uploadImage() {
       if(filePath != null){
            progressBar.visibility = View.VISIBLE
            val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString(), "Naam van foto", "Een alt tekst bij een foto")
                    isFotoToegevoegd = true
                    stap.aantalImages = stap.aantalImages + 1

                    progressBar.visibility = View.VISIBLE
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
           showToast("Upload een foto aub")
        }
    }

    private fun uploadVideo() {
        if(filePath2 != null){
            progressBar.visibility = View.VISIBLE
            val ref = storageReference?.child("videos/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath2!!)

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadVidRecordToDb(downloadUri.toString(), "Naam van video")
                    isVideoToegevoegd = true
                    stap.aantalVideos = stap.aantalVideos + 1

                    progressBar.visibility = View.GONE
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }
        else {
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, custom_toast_container)
            val txt: TextView = layout.findViewById(R.id.toast_text)
            showToast("Upload een video aub")
        }
    }

    private fun showToast(text: String){
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, custom_toast_container)
        val txt: TextView = layout.findViewById(R.id.toast_text)

        txt.text = text
        with (Toast(requireContext())) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }

    private fun showToastSuccess(text: String){
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast_two, custom_toast_container_success)
        val txt: TextView = layout.findViewById(R.id.toast_text_success)

        txt.text = text
        with (Toast(requireContext())) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }
}