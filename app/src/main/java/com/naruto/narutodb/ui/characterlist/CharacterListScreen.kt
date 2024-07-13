package com.naruto.narutodb.ui.characterlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.naruto.narutodb.ui.utils.shimmereffect.ShowShimmerAnimation
import com.naruto.core.data.Character
import com.naruto.narutodb.R
import com.naruto.narutodb.model.common.Result
import com.naruto.narutodb.util.Logger

@Composable
fun CharacterListScreen(characterSelected: (characterId: Int) -> Unit)
{

    val characterViewModel : CharacterListViewModel = viewModel()

    when (val currentState = characterViewModel.characters.value)
    {
        is Result.Error ->
        {

        }
        Result.Loading ->
        {
            Logger.debug("fatal", "loading called")
            ShowShimmerAnimation(repeatTimes = 10, size = 100.dp)
        }
        is Result.Success ->
        {
            Logger.debug("fatal", "state value called == ${currentState.data?.size}")
            DisplayCharacterList(
                characterList = currentState.data ?: arrayListOf(),
                onCharacterSearched =
                { characterId ->
                    characterViewModel.filterCharacterById(characterId)
                },
                onSearchCleared =
                {
                    characterViewModel.repostValue()
                },
                onCharacterSelected =
                { character ->
                    characterSelected(character.id ?: 0)
                })
        }
    }
}

@Composable
fun DisplayCharacterList(
    characterList: List<Character>,
    onCharacterSearched: (id: Int?) -> Unit,
    onSearchCleared: () -> Unit,
    onCharacterSelected: (character: Character) -> Unit)
{

    Scaffold(topBar = {
        AutoComplete(
            characterList = characterList,
            onCharacterSearched = onCharacterSearched,
            onSearchCleared = onSearchCleared
        )
    })
    { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues = padding)
        ) {
            items(characterList.size) { itemCount ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick =
                    {
                        onCharacterSelected(characterList[itemCount])
                    }
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val imageUrl: Any? =
                            if (!characterList.elementAt(itemCount).images.isNullOrEmpty())
                                characterList.elementAt(itemCount).images?.get(0)
                            else
                                "https://upload.wikimedia.org/wikipedia/commons/2/24/No_image_3x4_50_trans_borderless.svg"

                        if (null != imageUrl)
                        {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                modifier = Modifier.height(150.dp),
                                contentDescription = "Profile picture",
                                placeholder = painterResource(id = R.drawable.image_loading_placeholder),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        Text(
                            text = "  ${characterList.elementAt(itemCount).name}",
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(
    characterList: List<Character>,
    onCharacterSearched: (id: Int?) -> Unit,
    onSearchCleared: () -> Unit
) {

    var searchedText by remember { mutableStateOf("") }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // Category Field
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                expanded = false
            })
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = searchedText,
                    onValueChange = { name ->
                        searchedText = name
                        expanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (expanded)
                            {
                                onSearchCleared.invoke()
                                searchedText = ""
                            }
                            expanded = !expanded
                        }) {
                            if (expanded)
                            {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "arrow",
                                    tint = Color.Black
                                )
                            }
                            else
                            {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "arrow",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(text = "Search character by name")
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (searchedText.isNotEmpty())
                        {
                            items(
                                characterList.filter { character ->
                                    character.name?.lowercase()
                                        ?.contains(searchedText.lowercase()) == true
                                }
                            ) {
                                ItemsCategory(
                                    title = it.name ?: "",
                                    id = it.id ?: 0
                                ) { title, id ->
                                    searchedText = title
                                    expanded = false
                                    onCharacterSearched(id)
                                }
                            }
                        }
                        else
                        {
                            items(
                                characterList
                            ) {character ->
                                ItemsCategory(
                                    title = character.name ?: "",
                                    id = character.id ?: 0
                                ) { title, id ->
                                    searchedText = title
                                    expanded = false
                                    onCharacterSearched(id)
                                }
                            }
                        }

                    }

                }
            }

        }

    }
}

@Composable
fun ItemsCategory(
    title: String,
    id: Int,
    onSelect: (String, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title, id)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }

}