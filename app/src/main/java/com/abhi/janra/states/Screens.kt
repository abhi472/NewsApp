package com.abhi.alleimageviewer.states

sealed class Screens(val route : String) {
    object Pictures : Screens("picture_screen")
}