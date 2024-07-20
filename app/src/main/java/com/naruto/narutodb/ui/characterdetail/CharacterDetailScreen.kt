package com.naruto.narutodb.ui.characterdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.naruto.core.data.Character
import com.naruto.core.data.InfoSection
import com.naruto.core.data.Result
import com.naruto.narutodb.util.Logger

@Composable
fun CharacterDetailScreen() {
    val characterDetailViewModel: CharacterDetailViewModel = viewModel()

    when (val state = characterDetailViewModel.characterDetail.value)
    {
        is Result.Error ->
        {

        }
        Result.Loading ->
        {

        }
        is Result.Success ->
        {
            val character = state.data
            Scaffold()
            { padding ->
                Card(
                    modifier = Modifier.padding(paddingValues = padding)
                )
                {
                    DisplayCharacterDetails(character = character)
                }
            }
        }
    }
}

@Composable
fun DisplayCharacterDetails(character: Character)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        var imageIndex by remember { mutableIntStateOf(0) }
        val startingIndex = 0
        val endingIndex = (character.images?.size ?: 0) - 1

        val imageUrl: Any? =
            if (!character.images.isNullOrEmpty()
                && (character.images?.size ?: 0) > imageIndex)
                character.images?.get(imageIndex)
            else
                "https://upload.wikimedia.org/wikipedia/commons/2/24/No_image_3x4_50_trans_borderless.svg"

        Box(modifier = Modifier.height(250.dp))
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Profile picture",
//                placeholder = painterResource(id = R.drawable.image_loading_placeholder),
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                if (startingIndex < imageIndex)
                {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                Logger.debug("fatal", "left button clicked")
                                imageIndex--
                            },
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "previous image button"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (endingIndex > imageIndex)
                {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                Logger.debug("fatal", "right button clicked")
                                imageIndex++
                            },
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "next image button"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        character.name?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        character.infoSections?.forEach { item ->
            ExpandableInfoSection(infoSection = item)
        }
    }
}

@Composable
fun ExpandableInfoSection(
    infoSection: InfoSection,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 36.dp)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = infoSection.title, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.defaultMinSize(minHeight = 12.dp))

        AnimatedVisibility(visible = expanded) {
            Text(text = infoSection.content)
        }
    }
}