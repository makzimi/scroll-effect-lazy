package com.maxkach.elasticlist.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkach.elasticlist.R
import com.maxkach.elasticlist.ui.theme.ElasticListTheme

private data class DemoEntry(
    val title: String,
    val imageRes: Int,
    val description: String,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEcommerceClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSocialClick: () -> Unit,
    onCoffeeClick: () -> Unit,
) {
    val entries = listOf(
        DemoEntry(
            title = "E-commerce Products",
            imageRes = R.drawable.product_03,
            description = "Vertical list, big 1:1 cards",
            onClick = onEcommerceClick
        ),
        DemoEntry(
            title = "Restaurant Menu",
            imageRes = R.drawable.dish_09,
            description = "Vertical list, medium cards",
            onClick = onMenuClick
        ),
        DemoEntry(
            title = "Social Feed",
            imageRes = R.drawable.socbig_08,
            description = "Horizontal, ~80% width cards",
            onClick = onSocialClick
        ),
        DemoEntry(
            title = "Coffee Shop",
            imageRes = R.drawable.drink_04,
            description = "Horizontal, compact cards",
            onClick = onCoffeeClick
        ),
    )
    Scaffold(
        topBar = { TopAppBar(title = { Text("Sample Lists") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(entries) { entry ->
                DemoCard(entry)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DemoCard(entry: DemoEntry) {
    Card(
        onClick = entry.onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .height(92.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(entry.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    entry.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                modifier = Modifier.padding(end = 12.dp),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ElasticListTheme {
        HomeScreen({}, {}, {}, {})
    }
}
