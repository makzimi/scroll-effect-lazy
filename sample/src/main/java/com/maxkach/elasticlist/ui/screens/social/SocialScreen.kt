package com.maxkach.elasticlist.ui.screens.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkach.elasticlist.ui.effects.EffectPreset
import com.maxkach.elasticlist.ui.effects.EffectPresetPicker
import com.maxkach.elasticlist.ui.effects.asScrollEffect
import com.maxkach.elasticlist.ui.theme.ElasticListTheme
import com.maxkach.scrolleffects.ScrollEffectLazyRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    effectPreset: EffectPreset = EffectPreset.None,
    onEffectPreset: (EffectPreset) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TopAppBar(
                    title = { Text("Feed") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
                EffectPresetPicker(
                    selected = effectPreset,
                    onSelected = onEffectPreset,
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item { SectionHeader("Stories") }
                item {
                    ScrollEffectLazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        scrollEffect = effectPreset.asScrollEffect(),
                    ) {
                        items(socialStories, key = { it.id }) { story ->
                            StoryItem(story)
                        }
                    }
                }
                item { SectionHeader("Latest") }
                item {
                    ScrollEffectLazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        scrollEffect = effectPreset.asScrollEffect(),
                    ) {
                        items(socialSmallPhotos, key = { it.id }) { photo ->
                            SmallPhotoItem(photo)
                        }
                    }
                }
                item { SectionHeader("Featured") }
                item {
                    ScrollEffectLazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        scrollEffect = effectPreset.asScrollEffect(),
                    ) {
                        items(socialBigPhotos, key = { it.id }) { photo ->
                            BigPhotoItem(photo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
private fun StoryItem(image: SocialImage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(78.dp),
    ) {
        Image(
            painter = painterResource(image.resId),
            contentDescription = null,
            modifier = Modifier
                .size(78.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary, CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            image.label,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SmallPhotoItem(image: SocialImage) {
    Column(modifier = Modifier.width(130.dp)) {
        Image(
            painter = painterResource(image.resId),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            image.label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BigPhotoItem(image: SocialImage) {
    Column(modifier = Modifier.width(220.dp)) {
        Image(
            painter = painterResource(image.resId),
            contentDescription = null,
            modifier = Modifier
                .width(220.dp)
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            image.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SocialScreenPreview() {
    ElasticListTheme {
        SocialScreen()
    }
}
