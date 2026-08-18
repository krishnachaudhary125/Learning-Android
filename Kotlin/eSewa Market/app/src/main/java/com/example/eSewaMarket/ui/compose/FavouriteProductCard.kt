package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun FavouriteProductCard(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    brand: @Composable () -> Unit,
    price: @Composable () -> Unit,
    onClick: () -> Unit,
    optionClick: () -> Unit,
    addToCartClick: () -> Unit,
    tickClick: () -> Unit,
    checked: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
                .heightIn(min = 120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 48.dp,
                        bottom = 16.dp
                    )
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null,
                        onClick = onClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    image()
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {

                    title()
                    brand()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text = "Rs.",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.text_dark_400)
                        )

                        price()
                    }
                }
            }

            CleanIconButton(
                icon = R.drawable.ic_more_vertical,
                contentDescription = "Options",
                onClick = optionClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            )

            if (checked) {
                CleanIconButton(
                    icon = R.drawable.ic_fav_add_cart,
                    contentDescription = "Add to cart",
                    onClick = addToCartClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                        .size(36.dp)
                        .background(
                            color = colorResource(R.color.green),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }
        if (checked) {
            CleanIconButton(
                icon = R.drawable.ic_tick,
                contentDescription = "Checked",
                onClick = tickClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 6.dp, start = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouriteProductCardPreview() {

    FavouriteProductCard(
        image = {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(Color.LightGray)
            )
        },

        title = {
            Text(
                text = "Nike Air Max",
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 2.dp)
            )
        },

        brand = {
            Text(
                text = "Nike",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 2.dp)
            )
        },

        price = {
            Text(
                text = "1999",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },

        onClick = {},

        optionClick = {},

        addToCartClick = {},

        tickClick = {},

        checked = true
    )
}