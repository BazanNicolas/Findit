package com.products.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.products.app.data.remote.dto.SimpleAutosuggestDto
import com.products.app.data.remote.dto.SimpleSuggestedQueryDto
import com.products.app.domain.model.SearchSuggestion
import org.junit.Test

class SimpleAutosuggestMapperTest {

    @Test
    fun `SimpleAutosuggestDto toDomain should map all suggestions correctly`() {

        val dto = SimpleAutosuggestDto(
            q = "iphone",
            suggested_queries = listOf(
                SimpleSuggestedQueryDto(
                    q = "iphone 15",
                    match_start = 0,
                    match_end = 6,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = "iphone case",
                    match_start = 0,
                    match_end = 6,
                    is_verified_store = true,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = "iphone charger",
                    match_start = 0,
                    match_end = 6,
                    is_verified_store = false,
                    filters = emptyList()
                )
            ),
            filter_logos = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).hasSize(3)
        assertThat(result[0].query).isEqualTo("iphone 15")
        assertThat(result[0].matchStart).isEqualTo(0)
        assertThat(result[0].matchEnd).isEqualTo(6)
        assertThat(result[0].isVerifiedStore).isFalse()
        
        assertThat(result[1].query).isEqualTo("iphone case")
        assertThat(result[1].matchStart).isEqualTo(0)
        assertThat(result[1].matchEnd).isEqualTo(6)
        assertThat(result[1].isVerifiedStore).isTrue()
        
        assertThat(result[2].query).isEqualTo("iphone charger")
        assertThat(result[2].matchStart).isEqualTo(0)
        assertThat(result[2].matchEnd).isEqualTo(6)
        assertThat(result[2].isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleAutosuggestDto with empty suggestions should return empty list`() {

        val dto = SimpleAutosuggestDto(
            q = "test",
            suggested_queries = emptyList(),
            filter_logos = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isEmpty()
    }

    @Test
    fun `SimpleAutosuggestDto with null suggestions should return empty list`() {

        val dto = SimpleAutosuggestDto(
            q = "test",
            suggested_queries = null,
            filter_logos = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isEmpty()
    }

    @Test
    fun `SimpleAutosuggestDto with suggestions containing null queries should filter them out`() {

        val dto = SimpleAutosuggestDto(
            q = "test",
            suggested_queries = listOf(
                SimpleSuggestedQueryDto(
                    q = "valid suggestion",
                    match_start = 0,
                    match_end = 4,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = null, // This should be filtered out
                    match_start = 0,
                    match_end = 4,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = "another valid suggestion",
                    match_start = 0,
                    match_end = 7,
                    is_verified_store = true,
                    filters = emptyList()
                )
            ),
            filter_logos = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).hasSize(2) // Only valid suggestions
        assertThat(result[0].query).isEqualTo("valid suggestion")
        assertThat(result[1].query).isEqualTo("another valid suggestion")
    }

    @Test
    fun `SimpleSuggestedQueryDto toDomain should map all fields correctly`() {

        val dto = SimpleSuggestedQueryDto(
            q = "samsung galaxy s24",
            match_start = 0,
            match_end = 7,
            is_verified_store = true,
            filters = listOf("brand", "model")
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo("samsung galaxy s24")
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(7)
        assertThat(result?.isVerifiedStore).isTrue()
    }

    @Test
    fun `SimpleSuggestedQueryDto with null query should return null`() {

        val dto = SimpleSuggestedQueryDto(
            q = null,
            match_start = 0,
            match_end = 4,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNull()
    }

    @Test
    fun `SimpleSuggestedQueryDto with empty query should return SearchSuggestion`() {

        val dto = SimpleSuggestedQueryDto(
            q = "",
            match_start = 0,
            match_end = 0,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEmpty()
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(0)
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with null match_start should default to 0`() {

        val dto = SimpleSuggestedQueryDto(
            q = "test query",
            match_start = null,
            match_end = 4,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.matchStart).isEqualTo(0)
    }

    @Test
    fun `SimpleSuggestedQueryDto with null match_end should default to query length`() {

        val query = "test query"
        val dto = SimpleSuggestedQueryDto(
            q = query,
            match_start = 0,
            match_end = null,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.matchEnd).isEqualTo(query.length)
    }

    @Test
    fun `SimpleSuggestedQueryDto with null is_verified_store should default to false`() {

        val dto = SimpleSuggestedQueryDto(
            q = "test query",
            match_start = 0,
            match_end = 4,
            is_verified_store = null,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with all null optional fields should use defaults`() {

        val query = "default test"
        val dto = SimpleSuggestedQueryDto(
            q = query,
            match_start = null,
            match_end = null,
            is_verified_store = null,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo(query)
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(query.length)
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with special characters should map correctly`() {

        val dto = SimpleSuggestedQueryDto(
            q = "auriculares sony wh-1000xm5",
            match_start = 0,
            match_end = 11,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo("auriculares sony wh-1000xm5")
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(11)
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with very long query should map correctly`() {

        val longQuery = "This is a very long search suggestion that might exceed normal limits and should be handled correctly by the mapper"
        val dto = SimpleSuggestedQueryDto(
            q = longQuery,
            match_start = 0,
            match_end = 4,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo(longQuery)
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(4)
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with match_end greater than query length should handle correctly`() {

        val query = "short"
        val dto = SimpleSuggestedQueryDto(
            q = query,
            match_start = 0,
            match_end = 10, // Greater than query length
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo(query)
        assertThat(result?.matchStart).isEqualTo(0)
        assertThat(result?.matchEnd).isEqualTo(10) // Should preserve the value as-is
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleSuggestedQueryDto with negative match_start should handle correctly`() {

        val dto = SimpleSuggestedQueryDto(
            q = "test query",
            match_start = -1,
            match_end = 4,
            is_verified_store = false,
            filters = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).isNotNull()
        assertThat(result?.query).isEqualTo("test query")
        assertThat(result?.matchStart).isEqualTo(-1) // Should preserve the value as-is
        assertThat(result?.matchEnd).isEqualTo(4)
        assertThat(result?.isVerifiedStore).isFalse()
    }

    @Test
    fun `SimpleAutosuggestDto with mixed valid and invalid suggestions should filter correctly`() {

        val dto = SimpleAutosuggestDto(
            q = "test",
            suggested_queries = listOf(
                SimpleSuggestedQueryDto(
                    q = "valid suggestion 1",
                    match_start = 0,
                    match_end = 4,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = null, // Invalid
                    match_start = 0,
                    match_end = 4,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = "", // Invalid
                    match_start = 0,
                    match_end = 0,
                    is_verified_store = false,
                    filters = emptyList()
                ),
                SimpleSuggestedQueryDto(
                    q = "valid suggestion 2",
                    match_start = 0,
                    match_end = 4,
                    is_verified_store = true,
                    filters = emptyList()
                )
            ),
            filter_logos = emptyList()
        )
        val result = dto.toDomain()
        assertThat(result).hasSize(3) // All suggestions including empty string
        assertThat(result[0].query).isEqualTo("valid suggestion 1")
        assertThat(result[0].isVerifiedStore).isFalse()
        assertThat(result[1].query).isEmpty() // Empty string is not filtered out
        assertThat(result[1].isVerifiedStore).isFalse()
        assertThat(result[2].query).isEqualTo("valid suggestion 2")
        assertThat(result[2].isVerifiedStore).isTrue()
    }
}
