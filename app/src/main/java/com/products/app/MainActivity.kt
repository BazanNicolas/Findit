package com.products.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.products.app.presentation.productDetail.ProductDetailScreen
import com.products.app.presentation.productSearch.ProductSearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsApp {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "product_search"
                ) {
                    composable("product_search") {
                        ProductSearchScreen(
                            onProductClick = { product ->
                                navController.navigate("product_detail/${product.id}")
                            }
                        )
                    }
                    
                    composable("product_detail/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId") ?: ""
                        ProductDetailScreen(
                            productId = productId,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            viewModel = androidx.hilt.navigation.compose.hiltViewModel()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductsApp(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        Surface(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsAppPreview() {
    ProductsApp {
        ProductSearchScreen()
    }
}
