package guideme.bydgoszcz.pl.pourtheflower.model

data class User(val flowers: List<Flower>)
data class UserUiItem(var flowers: MutableList<FlowerUiItem>)