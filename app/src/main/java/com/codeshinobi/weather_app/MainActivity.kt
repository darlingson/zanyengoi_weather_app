package com.codeshinobi.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeshinobi.weather_app.ui.theme.Weather_appTheme
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Weather_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")
                    HomeScreenMain()
                }
            }
        }
    }
}

@Composable
fun HomeScreenMain() {
    Box(
        modifier = with (Modifier){
            fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.blantyre_large),
                    contentScale = ContentScale.FillHeight)

        })
    {
        Column {
            Greeting(name = "Darlingson")
            CurrentTempCard()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentTempCard(){
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDateAndTime = sdf.format(Date())
    val day = Date()

    val dow = SimpleDateFormat("EEEE")
    val d = Date()
    val dayOfTheWeek = dow.format(d)
    Card(
        onClick = { /*TODO*/ },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
        ) {
        Column(Modifier.padding(10.dp)) {
            CurrentTemp(name = "Darlingson")
            Text(text = currentDateAndTime)
            Text(text = dayOfTheWeek)
        }
    }
}
@Composable
fun CurrentTemp(name: String) {
    Box() {
        Text(text = "22 °C", modifier = Modifier.padding(10.dp))
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val sdf = SimpleDateFormat("HH")
    val currentTime = sdf.format(System.currentTimeMillis())
    var greetingText = ""
    if (currentTime.toInt() < 12) {
        greetingText = "Good Morning"
    }
    else if (currentTime.toInt() < 18) {
        greetingText = "Good Afternoon"
    }
    else {
        greetingText = "Good Evening"
    }
    Text(
        text = "$greetingText $name!",
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Weather_appTheme {
        Greeting("Android")
    }
}