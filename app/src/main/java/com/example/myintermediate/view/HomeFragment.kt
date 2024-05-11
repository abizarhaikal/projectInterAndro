package com.example.myintermediate.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.FragmentHomeBinding
import com.example.myintermediate.viewModel.HomeFragmentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentViewModel by viewModels<HomeFragmentViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }


    var name: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater ,container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater ,container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View ,savedInstanceState: Bundle?) {
        super.onViewCreated(view ,savedInstanceState)

        homeFragmentViewModel.getSession().observe(requireActivity()) { session ->
            val username = session.username
            binding.tvUser.text = username


            Log.d("HomeFragment" ,"this name $username")
        }

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

}