package com.example.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week3_assignment.ui.screen.SmartHomeScreen
import com.example.week3_assignment.ui.theme.Week3_assignmentTheme
import com.example.week3_assignment.ui.viewmodel.SmartHomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week3_assignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: SmartHomeViewModel = viewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    SmartHomeScreen(
                        uiState = uiState,
                        onSearchQueryChange = viewModel::onSearchQueryChanged,
                        onRoomSelect = viewModel::onRoomSelected,
                        onToggleDevice = viewModel::onToggleDevice
                    )
                }
            }
        }
    }
}
