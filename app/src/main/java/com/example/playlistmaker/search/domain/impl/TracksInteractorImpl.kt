package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.api.repositories.TracksRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
class TracksInteractorImpl(private val repository: TracksRepository, private val executor: ExecutorService) : TracksInteractor, KoinComponent {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                consumer.consume(repository.searchTracks(expression))
            } catch(e: UnknownHostException) {
                consumer.onError(e)
            }
        }
    }
}