package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
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

@OptIn(ExperimentalMaterial3Api::class)
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
    onProductClick: (ProductResponse) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showPromoSheet by rememberSaveable{
        mutableStateOf(false)
    }
    val promoSheetState = rememberModalBottomSheetState()

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
                onClick = {
                    showPromoSheet = true
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
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
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
    if(showPromoSheet){
        ModalBottomSheet(
            onDismissRequest = {
                showPromoSheet = false
            },
            sheetState = promoSheetState,
            dragHandle = null,
           containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    "Promocode",
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_dark_400),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )

                Text(
                    "Enter promocode",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 1.sp,
                    color = colorResource(id = R.color.text_dark_300),
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 4.dp
                        )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colorResource(id = R.color.compose_text_field),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    state = rememberTextFieldState(),
                    placeholder = {
                        Text(
                            text = "Promocode",
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 2.sp,
                            color = colorResource(R.color.text_dark_100)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Row() {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.text_dark_300),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 16.dp,
                                end = 8.dp
                            )
                    ) {
                        Text(
                            "CANCEL",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp,
                            letterSpacing = 4.sp,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                        )
                    }

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.green),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 16.dp,
                                start = 8.dp
                            )
                    ) {
                        Text(
                            "APPLY",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp,
                            letterSpacing = 4.sp,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}