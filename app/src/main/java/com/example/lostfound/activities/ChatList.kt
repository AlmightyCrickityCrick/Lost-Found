package com.example.lostfound.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.adapters.ContactAdapter
import com.example.lostfound.data.DebugConstants
import com.example.lostfound.data.model.Contact
import com.example.lostfound.listeners.ContactListener
import com.example.lostfound.utils.chat.ChatListViewModel
import com.example.lostfound.utils.setMenuButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatList.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatList : Fragment(), ContactListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ContactAdapter
    lateinit var chats :ArrayList<Contact>
    lateinit var chatListViewModel:ChatListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        chats = DebugConstants.getContacts()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatListViewModel = ChatListViewModel()
        chatListViewModel.getAllChats((activity as MainActivity).user.userId)

        chatListViewModel.chatList.observe(viewLifecycleOwner, Observer{
            val tmp = it ?: return@Observer
            if(tmp.success!=null)
                chats = tmp.success
        })

        recyclerView = view.findViewById(R.id.chat_list_view)
        var toolbar = view.findViewById<Toolbar>(R.id.toolbar_chat_list)
        activity?.let { setMenuButton(toolbar, it) }
        initAdapter()



    }
    private fun initAdapter() {
        adapter = ContactAdapter(chats, this)
        recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onContactClicked(contact: Contact) {
        var chatFragment = Chat(contact)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.activity_content, chatFragment)?.commit()
    }

    override fun onAcceptClicked(contact: Contact) {

        onContactClicked(contact)
    }

    override fun onDeclineClicked(contact: Contact) {
        TODO("Not yet implemented")
    }
}