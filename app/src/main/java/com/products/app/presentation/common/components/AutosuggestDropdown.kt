package com.products.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.products.app.R
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion

@Composable
fun AutosuggestDropdown(
    suggestions: List<SearchSuggestion>,
    loadingSuggestions: Boolean,
    searchHistory: List<SearchHistory>,
    onSuggestionClick: (String) -> Unit,
    onHistoryClick: (String) -> Unit,
    onSuggestionComplete: (String) -> Unit,
    onHistoryComplete: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (suggestions.isEmpty() && !loadingSuggestions && searchHistory.isEmpty()) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            if (loadingSuggestions) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.loading_suggestions),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    if (searchHistory.isNotEmpty()) {
                        items(searchHistory) { history ->
                            SearchHistoryItem(
                                history = history,
                                onClick = { onHistoryClick(history.query) },
                                onComplete = { onHistoryComplete(history.query) }
                            )
                        }
                    }
                    
                    if (suggestions.isNotEmpty()) {
                        items(suggestions) { suggestion ->
                            AutosuggestItem(
                                suggestion = suggestion,
                                onClick = { onSuggestionClick(suggestion.query) },
                                onComplete = { onSuggestionComplete(suggestion.query) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHistoryItem(
    history: SearchHistory,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_schedule_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = history.query,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        IconButton(
            onClick = onComplete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_insert_24),
                contentDescription = stringResource(R.string.complete_text),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        
    }
}


@Composable
private fun AutosuggestItem(
    suggestion: SearchSuggestion,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        
        val annotatedText = buildAnnotatedString {
            val query = suggestion.query
            val matchStart = suggestion.matchStart
            val matchEnd = suggestion.matchEnd
            
            if (matchStart < matchEnd && matchStart >= 0 && matchEnd <= query.length) {
                append(query.substring(0, matchStart))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(query.substring(matchStart, matchEnd))
                }
                append(query.substring(matchEnd))
            } else {
                append(query)
            }
        }
        
        Text(
            text = annotatedText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        
        if (suggestion.isVerifiedStore) {
            Text(
                text = stringResource(R.string.icon_check),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        IconButton(
            onClick = onComplete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_insert_24),
                contentDescription = stringResource(R.string.complete_text),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
