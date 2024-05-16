package org.singing.app.di

import androidx.compose.runtime.Composable
import org.koin.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Composable
inline fun <reified T> injectComponent(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return getKoin().get(qualifier, parameters)
}
