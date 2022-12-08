package com.example.lostfound.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityCreateAnnouncementBinding
import com.example.lostfound.databinding.ActivityMainBinding
import com.example.lostfound.utils.memento.AnnouncementSnapshot
import com.example.lostfound.utils.memento.History
import com.example.lostfound.utils.memento.Snapshot

class CreateAnnouncementActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAnnouncementBinding
    lateinit var title :EditText
    lateinit var details: EditText
    lateinit var typeChoice:RadioGroup
    lateinit var history :History
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityCreateAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = binding.titleInput
        details = binding.detailsInput
        var back = binding.toolbar.toolbarReturnButton
        typeChoice = binding.annTypeRadio
        history = History(this)

        typeChoice.setOnCheckedChangeListener { group, checkedId ->
            typeChoice.clearCheck()
            typeChoice.check(checkedId)
        }

        back.setOnClickListener {
            history.undo()
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