package work.fodor.life.domain.usecase

import work.fodor.life.domain.model.World
import io.reactivex.Single
import javax.inject.Inject


open class GetWorldEvolutionStep @Inject constructor() {

    open fun execute(initialState: World): Single<World> {
        return Single.just(calculateNextIteration(initialState))
    }


    // Internal.

    /**
     *
     * Calculates the next state for every cell in the grid and increments the world age counter.
     *
     * @param currentState The grid data.
     *
     * @return
     */

    protected fun calculateNextIteration(currentState: World): World {
        val nextState = World(currentState.numberOfColumns, currentState.numberOfRows)

        for (x in 0 until currentState.numberOfColumns) {
            for (y in 0 until currentState.numberOfRows) {
                nextState.cells[x][y] = isCellAliveInNextIteration(x, y, currentState)
            }
        }

        nextState.age = currentState.age + 1
        return nextState
    }

    /**
     *
     * Checks if the cell is alive with the game rules are applied.
     *
     * @param x The x position
     *
     * @param y The y position
     *
     * @param world The grid data.
     *
     * @return
     */

    private fun isCellAliveInNextIteration(x: Int, y: Int, world: World): Boolean {
        var surroundingPopulation = 0

        //count living neighbours
        for (i in Math.max(0, x - 1)..Math.min(x + 1, world.numberOfColumns - 1)) {
            for (j in Math.max(0, y - 1)..Math.min(y + 1, world.numberOfRows - 1)) {
                if (i == x && j == y) //exclude the current cell
                    continue

                if (world.cells[i][j]) {
                    surroundingPopulation++
                }
            }
        }

        //apply life rules
        return if (!world.cells[x][y]) {
            //cell will reborn if it has exactly 3 living neighbours
            surroundingPopulation == 3
        } else {
            //cell will dies if it has less than 2 (under population) or more than 3 (over population) living neighbours
            !(surroundingPopulation < 2 || surroundingPopulation > 3)
        }
    }
}