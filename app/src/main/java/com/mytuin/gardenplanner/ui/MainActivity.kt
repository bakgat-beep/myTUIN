package com.mytuin.gardenplanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mytuin.gardenplanner.ui.navigation.MyTuinNavHost
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyTuinTheme {
                MyTuinNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}