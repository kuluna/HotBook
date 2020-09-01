package jp.kuluna.hotbook.models

import android.net.Uri

data class Entry(
        val count: Int,
        val favicon_url: String,
        val image_l: EntryImage?,
        val eid: String,
        val description: String,
        val entry_url: String?,
        val image: String,
        val root_url: String,
        val url: String,
        val title: String
) {
    fun getImageUrl(): String? = image_l?.url

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
