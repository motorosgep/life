package work.fodor.life.domain.usecase

import io.reactivex.Single
import work.fodor.life.domain.repository.WorldRepository
import javax.inject.Inject


open class GetWorldPresets @Inject constructor(
    private val worldRepository: WorldRepository
) {

    open fun execute(): Single<Array<String>> {
        return worldRepository.getPresets()
    }
}