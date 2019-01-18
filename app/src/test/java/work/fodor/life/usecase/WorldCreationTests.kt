package work.fodor.life.usecase

import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import work.fodor.life.domain.usecase.CreateWorldFromPreset
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when`
import work.fodor.life.domain.model.World
import work.fodor.life.domain.repository.WorldRepository
import work.fodor.life.domain.usecase.CreateRandomWorld
import work.fodor.life.domain.usecase.GetWorldPresets


@RunWith(MockitoJUnitRunner::class)
class WorldCreationTests {

    private val presetsResponse = Single.just(arrayOf("blinker", "toad"))
    private val worldResponse = Single.just(World(3, 3))

    private lateinit var worldPresetsUseCase: GetWorldPresets
    private lateinit var randomWorldUseCase: CreateRandomWorld
    private lateinit var presetWorldUseCase: CreateWorldFromPreset

    @Mock
    lateinit var repository: WorldRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        worldPresetsUseCase = Mockito.spy(GetWorldPresets(repository))
        randomWorldUseCase = Mockito.spy(CreateRandomWorld())
        presetWorldUseCase = Mockito.spy(CreateWorldFromPreset(repository))

    }


    @Test
    fun testGetPresetsFunction() {
        `when`(repository.getPresets()).thenReturn(presetsResponse)

        assert(worldPresetsUseCase.execute() == presetsResponse)
    }

    @Test
    fun testGetRandomFunction() {
        randomWorldUseCase.execute()
            .test()
            .assertValue{ worldResult ->

                // Test if the World has any living cells (every cell is dead by default)
                var indicator = false
                worldResult.cells.forEach { cellArray -> cellArray.forEach { cell -> indicator = indicator || cell} }
                indicator

            }
    }

    @Test
    fun testGetPresetFunction() {
        `when`(repository.getPreset("blinker")).thenReturn(worldResponse)

        assert(presetWorldUseCase.execute("blinker") == worldResponse)
    }
}
