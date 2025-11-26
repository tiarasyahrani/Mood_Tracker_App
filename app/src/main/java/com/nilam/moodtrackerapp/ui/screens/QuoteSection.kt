package com.nilam.moodtrackerapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.*
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/* ---------------------------------------------------
 BEAUTIFUL MODERN SEARCH BAR (2025 Design)
---------------------------------------------------- */
@Composable
fun BeautifulSearchBar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text("Search quotes...", color = Color(0xAA000000))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF01579B)
            )
        },
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF0277BD),
            unfocusedBorderColor = Color(0x330277BD),
            cursorColor = Color(0xFF0277BD),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp), clip = false)
            .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
    )
}

/* ---------------------------------------------------
 QUOTE SCREEN MODERN
---------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen() {

    /* STATE */
    var quotes by remember { mutableStateOf(listOf<String>()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val client = remember { HttpClient(CIO) }

    /* FETCH QUOTES */
    fun fetchQuotes() {
        scope.launch {
            try {
                isRefreshing = true

                val response: String =
                    client.get("https://zenquotes.io/api/quotes").body()

                val json = Json.parseToJsonElement(response).jsonArray

                val list = json.take(30).map {
                    val quote = it.jsonObject["q"]!!.jsonPrimitive.content
                    val author = it.jsonObject["a"]!!.jsonPrimitive.content
                    "\"$quote\"\n— $author"
                }

                quotes = list
            } catch (e: Exception) {
                quotes = listOf("Failed to load quotes: ${e.localizedMessage}")
            } finally {
                isRefreshing = false
            }
        }
    }

    /* SHARE QUOTE */
    fun shareQuote(q: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, q)
        context.startActivity(Intent.createChooser(intent, "Share Quote via"))
    }

    /* COPY QUOTE */
    fun copyQuote(q: String) {
        clipboard.setText(androidx.compose.ui.text.AnnotatedString(q))
    }

    val filteredQuotes = quotes.filter {
        it.contains(searchQuery.text, ignoreCase = true)
    }

    /* AUTO FETCH WHEN OPEN */
    LaunchedEffect(Unit) { fetchQuotes() }

    /* ---------------------------------------------------
       UI LAYOUT
    ---------------------------------------------------- */
    Scaffold { paddingValues ->

        /* GRADIENT BACKGROUND MODERN LEMBUT */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFBFE0EF),
                            Color(0xFF57B0D9),
                        )
                    )
                )
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {

                /* SEARCH BAR */
                BeautifulSearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                /* PULL TO REFRESH */
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { fetchQuotes() },
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        items(filteredQuotes) { q ->

                            /* ---------------------------------------------------
                               GLASSMORPHISM CARD STYLE
                            ---------------------------------------------------- */
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .blur(0.5.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.20f),
                                        RoundedCornerShape(18.dp)
                                    )
                                    .padding(14.dp)
                            ) {

                                Column {

                                    /* QUOTE TEXT */
                                    Text(
                                        text = q,
                                        color = Color.Black,
                                        textAlign = TextAlign.Start,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Spacer(Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        /* COPY BUTTON ROUND */
                                        IconButton(
                                            onClick = { copyQuote(q) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    Color.White.copy(alpha = 0.35f),
                                                    CircleShape
                                                )
                                        ) {
                                            Icon(
                                                Icons.Default.ContentCopy,
                                                contentDescription = "Copy",
                                                tint = Color(0xFF1C4564)
                                            )
                                        }

                                        Spacer(Modifier.width(15.dp))

                                        IconButton(
                                            onClick = { shareQuote(q) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    Color.White.copy(alpha = 0.35f),
                                                    CircleShape
                                                )
                                        ) {
                                            Icon(
                                                Icons.Default.Share,
                                                contentDescription = "Share",
                                                tint = Color(0xFF1C4564)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
