package com.pdm.to_do_compose

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

//open class BaseActivityTest<reified T : ComponentActivity> {
//    @get:Rule
//    val composableRuleTest = createAndroidComposeRule<T>()
//
//    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
//}

class BaseActivityTest<T : Any> @PublishedApi internal constructor(
    private val classT: KClass<T>
) {

//    @get:Rule
//    val composableRuleTest = createAndroidComposeRule<T>()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    companion object {
        @JvmStatic
        @JvmName(name = "create")
        inline operator fun <reified T : Any> invoke() =
            BaseActivityTest(T::class)
    }

    fun <U> safeCast(value: U) =
        classT.safeCast(value)

    inline fun <U : R, R> isInstanceOrElse(value: U, otherwise: () -> R) =
        safeCast(value) ?: otherwise()
}