package com.products.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.products.app.R

/**
 * Composable component that displays a loading state with progress indicator.
 * 
 * This component provides a consistent loading display across the app with:
 * - Circular progress indicator with primary theme color
 * - Optional loading message text
 * - Material Design 3 styling with proper typography
 * - Centered layout with appropriate spacing
 * 
 * The component is typically used during data loading operations to provide
 * visual feedback to users that content is being fetched.
 * 
 * @param modifier Modifier to be applied to the component
 * @param message Loading message text (uses default loading text if empty)
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    message: String = ""
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message.ifEmpty { stringResource(R.string.loading) },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
