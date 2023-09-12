package com.example.apicalldemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.apicalldemo.adapter.ViewPagerAdapter
import com.example.apicalldemo.chat.MovieListAdapter
import com.example.apicalldemo.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : Fragment() {
    lateinit var binding: FragmentMovieListBinding
    val args: MovieListFragmentArgs by navArgs()
    lateinit var adapter: MovieListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MovieListAdapter(requireContext(), arrayListOf())
        /*binding.rvMovieList.adapter = ViewPagerAdapter(requireContext(),args.data.images)*/
        binding.title.text = args.data.title
        /*SomeTask(binding.img,requireContext()).execute(args.data.poster)*/
        Glide.with(requireContext())
            .load(args.data.poster)
            .into(binding.img)
       /* CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            Glide.with(requireContext())
                .load(args.data.poster)
                .into(binding.img)
        }*/
        binding.txtActors.text = "Actors:" + args.data.actors
        binding.txtDirector.text = "Director:" + args.data.director
        binding.txtReleased.text = "Released:" + args.data.released
        binding.txtCountry.text = "Country:" + args.data.country
    }


    @SuppressLint("StaticFieldLeak")
     class SomeTask(private val imageView: ImageView, var context: Context) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg strings: String): String {
            return strings[0]
        }
        override fun onPostExecute(s: String) {
            Glide.with(context)
                .load(s)
                .into(imageView)
        }
    }
}