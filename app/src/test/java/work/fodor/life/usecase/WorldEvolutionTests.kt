package work.fodor.life.usecase

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import work.fodor.life.domain.model.World
import work.fodor.life.domain.usecase.GetWorldEvolutionInfiniteSteps
import work.fodor.life.domain.usecase.GetWorldEvolutionStep


@RunWith(MockitoJUnitRunner::class)
class WorldEvolutionTests {

    lateinit var evolutionStepUseCase: GetWorldEvolutionStep
    lateinit var evolutionTimelineUseCase: GetWorldEvolutionInfiniteSteps

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        evolutionStepUseCase = Mockito.spy(GetWorldEvolutionInfiniteSteps())
        evolutionTimelineUseCase = Mockito.spy(GetWorldEvolutionInfiniteSteps())
    }


    @Test
    fun testExecuteNextStepFunction_onBlinker() {
        val worldInput = World(3, 3).apply {
            cells[0][1] = true
            cells[1][1] = true
            cells[2][1] = true
        }
        val worldIteration = World(3, 3).apply {
            cells[1][0] = true
            cells[1][1] = true
            cells[1][2] = true
        }

        evolutionStepUseCase.execute(worldInput).test()
            .assertValueAt(0) { worldResult ->
                worldIteration.toString() == worldResult.toString()
            }
    }

    @Test
    fun testExecuteInfiniteStepsFunction_onBlinker() {
        val worldInput = World(3, 3).apply {
            cells[0][1] = true
            cells[1][1] = true
            cells[2][1] = true
        }

        val worldIteration = World(3, 3).apply {
            cells[1][0] = true
            cells[1][1] = true
            cells[1][2] = true
        }

        evolutionTimelineUseCase.execute(worldInput, 0)
            .elementAt(0)
            .subscribe({ worldResult -> assert(worldIteration.toString() == worldResult.toString()) },
                { assert(false) }
            )

        evolutionTimelineUseCase.execute(worldInput, 0)
            .elementAt(1)
            .subscribe({ worldResult -> assert(worldInput.toString() == worldResult.toString()) },
                { assert(false) }
            )
    }

}
