

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConcatenateApp()
        }
    }
}

@Composable
fun ConcatenateApp() {

    var firstText by remember {
        mutableStateOf("")
    }

    var secondText by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            value = firstText,
            onValueChange = {
                firstText = it
            },
            label = {
                Text("First Input")
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = secondText,
            onValueChange = {
                secondText = it
            },
            label = {
                Text("Second Input")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                result = firstText + secondText
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Concatenate")
        }

        Text(
            text = result
        )
    }
}





