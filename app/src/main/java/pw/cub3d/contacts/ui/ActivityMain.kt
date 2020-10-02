package pw.cub3d.contacts.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.map
import pw.cub3d.contacts.R
import pw.cub3d.contacts.contactslist.ContactSelectedEvent
import pw.cub3d.contacts.dataSources.Contact
import pw.cub3d.contacts.dataSources.IContactRepository
import pw.cub3d.contacts.dataSources.NullContactRepository
import pw.cub3d.contacts.post
import java.util.*


private val DarkColors = darkColors(
        primary = Color(0xFFBA86FC),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212)
)

private val backgroundColours = listOf(
        android.graphics.Color.rgb(200, 0, 0),
        android.graphics.Color.rgb(0, 200, 0),
        android.graphics.Color.rgb(0, 0, 200),
        android.graphics.Color.rgb(0, 200, 200),
        android.graphics.Color.rgb(200, 0, 200),
        android.graphics.Color.rgb(200, 200, 0)
)

@Composable
fun header(
    searchQuery: String,
    setSearchQuery: (String)->Unit,
    searchMode: MutableState<Boolean>,
) {
    Box(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)
        .then(Modifier.background(shape = RoundedCornerShape(10.dp), color = Color(0xFF1D1D1D)))
        .then(Modifier.height(50.dp))
        .then(Modifier.fillMaxWidth())) {

        Box(modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 0.dp)
            .then(Modifier.fillMaxHeight())
            .then(Modifier.align(Alignment.Center))
            .then(Modifier.clickable(onClick = {
                searchMode.value = true
            }))) {

            if (!searchMode.value) {
                Text(
                        "Search Contacts",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
            } else {
                TextField(value = searchQuery , onValueChange = setSearchQuery, label = {
                    Text(text = "Label")
                })
            }
        }
    }
}

@Composable
fun activityMain(contacts: IContactRepository) {
    val image = vectorResource(id = R.drawable.ic_plus)

    MaterialTheme(colors = DarkColors) {
        Surface {
            Column {
                val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
                val searchMode = remember { mutableStateOf(false) }

                header(searchQuery, setSearchQuery, searchMode)

                val contactsState = contacts.getContacts().map{
                    if (searchQuery.isNotBlank()){
                        it.filter { contact ->
                            contact.contains(searchQuery.toLowerCase(Locale.getDefault()))
                        }
                    } else {
                        it
                    }
                }.collectAsState(initial = emptyList())


                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyColumnForIndexed(
                        items = contactsState.value,
                        modifier = Modifier.fillMaxSize()
                    ) { index, contact ->
                        val showLetter = index == 0 || contactsState.value[index - 1].firstInitial != contact.firstInitial

                        contactRow(contact = contact, showLetter = showLetter)
                    }

                    FloatingActionButton(
                        onClick = {},
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .then(Modifier.padding(16.dp))
                    ) {
                        Image(asset = image)
                    }
                }
            }
        }
    }
}

@Composable
fun contactRow(contact: Contact, showLetter: Boolean) {
    Row(modifier = Modifier.clickable(onClick = {
        ContactSelectedEvent(contact).post()
    }).then(Modifier.draggable(Orientation.Horizontal, onDrag = {
        ContactSelectedEvent(contact).post()
    })).then(Modifier.fillMaxWidth())) {
        if (showLetter) {
            Text(
                text = contact.firstInitial,
                modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 0.dp)
            )
        } else {
            Text(
                text = "A",
                modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 0.dp),
                color = Color.Transparent
            )
        }

        Row(modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)) {
            if (contact.hasPhoto) {
                contact.getImageBitmap(ContextAmbient.current)?.let {
                    Image(asset = it.asImageAsset(), modifier = Modifier.clip(CircleShape))
                }
            } else {

                Box(modifier =
                    Modifier.background(
                        color = Color(backgroundColours[contact.contactID.hashCode() % backgroundColours.size]),
                        shape = CircleShape,
                    ).then(Modifier.size(36.dp))
                ) {

                    Box(modifier = Modifier.fillMaxSize().then(Modifier.align(Alignment.Center))) {
                        Text(
                            text = contact.firstInitial,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Text(
                text = contact.displayName,
                modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun preview_contactRow() {
    MaterialTheme(colors = DarkColors) {
        Surface {
            contactRow(
                contact =
                Contact(
                    0,
                    "Mr",
                    "Test",
                    "ATest",
                    "User",
                    "",
                    "",
                    0,
                    0,
                    ""
                ), false
            )
        }
    }
}


@Preview
@Composable
fun preview_ActivityMain() {
    activityMain(NullContactRepository())
}

@Preview
@Composable
fun preview_header() {
    header("", { }, remember { mutableStateOf(false) })
}