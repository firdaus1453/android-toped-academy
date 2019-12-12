package com.example.moviedetail.ui

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moviedetail.R
import com.example.moviedetail.domain.MovieDetailUseCase
import com.example.moviedetail.factory.MovieDetailFactory
import tokopedia.app.abstraction.base.BaseActivity
import tokopedia.app.data.repository.moviedetail.MovieDetailRepository
import tokopedia.app.data.repository.moviedetail.MovieDetailRepositoryImpl
import tokopedia.app.data.routes.NetworkServices
import tokopedia.app.network.Network

class MovieDetailActivity : BaseActivity() {
    override fun contentView(): Int = R.layout.activity_movie_detail

    private lateinit var repository: MovieDetailRepository
    private lateinit var useCase: MovieDetailUseCase
    private lateinit var viewModel: MovieDetailViewModel


    override fun initView() {
        // get movie id
        intent?.data?.lastPathSegment?.let {
            viewModel.setMovieId(it)
        }

        viewModel.error.observe(this, onShowError())

        viewModel.movie.observe(this, Observer {
            showToast(it.title)
        })
    }

    private fun showToast(title: String) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }

    private fun onShowError(): Observer<String> {
        return Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun initObservable() {
        val networkBuilder = Network.builder().create(NetworkServices::class.java)

        // unit repository
        repository = MovieDetailRepositoryImpl(networkBuilder)

        // init usecase
        useCase = MovieDetailUseCase(repository)

        // ini viewmodel
        viewModel = ViewModelProviders.of(this, MovieDetailFactory(useCase))
            .get(MovieDetailViewModel::class.java)
    }
}