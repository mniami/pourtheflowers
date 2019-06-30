package pl.bydgoszcz.guideme.podlewacz.views.fragments.providers

object ShortDesriptionProvider {
    fun provide(description: String): String {
        val maxLength = 100
        if (description.length > maxLength) {
            val idx = description.indexOfAny(charArrayOf(',', '.'), maxLength)
            return if (idx > -1) {
                description.indexOfAny(charArrayOf(',', '.'))
                description.substring(0, idx) + "."
            } else {
                description.substring(0, maxLength)
            }
        }
        return description
    }
}