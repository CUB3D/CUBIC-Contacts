package pw.cub3d.contacts.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.ui.tooling.preview.Preview
import com.github.abdularis.civ.AvatarImageView
import kotlinx.coroutines.flow.map
import pw.cub3d.contacts.R
import pw.cub3d.contacts.contactslist.ContactSelectedEvent
import pw.cub3d.contacts.dataSources.IContactRepository
import pw.cub3d.contacts.dataSources.NullContactRepository
import pw.cub3d.contacts.post


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
fun activityMain(contacts: IContactRepository) {
    val image = vectorResource(id = R.drawable.ic_plus)

    MaterialTheme(colors = DarkColors) {
        Surface {
            Column {

                Box(shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0xFF1D1D1D),
                modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp).then(Modifier.height(50.dp))) {
                    Text(
                            "Search Contacts",
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                    )
                }

                val contacts = contacts.getContacts()
                        .map {
                    it.mapIndexed { i, v ->
                        if (i == 0) {
                            Pair(true, v)
                        } else {
                            Pair(it[i - 1].firstInitial != v.firstInitial, v)
                        }
                    }
                }.collectAsState(initial = emptyList())


                Stack(modifier = Modifier.fillMaxWidth()) {
                    LazyColumnFor(items = contacts.value, modifier = Modifier.fillMaxSize()) { contact ->
                        Row(modifier = Modifier.clickable(onClick = {
                            ContactSelectedEvent(contact.second).post()
                        }).then(Modifier.fillMaxWidth())) {
                            if (contact.first) {
                                Text(text = contact.second.firstInitial, modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 0.dp))
                            } else {
                                Text(text = "A", modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 0.dp), color = Color.Transparent)
                            }

                            Row(modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)) {
                                if(contact.second.hasPhoto) {
                                    contact.second.getImageBitmap(ContextAmbient.current)?.let {
                                        Image(asset = it.asImageAsset(), modifier = Modifier.clip(CircleShape))
                                    }
                                } else {
                                    Box(shape = CircleShape,
                                        modifier = Modifier.size(36.dp),
                                        backgroundColor = Color(backgroundColours[contact.second.contactID.hashCode() % backgroundColours.size])) {
                                        Text(text = contact.second.firstInitial, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
                                    }
                                }
                                Text(text = contact.second.displayName, modifier = Modifier.padding(24.dp, 8.dp, 8.dp, 8.dp))
                            }
                        }
                    }

                    FloatingActionButton(onClick = {}, modifier = Modifier.gravity(Alignment.BottomEnd).then(Modifier.padding(16.dp))) {
                        Image(asset = image)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun preview_ActivityMain() {
    activityMain(NullContactRepository())
}