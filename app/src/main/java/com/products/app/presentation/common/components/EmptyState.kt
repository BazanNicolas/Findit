package com.products.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.products.app.R

/**
 * Composable component that displays an empty state with customizable content.
 * 
 * This component provides a consistent empty state display across the app with:
 * - Customizable empty state icon (defaults to search icon)
 * - Title and subtitle text for context
 * - Material Design 3 styling with proper color theming
 * - Centered layout with appropriate spacing
 * 
 * The component is typically used when lists or search results are empty,
 * providing clear feedback to users about the current state.
 * 
 * @param modifier Modifier to be applied to the component
 * @param icon Custom empty state icon (uses default search icon if empty)
 * @param title Empty state title text (uses default empty title if empty)
 * @param subtitle Empty state subtitle text (uses default empty subtitle if empty)
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    icon: String = "",
    title: String = "",
    subtitle: String = ""
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = icon.ifEmpty { stringResource(R.string.icon_search) },
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = title.ifEmpty { stringResource(R.string.empty_title) },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle.ifEmpty { stringResource(R.string.empty_subtitle) },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
