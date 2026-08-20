package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    address: String
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){

            AddressCard(
                address = {
                    Text(
                        "$address",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.color_charcoal),
                        lineHeight = 24.sp,
                        letterSpacing = 2.sp
                    )
                }
            )

            Text(
                "Order Summary",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = colorResource(id = R.color.text_dark_300),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )
        }
    }
}