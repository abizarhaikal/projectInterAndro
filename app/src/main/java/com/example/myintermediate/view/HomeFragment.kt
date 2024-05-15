package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.adapter.StoryAdapter
import com.example.myintermediate.data.remote.ListStoryItem
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
    private lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentViewModel.getSession().observe(requireActivity()) { session ->

            val username = session.username
            binding.tvUser.text = username
            Log.d("HomeFragment", "this name $username")

        }
        setupData()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            startActivity(intent)
        }


    }


    private fun setupData() {
        homeFragmentViewModel.getAll().observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        result.data.listStory.let { setStory(it) }
                        isLoading(false)
                    }

                    is Result.Error -> {
                        isLoading(false)
                        Log.d("HomeFragment", "this error ${result.error}")
                    }
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.lotLoading.visibility = View.VISIBLE
        } else {
            binding.lotLoading.visibility = View.GONE
        }

    }

    private fun setStory(story: List<ListStoryItem?>) {
        val storyAdapter = StoryAdapter()
        storyAdapter.submitList(story)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHome.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvHome.addItemDecoration(itemDecoration)
        binding.rvHome.adapter = storyAdapter
    }


    override fun onResume() {
        super.onResume()
        homeFragmentViewModel.getAll()
    }
}
