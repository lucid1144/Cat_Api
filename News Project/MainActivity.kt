package com.example.project.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NewsApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val newsViewModel: NewsViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != "article/{articleId}",
        drawerContent = {
                    ModalDrawerSheet {
                        Text(
                            "News App",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NavigationDrawerItem(
                            label = { Text("Share") },
                            selected = false,
                            icon = { Icon(Icons.Default.Share, contentDescription = "Share") },
                            onClick = { scope.launch { drawerState.close() } })
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = false,
                            icon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            onClick = { scope.launch { drawerState.close() } }
                        )
                }
            }
    ) {
        Scaffold(
            bottomBar = { BottomBar(navController) },
            topBar = {
                TopAppBar(
                    title = {
                        if (currentRoute != "article/{articleId}") {
                            OutlinedTextField(
                                value = newsViewModel.searchQuery,
                                onValueChange = { newsViewModel.searchQuery = it },
                                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                                placeholder = { Text("Search News") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                singleLine = true
                            )
                        } else {
                            Text("Articles")
                        }
                    },
                    navigationIcon = {
                        if (currentRoute != "article/{articleId}") {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Default.Menu, contentDescription = "Menu",
                                    tint = Color.Black, modifier = Modifier.size(25.dp)
                                )
                            }
                        } else {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.Default.ArrowBack, contentDescription = "Back",
                                    tint = Color.Blue, modifier = Modifier.size(25.dp)
                                )
                            }
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
                    HomeScreen(newsViewModel, navController)
                }
                composable("search") {
                    SearchScreen()
                }
                composable("saved") {
                    SavedScreen()
                }
                composable("article/{articleId}") { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getString("articleId")
                    val article = newsViewModel.articles.find { it.title == articleId }
                    if (article != null) {
                        ArticleDetail(article)
                    } else {
                        Text("Article not found")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(vm: NewsViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        vm.getNews()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Top News Updates",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (vm.loading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else if (vm.error != null) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${vm.error}", color = Color.Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(vm.filteredArticles) { article ->
                    NewsCard(article, navController = navController)
                }
            }
        }
    }
}

@Composable
fun NewsCard(article: Article, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier
            .clickable { navController.navigate("article/${article.title}") }
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "urlToImage",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = article.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = article.author ?: "", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = article.source.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = article.publishedAt, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Box(
        modifier = Modifier.fillMaxWidth().height(100.dp)
            .background(color = Color.LightGray,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == "home") {
                navController.navigate("home")
            }
            BottomNavItem(
                icon = Icons.Default.Search,
                label = "Search",
                selected = currentRoute == "search",
                onClick = {
                    navController.navigate("search")
                },
            )
            BottomNavItem(
                icon = Icons.Default.Star,
                label = "Saved",
                selected = currentRoute == "saved",
                onClick = {
                    navController.navigate("saved")
                },
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean, onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
            .size(50.dp),
           /* .background(color = if (selected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(16.dp)),*/
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label,
            tint = if (selected) Color.Black else Color.Gray)
        Text(text = label,
            color = if (selected) Color.Black else Color.Gray)

    }
}

@Composable
fun ArticleDetail(article: Article) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = "urlToImage",
            modifier = Modifier.fillMaxWidth().height(250.dp)
            )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = article.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        Text(text = "By ${article.author ?: "Unknown"}", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Text(text = article.publishedAt, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(16.dp))
        Text(text = article.content ?: "", fontSize = 16.sp, modifier = Modifier.padding(16.dp))
        Text(text = article.description ?: "", fontSize = 16.sp, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            Text(
                text = "Read Full Article",
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    context.startActivity(intent)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Search Screen",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Composable
fun SavedScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Saved Screen",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),)
    }
}
