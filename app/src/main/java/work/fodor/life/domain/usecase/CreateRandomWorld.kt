package work.fodor.life.domain.usecase

import work.fodor.life.domain.model.World

import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random


open class CreateRandomWorld @Inject constructor() {

    open fun execute(): Single<World> {
        // create an empty grid
        val numberOfColumns = 100
        val numberOfRows = 100
        val world = World(100, 100)

        // decide on how many living cells we want start with
        val lifeProbability = 0.5f
        val seedCount = Random.nextInt((numberOfColumns * numberOfRows * lifeProbability).toInt())

        // place the living cells randomly
        for (i in 0..seedCount) {
            world.cells[Random.nextInt(numberOfColumns)][Random.nextInt(numberOfRows)] = true
        }

        return Single.just(world)
    }

}