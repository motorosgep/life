package work.fodor.life.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


/**
 * Provides different types of schedulers.
 */
class SchedulerProvider
private constructor() : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        private var internal: SchedulerProvider? = null

        val instance: SchedulerProvider
            @Synchronized get() {
                if (internal == null) {
                    internal = SchedulerProvider()
                }
                return internal as SchedulerProvider
            }
    }
}