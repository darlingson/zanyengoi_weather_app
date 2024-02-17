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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codeshinobi.weather_app.api.HourlyForecast
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
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
        setContent {
            val viewModel: ForecastViewModel = viewModel()
            Weather_appTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenMain(viewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreenMain(ForecastViewModel: ForecastViewModel) {
    LaunchedEffect(Unit) {
        ForecastViewModel.getForecasts()
    }
    var selectedCity by remember { mutableStateOf("Blantyre") }
    var forecasts by remember { mutableStateOf<List<HourlyForecast>?>(null) }
    Box(
        modifier = with(Modifier) {
            fillMaxSize()
                .paint(
//                    painterResource(id = R.drawable.blantyre_large),
                    painter = painterResource(id = getDrawableResourceId(selectedCity)),
                    contentScale = ContentScale.FillHeight
                )

        })
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Greeting(name = "Darlingson")
            CityDropdown(
                onCitySelected = { newCity ->
                    selectedCity = newCity
                }
            )
            Row(Modifier.fillMaxWidth()) {
                CurrentTempCard(ForecastViewModel)
                Spacer(Modifier.weight(1f))
                TimeIndicatorIcon()
            }
            Spacer(modifier = Modifier.weight(1f))
            ForecastScreen(ForecastViewModel)
        }
    }
}
@Composable
fun getDrawableResourceId(city: String): Int {
    val cityDrawableMap = mapOf(
        "Blantyre" to R.drawable.blantyre_large,
        "Zomba" to R.drawable.zomba_large,
        "Lilongwe" to R.drawable.lilongwe_large,
        "Mzuzu" to R.drawable.mzuzu_large,
        "Mangochi" to R.drawable.mangochi_large,
        "Mulanje" to R.drawable.mulanje_large
    )
    return cityDrawableMap[city] ?: R.drawable.blantyre_large
}
@Composable
fun CityDropdown(onCitySelected: (String) -> Unit) {
//    Text(text = "Blantyre")
    val citiesList = listOf<String>("Blantyre", "Zomba", "Lilongwe", "Mzuzu", "Mangochi", "Mulanje")
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    var buttonModifier = Modifier.width(100.dp)

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        DropdownList(
            itemList = citiesList,
            selectedIndex = selectedIndex,
            modifier = buttonModifier,
            onItemClick = {
                selectedIndex = it
                onCitySelected(citiesList[it])
            }
        )
//        Text(text = "You have chosen ${citiesList[selectedIndex]}",
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(3.dp)
//                .fillMaxWidth()
//                .background(Color.LightGray),)
    }
}

