package dev.n45.krishimitra

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.n45.krishimitra.adapter.ChatAdapter
import dev.n45.krishimitra.api.ai.AiClient
import dev.n45.krishimitra.api.ai.AiRequest
import dev.n45.krishimitra.api.ai.Content
import dev.n45.krishimitra.api.ai.Part
import dev.n45.krishimitra.api.data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var fieldTextMessage: com.google.android.material.textfield.TextInputLayout
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: ChatAdapter
    private val apiService = AiClient.aiService
    private val apiKey = "AIzaSyCk8m-MqJDix-aWE5lHb3yZ8JrWNSmCXFc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        topBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        editTextMessage = findViewById(R.id.editTextMessage)

        adapter = ChatAdapter(messages)
        recyclerViewChat.adapter = adapter
        recyclerViewChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        fieldTextMessage = findViewById(R.id.fieldTextMessage)

        fieldTextMessage.setEndIconOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                messages.add(Message(messageText, true))
                adapter.notifyItemInserted(messages.size - 1)
                recyclerViewChat.scrollToPosition(messages.size - 1)

                fetchAiReply(messageText)

                editTextMessage.text.clear()
            }
        }
    }

    private fun fetchAiReply(userMessage: String) {
        messages.add(Message("Typing...", false))
        val loadingIndex = messages.size - 1
        adapter.notifyItemInserted(loadingIndex)
        recyclerViewChat.scrollToPosition(loadingIndex)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val request = AiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(Part(text = "[$userMessage.] this the message, reply as real human within 100 words, no markdown."))
                        )
                    )
                )
                val response = withContext(Dispatchers.IO) {
                    apiService.generateContent(apiKey, request)
                }
                messages.removeAt(loadingIndex)
                adapter.notifyItemRemoved(loadingIndex)

                val aiReply = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (aiReply != null) {
                    messages.add(Message(aiReply, false))
                    adapter.notifyItemInserted(messages.size - 1)
                    recyclerViewChat.scrollToPosition(messages.size - 1)
                } else {
                    messages.add(Message("No response from AI.", false))
                    adapter.notifyItemInserted(messages.size - 1)
                    recyclerViewChat.scrollToPosition(messages.size - 1)
                }
            } catch (e: Exception) {
                messages.removeAt(loadingIndex)
                adapter.notifyItemRemoved(loadingIndex)

                messages.add(Message("Error: ${e.message}", false))
                adapter.notifyItemInserted(messages.size - 1)
                recyclerViewChat.scrollToPosition(messages.size - 1)
            }
        }
    }
}