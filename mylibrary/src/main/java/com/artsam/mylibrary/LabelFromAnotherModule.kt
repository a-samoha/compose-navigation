package com.artsam.mylibrary

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

//@Stable
@Immutable
data class LabelFromAnotherModule(
    val text: String,
)