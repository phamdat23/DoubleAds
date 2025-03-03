package com.itsol.ironsourcelib

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun LayoutShimmerBanner(modifier: Modifier=Modifier){
    Row(
        modifier = modifier
            .shimmer() // <- Affects all subsequent UI elements
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(Color.LightGray),
            )
        }
    }
}

@Composable
fun LayoutShimmerNative(modifier: Modifier=Modifier, size: GoogleENative){
    Column(
        modifier = Modifier
            .shimmer() // <- Affects all subsequent UI elements
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (size == GoogleENative.UNIFIED_MEDIUM) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(Color.LightGray),
            )
        }
    }
}