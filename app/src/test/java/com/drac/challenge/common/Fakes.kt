package com.drac.challenge.common

import com.drac.challenge.data.network.model.DescriptionModel
import com.drac.challenge.data.network.model.ItemModel
import com.drac.challenge.data.network.model.PictureModel
import com.drac.challenge.domain.model.Item
import com.drac.challenge.mapper.toDomain

val fakeId = "MCO658984720"

val fakePictures = listOf(
    PictureModel(
        id = "846560-MLA45261120820_032021",
        url = "http://http2.mlstatic.com/D_846560-MLA45261120820_032021-O.jpg",
        secureUrl = "https://http2.mlstatic.com/D_846560-MLA45261120820_032021-O.jpg"
    )
)

val itemFakeWithoutPictures = ItemModel(
    "MCO658984720",
    "Audífonos Inalámbricos Sony Wh-ch510 Negro",
    169900L,
    50,
    500,
    "http://http2.mlstatic.com/D_846560-MLA45261120820_032021-I.jpg"
)

val itemFakeWithPictures = ItemModel(
    "MCO658984720",
    "Audífonos Inalámbricos Sony Wh-ch510 Negro",
    169900L,
    50,
    500,
    "http://http2.mlstatic.com/D_846560-MLA45261120820_032021-I.jpg",
    pictures = fakePictures
)


val descriptionFake = DescriptionModel(
    text = "",
    plainText = "Sony, sin lugar a dudas es una de las marcas más reconocidas en el mundo por la fabricación de dispositivos de audio. Su gama de audífonos se caracteriza por brindar siempre una gran experencia de uso en sus usuarios y ofrecer una alta calidad en todos los componentes de sus reproductores. Esto hace que puedas notar un gran sonido desde su primer uso. El formato perfecto para ti Al ser on-ear se apoyan en tus orejas cómodamente y ofrecen una gran calidad de sonido. Úsalos en viajes largos o actividades al aire libre. Bluetooth de última generación Con la versión de bluetooth 5.0 tienes un montón de beneficios para aprovechar. En comparación a su antecesor BT 4.2, obtendrás velocidades de transmisión de hasta 2.2 Mbps de datos y alcanzarás una distancia máxima de 200 metros de conexión. Pero una de las novedades más sobresalientes es que con su modo dual tendrás la posibilidad de reproducir audio al mismo tiempo en dos dispositivos diferentes."
)


val itemFakeWithPicturesAndDesc_Domain = Item(
    "MCO658984720",
    "Audífonos Inalámbricos Sony Wh-ch510 Negro",
    169900L,
    50,
    500,
    "http://http2.mlstatic.com/D_846560-MLA45261120820_032021-I.jpg",
    pictures = fakePictures.map { it.toDomain() },
    description = descriptionFake.toDomain()
)


val itemFakeWithPictures_Domain = Item(
    "MCO658984720",
    "Audífonos Inalámbricos Sony Wh-ch510 Negro",
    169900L,
    50,
    500,
    "http://http2.mlstatic.com/D_846560-MLA45261120820_032021-I.jpg",
    pictures = fakePictures.map { it.toDomain() }
)


