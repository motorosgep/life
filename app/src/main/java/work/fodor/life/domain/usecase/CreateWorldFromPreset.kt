package work.fodor.life.domain.usecase

import work.fodor.life.domain.model.World

import io.reactivex.Single
import work.fodor.life.domain.repository.WorldRepository
import javax.inject.Inject


open class CreateWorldFromPreset @Inject constructor(
    private val worldRepository: WorldRepository
) {

    open fun execute(preset: String): Single<World> {
        return worldRepository.getPreset(preset)
    }
}