package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = colorResource(id = R.color.background),
        topBar = {
            AppToolBar(
                modifier = Modifier.statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "Checkout",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->

    }
}