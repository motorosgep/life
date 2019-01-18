package work.fodor.life.ui


interface BaseContract {

    interface View {

        fun onError(throwable: Throwable)
    }

    interface Presenter {

        fun unsubscribe()
    }
}