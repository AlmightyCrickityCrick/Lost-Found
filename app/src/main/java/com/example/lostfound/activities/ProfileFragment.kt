package com.example.lostfound.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.lostfound.R
import com.example.lostfound.data.Result
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.data.user.LoginDataSource
import com.example.lostfound.utils.setMenuButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var user :LoggedInUser
    lateinit var username:TextView
    lateinit var rating:TextView
    lateinit var email:TextView
    lateinit var phone:TextView
    lateinit var birthday:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val dataSource = LoginDataSource()
        getInfo(dataSource)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(view)
        getViews(view)
        setViews()



    }

    private fun setViews(){
        username.text = user.displayName
        rating.text = "Rating ${user.rating}"
        email.text=user.email
        phone.text=user.phone
        birthday.text=user.dateOfBirth
    }

    private fun getViews(view: View){
        username = view.findViewById(R.id.profile_username)
        rating = view.findViewById(R.id.profile_rating)
        email = view.findViewById(R.id.profile_email)
        phone=view.findViewById(R.id.profile_phone)
        birthday=view.findViewById(R.id.profile_birthday)
    }

    private fun setToolbar(view: View){
        var toolbar = view.findViewById<Toolbar>(R.id.profile_toolbar)
        activity?.let { setMenuButton(toolbar, it) }
            var butChange = toolbar.findViewById<ImageView>(R.id.toolbar_btn_change_info)
            butChange.setOnClickListener {
                activity?.let {
                    var intent = Intent(it, RegisterInformationActivity::class.java)
                    intent.putExtra("USER", user)
                    startActivity(intent)
                }
            }
        }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getInfo(dataSource: LoginDataSource){
        runBlocking {
            val job = GlobalScope.async { dataSource.getUserInfo() }
            val result = job.await()

            if (result is Result.Success) {
                user = result.data
            }
        }
    }
}