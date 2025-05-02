package kz.qamshy.app.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging
import kz.qamshy.app.R
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.components.global.NoInternetPage
import kz.qamshy.app.ui.screens.CategoryScreen
import kz.qamshy.app.ui.screens.HomeScreen
import kz.qamshy.app.ui.screens.NewsScreen
import kz.qamshy.app.ui.screens.SearchScreen
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.QamshyTheme
import kz.qamshy.app.ui.theme.selectedColor
import kz.qamshy.app.ui.theme.tabTextColor
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.common.ConnectivityObserver
import kz.qamshy.app.common.ConnectivityStatus
import kz.qamshy.app.common.Cyrl2LatynHelper
import kz.qamshy.app.common.Cyrl2ToteHelper
import com.example.mylibrary.ThemeHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.common.Translator
import kz.qamshy.app.ui.theme.darkBac
import kz.qamshy.app.viewmodels.CategoryViewModel
import kz.qamshy.app.viewmodels.CurrencyViewModel
import kz.qamshy.app.viewmodels.NewsViewModel
import kz.qamshy.app.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    private val categoryViewModel: CategoryViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val newsViewModel : NewsViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by viewModel()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QamshyTheme(
            ){
                var isSuccess by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val currentLanguage by QamshyApp.currentLanguage.collectAsState()

                val themeHelper = ThemeHelper(context)
                val isDarkMode = themeHelper.isDarkModeEnabled()
                val systemUiController = rememberSystemUiController()
                Translator.loadLanguagePack(currentLanguage){ success ->
                    if(success){
                        isSuccess = true
                    }else{

                    }
                }
                SideEffect {
                    if(isDarkMode){
                        systemUiController.setStatusBarColor(
                            color = Color.Transparent,
                            darkIcons = false,
                        )
                    }else{
                        systemUiController.setStatusBarColor(
                            color = Color.Transparent,
                            darkIcons = true,
                        )
                    }

                }

                if(isSuccess){
                    NavigationMain(currentLanguage,homeViewModel)
                }


            }

        }


    }

    override fun onStop() {
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun NavigationMain(currentLanguage: LanguageModel, homeViewModel:HomeViewModel) {
        val navController = rememberNavController()
        val context = LocalContext.current
        val themeHelper = remember { ThemeHelper(context) }
        val isDarkMode = themeHelper.isDarkModeEnabled()
        if(isDarkMode){
            QamshyApp.updateThemeType("dark")
        }else{
            QamshyApp.updateThemeType("light")
        }
        val bacColor = if (isDarkMode) darkBac else Color(0xFFFFFFFF)
        val connectivityObserver = remember { ConnectivityObserver(context) }
        val status by connectivityObserver.status.collectAsState(initial = ConnectivityStatus.Available)
        Box(modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    CustomBottomAppBar(navController,currentLanguage,isDarkMode)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                        .padding(top = 0.dp)
                ) {
                    when(status){
                        ConnectivityStatus.Available -> {
                            composable("home") { HomeScreen(context,isDarkMode,homeViewModel,currencyViewModel,bacColor) }
                            composable("category") { CategoryScreen(context,isDarkMode,categoryViewModel,currentLanguage,bacColor) }
                            composable("search") {  SearchScreen(context,isDarkMode,searchViewModel,bacColor) }
                            composable("news") { NewsScreen(context,isDarkMode,newsViewModel,bacColor) }
                        }
                        ConnectivityStatus.Unavailable -> {
                            composable("home") {  NoInternetPage() }
                            composable("category") {  NoInternetPage() }
                            composable("search") {  NoInternetPage() }
                            composable("news") {NoInternetPage()  }
                        }
                    }
                }
            }

        }
    }


    @Composable
    fun CustomBottomAppBar(navController: NavHostController,currentLanguage:LanguageModel,isDarkMode:Boolean) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        val homeText = when(currentLanguage.languageCulture){
                "tote" -> Cyrl2ToteHelper.cyrl2Tote("Басбет")
                "latyn" -> Cyrl2LatynHelper.cyrl2Latyn("Басбет")
            else -> T("ls_Home",currentLanguage)
        }
        val categoryText = when(currentLanguage.languageCulture){
            "tote" -> Cyrl2ToteHelper.cyrl2Tote("Категория")
            "latyn" -> Cyrl2LatynHelper.cyrl2Latyn("Категория")
            else -> T("ls_Category",currentLanguage)
        }
        val searchText = when(currentLanguage.languageCulture){
            "tote" -> Cyrl2ToteHelper.cyrl2Tote("Іздеу")
            "latyn" -> Cyrl2LatynHelper.cyrl2Latyn("Іздеу")
            else -> T("ls_Search",currentLanguage)
        }
        val newsText = when(currentLanguage.languageCulture){
            "tote" -> Cyrl2ToteHelper.cyrl2Tote("Жаңалықтар")
            "latyn" -> Cyrl2LatynHelper.cyrl2Latyn("Жаңалықтар")
            else -> T("ls_News",currentLanguage)
        }
        val bacColor = if (isDarkMode) darkBac else Color(0xFFFFFFFF)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(89.dp)
                .background(color = bacColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(currentLanguage.languageCulture != "tote"){
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.home_icon,
                            iconResSelected = R.drawable.home_selected,
                            label = homeText,
                            selected = currentRoute == "home",
                            onClick = { navController.navigateSingleTop("home") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.category_icon,
                            iconResSelected = R.drawable.category_selected,
                            label = categoryText,
                            selected = currentRoute == "category",
                            onClick = { navController.navigateSingleTop("category") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.search_icon,
                            iconResSelected = R.drawable.search_selected,
                            label =searchText ,
                            selected = currentRoute == "search",
                            onClick = { navController.navigateSingleTop("search") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.news_icon,
                            iconResSelected = R.drawable.news_selected,
                            label = newsText,
                            selected = currentRoute == "news",
                            onClick = { navController.navigateSingleTop("news") }
                        )
                    }
                }else{
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.news_icon,
                            iconResSelected = R.drawable.news_selected,
                            label = newsText,
                            selected = currentRoute == "news",
                            onClick = { navController.navigateSingleTop("news") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.search_icon,
                            iconResSelected = R.drawable.search_selected,
                            label =searchText ,
                            selected = currentRoute == "search",
                            onClick = { navController.navigateSingleTop("search") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.category_icon,
                            iconResSelected = R.drawable.category_selected,
                            label = categoryText,
                            selected = currentRoute == "category",
                            onClick = { navController.navigateSingleTop("category") }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButtonWithLabel(
                            iconRes = R.drawable.home_icon,
                            iconResSelected = R.drawable.home_selected,
                            label = homeText,
                            selected = currentRoute == "home",
                            onClick = { navController.navigateSingleTop("home") }
                        )
                    }
                }
            }
        }

    }

    @Composable
    fun IconButtonWithLabel(
        iconRes: Int,
        iconResSelected: Int,
        label: String,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val colorScheme = MaterialTheme.colorScheme
        val color by remember(selected) {
            mutableStateOf(if (selected)  selectedColor else tabTextColor)
        }

        val labelColor = if (selected) selectedColor else tabTextColor
        val labelStyle =  TextStyle(
            fontSize = 11.sp,
            fontFamily = PrimaryFontFamily,
            fontWeight = FontWeight(500),
            color = labelColor,
            textAlign = TextAlign.Center
            )

        val icon by remember(selected) {
            mutableIntStateOf(if (selected) iconResSelected else iconRes)
        }

        var isPressed by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "IconButtonScaleAnimation"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                            onClick()
                        }
                    )
                }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id= icon),
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(text = label, style = labelStyle)
        }
    }


    private fun NavHostController.navigateSingleTop(route: String) {
        navigate(route) {
            popUpTo(graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}