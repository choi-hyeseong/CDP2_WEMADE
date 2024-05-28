package com.home

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Toast 발생했는지 확인하기 위한 Matcher
 */
class ToastMatcher : TypeSafeMatcher<Root?>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }

    override fun matchesSafely(root: Root?): Boolean {
        val type = root?.windowLayoutParams?.get()?.type
        if (type == WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                // means this window isn't contained by any other windows.
                return true
            }
        }
        return false
    }
}