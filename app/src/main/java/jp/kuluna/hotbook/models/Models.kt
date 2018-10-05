package jp.kuluna.hotbook.models

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

data class Entry(
        val count: String,
        val favicon_url: String,
        val image_l: EntryImage?,
        val eid: String,
        val description: String,
        val entry_url: String?,
        val image: String,
        val root_url: String,
        val url: String,
        val title: String,
        val is_pr: Int
) {
    fun getImageUrl(): String? = image_l?.url

    fun count(): Int = count.toInt()

    fun getHost(): String = Uri.parse(root_url).host!!
}

data class EntryImage(
        val width: Int,
        val url: String,
        val height: Int
)

data class CommentResponse(
        val count: Int,
        val bookmarks: List<Bookmark>?,
        val url: String,
        val eid: Long,
        val title: String,
        val screenshot: String,
        val entry_url: String
)

data class Bookmark(
        val timestamp: String,
        val comment: String,
        val user: String,
        val tags: List<String>
) {
    fun getTimeStampDate(): Date {
        val format = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.JAPAN)
        return format.parse(timestamp)
    }

    fun getUserIconUrl(): String = "https://cdn.profile-image.st-hatena.com/users/$user/profile.png"
}

//////////////////// Wrapper ////////////////////
data class ResponseBody<T>(
        val body: T?,
        val error: ResponseError?
)

data class ResponseError(
        val statusCode: Int,
        val message: String
)
