package com.tiuho22bangkit.gizi.ui.nutriai

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.remote.ChatMessage

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
        private const val VIEW_TYPE_LOADING = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = inflater.inflate(R.layout.item_chat_user, parent, false)
                UserMessageViewHolder(view)
            }
            VIEW_TYPE_BOT -> {
                val view = inflater.inflate(R.layout.item_chat_bot, parent, false)
                BotMessageViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_chat_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is LoadingViewHolder -> {
                if (message.isLoading) {
                    holder.startLoadingAnimation()
                } else {
                    holder.stopLoadingAnimation()
                }
            }
            is UserMessageViewHolder -> holder.bind(message.message)
            is BotMessageViewHolder -> holder.bind(message.message)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return when {
            messages[position].isLoading -> VIEW_TYPE_LOADING
            messages[position].isUser -> VIEW_TYPE_USER
            else -> VIEW_TYPE_BOT
        }
    }

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)

        fun bind(message: String) {
            messageText.text = message
        }
    }

    class BotMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)

        fun bind(message: String) {
            messageText.text = HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val loadingText: TextView = itemView.findViewById(R.id.loading_text)
        private val handler = Handler(Looper.getMainLooper())
        private val loadingStates = arrayOf("•", "• •", "• • •")
        private var index = 0
        private var isAnimating = false

        private val runnable = object : Runnable {
            override fun run() {
                if (isAnimating) {
                    loadingText.text = loadingStates[index]
                    index = (index + 1) % loadingStates.size
                    handler.postDelayed(this, 500)
                }
            }
        }

        fun startLoadingAnimation() {
            if (!isAnimating) {
                isAnimating = true
                handler.post(runnable)
            }
        }

        fun stopLoadingAnimation() {
            isAnimating = false
            handler.removeCallbacks(runnable)
            loadingText.text = ""
        }
    }
}


