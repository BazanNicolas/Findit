package com.products.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.products.app.data.local.entity.SearchHistoryEntity
import com.products.app.domain.model.SearchHistory
import org.junit.Test

/**
 * Test suite for SearchHistoryMapper extension functions.
 * 
 * This test class verifies the bidirectional mapping between SearchHistoryEntity
 * (database entity) and SearchHistory domain model. It covers various scenarios including:
 * - Basic field mapping in both directions
 * - Edge cases with empty queries
 * - Special characters and long queries
 * - Timestamp handling (including edge cases)
 * - Round-trip conversion integrity
 * 
 * The tests ensure that the mapper preserves data integrity during
 * entity-domain conversions and handles all edge cases gracefully.
 */
class SearchHistoryMapperTest {

    @Test
    fun `SearchHistoryEntity toDomain should map all fields correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = SearchHistoryEntity(
            query = "iphone 15 pro max",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.query).isEqualTo("iphone 15 pro max")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistoryEntity with empty query should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = SearchHistoryEntity(
            query = "",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.query).isEmpty()
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistoryEntity with special characters should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = SearchHistoryEntity(
            query = "samsung galaxy s24 ultra 256gb azul",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.query).isEqualTo("samsung galaxy s24 ultra 256gb azul")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistoryEntity with very long query should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val longQuery = "This is a very long search query that might exceed normal limits and should be handled correctly by the mapper"
        val entity = SearchHistoryEntity(
            query = longQuery,
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.query).isEqualTo(longQuery)
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistoryEntity with zero timestamp should map correctly`() {

        val entity = SearchHistoryEntity(
            query = "test query",
            timestamp = 0L
        )
        val result = entity.toDomain()
        assertThat(result.query).isEqualTo("test query")
        assertThat(result.timestamp).isEqualTo(0L)
    }

    @Test
    fun `SearchHistoryEntity with negative timestamp should map correctly`() {

        val entity = SearchHistoryEntity(
            query = "test query",
            timestamp = -1000L
        )
        val result = entity.toDomain()
        assertThat(result.query).isEqualTo("test query")
        assertThat(result.timestamp).isEqualTo(-1000L)
    }

    @Test
    fun `SearchHistory toEntity should map all fields correctly`() {

        val timestamp = System.currentTimeMillis()
        val searchHistory = SearchHistory(
            query = "macbook pro m3",
            timestamp = timestamp
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEqualTo("macbook pro m3")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistory with empty query should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val searchHistory = SearchHistory(
            query = "",
            timestamp = timestamp
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEmpty()
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistory with special characters should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val searchHistory = SearchHistory(
            query = "auriculares sony wh-1000xm5 negro",
            timestamp = timestamp
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEqualTo("auriculares sony wh-1000xm5 negro")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistory with very long query should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val longQuery = "This is a very long search query that might exceed normal limits and should be handled correctly by the mapper"
        val searchHistory = SearchHistory(
            query = longQuery,
            timestamp = timestamp
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEqualTo(longQuery)
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `SearchHistory with zero timestamp should map to entity correctly`() {

        val searchHistory = SearchHistory(
            query = "test query",
            timestamp = 0L
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEqualTo("test query")
        assertThat(result.timestamp).isEqualTo(0L)
    }

    @Test
    fun `SearchHistory with negative timestamp should map to entity correctly`() {

        val searchHistory = SearchHistory(
            query = "test query",
            timestamp = -1000L
        )
        val result = searchHistory.toEntity()
        assertThat(result.query).isEqualTo("test query")
        assertThat(result.timestamp).isEqualTo(-1000L)
    }

    @Test
    fun `round trip conversion should preserve data`() {

        val originalEntity = SearchHistoryEntity(
            query = "round trip test query",
            timestamp = System.currentTimeMillis()
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.query).isEqualTo(originalEntity.query)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }

    @Test
    fun `round trip conversion with special characters should preserve data`() {

        val originalEntity = SearchHistoryEntity(
            query = "test query with special chars: @#$%^&*()",
            timestamp = System.currentTimeMillis()
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.query).isEqualTo(originalEntity.query)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }

    @Test
    fun `round trip conversion with empty query should preserve data`() {

        val originalEntity = SearchHistoryEntity(
            query = "",
            timestamp = System.currentTimeMillis()
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.query).isEqualTo(originalEntity.query)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }
}
