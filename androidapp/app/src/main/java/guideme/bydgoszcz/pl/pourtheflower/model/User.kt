package guideme.bydgoszcz.pl.pourtheflower.model

data class User(val flowers: List<Flower>)
data class UserUiItem(val flowers: MutableList<FlowerUiItem>)