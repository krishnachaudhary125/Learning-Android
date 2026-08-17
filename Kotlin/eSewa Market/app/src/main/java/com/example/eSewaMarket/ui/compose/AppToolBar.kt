package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R

@Composable
fun AppToolBar(
    onBackClick: () -> Unit,
    title: (@Composable () -> Unit)? = {},
    actionBtn: (@Composable () -> Unit)? = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = Color.White)
            .padding(horizontal = 8.dp)
    ){
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ){
            CleanIconButton(
                icon = R.drawable.ic_back_arrow,
                contentDescription = "Back",
                onClick = onBackClick
            )
        }

        title?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ){
                it()
            }
        }

        actionBtn?.let {
            Box(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                it()
            }
        }
    }
}