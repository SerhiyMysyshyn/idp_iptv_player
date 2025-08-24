package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract

data class MainScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val showAddNewPlayListDialog: Boolean = false,
    val selectedMenuItemIndex: Long = 0
)