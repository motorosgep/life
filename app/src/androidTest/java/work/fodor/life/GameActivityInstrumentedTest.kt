package work.fodor.life

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.test.rule.ActivityTestRule
import android.view.View


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import work.fodor.life.ui.GameActivity
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import work.fodor.life.ui.widget.PixelGridView


@RunWith(AndroidJUnit4::class)
class GameActivityInstrumentedTest {

    @get:Rule
    var rule: ActivityTestRule<GameActivity> = ActivityTestRule(GameActivity::class.java)

    @Test
    fun ensurePlayButtonIsPresent() {
        assertImageButton(R.id.start_playback_btn)
    }

    @Test
    fun ensurePauseButtonIsPresent() {
        assertImageButton(R.id.pause_playback_btn)
    }

    @Test
    fun ensureStepButtonIsPresent() {
        assertImageButton(R.id.step_btn)
    }

    @Test
    fun ensureToggleGridButtonIsPresent() {
        assertImageButton(R.id.toggle_grid_btn)
    }

    @Test
    fun ensureRandomButtonIsPresent() {
        assertImageButton(R.id.random_btn)
    }

    @Test
    fun ensurePresetButtonIsPresent() {
        assertImageButton(R.id.preset_btn)
    }

    @Test
    fun ensureSpeedSeekBarIsPresent() {
        val activity = rule.activity
        val viewById = activity.findViewById(R.id.playback_speed_sb) as View
        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(SeekBar::class.java))
    }

    @Test
    fun ensureWorldPixelGridViewIsPresent() {
        val activity = rule.activity
        val viewById = activity.findViewById(R.id.world_pgv) as View
        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(PixelGridView::class.java))
    }

    @Test
    fun validateStepClick() {
        val activity = rule.activity
        val ageTextView = activity.findViewById(R.id.age_tv) as TextView

        assert(ageTextView.text.contains("0"))
        (activity.findViewById(R.id.step_btn) as ImageButton).callOnClick()
        assert(ageTextView.text.contains("1"))
    }

    @Test
    fun validatePresetsClick() {
        val activity = rule.activity

        (activity.findViewById(R.id.preset_btn) as ImageButton).callOnClick()

        onView(withText("Blinker"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    private fun assertImageButton(@IdRes resourceId: Int) {
        val activity = rule.activity
        val viewById = activity.findViewById(resourceId) as View
        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(ImageButton::class.java))
    }
}
