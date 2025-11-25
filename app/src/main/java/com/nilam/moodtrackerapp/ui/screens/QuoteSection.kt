package com.nilam.moodtrackerapp.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 SEARCH BAR CANTIK
---------------------------------------------------- */
@Composable
fun BeautifulSearchBar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF01579B)
            )
        },
        placeholder = { Text("Search quotes...") },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF0288D1),
            unfocusedBorderColor = Color(0xFF81D4FA),
            cursorColor = Color(0xFF0277BD),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(bottom = 8.dp)
            .shadow(4.dp, RoundedCornerShape(30.dp))
    )
}

/* ---------------------------------------------------
 QUOTE SCREEN
---------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen() {
    var quotes by remember { mutableStateOf(listOf<String>()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val client = remember { HttpClient(CIO) }

    /* FETCH QUOTES FUNCTION */
    fun fetchQuotes() {
        scope.launch {
            try {
                isRefreshing = true

                val responseString: String =
                    client.get("https://zenquotes.io/api/quotes").body()

                val json = Json.parseToJsonElement(responseString).jsonArray

                val list = json.take(30).map {
                    val quote = it.jsonObject["q"]?.jsonPrimitive?.content ?: ""
                    val author = it.jsonObject["a"]?.jsonPrimitive?.content ?: "Unknown"
                    "\"$quote\"\n- $author"
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

    LaunchedEffect(Unit) { fetchQuotes() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { fetchQuotes() },
                containerColor = Color(0xFF0277BD)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->

        /* BACKGROUND GRADIENT */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4FC3F7),
                            Color(0xFF0288D1),
                            Color(0xFF01579B)
                        )
                    )
                )
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                /* Search Bar */
                BeautifulSearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }
                )

                Spacer(modifier = Modifier.height(6.dp))

                /** ⬇️ PULL TO REFRESH (ala Instagram) */
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { fetchQuotes() },
                ) {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredQuotes) { q ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = q,
                                        textAlign = TextAlign.Start,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Spacer(Modifier.height(10.dp))

                                    /* SHARE + COPY Buttons */
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = { copyQuote(q) }) {
                                            Icon(
                                                Icons.Default.ContentCopy,
                                                contentDescription = "Copy",
                                                tint = Color(0xFF0277BD)
                                            )
                                        }

                                        IconButton(onClick = { shareQuote(q) }) {
                                            Icon(
                                                Icons.Default.Share,
                                                contentDescription = "Share",
                                                tint = Color(0xFF01579B)
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
