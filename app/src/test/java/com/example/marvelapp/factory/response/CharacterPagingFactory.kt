package com.example.marvelapp.factory.response

import com.example.core.domain.model.Character
import com.example.core.domain.model.CharacterPaging
import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse

class CharacterPagingFactory {

    fun create() = CharacterPaging(
        offset = 0,
        total = 2,
        character = listOf(
            Character(
                id = 1512315,
                name = "3-D Man",
                imageUrl = ""
            ),
            Character(
                id = 1000315,
                name = "A-Bomb (HAS)",
                imageUrl = ""
            )
        )
    )
}