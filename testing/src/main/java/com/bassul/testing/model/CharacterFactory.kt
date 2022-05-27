package com.bassul.testing.model

import com.example.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when(hero) {
        Hero.ThreeDMan -> Character(
            "3-D Man",
            ".jpg"
        )
        Hero.ABomb -> Character(
            "A-Bomb (HAS)",
            ".jpg"
        )
    }

    sealed class Hero {
        object ThreeDMan : Hero()
        object ABomb : Hero()
    }
}