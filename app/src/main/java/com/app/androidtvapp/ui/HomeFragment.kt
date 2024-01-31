package com.app.androidtvapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.app.androidtvapp.R
import com.app.androidtvapp.data.remote.Result
import com.app.androidtvapp.databinding.FragmentHomeBinding
import com.app.androidtvapp.ui.home.ListFragment
import com.app.androidtvapp.ui.home.ListFragmentViewModel
import com.app.androidtvapp.util.Resourse

class HomeFragment : Fragment() {

    private val viewModel: ListFragmentViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    private lateinit var listFragment: ListFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        listFragment = ListFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, listFragment)
        transaction.commit()
        viewModel.movieResponse.asLiveData().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resourse.Idle -> {
                }

                is Resourse.Loading -> {

                }

                is Resourse.Success -> {
                    listFragment.displayData(resource.data, "Now Playing")
                    //                    startEntranceTransition()
                }

                is Resourse.Error -> {
                    print("data: $resource")
                }
            }
        }
        viewModel.selectedMovie.observe(viewLifecycleOwner) {
            updateView(it)
        }


    }

    private fun updateView(movieItem: Result) {
        println("selected ${movieItem}")
        binding.thumbImage.load("https://www.themoviedb.org/t/p/w780" + movieItem.backdrop_path)
        binding.title.text = movieItem.title
        binding.subTitle.text = movieItem.release_date
        binding.description.text = movieItem.overview
    }
}


