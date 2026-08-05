package com.serhiimysyshyn.devlightiptvclient.data.mappers

import com.serhiimysyshyn.devlightiptvclient.data.database.entities.PlaylistEntity
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist

internal fun List<PlaylistEntity>.toDomainList(): List<Playlist> = map { it.toDomain() }

internal fun PlaylistEntity.toDomain() = Playlist(
    id = id,
    name = name,
    url = url,
    description = description.orEmpty(),
    createdAt = createdAt,
)

internal fun Playlist.toEntity() = PlaylistEntity(
    name = name,
    url = url,
    description = description,
)
