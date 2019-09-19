package com.nlptech.function.gifsending.send.gif

import com.nlptech.function.gifsending.dataclass.GifItem

class GifItems {
    /**
     * tag : wow
     * locale : en
     * content : {"gif":[{"title":"shocked","id":"tenor_4108687","media":[{"type":"nano","url":"https://media.tenor.com/images/a597d0df1d50d0922cffde500d5b9d65/tenor.gif","dims":[119,90],"size":38621},{"type":"tiny","url":"https://media.tenor.com/images/e82def4b9cb2607a7d0d35dcb96c3793/tenor.gif","dims":[220,165],"size":115890},{"type":"medium","url":"https://media.tenor.com/images/4585d0d4b680afe518b0482709495de6/tenor.gif","dims":[396,298],"size":947004},{"type":"full","url":"https://media.tenor.com/images/6eaab0d39bd1afa7be8985eb7ac2d28b/tenor.gif","dims":[396,298],"size":1143389}]},{"title":"","id":"tenor_5571450","media":[{"type":"nano","url":"https://media.tenor.com/images/25a084d4aa2e79d5ff4ebfc3a5a3c6d4/tenor.gif","dims":[112,90],"size":49873},{"type":"tiny","url":"https://media.tenor.com/images/f9b6ff59de7341b51eab1b6c7c250e91/tenor.gif","dims":[220,176],"size":157097},{"type":"medium","url":"https://media.tenor.com/images/339c8ba17f8e696b5599a689ca1abbac/tenor.gif","dims":[320,256],"size":464219},{"type":"full","url":"https://media.tenor.com/images/e5dab3697b62150a3b5b0f4b1a1f76f5/tenor.gif","dims":[320,256],"size":674705}]},{"title":"","id":"tenor_6103373","media":[{"type":"nano","url":"https://media.tenor.com/images/cbb1bba25d0b280e0dcfe0b355475aee/tenor.gif","dims":[160,90],"size":43817},{"type":"tiny","url":"https://media.tenor.com/images/d9762dbaa0f94f9637e91a8e1da07c80/tenor.gif","dims":[220,123],"size":75912},{"type":"medium","url":"https://media.tenor.com/images/b0e54c6fd1160fdf9385c820cc4e149d/tenor.gif","dims":[498,280],"size":393732},{"type":"full","url":"https://media.tenor.com/images/1dd03671ab0311a6ec446dd1ce4d91a9/tenor.gif","dims":[498,280],"size":579392}]}],"next_offset":3}
     */

    var tag: String? = null
    var locale: String? = null
    var content: ContentBean? = null

    inner class ContentBean {
        /**
         * gif : [{"title":"shocked","id":"tenor_4108687","media":[{"type":"nano","url":"https://media.tenor.com/images/a597d0df1d50d0922cffde500d5b9d65/tenor.gif","dims":[119,90],"size":38621},{"type":"tiny","url":"https://media.tenor.com/images/e82def4b9cb2607a7d0d35dcb96c3793/tenor.gif","dims":[220,165],"size":115890},{"type":"medium","url":"https://media.tenor.com/images/4585d0d4b680afe518b0482709495de6/tenor.gif","dims":[396,298],"size":947004},{"type":"full","url":"https://media.tenor.com/images/6eaab0d39bd1afa7be8985eb7ac2d28b/tenor.gif","dims":[396,298],"size":1143389}]},{"title":"","id":"tenor_5571450","media":[{"type":"nano","url":"https://media.tenor.com/images/25a084d4aa2e79d5ff4ebfc3a5a3c6d4/tenor.gif","dims":[112,90],"size":49873},{"type":"tiny","url":"https://media.tenor.com/images/f9b6ff59de7341b51eab1b6c7c250e91/tenor.gif","dims":[220,176],"size":157097},{"type":"medium","url":"https://media.tenor.com/images/339c8ba17f8e696b5599a689ca1abbac/tenor.gif","dims":[320,256],"size":464219},{"type":"full","url":"https://media.tenor.com/images/e5dab3697b62150a3b5b0f4b1a1f76f5/tenor.gif","dims":[320,256],"size":674705}]},{"title":"","id":"tenor_6103373","media":[{"type":"nano","url":"https://media.tenor.com/images/cbb1bba25d0b280e0dcfe0b355475aee/tenor.gif","dims":[160,90],"size":43817},{"type":"tiny","url":"https://media.tenor.com/images/d9762dbaa0f94f9637e91a8e1da07c80/tenor.gif","dims":[220,123],"size":75912},{"type":"medium","url":"https://media.tenor.com/images/b0e54c6fd1160fdf9385c820cc4e149d/tenor.gif","dims":[498,280],"size":393732},{"type":"full","url":"https://media.tenor.com/images/1dd03671ab0311a6ec446dd1ce4d91a9/tenor.gif","dims":[498,280],"size":579392}]}]
         * next_offset : 3
         */

        var next_offset: Int = 0
        var gif: List<GifItem>? = null
    }
}
