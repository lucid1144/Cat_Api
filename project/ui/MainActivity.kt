package com.example.project.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApplication() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val coinViewModel: CoinViewModel = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "CoinMarket",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp)
                )
                Spacer(Modifier.height(20.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Profile", fontWeight = FontWeight.Bold) },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = {
                        navController.navigate("Profile")
                        scope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Settings", fontWeight = FontWeight.Bold) },
                    selected = false,
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    onClick = {
                        navController.navigate("Settings")
                        scope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Details", fontWeight = FontWeight.Bold) },
                    selected = false,
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    onClick = {
                        navController.navigate("details/Bitcoin") 
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = { BottomBar(navController) },
            topBar = {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = coinViewModel.searchQuery,
                            onValueChange = { coinViewModel.searchQuery = it },
                            placeholder = { Text("Search coins...") },
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                            singleLine = true
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") {
                    HomeScreen(coinViewModel, navController)
                }
                composable("details/{coinName}") { backStackEntry ->
                    val coinName = backStackEntry.arguments?.getString("coinName")
                    val coin = coinViewModel.getCoinByName(coinName)
                    if (coin != null) {
                        CoinDetailScreen(coin = coin, vm = coinViewModel)
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Coin not found")
                        }
                    }
                }
                composable("Settings") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Settings")
                    }
                }
                composable("Search") {
                    SearchScreen()
                }
                composable("Favorite") {
                    FavoriteScreen(navController = navController, vm = coinViewModel)
                }
                composable("Profile") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Profile")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(vm: CoinViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        vm.getCoins()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Coin Lists",
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp)
        )
        Spacer(Modifier.height(20.dp))

        if (vm.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (vm.error != null) {
            Text(
                text = "Error: ${vm.error}",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(vm.filteredCoins) { coin ->
                CoinCard(coin, navController)
            }
        }
    }
}

@Composable
fun CoinCard(coin: Coin, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("details/${coin.name}") }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coin.image,
                contentDescription = coin.name,
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = coin.symbol.uppercase(),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${coin.price}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
            .background(color = Color.LightGray,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(start = 60.dp, end = 60.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,


        ) {
            IconButton(onClick = {navController.navigate("home") }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
                    Text(text = "Home", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate("Search") }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                    Text(text = "Search", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate("Favorite") }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.Black)
                    Text(text = "Hot", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CoinDetailScreen(coin: Coin, vm: CoinViewModel) {
    val isFavorite = vm.favoriteCoins.contains(coin)
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = coin.image,
            contentDescription = coin.name,
            modifier = Modifier.size(120.dp).padding(top = 32.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = coin.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = coin.symbol.uppercase(),
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.AddCircle,
                    contentDescription = "Toggle favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.clickable { 
                        vm.toggleFavorite(coin)
                    }
                )
                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Price: $${coin.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

        }
        Text(
            text = "Price Change: ${coin.price_change_percentage_24h}%",
            fontSize = 16.sp)
        Text(
            text = "Market Cap: $${coin.market_cap}",
            fontSize = 16.sp)
    }
}
