package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.eSewaMarket.data.models.FavouriteResponse
import kotlinx.coroutines.launch

@Composable
fun FavouriteFragmentScreen(
    products: List<FavouriteResponse>,
    onBackClick: () -> Unit,
    noOfItems: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    deleteAll: () -> Unit,
    continueShopping: () -> Unit,
    onProductClick: (FavouriteResponse) -> Unit,
    onAddToCartClick: (Long) -> Unit,
    onOptionClick: (Long) -> Unit,
    onTickClick: (Long) -> Unit,
) {

    Column(
        modifier = Modifier.background(colorResource(id = R.color.background))
    ) {

        AppToolBar(
            modifier = Modifier.statusBarsPadding(),
            onBackClick = onBackClick,
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
                        .padding(end = 8.dp)
                        .size(56.dp)
                        .background(
                            color = colorResource(id = R.color.esewa_bg_light),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            if (noOfItems > 0) {
                CustomCheckbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Text(
                "Items  ( $noOfItems )",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_dark_300),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(end = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            if (checked) {
                Text(
                    "DELETE ALL",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_dark_300),
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = deleteAll
                        )
                )
            }
        }

        if (noOfItems == 0) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 326.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource( R.drawable.ic_empty_cart),
                        contentDescription = "Empty Favourite",
                        modifier = Modifier.padding(top = 32.dp)
                    )

                    Text(
                        "No favourites yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_400),
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        "Add your favourites to wishlist and\nthey will show here.",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        lineHeight = 24.sp,
                        color = colorResource(id = R.color.text_dark_200),
                        modifier = Modifier.padding(8.dp)
                    )

                    Button(
                        onClick = continueShopping,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.green),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                    ) {
                        Text(
                            "CONTINUE SHOPPING",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

        } else {

            LazyColumn() {
                items(
                    items = products,
                    key = { product ->
                        product.productId
                    }
                ) { product ->
                    FavouriteProductCard(
                        image = {
                            AsyncImage(
                                model = product.thumbnail,
                                contentDescription = "Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        },

                        title = {
                            Text(
                                text = product.title,
                                maxLines = 1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                lineHeight = 24.sp,
                                color = colorResource(id = R.color.text_dark_400),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        },

                        brand = {
                            Text(
                                text = product.brand.uppercase(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 2.sp,
                                color = colorResource(id = R.color.text_dark_200),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        },

                        price = {
                            Text(
                                text = "%.2f".format(product.price),
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = colorResource(id = R.color.text_dark_400)
                            )
                        },

                        onClick = {
                            onProductClick(product)
                        },

                        optionClick = {
                            onOptionClick(product.productId)
                        },

                        addToCartClick = {
                            onAddToCartClick(product.productId)
                        },

                        tickClick = {
                            onTickClick(product.productId)
                        },

                        checked = checked
                    )
                }
            }
        }
    }
}