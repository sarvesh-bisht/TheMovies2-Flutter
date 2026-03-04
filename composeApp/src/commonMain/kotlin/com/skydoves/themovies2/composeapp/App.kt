package com.skydoves.themovies2.composeapp

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.skydoves.themovies2.shared.PlatformBootstrap

@Composable
fun App() {
  MaterialTheme {
    Text(text = PlatformBootstrap().status())
  }
}
