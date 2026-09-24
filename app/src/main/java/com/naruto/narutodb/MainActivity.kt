package com.naruto.narutodb

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naruto.narutodb.ui.character.CharacterDetailScreen
import com.naruto.narutodb.ui.character.CharacterListScreen
import com.naruto.narutodb.ui.character.CharacterViewModel
import com.naruto.narutodb.ui.theme.NarutoDBTheme
import com.naruto.narutodb.ui.utils.UiNavigationConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = LocalContext.current
            val activity = context.findActivity()
            val sizeClass = activity?.let { calculateWindowSizeClass(activity = it) }
            val isPhone = sizeClass?.widthSizeClass == WindowWidthSizeClass.Compact
            val characterListScreenWeight = if (isPhone) 1f else 0.30f

            NarutoDBTheme {

                val viewModel: CharacterViewModel by viewModels()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    if (isPhone) {
                        PhoneNavigation(
                            viewModel = viewModel,
                            characterListScreenWeight = characterListScreenWeight
                        )
                    } else {
                        TabletNavigation(
                            viewModel = viewModel,
                            characterListScreenWeight = characterListScreenWeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabletNavigation(viewModel: CharacterViewModel, characterListScreenWeight: Float) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        CharacterListScreen(
            characterListScreenWeight = characterListScreenWeight,
            viewModel = viewModel
        ) {
            //we do nothing here
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = 2.dp)
        )

        VerticalDivider()

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = 2.dp)
        )

        CharacterDetailScreen(viewModel = viewModel)
    }
}

@Composable
fun PhoneNavigation(viewModel: CharacterViewModel, characterListScreenWeight: Float) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UiNavigationConstants.CHARACTER_LIST_SCREEN,
        builder =
            {
                composable(route = UiNavigationConstants.CHARACTER_LIST_SCREEN)
                {
                    CharacterListScreen(
                        characterListScreenWeight = characterListScreenWeight,
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
        PhoneNavigation(viewModel = viewModel(), characterListScreenWeight = 1.0f)
    }
}