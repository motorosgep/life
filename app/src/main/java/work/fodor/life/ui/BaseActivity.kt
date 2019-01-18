package work.fodor.life.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import work.fodor.life.R

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    // To implement.

    @get:LayoutRes
    abstract val layoutResId: Int

    abstract fun initViews()
    abstract fun teardown()
    abstract fun injectDagger()


    // Lifecycle.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        injectDagger()

        setContentView(layoutResId)

        initViews()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        teardown()
    }

    // BaseContract.View shared implementation.

    override fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        Toast.makeText(
            this,
            throwable.localizedMessage ?: throwable.message ?: getString(R.string.unknown_error),
            Toast.LENGTH_LONG
        ).show()
    }

}
