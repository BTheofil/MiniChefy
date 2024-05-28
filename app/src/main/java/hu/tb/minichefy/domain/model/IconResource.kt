package hu.tb.minichefy.domain.model

import android.net.Uri

sealed interface IconResource {
    val resource: Any

    data class DrawableIconImpl(override val resource: Int) : DrawableIcon
    data class GalleryIconImpl(override val resource: Uri) : GalleryIcon

    interface DrawableIcon : IconResource {
        override val resource: Int
    }
    interface GalleryIcon : IconResource {
        override val resource: Uri
    }
}