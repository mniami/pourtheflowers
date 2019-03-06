package pl.bydgoszcz.guideme.podlewacz.model

data class User(val items: List<Item>)
data class UserUiItem(var items: MutableList<UiItem>)