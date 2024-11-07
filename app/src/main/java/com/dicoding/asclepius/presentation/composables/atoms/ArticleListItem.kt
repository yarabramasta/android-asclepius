package com.dicoding.asclepius.presentation.composables.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.asclepius.domain.models.Article
import com.dicoding.asclepius.presentation.composables.theme.AppTheme
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun ArticleListItem(
	modifier: Modifier = Modifier,
	item: Article,
	onPressed: (Article) -> Unit,
) {
	ListItem(
		modifier = modifier.clickable { onPressed(item) },
		leadingContent = {
			CoilImage(
				imageModel = {
					item.urlToImage ?: "https://avatars.jakerunzer.com/${item.title.hashCode()}"
				},
				loading = { ShimmerBox(modifier = Modifier.size(64.dp)) },
				failure = { ShimmerBox(modifier = Modifier.size(64.dp), animate = false) },
				modifier = Modifier
					.size(64.dp)
					.aspectRatio(1f)
					.clip(MaterialTheme.shapes.small)
			)
		},
		headlineContent = {
			Text(
				item.title,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.primary,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				modifier = Modifier.padding(bottom = 8.dp)
			)
		},
		supportingContent = {
			Text(
				item.description,
				maxLines = 3,
				color = MaterialTheme.colorScheme.outline,
				overflow = TextOverflow.Ellipsis,
			)
		},
	)
}

@Preview(showBackground = true)
@Composable
private fun ArticleListItemPreview() {
	val item = Article.fake()

	AppTheme {
		ArticleListItem(
			item = item,
			onPressed = {}
		)
	}
}