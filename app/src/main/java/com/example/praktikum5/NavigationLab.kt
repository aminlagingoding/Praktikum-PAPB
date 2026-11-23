package com.example.praktikum5

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikum5.data.DefaultStudentRepository
import com.example.praktikum5.data.local.DatabaseProvider
import com.example.praktikum5.data.location.DefaultLocationRepository
import com.example.praktikum5.data.preference.UserPreferencesRepository
import com.example.praktikum5.data.remote.RetrofitClient
import com.example.praktikum5.data.remote.StudentRemoteDataSource
import com.example.praktikum5.data.repository.FavoriteRepository
import com.example.praktikum5.screen.AboutScreen
import com.example.praktikum5.screen.DetailScreen
import com.example.praktikum5.screen.FavoritesScreen
import com.example.praktikum5.screen.HomeScreen
import com.example.praktikum5.screen.ProfileScreen
import com.example.praktikum5.ui.theme.Location.LocationScreen
import com.example.praktikum5.ui.theme.Location.LocationViewModel
import com.example.praktikum5.ui.theme.detail.DetailViewModel
import com.example.praktikum5.ui.theme.favorite.FavoritesViewModel
import com.example.praktikum5.ui.theme.home.HomeViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun NavigationLab() {
    val context = LocalContext.current
    val remoteDataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository = remember { DefaultStudentRepository(remoteDataSource) }
    val preferencesRepository = remember {
        UserPreferencesRepository(context.applicationContext)
    }
    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context).favoriteStudentDao()
        )
    }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context.applicationContext)
    }
    val locationRepository = remember {
        DefaultLocationRepository(context.applicationContext, fusedLocationClient)
    }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository, preferencesRepository)
            )
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                searchQuery = searchQuery,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onOpenFavorites = {
                    navController.navigate(Routes.FAVORITES)
                },
                onOpenLocation = {
                    navController.navigate(Routes.LOCATION)
                },
                onOpenAbout = { id ->
                    navController.navigate(Routes.about(id))
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onSearch = homeViewModel::refresh,
                onRefresh = homeViewModel::refresh,
                onRetry = homeViewModel::refresh
            )
        }
        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository)
            )
            val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = favoritesUiState,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.LOCATION) {
            val locationViewModel: LocationViewModel = viewModel(
                factory = LocationViewModel.factory(locationRepository)
            )
            LocationScreen(
                viewModel = locationViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }
            val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()
            DetailScreen(
                uiState = detailUiState,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ABOUT,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            AboutScreen(
                studentId = studentId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
