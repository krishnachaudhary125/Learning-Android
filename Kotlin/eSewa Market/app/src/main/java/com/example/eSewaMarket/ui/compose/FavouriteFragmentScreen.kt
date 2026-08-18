package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R
import androidx.compose.ui.unit.sp

@Composable
fun FavouriteFragmentScreen(

){
    Column() {

        AppToolBar(
            modifier = Modifier.statusBarsPadding(),
            onBackClick = {},
            title = {
                Text(
                    "Favourites",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_dark_400)
                    )
            },
            actionBtn = {
                CleanIconButton(
                    icon = R.drawable.ic_cart,
                    contentDescription = "Cart",
                    onClick = {

                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = colorResource(id = R.color.esewa_bg_light),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        )
    }
}