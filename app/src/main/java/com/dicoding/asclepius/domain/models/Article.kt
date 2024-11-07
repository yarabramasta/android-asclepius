package com.dicoding.asclepius.domain.models

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Article(
	val description: String,
	val title: String,
	val url: String,
	val urlToImage: String? = null,
) {
	companion object {
		fun fake() = Article(
			title = "James Van Der Beek, Jenna Fischer and the rise of young people getting cancer - USA TODAY",
			description = "Why are young people getting cancer at higher rates? No one knows, but here's how to stay proactive and vigilant.",
			url = "https://www.usatoday.com/story/life/health-wellness/2024/11/04/james-van-der-beek-jenna-fischer-cancer-young/75576304007/",
			urlToImage = "https://avatars.jakerunzer.com/${"fake_article".hashCode()}"
		)
	}
}

fun Article.getBrowsableUri(): Uri = Uri.parse(url)