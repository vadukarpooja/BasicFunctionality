package com.example.apicalldemo.roomDataBase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apicalldemo.databinding.FragmentMovieDetailUpdateBinding
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.viewModel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieItemEdit : Fragment() {
    lateinit var binding: FragmentMovieDetailUpdateBinding
    val args:MovieItemEditArgs by navArgs()
    private val movieVM: MovieListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtDate.setText(args.data.released)
        binding.edtDirector.setText(args.data.director)
        binding.edtName.setText(args.data.title)
        binding.edtActors.setText(args.data.actors)
        binding.btnUpdate.setOnClickListener {
           update()
        }
    }
    private fun update(){
        val responseItem: ResponseItem = ResponseItem(id= args.data.id,released =  binding.edtDate.text.toString(), director =   binding.edtDirector.text.toString(), title =  binding.edtName.text.toString(),
            actors = binding.edtActors.text.toString(), country = "Bharat")
            movieVM.updateMovieList(responseItem)
            findNavController().navigate(MovieItemEditDirections.actionMovieItemEditToListFragment(list = ""))

    }

}