@Composable
fun DropdownList(itemList: List<String>, selectedIndex: Int, modifier: Modifier, onItemClick: (Int) -> Unit) {

    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Row {
            Text(text = "Current city: ", modifier = Modifier.padding(3.dp))
            Box(
                modifier = modifier
                    .shadow(10.dp, shape = MaterialTheme.shapes.extraSmall)
                    .background(Color.Gray)
                    .clickable { showDropdown = true },
                //            .clickable { showDropdown = !showDropdown },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = itemList[selectedIndex], modifier = Modifier.padding(3.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // dropdown list
        Box() {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    // to dismiss on click outside
                    onDismissRequest = { showDropdown = false }
                ) {

                    Column(
                        modifier = modifier
                            .heightIn(max = 90.dp)
                            .verticalScroll(state = scrollState)
                            .border(width = 1.dp, color = Color.Gray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        itemList.onEachIndexed { index, item ->
                            if (index != 0) {
                                Divider(thickness = 1.dp, color = Color.LightGray)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color.Green)
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClick(index)
                                        showDropdown = !showDropdown
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item,)
                            }
                        }

                    }
                }
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
    } else if (currentTime.toInt() < 16) {
        greetingText = painterResource(id = R.drawable.sun_one)
    } else if (currentTime.toInt() < 28) {
        greetingText = painterResource(id = R.drawable.sunset)
    } else {
        greetingText = painterResource(id = R.drawable.moon_4214952)
    }
    Card {
        Image(painter = greetingText, contentDescription = "Current time Icon")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentTempCard(ForecastViewModel: ForecastViewModel) {
    val forecasts by ForecastViewModel.forecasts
    var currentTemp by remember { mutableStateOf("22") }
    val sdf_hour = SimpleDateFormat("HH")
    val currentTime = sdf_hour.format(System.currentTimeMillis())
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val currentDateAndTime = sdf.format(Date())

    when {
        forecasts == null -> {
        }

        forecasts!!.isNotEmpty() -> {
            for (forecast in forecasts!!) {
                var forecastTimeDateList = forecast.time.split("T")
                var forecastHourMinList = forecastTimeDateList[1].split(":")
//                if (forecastHourMinList[0] == currentTime) {
//                    currentTemp = forecast.temperature.toString()
//                    Log.d("TAG", "CurrentTemp: $currentTemp, CurrentTime: $currentTime")
//                }
                Log.d("TAG", forecastHourMinList[0])
                Log.d("TAG", currentTime)
                if ( forecastHourMinList[0] == currentTime && forecastTimeDateList[0] == currentDateAndTime) {
                    Log.d("TAG", "They are the same")
                    currentTemp = forecast.temperature.toString()
                    Log.d("TAG", "CurrentTemp: $currentTemp, CurrentTime: $currentTime")
                    break
                }
            }
        }

        else -> {

        }
    }



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
            containerColor = Color.Gray.copy(alpha = 0.2f)
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            Modifier.padding(10.dp)
        ) {
            showImage()
            CurrentTemp(temp = currentTemp)
            Text(text = currentDateAndTime, fontSize = fontSizeMedium)
            Text(text = dayOfTheWeek, fontSize = fontSizeMedium)
        }
    }
}

@Composable
fun CurrentTemp(temp: String) {

    Box() {
        Text(text = temp, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun showImage() {
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
    } else if (currentTime.toInt() < 18) {
        greetingText = "Good Afternoon"
    } else {
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
    val forecasts by viewModel.forecasts

    when {
        forecasts == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }

        forecasts!!.isNotEmpty() -> {
            LazyRow {
                items(forecasts!!.size) { index ->
                    ForecastCard(forecast = forecasts!![index])
                }
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text("Failed to load forecasts")
            }
        }
    }
}

@Composable
fun ForecastCard(forecast: HourlyForecast) {
    val forecastList = forecast.time.split("T")
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.5f)
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = forecastList[0])
            Text(text = forecastList[1])
            TempIcon(forecast.temperature)
            Text(text = "${forecast.temperature} °C")
        }
    }
}

@Composable
fun TempIcon(temperature: Double) {
    if (temperature > 0 && temperature < 10) {
        Image(
            painter = painterResource(id = R.drawable.temperature_cold),
            contentDescription = "Icon for the temperature cold",
            modifier = Modifier
                .padding(10.dp)
                .height(20.dp)
        )
    } else if (temperature > 10 && temperature < 20) {
        Image(
            painter = painterResource(id = R.drawable.temperature_warm),
            contentDescription = "Icon for the temperature moderate",
            modifier = Modifier
                .padding(10.dp)
                .height(20.dp)
        )
    } else if (temperature > 20 && temperature < 30) {
        Image(
            painter = painterResource(id = R.drawable.temperature_hot),
            contentDescription = "Icon for the temperature warm",
            modifier = Modifier
                .padding(10.dp)
                .height(40.dp)
        )
    } else if (temperature > 30) {
        Image(
            painter = painterResource(id = R.drawable.sun),
            contentDescription = "Icon for the temperature hot",
            modifier = Modifier
                .padding(10.dp)
                .height(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Weather_appTheme {
        Greeting("Android")
    }
}
