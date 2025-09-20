package com.products.app.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.products.app.R
import com.products.app.domain.model.Product
import com.products.app.domain.model.ViewedProduct

/**
 * A reusable product card component that displays product information in a Material Design card.
 * 
 * This composable renders a product with its image, name, and handles user interactions.
 * It includes press animations and proper accessibility support.
 * 
 * @param modifier Modifier to be applied to the card
 * @param product The Product domain model containing product information
 * @param onProductClick Callback invoked when the product card is clicked
 */
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: (Product) -> Unit = {}
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "cardScale"
    )
    
    val imageUrl = product.thumbnailUrl?.takeIf { it.isNotBlank() } 
        ?: product.pictureUrls.firstOrNull()
    
    val imageRequest: ImageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
    }
    
    Card(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onProductClick(product) }
            .scale(scale),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "${stringResource(R.string.product_image)} ${product.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * A specialized product card component for displaying recently viewed products.
 * 
 * This composable renders a viewed product with its image, name, and includes
 * a delete button for removing the product from the viewed history. It's
 * optimized for smaller display sizes compared to the main ProductCard.
 * 
 * @param modifier Modifier to be applied to the card
 * @param product The ViewedProduct domain model containing viewed product information
 * @param onProductClick Callback invoked when the product card is clicked (passes product ID)
 * @param onDeleteClick Callback invoked when the delete button is clicked
 */
@Composable
fun ViewedProductCard(
    modifier: Modifier = Modifier,
    product: ViewedProduct,
    onProductClick: (String) -> Unit = {},
    onDeleteClick: (ViewedProduct) -> Unit = {}
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "cardScale"
    )
    
    val imageRequest: ImageRequest = remember(product.thumbnailUrl) {
        ImageRequest.Builder(context)
            .data(product.thumbnailUrl)
            .build()
    }
    
    Card(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onProductClick(product.productId) }
            .scale(scale),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "${stringResource(R.string.product_image)} ${product.productName}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { onDeleteClick(product) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
