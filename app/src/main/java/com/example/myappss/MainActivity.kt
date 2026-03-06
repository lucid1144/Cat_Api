package com.example.myappss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatScreen()
        }
    }
}

@Composable
fun CatScreen(vm: CatViewModel = viewModel()) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .background(color = Color.LightGray,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (vm.loading) {
                CircularProgressIndicator()
            }
            else if (vm.error != null) {
                Text("Error: ${vm.error}")
            }
            else if (vm.facts.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(vm.facts.size) { index ->
                        Text(
                            text = vm.facts[index]?.fact ?: "",

                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text("No facts available")
            }
        }
        Button(onClick = { vm.getFacts()}
            , modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)) {
            Text("Get cat facts",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center)
        }
    }

}