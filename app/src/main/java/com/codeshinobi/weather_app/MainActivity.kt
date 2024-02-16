package com.codeshinobi.weather_app

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeshinobi.weather_app.ui.theme.Weather_appTheme
import com.codeshinobi.weather_app.viewmodels.ForecastViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fusedLocationProviderClient = FusedLocationProviderClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if (p0 != null) {
                    super.onLocationResult(p0)
                }
                p0?.lastLocation?.let {
                    val locationByGps = it
                    currentLocation = locationByGps
                    latitude = currentLocation?.latitude
                    longitude = currentLocation?.longitude
                    // use latitude and longitude as per your need
                } ?: {
                    Log.d(TAG, "Location information isn't available.")
                }
            }
        }
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val requestCode = 0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        setContent {
            val viewModel: ForecastViewModel = viewModel()
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
            Row(Modifier.fillMaxWidth()) {
                CurrentTempCard()
                Spacer(Modifier.weight(1f))
                TimeIndicatorIcon()
            }
        }

    }
}
@Composable
fun TimeIndicatorIcon() {
    val sdf = SimpleDateFormat("HH")
    val currentTime = sdf.format(System.currentTimeMillis())
    var greetingText = painterResource(id = R.drawable.morning)
    if (currentTime.toInt() < 12) {
        greetingText = painterResource(id = R.drawable.morning)
    }
    else if (currentTime.toInt() < 16) {
        greetingText = painterResource(id = R.drawable.sun_one)
    }
    else if (currentTime.toInt() < 28) {
        greetingText = painterResource(id = R.drawable.sunset)
    }
    else {
        greetingText = painterResource(id = R.drawable.moon_4214952)
    }
    Card {
        Image(painter = greetingText, contentDescription = "Current time Icon")
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
    val fontSizeLarge = MaterialTheme.typography.titleLarge.fontSize
    val fontSizeMedium = MaterialTheme.typography.titleMedium.fontSize
    Card(
        onClick = { /*TODO*/ },
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.padding(10.dp)
        ) {
        Column(Modifier.padding(10.dp)) {
            showImage()
            CurrentTemp(name = "Darlingson")
            Text(text = currentDateAndTime, fontSize = fontSizeMedium)
            Text(text = dayOfTheWeek, fontSize = fontSizeMedium)
        }
    }
}
@Composable
fun CurrentTemp(name: String) {

    Box() {
        Text(text = "22 °C", style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun showImage(){
    Image(
        painter = painterResource(id = R.drawable.sun),
        contentDescription = "Icon for the temperature",
        modifier = Modifier
            .padding(10.dp)
            .height(100.dp)
    )
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
@Composable
fun ForecastScreen(viewModel: ForecastViewModel) {
    val forecast by viewModel.forecast.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.getForecast()
    }

    // Now you can use 'forecast' in your composable
    Column {
        forecast?.let {
            it.forEach { forecastItem ->
                ForecastCard(forecastItem)
            }
        }
    }
}

@Composable
fun ForecastCard(forecast: Forecast) {
    Card {
        Text(text = forecast.toString())  // Replace this with the actual data you want to display
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Weather_appTheme {
        Greeting("Android")
    }
}