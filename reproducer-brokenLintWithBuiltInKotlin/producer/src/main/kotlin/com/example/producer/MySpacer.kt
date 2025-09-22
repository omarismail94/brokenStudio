package com.example.producer

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceNode
import androidx.glance.layout.EmittableSpacer

@SuppressLint("RestrictedApi")
@Composable
fun MySpacer(modifier: GlanceModifier = GlanceModifier) {
    GlanceNode(factory = ::EmittableSpacer, update = { this.set(modifier) { this.modifier = it } })
}