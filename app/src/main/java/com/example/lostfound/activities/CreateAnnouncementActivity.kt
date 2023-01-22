package com.example.lostfound.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityCreateAnnouncementBinding
import com.example.lostfound.utils.location.NavigationService
import com.example.lostfound.utils.announcement.AnnouncementViewModel
import com.example.lostfound.utils.location.CoordToAddressDecoder
import com.example.lostfound.utils.memento.AnnouncementSnapshot
import com.example.lostfound.utils.memento.History
import com.google.android.gms.maps.model.LatLng

class CreateAnnouncementActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAnnouncementBinding
    lateinit var title :EditText
    lateinit var details: EditText
    lateinit var typeChoice:RadioGroup
    lateinit var history :History
    lateinit var coords : LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityCreateAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ContextCompat.startForegroundService(this, Intent(this, NavigationService::class.java))
        var img: Uri? = null
        title = binding.titleInput
        details = binding.detailsInput
        val back = binding.toolbar.toolbarReturnButton
        typeChoice = binding.annTypeRadio
        val getImage = binding.btnAnnAddPic
        history = History(this)
        val finish = binding.btnFinishAddAnnouncement

        val createAnnViewModel = AnnouncementViewModel()

        createAnnViewModel.annResult.observe(this@CreateAnnouncementActivity, Observer {
            var creationState = it?:return@Observer

            if (creationState.error!= null){
                Toast.makeText(this, "Error ${creationState.error.toString()}", Toast.LENGTH_SHORT).show()
            }
            if (creationState.success!=null ){
                Toast.makeText(this, "Announcement Created", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        typeChoice.setOnCheckedChangeListener { group, checkedId ->
            typeChoice.check(checkedId)
        }

        back.setOnClickListener {
            history.undo()
        }

        finish.setOnClickListener {
            intent.getStringExtra("USER_ID")?.toInt()?.let { it1 ->
                createAnnViewModel.create(
                    this,
                    if(typeChoice.checkedRadioButtonId == R.id.ann_type_lost) "LOST" else "FOUND",
                    img,
                    details.text.toString(),
                    "${NavigationService.mLocation?.latitude}|${NavigationService.mLocation?.longitude}",
                    CoordToAddressDecoder.getAddressFromLocation(NavigationService.mLocation?.latitude, NavigationService.mLocation?.longitude, this),
                    "wallet",
                    title.text.toString(),
                    it1
                )
            }
        }
        val pickImage  = registerForActivityResult( ActivityResultContracts.GetContent()
        ) {
            img = it
        }
        getImage.setOnClickListener {
            pickImage.launch("image/*")
        }




    }

    fun createSnapshot():AnnouncementSnapshot{
        var c = if(typeChoice.checkedRadioButtonId == R.id.ann_type_lost) "lost" else "found"
        return AnnouncementSnapshot(this, title.text.toString(), details.text.toString(), c, "wallet")
    }

    fun restore( title:String,  details:String,  type :String,  tag :String){
        this.title.setText(title)
        this.details.setText(details)
        if(type == "lost") typeChoice.check(R.id.ann_type_lost) else typeChoice.check(R.id.ann_type_found)
    }
}