package guideme.bydgoszcz.pl.pourtheflower.model

data class User(val items: List<Item>)
data class UserUiItem(var items: MutableList<UiItem>)