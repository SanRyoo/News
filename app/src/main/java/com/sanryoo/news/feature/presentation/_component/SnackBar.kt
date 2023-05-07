package com.sanryoo.news.feature.presentation._component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SnackBar(snackBarData: SnackbarData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        shape = RoundedCornerShape(10.dp),
        contentColor = Color.White,
        color = Color(0xFF000000),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (snackBarData.actionLabel != null) Arrangement.SpaceBetween
            else Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = snackBarData.message,
                    modifier = Modifier.padding(15.dp),
                    style = TextStyle(textAlign = TextAlign.Center),
                    fontSize = 18.sp
                )
            }
            if (snackBarData.actionLabel != null) {
                Text(
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .clickable(interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                snackBarData.performAction()
                            }
                        ),
                    text = snackBarData.actionLabel ?: "",
                    style = TextStyle(textAlign = TextAlign.Center),
                    fontSize = 18.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}