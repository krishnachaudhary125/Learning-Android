package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.eSewaMarket.data.models.ProductResponse

@Composable
fun CheckoutScreen(
    checkoutProducts: List<ProductResponse>,
    onBackClick: () -> Unit,
    totalPrice: Double,
    itemCount: Int,
    productPrice: Double,
    totalTax: Double,
    shippingCharge: Double,
    address: String,
    promoBtn: () -> Unit,
    onProductClick: (ProductResponse) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

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
            CheckoutBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                totalPrice = totalPrice,
                isExpanded = isExpanded,
                itemCount = itemCount,
                productPrice = productPrice,
                totalTax = totalTax,
                shippingCharge = shippingCharge,
                onToggleClick = {
                    isExpanded = !isExpanded
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            AddressCard(
                address = {
                    Text(
                        address,
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
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )

            LazyColumn() {
                items(
                    items = checkoutProducts,
                    key = { product ->
                        product.productId
                    }
                ) { product ->
                    CheckoutProductCard(
                        onClick = {
                            onProductClick(product)
                        },
                        image = {
                            AsyncImage(
                                model = product.thumbnail,
                                contentDescription = "Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        },
                        title = product.title,
                        brand = product.brand,
                        price = product.price
                    )
                }
            }

            Button(
                onClick = promoBtn,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = colorResource(id = R.color.green)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorResource(id = R.color.green)
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Text(
                    "HAVE A PROMOCODE?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                )
            }
        }
    }
}