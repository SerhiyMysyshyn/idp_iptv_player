package com.serhiimysyshyn.devlightiptvclient.data.mappers

import com.serhiimysyshyn.devlightiptvclient.data.database.entities.ChannelEntity
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel

internal fun List<ChannelEntity>.toDomainList(): List<Channel> = map { it.toDomain() }

internal fun ChannelEntity.toDomain() = Channel(
    id = id,
    name = name,
    url = url,
    category = category,
    isFavorite = isFavorite,
    playlistId = playlistId,
)

internal fun Channel.toEntity() = ChannelEntity(
    name = name,
    url = url,
    category = category,
    isFavorite = isFavorite,
    playlistId = playlistId,
)
