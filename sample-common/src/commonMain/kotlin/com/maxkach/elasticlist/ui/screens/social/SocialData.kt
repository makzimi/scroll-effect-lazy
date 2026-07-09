package com.maxkach.elasticlist.ui.screens.social

import org.jetbrains.compose.resources.DrawableResource
import `scroll effect lazy`.sample_common.generated.resources.*

data class SocialImage(
    val id: Int,
    val resId: DrawableResource,
    val label: String,
)

val socialStories = listOf(
    SocialImage(
        id = 1,
        resId = Res.drawable.story_01,
        label = "buddy"
    ),
    SocialImage(
        id = 2,
        resId = Res.drawable.story_02,
        label = "luna"
    ),
    SocialImage(
        id = 3,
        resId = Res.drawable.story_03,
        label = "rory"
    ),
    SocialImage(
        id = 4,
        resId = Res.drawable.story_04,
        label = "bao"
    ),
    SocialImage(
        id = 5,
        resId = Res.drawable.story_05,
        label = "coco"
    ),
    SocialImage(
        id = 6,
        resId = Res.drawable.story_06,
        label = "mia"
    ),
    SocialImage(
        id = 7,
        resId = Res.drawable.story_07,
        label = "leo"
    ),
    SocialImage(
        id = 8,
        resId = Res.drawable.story_08,
        label = "kai"
    ),
    SocialImage(
        id = 9,
        resId = Res.drawable.story_09,
        label = "bell"
    ),
    SocialImage(
        id = 10,
        resId = Res.drawable.story_10,
        label = "truffle"
    ),
    SocialImage(
        id = 11,
        resId = Res.drawable.story_11,
        label = "milo"
    ),
    SocialImage(
        id = 12,
        resId = Res.drawable.story_12,
        label = "pepe"
    ),
)

val socialSmallPhotos = listOf(
    SocialImage(
        id = 101,
        resId = Res.drawable.socsmall_01,
        label = "Golden Retriever"
    ),
    SocialImage(
        id = 102,
        resId = Res.drawable.socsmall_02,
        label = "Roadster"
    ),
    SocialImage(
        id = 103,
        resId = Res.drawable.socsmall_03,
        label = "Skyline"
    ),
    SocialImage(
        id = 104,
        resId = Res.drawable.socsmall_04,
        label = "Tabby Cat"
    ),
    SocialImage(
        id = 105,
        resId = Res.drawable.socsmall_05,
        label = "Off-Road SUV"
    ),
    SocialImage(
        id = 106,
        resId = Res.drawable.socsmall_06,
        label = "Harbor"
    ),
    SocialImage(
        id = 107,
        resId = Res.drawable.socsmall_07,
        label = "Border Collie"
    ),
    SocialImage(
        id = 108,
        resId = Res.drawable.socsmall_08,
        label = "Supercar"
    ),
    SocialImage(
        id = 109,
        resId = Res.drawable.socsmall_09,
        label = "Bridge"
    ),
    SocialImage(
        id = 110,
        resId = Res.drawable.socsmall_10,
        label = "Siamese"
    ),
)

val socialBigPhotos = listOf(
    SocialImage(
        id = 201,
        resId = Res.drawable.socbig_01,
        label = "Dolomites"
    ),
    SocialImage(
        id = 202,
        resId = Res.drawable.socbig_02,
        label = "Downtown Sunset"
    ),
    SocialImage(
        id = 203,
        resId = Res.drawable.socbig_03,
        label = "Cable Bridge"
    ),
    SocialImage(
        id = 204,
        resId = Res.drawable.socbig_04,
        label = "Coastline"
    ),
    SocialImage(
        id = 205,
        resId = Res.drawable.socbig_05,
        label = "Nightscape"
    ),
    SocialImage(
        id = 206,
        resId = Res.drawable.socbig_06,
        label = "Valley Trail"
    ),
    SocialImage(
        id = 207,
        resId = Res.drawable.socbig_07,
        label = "Husky Pack"
    ),
    SocialImage(
        id = 208,
        resId = Res.drawable.socbig_08,
        label = "Italian Coupe"
    ),
)
