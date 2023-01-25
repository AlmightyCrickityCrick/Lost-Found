package com.example.lostfound.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.adapters.ChatAdapter
import com.example.lostfound.data.DebugConstants
import com.example.lostfound.data.model.Contact
import com.example.lostfound.utils.setMenuButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Chat.newInstance] factory method to
 * create an instance of this fragment.
 */
class Chat(private var contact:Contact) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChatAdapter
    var messages = DebugConstants.getMessages()
    //lateinit var adapter:ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var toolbar:androidx.appcompat.widget.Toolbar = view.findViewById(R.id.single_chat_toolbar)
        activity?.let { setMenuButton(toolbar, it) }
        var name:TextView = toolbar.findViewById(R.id.toolbar_fragment_name)
        name.setText(contact.user.user_name)
        recyclerView = view.findViewById(R.id.message_recycler_view)
        initAdapter()
    }
    private fun initAdapter(){
        adapter = ChatAdapter(messages, R.drawable.ic_account, 1)
        recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Chat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, contact: Contact) =
            Chat(contact).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}