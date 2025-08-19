package com.serhiimysyshyn.devlightiptvclient.data.mappers

import com.serhiimysyshyn.devlightiptvclient.data.database.entities.PlaylistEntity
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist

fun List<PlaylistEntity>.toUIModelList() = this.map { it.toUIModel() }

fun PlaylistEntity.toUIModel() = Playlist(
    id = this.id,
    name = this.name,
    url = this.url,
    description = this.description ?: "",
    createdAt = this.createdAt
)

fun Playlist.toEntity() = PlaylistEntity(
    name = this.name,
    url = this.url,
    description = this.description
)