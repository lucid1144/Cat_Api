package com.example.project.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CoinViewModel: ViewModel() {
    private val apiService = ApiService()

    private var _coins by mutableStateOf<List<Coin>>(emptyList())
    
    var searchQuery by mutableStateOf("")

    var favoriteCoins by mutableStateOf<List<Coin>>(emptyList())
        private set


    val filteredCoins: List<Coin>
        get() = if (searchQuery.isEmpty()) {
            _coins
        } else {
            _coins.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.symbol.contains(searchQuery, ignoreCase = true)
            }
        }

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun getCoinByName(name: String?): Coin? {
        return _coins.find { it.name == name }
    }

    fun toggleFavorite(coin: Coin) {
        if (favoriteCoins.contains(coin)) {
            favoriteCoins = favoriteCoins - coin
            removeFromFavorites(coin)
        } else {
            favoriteCoins = favoriteCoins + coin
        }
    }
    fun removeFromFavorites(coin: Coin) {
        favoriteCoins = favoriteCoins - coin
    }



    fun getCoins() {
        if (_coins.isNotEmpty()) return
        loading = true
        error = null

        viewModelScope.launch {
            try {
                _coins = apiService.getCoins()
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }
}
