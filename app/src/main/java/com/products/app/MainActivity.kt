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
import com.products.app.presentation.home.HomeScreen
import com.products.app.presentation.productDetail.ProductDetailScreen
import com.products.app.presentation.productSearch.ProductSearchScreen
import com.products.app.presentation.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity that serves as the entry point of the Products application.
 * 
 * This activity handles navigation between different screens using Jetpack Compose Navigation.
 * It manages the main navigation flow between Home, Search, Product Search, and Product Detail screens.
 * 
 * The app follows a single-activity architecture pattern where all navigation is handled
 * through composable destinations rather than multiple activities.
 * 
 * @see HomeScreen for the main landing screen
 * @see ProductSearchScreen for product search functionality
 * @see ProductDetailScreen for individual product details
 * @see SearchScreen for search history and suggestions
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     * 
     * Sets up the Compose UI with navigation between different screens.
     * The navigation graph defines all possible routes in the application.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsApp {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    // Home screen - Main landing page with search functionality
                    composable("home") {
                        HomeScreen(
                            onSearchClick = { query ->
                                navController.navigate("product_search/$query")
                            },
                            onProductClick = { productId ->
                                navController.navigate("product_detail/$productId")
                            },
                            onNavigateToSearch = {
                                navController.navigate("search")
                            }
                        )
                    }
                    
                    // Search screen - Shows search history and suggestions
                    composable("search") {
                        SearchScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onSearchClick = { query ->
                                navController.navigate("product_search/$query")
                            },
                            onProductClick = { productId ->
                                navController.navigate("product_detail/$productId")
                            }
                        )
                    }
                    
                    // Product search results screen - Shows search results with pagination
                    composable("product_search/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        ProductSearchScreen(
                            initialQuery = query,
                            onProductClick = { product ->
                                navController.navigate("product_detail/${product.id}")
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    
                    // Product detail screen - Shows detailed information about a specific product
                    composable("product_detail/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId") ?: ""
                        ProductDetailScreen(
                            productId = productId,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            viewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Root composable that wraps the entire application with Material Design theme.
 * 
 * This composable applies the Material Design 3 theme to all child composables
 * and provides a consistent visual foundation for the app.
 * 
 * @param content The main content of the application
 */
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

/**
 * Preview composable for development and testing purposes.
 * 
 * Shows a preview of the ProductsApp with ProductSearchScreen for design validation.
 */
@Preview(showBackground = true)
@Composable
fun ProductsAppPreview() {
    ProductsApp {
        ProductSearchScreen()
    }
}
