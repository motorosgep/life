package com.canon.eoscompanion.ui.onboarding

import io.reactivex.disposables.CompositeDisposable
import work.fodor.life.domain.model.World
import work.fodor.life.domain.usecase.*
import work.fodor.life.ui.GameContract
import work.fodor.life.util.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Listens to user actions from the UI ({@link GameActivity}), triggers the necessary use-cases and updates the
 * UI as required.
 */
open class GamePresenter @Inject internal constructor(
    private val view: GameContract.View,
    private val randomWorld: CreateRandomWorld,
    private val getWorldPresets: GetWorldPresets,
    private val presetWorld: CreateWorldFromPreset,
    private val evolutionStep: GetWorldEvolutionStep,
    private val evolutionTimeline: GetWorldEvolutionInfiniteSteps,
    private val schedulerProvider: BaseSchedulerProvider
) : GameContract.Presenter {

    private var lastWorldState: World? = null
    private var showGrid = true
    private var isPlaying = false
    private var playbackSpeed = GameContract.PlaybackSpeed.MEDIUM
    private val subscriptions = CompositeDisposable()

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun start() {
        if (lastWorldState != null)
            view.onBoardReady(lastWorldState!!)
        else
            requestRandomWorld()

        view.onSettingsReady(showGrid, playbackSpeed)
    }

    override fun requestToggleGrid() {
        showGrid = !showGrid
        view.onSettingsReady(showGrid, playbackSpeed)
    }

    override fun requestAnimationSpeedChange(speed: GameContract.PlaybackSpeed) {
        playbackSpeed = speed
        view.onSettingsReady(showGrid, playbackSpeed)

        if (isPlaying)
            requestPlay()
    }

    override fun requestPresetList() {
        subscriptions.add(
            getWorldPresets
                .execute()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(view::onPresetsReady, view::onError)
        )
    }

    override fun requestPresetWorld(preset: String) {
        requestPause()

        subscriptions.add(
            presetWorld
                .execute(preset)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ update: World -> this.updateBoard(update) }, view::onError)
        )
    }

    override fun requestRandomWorld() {
        requestPause()

        subscriptions.add(
            randomWorld
                .execute()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ update: World -> this.updateBoard(update) }, view::onError)
        )
    }

    override fun requestPlay() {
        lastWorldState?.let { board ->
            requestPause()

            //subscribe to new reactive stream
            subscriptions.add(
                evolutionTimeline
                    .execute(board, getDelayForPlaybackSpeed())
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(
                        { update ->
                            updateBoard(update)
                        },
                        view::onError
                    )
            )
            isPlaying = true
        }

    }

    override fun requestStep() {
        lastWorldState?.let { board ->
            requestPause()

            subscriptions.add(
                evolutionStep
                    .execute(board)
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(
                        { update ->
                            updateBoard(update)
                        },
                        view::onError
                    )
            )
        }

    }

    override fun requestPause() {
        // unsubscribe from previous Reactive streams
        unsubscribe()
        isPlaying = false
    }

    // Internal.

    private fun updateBoard(update: World) {
        this.lastWorldState = update
        view.onBoardReady(update)
    }

    private fun getDelayForPlaybackSpeed(): Long {
        return when (playbackSpeed) {
            GameContract.PlaybackSpeed.FAST -> 50
            GameContract.PlaybackSpeed.MEDIUM -> 200
            GameContract.PlaybackSpeed.SLOW -> 1000
        }
    }


}
