package com.nlptech.function.gifsending.dataclass

import androidx.recyclerview.widget.DiffUtil

class GifItem {

    /**
     * title : just sayin' hi
     * id : tenor_3950966
     * media : [{"type":"nano","url":"https://media.tenor.com/images/d39683716115bf4968d23162769ff904/tenor.gif","dims":[187,90],"size":17561},{"type":"tiny","url":"https://media.tenor.com/images/78edbb1f8c34b17b20e9e0987914001e/tenor.gif","dims":[220,105],"size":22516},{"type":"medium","url":"https://media.tenor.com/images/597baedc96953110595c3c07eac205ee/tenor.gif","dims":[498,238],"size":222063},{"type":"full","url":"https://media.tenor.com/images/ea9df861113fecec5bb17bf1faa0124e/tenor.gif","dims":[498,239],"size":613887}]
     */

    var title: String? = null
    var id: String? = null
    var media: List<MediaBean>? = null

    fun getUrl(type: String): String {
        for (m in this.media!!) {
            if (m.type == type) {
                return m.url!!
            }
        }
        return media!![0].url!!
    }

    class MediaBean {

        /**
         * type : nano
         * url : https://media.tenor.com/images/d39683716115bf4968d23162769ff904/tenor.gif
         * dims : [187,90]
         * size : 17561
         */

        var type: String? = null
        var url: String? = null
        var size: Int = 0
        var dims: List<Int>? = null

        companion object {
            val TYPE_NANO = "nano"
            val TYPE_TINY = "tiny"
            val TYPE_MEDIUM = "medium"
            val TYPE_FULL = "full"
        }

    }

    override fun toString(): String {
        return "GifItem(title=$title, id=$id, media=$media)"
    }

    object GifItemDiffUtils : DiffUtil.ItemCallback<GifItem>() {
        override fun areItemsTheSame(oldItem: GifItem, newItem: GifItem): Boolean {
            return oldItem.id.equals(oldItem.id)
        }

        override fun areContentsTheSame(oldItem: GifItem, newItem: GifItem): Boolean {
            return oldItem.id.equals(oldItem.id)
        }
    }

}
