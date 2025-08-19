package com.serhiimysyshyn.devlightiptvclient.data.mappers

import com.serhiimysyshyn.devlightiptvclient.data.database.entities.ChannelEntity
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel

fun List<ChannelEntity>.toUIModelList() = this.map { it.toUIModel() }

fun ChannelEntity.toUIModel() = Channel(
    id = this.id,
    name = this.name,
    url = this.url,
    category = this.category,
    isFavorite = this.isFavorite,
    playlistId = this.playlistId
)

fun Channel.toEntity() = ChannelEntity(
    name = this.name,
    url = this.url,
    category = this.category,
    isFavorite = this.isFavorite,
    playlistId = this.playlistId
)