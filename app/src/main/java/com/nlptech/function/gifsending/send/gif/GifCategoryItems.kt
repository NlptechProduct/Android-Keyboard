package com.nlptech.function.gifsending.send.gif

class GifCategoryItems {
    var tags: List<GifCategoryItem>? = null

    var locale: String? = null

    override fun toString(): String {
        return "{locale=" + locale +
                ",tags=" + tags
    }

    inner class GifCategoryItem {
        var name: String? = null

        var value: String? = null

        var img: String? = null

        override fun toString(): String {
            return "{name=" + name +
                    ",value=" + value +
                    ",img=" + img
        }
    }
}
