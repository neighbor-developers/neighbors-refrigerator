package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShowDialogViewModel: ViewModel() {
    private val _showDialog = MutableStateFlow ( false )
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onOpenDialogClicked(){
        _showDialog.value = true
    }

    fun onDialogConfirm(){
        _showDialog.value = false
    }

    fun onmDialogDismiss(){
        _showDialog.value = false
    }
}