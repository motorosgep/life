package work.fodor.life

import com.canon.eoscompanion.ui.onboarding.GamePresenter
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import work.fodor.life.ui.GameContract
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import work.fodor.life.domain.model.World
import work.fodor.life.util.ImmediateSchedulerProvider
import org.mockito.Mockito.`when`
import work.fodor.life.domain.usecase.*
import java.util.concurrent.TimeUnit


/**
 * Unit tests for the implementation of {@link TasksPresenter}
 */
@RunWith(MockitoJUnitRunner::class)
class GamePresenterTest {

    private val worldParam = World(3, 3)
    private val presetsParam = arrayOf("blinker", "toad")


    lateinit var presenter: GameContract.Presenter

    @Mock
    lateinit var viewContract: GameContract.View

    @Mock
    lateinit var worldPresetsUseCase: GetWorldPresets

    @Mock
    lateinit var randomWorldUseCase: CreateRandomWorld

    @Mock
    lateinit var presetWorldUseCase: CreateWorldFromPreset

    @Mock
    lateinit var evolutionStepUseCase: GetWorldEvolutionStep

    @Mock
    lateinit var evolutionTimelineUseCase: GetWorldEvolutionInfiniteSteps

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)


        // Get a reference to the class under test, with immediate scheduler
        presenter = Mockito.spy(
            GamePresenter(
                viewContract,
                randomWorldUseCase,
                worldPresetsUseCase,
                presetWorldUseCase,
                evolutionStepUseCase,
                evolutionTimelineUseCase,
                ImmediateSchedulerProvider()))
    }


    @Test
    fun testStartFunction() {
        `when`(randomWorldUseCase.execute()).thenReturn(Single.just(worldParam))

        presenter.start()
        verify(viewContract).onBoardReady(worldParam)
        verify(viewContract).onSettingsReady(true, GameContract.PlaybackSpeed.MEDIUM)
    }


    @Test
    fun testToggleGridFunction() {
        presenter.requestToggleGrid()
        verify(viewContract, times(1)).onSettingsReady(false, GameContract.PlaybackSpeed.MEDIUM)

        presenter.requestToggleGrid()
        verify(viewContract, times(1)).onSettingsReady(true, GameContract.PlaybackSpeed.MEDIUM)
    }

    @Test
    fun testChangeGameSpeedFunction() {
        presenter.requestAnimationSpeedChange(GameContract.PlaybackSpeed.SLOW)
        verify(viewContract, times(1)).onSettingsReady(true, GameContract.PlaybackSpeed.SLOW)

        presenter.requestAnimationSpeedChange(GameContract.PlaybackSpeed.MEDIUM)
        verify(viewContract, times(1)).onSettingsReady(true, GameContract.PlaybackSpeed.MEDIUM)

        presenter.requestAnimationSpeedChange(GameContract.PlaybackSpeed.FAST)
        verify(viewContract, times(1)).onSettingsReady(true, GameContract.PlaybackSpeed.FAST)
    }

    @Test
    fun testStepFunction() {
        `when`(randomWorldUseCase.execute()).thenReturn(Single.just(worldParam))
        `when`(evolutionStepUseCase.execute(worldParam)).thenReturn(Single.just(worldParam))

        //The presenter won't advance the board unless there is a lastWorldState
        presenter.requestRandomWorld()
        presenter.requestStep()
        verify(viewContract, times(2)).onBoardReady(worldParam)
    }

    @Test
    fun testPlayFunction() {
        `when`(randomWorldUseCase.execute()).thenReturn(Single.just(worldParam))
        `when`(evolutionTimelineUseCase.execute(worldParam, 200))
            .thenReturn(Flowable.just(worldParam))

        //The presenter won't advance the board unless there is a lastWorldState
        presenter.requestRandomWorld()
        presenter.requestPlay()
        verify(viewContract, after(1000).atLeast(2)).onBoardReady(worldParam)
    }

    @Test
    fun testPauseFunction() {
        `when`(randomWorldUseCase.execute()).thenReturn(Single.just(worldParam))
        `when`(evolutionTimelineUseCase.execute(worldParam, 200))
            .thenReturn(Flowable.just(worldParam))

        //The presenter won't advance the board unless there is a lastWorldState
        presenter.requestRandomWorld()
        presenter.requestPlay()
        presenter.requestPause()
        verify(viewContract, after(1000).times(2)).onBoardReady(worldParam)
    }

    @Test
    fun testGetPresetsFunction() {
        `when`(worldPresetsUseCase.execute()).thenReturn(Single.just(presetsParam))

        presenter.requestPresetList()
        verify(viewContract, times(1)).onPresetsReady(presetsParam)
    }

}
