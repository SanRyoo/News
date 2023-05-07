package com.sanryoo.news.feature.presentation._component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.sanryoo.news.feature.domain.modal.Article
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    article: Article,
    onClickArticle: (Article) -> Unit = {},
    icon: Int,
    onClickIcon: (Article) -> Unit = {}
) {
    val swipebleState = rememberSwipeableState(initialValue = 0)
    val pxValue = with(LocalDensity.current) { 100.dp.toPx() }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .swipeable(
                state = swipebleState,
                anchors = mapOf(
                    0f to 0,
                    -pxValue to 1
                ),
                thresholds = { _, _ ->
                    FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            )
    ) {
        ConstraintLayout(modifier = Modifier.offset {
            IntOffset(swipebleState.offset.value.roundToInt(), 0)
        }) {
            val (item, reveal) = createRefs()
            Column(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(item) {}
                .clickable(onClick = { onClickArticle(article) })
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = article.urlToImage),
                        contentDescription = "Image",
                        modifier = Modifier
                            .weight(5f)
                            .aspectRatio(1.5f)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = article.title ?: "",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(5f)
                    )
                }
                Text(
                    text = article.content ?: "", modifier = modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "${article.source.name}, ${article.publishedAt}",
                    modifier = modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .border(width = 1.dp, color = Color(0xFFCBC3C3))
                    .constrainAs(reveal) {
                        start.linkTo(item.end)
                        top.linkTo(item.top)
                        bottom.linkTo(item.bottom)
                        height = Dimension.fillToConstraints
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon favorite",
                    tint = Color.Red,
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                onClickIcon(article)
                                scope.launch {
                                    swipebleState.animateTo(0, tween(300))
                                }
                            }
                        )
                )
            }
        }
    }
}