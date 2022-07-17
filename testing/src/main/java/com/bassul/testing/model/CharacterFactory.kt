package com.bassul.testing.model

import com.example.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when(hero) {
        Hero.ThreeDMan ->Character(
            id = 1512315,
            name = "3-D Man",
            imageUrl = ""
        )
        Hero.ABomb ->  Character(
                id = 1000315,
                name = "A-Bomb (HAS)",
                imageUrl = ""
            )
    }

    sealed class Hero {
        object ThreeDMan : Hero()
        object ABomb : Hero()
    }
}