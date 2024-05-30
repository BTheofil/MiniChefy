package hu.tb.minichefy.data.mapper

import android.net.Uri
import hu.tb.minichefy.domain.model.IconResource

class ImageToIconResource {

    fun map(image: String): IconResource {
        val result = image.toIntOrNull()
        return if (result != null) {
            IconResource.DrawableIconImpl(result)
        } else {
            IconResource.GalleryIconImpl(Uri.parse(image))
        }
    }
}