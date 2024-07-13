package com.naruto.narutodb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naruto.narutodb.ui.characterdetail.CharacterDetailScreen
import com.naruto.narutodb.ui.characterlist.CharacterListScreen
import com.naruto.narutodb.ui.theme.NarutoDBTheme
import com.naruto.narutodb.ui.utils.UiNavigationConstants
import com.naruto.narutodb.util.Logger

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        Logger.debug("fatal","on create called")
        setContent {
            Logger.debug("fatal","set content called again")

            NarutoDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    CharacterNavigation()
                }
            }
        }
    }
}

@Composable
fun CharacterNavigation()
{
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UiNavigationConstants.CHARACTER_LIST_SCREEN,
        builder =
        {
            composable(route = UiNavigationConstants.CHARACTER_LIST_SCREEN)
            {
                CharacterListScreen()
                { characterId ->
                    navController.navigate("${UiNavigationConstants.CHARACTER_DETAIL_SCREEN}/$characterId")
                }
            }
            composable(
                route = "${UiNavigationConstants.CHARACTER_DETAIL_SCREEN}/{character_key}",
                arguments = listOf(
                    navArgument("character_key") {
                        type = NavType.IntType
                    }
                )
            )
            {
                CharacterDetailScreen()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview()
{
    NarutoDBTheme {
        CharacterNavigation()
    }
}