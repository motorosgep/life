package work.fodor.life.ui

import work.fodor.life.domain.model.World

/**
 * This specifies the contract between the view and the presenter.
 */
interface GameContract {

    enum class PlaybackSpeed {
        SLOW, MEDIUM, FAST
    }

    interface View : BaseContract.View {

        fun onBoardReady(board: World)

        fun onPresetsReady(presets: Array<String>)

        fun onSettingsReady(showGrid: Boolean, playbackSpeed: PlaybackSpeed)
    }

    interface Presenter : BaseContract.Presenter {

        fun start()

        fun requestToggleGrid()

        fun requestAnimationSpeedChange(speed: PlaybackSpeed)

        fun requestPresetWorld(preset: String)

        fun requestRandomWorld()

        fun requestPresetList()

        fun requestPlay()

        fun requestPause()

        fun requestStep()

    }
}