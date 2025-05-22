package com.kmaslowiec.lookgo.common.utils

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun LoremIpsum.showWords(numberOfWords: Int) =
    LoremIpsum(numberOfWords).values.joinToString("")