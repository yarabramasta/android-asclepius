package com.dicoding.asclepius.presentation.composables.atoms

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.models.getConfidenceScoreString

@Composable
fun HistoryListItem(
	modifier: Modifier = Modifier,
	item: AnalyzeResult,
	onDeletePressed: (AnalyzeResult) -> Unit,
) {
	val context = LocalContext.current
	var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

	DisposableEffect(item.imageUri) {
		val bitmap = item.imageUri.let { uri ->
			BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
		}
		imageBitmap = bitmap.asImageBitmap()

		onDispose {
			bitmap?.recycle()
		}
	}

	ListItem(
		modifier = modifier,
		leadingContent = {
			if (imageBitmap != null) {
				Image(
					bitmap = imageBitmap!!,
					contentDescription = null,
					modifier = Modifier
						.size(64.dp)
						.aspectRatio(1f)
						.clip(MaterialTheme.shapes.small)
				)
			} else {
				ShimmerBox(modifier = Modifier.size(64.dp))
			}
		},
		headlineContent = {
			Text(
				item.getConfidenceScoreString(),
				maxLines = 2,
				overflow = TextOverflow.Ellipsis
			)
		},
		trailingContent = {
			IconButton(onClick = { onDeletePressed(item) }) {
				Icon(
					Icons.Filled.DeleteForever,
					contentDescription = "Delete",
					tint = MaterialTheme.colorScheme.tertiary
				)
			}
		},
	)
}