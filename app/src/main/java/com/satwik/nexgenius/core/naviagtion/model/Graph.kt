package com.satwik.nexgenius.core.naviagtion.model

sealed class Graph(val route:String){
    object Auth: Graph(route="auth_graph")
}