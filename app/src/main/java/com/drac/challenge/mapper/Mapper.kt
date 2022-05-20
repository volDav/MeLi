package com.drac.challenge.mapper

import com.drac.challenge.data.network.model.CategoryModel
import com.drac.challenge.data.network.model.DescriptionModel
import com.drac.challenge.data.network.model.ItemModel
import com.drac.challenge.data.network.model.PictureModel
import com.drac.challenge.domain.model.Category
import com.drac.challenge.domain.model.Description
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.model.Picture

fun ItemModel.toDomain() : Item {
    return Item(
        id = id,
        title = title,
        price = price,
        availableQuantity = availableQuantity,
        soldQuantity = soldQuantity,
        thumbnail = thumbnail,
        pictures = pictures?.map { it.toDomain() },
        description = description?.toDomain()
    )
}

fun DescriptionModel.toDomain() : Description {
    return Description(
        text = text,
        plainText = plainText
    )
}

fun PictureModel.toDomain(): Picture {
    return Picture(
        id = id,
        url = url,
        secureUrl = secureUrl
    )
}


fun CategoryModel.toDomain(): Category {
    return Category(
        id = id,
        name = name
    )
}