package work.fodor.life.domain.usecase

import work.fodor.life.domain.model.World
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import java.lang.Thread.sleep
import java.util.concurrent.Callable
import javax.inject.Inject


open class GetWorldEvolutionInfiniteSteps @Inject constructor() : GetWorldEvolutionStep() {

    open fun execute(initialState: World, delayBetweenEvents: Long): Flowable<World> {
        return Flowable.generate<World, World>(
            Callable<World> { initialState },
            BiFunction<World, Emitter<World>, World> { state, emitter ->
                val nextState = calculateNextIteration(state)
                // emit the new state via the Flowable
                emitter.onNext(nextState)
                // wait a bit
                sleep(delayBetweenEvents)
                // return the state to used in the next cycle
                return@BiFunction nextState
            })
    }
}