package com.naruto.narutodb

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naruto.narutodb.ui.character.CharacterDetailScreen
import com.naruto.narutodb.ui.character.CharacterListScreen
import com.naruto.narutodb.ui.character.CharacterViewModel
import com.naruto.narutodb.ui.theme.NarutoDBTheme
import com.naruto.narutodb.ui.utils.UiNavigationConstants
import com.naruto.narutodb.util.Logger

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.debug("fatal", "on create called")
        setContent {
            Logger.debug("fatal", "set content called again")

            val context = LocalContext.current
            val activity = context.findActivity()
            val sizeClass = activity?.let { calculateWindowSizeClass(activity = it) }
//
            Logger.debug(tag = "fatal", "size class value == $sizeClass")
//
            val isPhone = sizeClass?.widthSizeClass == WindowWidthSizeClass.Compact

            NarutoDBTheme {

                val viewModel: CharacterViewModel = viewModel()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    if (isPhone) {
                        PhoneNavigation(isPhone = isPhone, viewModel = viewModel)
                    } else {
                        TabletNavigation(isPhone = isPhone, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TabletNavigation(isPhone: Boolean, viewModel: CharacterViewModel) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        CharacterListScreen(isPhone = isPhone, viewModel = viewModel) {
            //we do nothing here
        }

        Spacer(modifier = Modifier
            .fillMaxHeight()
            .width(width = 2.dp))

        VerticalDivider()

        Spacer(modifier = Modifier
            .fillMaxHeight()
            .width(width = 2.dp))

        CharacterDetailScreen(viewModel = viewModel)
    }
}

@Composable
fun PhoneNavigation(isPhone: Boolean, viewModel: CharacterViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UiNavigationConstants.CHARACTER_LIST_SCREEN,
        builder =
            {
                composable(route = UiNavigationConstants.CHARACTER_LIST_SCREEN)
                {
                    CharacterListScreen(
                        isPhone = isPhone,
                        viewModel = viewModel,
                        characterSelected = {
                            navController.navigate(route = UiNavigationConstants.CHARACTER_DETAIL_SCREEN)
                        },
                    )
                }
                composable(route = UiNavigationConstants.CHARACTER_DETAIL_SCREEN)
                {
                    CharacterDetailScreen(viewModel = viewModel)
                }
            })
}

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NarutoDBTheme {
        PhoneNavigation(isPhone = true, viewModel = viewModel())
    }
}