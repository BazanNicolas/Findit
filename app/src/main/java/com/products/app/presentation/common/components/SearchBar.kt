package com.products.app.presentation.common.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.products.app.R

/**
 * A reusable search bar component with Material Design styling.
 * 
 * This composable provides a search input field with a search button and clear functionality.
 * It handles focus management, keyboard actions, and provides visual feedback for user interactions.
 * The component automatically manages focus state and triggers appropriate callbacks.
 * 
 * @param modifier Modifier to be applied to the search bar container
 * @param query Current search query text
 * @param onQueryChange Callback invoked when the query text changes
 * @param onSearchClick Callback invoked when search is triggered (button click or keyboard action)
 * @param onFocusChange Callback invoked when focus state changes (for showing/hiding suggestions)
 * @param placeholder Custom placeholder text (defaults to resource string)
 * @param enabled Whether the search bar is enabled for user interaction
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFocusChange: (Boolean) -> Unit = {},
    placeholder: String = "",
    enabled: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    LaunchedEffect(isFocused) {
        if (isFocused && query.isBlank()) {
            onFocusChange(true)
        } else if (!isFocused && query.isBlank()) {
            onFocusChange(false)
        }
    }
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = placeholder.ifEmpty { stringResource(R.string.search_placeholder) },
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.search_clear),
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick()
                    focusManager.clearFocus()
                }
            ),
            interactionSource = interactionSource,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White.copy(alpha = 0.7f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodySmall
        )
        
        IconButton(
            onClick = {
                onSearchClick()
                focusManager.clearFocus()
            },
            enabled = enabled && query.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_button),
                tint = if (enabled && query.isNotBlank()) 
                    Color.White 
                else 
                    Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
