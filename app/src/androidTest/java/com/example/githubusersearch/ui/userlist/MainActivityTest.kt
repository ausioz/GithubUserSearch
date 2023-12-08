package com.example.githubusersearch.ui.userlist


import android.view.KeyEvent
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubusersearch.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun menuTest() {
        //light or dark mode
        onView(withId(R.id.menu_switch)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_switch)).perform(click())

        onView(withId(R.id.menu_switch)).perform(click())
        onView(withId(R.id.menu_switch)).check(matches(isDisplayed()))

        //to FavoriteActivity.kt
        onView(withId(R.id.favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.favorite)).perform(click())

        onView(withContentDescription((R.string.tomainactivity))).perform(click())
        onView(withId(R.id.favorite)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun searchTest() {
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()))
        onView(withId(R.id.searchBar)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(
            typeText("dicoding"), pressKey(KeyEvent.KEYCODE_ENTER)
        )
        Thread.sleep(2000)
        onView(withId(R.id.recyclerView_main)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView_main)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
        Thread.sleep(2000)
        onView(withId(R.id.recyclerView_main)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )
        Thread.sleep(2000)
        onView(withId(R.id.recyclerView_main)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )
        Thread.sleep(2000)
        pressBack()
        Thread.sleep(2000)
        onView(withId(R.id.recyclerView_main)).check(matches(isDisplayed()))
    }
}
