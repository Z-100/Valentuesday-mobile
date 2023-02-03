package com.z100.valentuesday.util

import com.z100.valentuesday.api.components.Question

class Debug {
    companion object Factory {
        var counter: Long = 0
        var questionList = listOf(
            Question(1L, "Assuming 2 + 2 = 5, how many trees?", 1, "A thousand", "A million", "Negative three"),
            Question(2L, "Is this a question?", 3, "No", "Yes", "Is this an answer?"),
            Question(3L, "How many people were involved in this project?", 1, "One", "Two", "Three"),
            Question(4L, "What's Java?", 2, "A Scripting language", "An island", "A country"),
            Question(5L, "How tall is the Mt. Everest?", 2, "It's 1234m tall", "It's > 8000m tall", "About 20 feet")
        )
    }
}
