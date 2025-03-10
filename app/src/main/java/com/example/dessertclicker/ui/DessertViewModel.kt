package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.data.DessertUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    private val _leftDessertUiState = MutableStateFlow(DessertUiState())
    private val _rightDessertUiState = MutableStateFlow(DessertUiState())
    val leftDessertUiState: StateFlow<DessertUiState> = _leftDessertUiState.asStateFlow()
    val rightDessertUiState: StateFlow<DessertUiState> = _rightDessertUiState.asStateFlow()

    private val _totalDessertsSold = MutableStateFlow(0)
    val totalDessertsSold: StateFlow<Int> = _totalDessertsSold.asStateFlow()

    private val _totalRevenue = MutableStateFlow(0)
    val totalRevenue: StateFlow<Int> = _totalRevenue.asStateFlow()

    init {
        combine(_leftDessertUiState, _rightDessertUiState) { left, right ->
            left.dessertsSold + right.dessertsSold
        }.onEach { _totalDessertsSold.value = it }
            .launchIn(viewModelScope)

        combine(_leftDessertUiState, _rightDessertUiState) { left, right ->
            left.revenue + right.revenue
        }.onEach { _totalRevenue.value = it }
            .launchIn(viewModelScope)
    }

    fun updateLeftDessertState() {
        _leftDessertUiState.update { currentUiState ->
            val dessertsSold = currentUiState.dessertsSold + 1
            val revenue = currentUiState.revenue + currentUiState.currentDessertPrice
            val nextDessertIndex = determineDessertIndexBasedOnDessertsSold(dessertsSold)
            currentUiState.copy(
                dessertsSold = dessertsSold,
                revenue = revenue,
                currentDessertIndex = nextDessertIndex,
                currentDessertPrice = dessertList[nextDessertIndex].price,
                currentDessertImageId = dessertList[nextDessertIndex].imageId,
            )
        }
    }

    fun updateRightDessertState() {
        _rightDessertUiState.update { currentUiState ->
            val dessertsSold = currentUiState.dessertsSold + 1
            val revenue = currentUiState.revenue + currentUiState.currentDessertPrice
            val nextDessertIndex = determineDessertIndexBasedOnDessertsSold(dessertsSold)
            currentUiState.copy(
                dessertsSold = dessertsSold,
                revenue = revenue,
                currentDessertIndex = nextDessertIndex,
                currentDessertPrice = dessertList[nextDessertIndex].price,
                currentDessertImageId = dessertList[nextDessertIndex].imageId,
            )
        }
    }

    private fun determineDessertIndexBasedOnDessertsSold(dessertsSold: Int): Int {
        var dessertToShow = 0
        for (dessert in dessertList) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessertList.indexOf(dessert)
            } else {
                break
            }
        }
        return dessertToShow
    }
}
