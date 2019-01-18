package work.fodor.life

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import work.fodor.life.data.WorldRepositoryImpl
import work.fodor.life.domain.model.World
import work.fodor.life.domain.repository.WorldRepository


/**
 * Unit tests for the implementation of {@link WorldRepository}
 */
@RunWith(MockitoJUnitRunner::class)
class WorldRepositoryTest {

    lateinit var repository: WorldRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)


        // Get a reference to the class under test, with immediate scheduler
        repository = Mockito.spy(
            WorldRepositoryImpl()
        )
    }

    @Test
    fun testGetPresetsFunction() {
        repository.getPresets().test().assertValue {
            !it.isEmpty()
        }
    }

    @Test
    fun testGetPresetFunction() {
        val worldExpected = World(5, 5).apply {
            cells[1][2] = true
            cells[2][2] = true
            cells[3][2] = true
        }

        repository.getPreset("Blinker").test().assertValue {
            it.toString() == worldExpected.toString()
        }
    }

}
