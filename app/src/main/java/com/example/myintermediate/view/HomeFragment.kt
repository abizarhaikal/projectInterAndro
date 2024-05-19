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
import com.example.myintermediate.adapter.LoadingStateAdapter
import com.example.myintermediate.adapter.StoryAdapter
import com.example.myintermediate.data.remote.ListStoryItem
import com.example.myintermediate.data.remote.Story
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

        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())

    }
    private fun setupData() {
        storyAdapter = StoryAdapter()
        binding.rvHome.adapter = storyAdapter
        binding.rvHome.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        isLoading(true)
        homeFragmentViewModel.story.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
            isLoading(false)
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.lotLoading.visibility = View.VISIBLE
        } else {
            binding.lotLoading.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        homeFragmentViewModel.story
    }

}
