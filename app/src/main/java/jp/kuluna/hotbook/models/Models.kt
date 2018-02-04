package jp.kuluna.hotbook.models

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

data class Entry(
        var count: String,
        var favicon_url: String,
        var image_l: EntryImage?,
        var eid: String,
        var description: String,
        var entry_url: String?,
        var image: String,
        var root_url: String,
        var url: String,
        var title: String,
        var is_pr: Int
) {
    fun getImageUrl(): String? = image_l?.url

    fun count(): Int = count.toInt()

    fun getHost(): String = Uri.parse(root_url).host
}

data class EntryImage(
        var width: Int,
        var url: String,
        var height: Int
)

data class CommentResponse(
        var count: Int,
        var bookmarks: List<Bookmark>,
        var url: String,
        var eid: Long,
        var title: String,
        var screenshot: String,
        var entry_url: String
)

data class Bookmark(
        var timestamp: String,
        var comment: String,
        var user: String,
        var tags: List<String>
) {
    fun getTimeStampDate(): Date {
        val format = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.JAPAN)
        return format.parse(timestamp)
    }

    fun getUserIconUrl(): String = "https://cdn.profile-image.st-hatena.com/users/${user}/profile.png"
}

//////////////////// Wrapper ////////////////////
data class ResponseBody<T>(
        var body: T?,
        var error: ResponseError?
)

data class ResponseError(
        var statusCode: Int,
        var message: String
)
