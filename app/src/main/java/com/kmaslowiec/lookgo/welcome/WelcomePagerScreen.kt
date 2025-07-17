package com.kmaslowiec.lookgo.welcome

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class PageBackgroundColor(val color: @Composable () -> Color) {
    ThemePrimary({ MaterialTheme.colorScheme.primary }),
    ThemeSecondary({ MaterialTheme.colorScheme.secondary }),
    ThemeTertiary({ MaterialTheme.colorScheme.tertiary })
}

@Composable
fun WelcomePagerScreen(modifier: Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
        ) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(pageBackgroundColor(page))
            ) {
                Text(text = "Page ${page + 1}")
            }
        }

        DotsIndicator(
            totalDots = 3,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedColor: Color = Color.Gray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(pageBackgroundColor(selectedIndex))
            .fillMaxWidth()
    ) {
        for (i in 0 until totalDots) {
            val isSelected = i == selectedIndex
            val size by animateDpAsState(if (isSelected) 16.dp else 12.dp)
            val scale by animateFloatAsState(if (isSelected) 1.2f else 1.0f)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(size)
                    .scale(scale)
                    .background(
                        color = if (isSelected) selectedColor else unSelectedColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun pageBackgroundColor(page: Int): Color {
    return when (page) {
        0 -> PageBackgroundColor.ThemePrimary.color()
        1 -> PageBackgroundColor.ThemeSecondary.color()
        2 -> PageBackgroundColor.ThemeTertiary.color()
        else -> MaterialTheme.colorScheme.background
    }
}

@Preview
@Composable
fun DotsIndicatorPreview() {
    DotsIndicator(
        totalDots = 5,
        selectedIndex = 2,
        modifier = Modifier.padding(16.dp)
    )
}
