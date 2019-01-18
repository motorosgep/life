package work.fodor.life.ui

import android.support.v7.app.AlertDialog
import android.widget.SeekBar
import android.widget.Toast
import com.canon.eoscompanion.ui.onboarding.GamePresenter
import kotlinx.android.synthetic.main.activity_game.*
import work.fodor.life.R
import work.fodor.life.dagger.DaggerGameComponent
import work.fodor.life.dagger.DomainModule
import work.fodor.life.dagger.GameModule
import work.fodor.life.domain.model.World
import javax.inject.Inject


class GameActivity : BaseActivity(), GameContract.View {

    @Inject
    lateinit var presenter: GamePresenter


    // BaseActivity implementation.

    override val layoutResId = R.layout.activity_game

    override fun initViews() {
        setUpOnClickListeners()
        setUpSeekBarChangeListener()

        presenter.start()
    }

    override fun teardown() {
        presenter.unsubscribe()
    }

    override fun injectDagger() {
        DaggerGameComponent.builder()
            .gameModule(GameModule(this))
            .domainModule(DomainModule())
            .build()
            .inject(this)
    }


    // GameContract.View implementation.

    override fun onBoardReady(world: World) {
        world_pgv?.setGridData(world)
        age_tv?.text = resources.getString(R.string.generation, world.age)
    }

    override fun onSettingsReady(showGrid: Boolean, playbackSpeed: GameContract.PlaybackSpeed) {
        toggle_grid_btn?.setImageResource(if (showGrid) R.drawable.ic_round_grid_off else R.drawable.ic_round_grid_on)
        world_pgv.setShowGrid(showGrid)
        playback_speed_sb?.apply {
            progress = when (playbackSpeed) {
                GameContract.PlaybackSpeed.FAST -> 2
                GameContract.PlaybackSpeed.MEDIUM -> 1
                GameContract.PlaybackSpeed.SLOW -> 0
            }
        }
    }

    override fun onPresetsReady(presets: Array<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setItems(presets) { dialog, which ->
            presenter.requestPresetWorld(presets[which])
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // BaseContract.View implementation.

    override fun onError(throwable: Throwable) {
        val message = throwable.localizedMessage ?: throwable.message ?: getString(R.string.unknown_error)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Internal.

    private fun setUpOnClickListeners() {
        random_btn?.setOnClickListener {
            presenter.requestRandomWorld()
        }

        preset_btn?.setOnClickListener {
            presenter.requestPresetList()
        }

        start_playback_btn?.setOnClickListener {
            presenter.requestPlay()
        }

        pause_playback_btn?.setOnClickListener {
            presenter.requestPause()
        }

        step_btn?.setOnClickListener {
            presenter.requestStep()
        }

        toggle_grid_btn?.setOnClickListener {
            presenter.requestToggleGrid()
        }
    }

    private fun setUpSeekBarChangeListener() {
        playback_speed_sb?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekbar: SeekBar?) {}

            override fun onStopTrackingTouch(seekbar: SeekBar?) {}

            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    presenter.requestAnimationSpeedChange(
                        when (progress) {
                            2 -> GameContract.PlaybackSpeed.FAST
                            1 -> GameContract.PlaybackSpeed.MEDIUM
                            0 -> GameContract.PlaybackSpeed.SLOW
                            else -> GameContract.PlaybackSpeed.SLOW
                        }
                    )
                }
            }
        })
    }
}