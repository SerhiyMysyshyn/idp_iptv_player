package com.serhiimysyshyn.devlightiptvclient.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds `ChannelEntity.logoUrl` (the playlist's `tvg-logo`).
 *
 * Written rather than falling back to a destructive migration so saved playlists and favourites
 * survive the upgrade. Existing rows get an empty logo and pick one up the next time their
 * playlist is refreshed; until then the UI shows a generated letter avatar.
 */
internal val migration1To2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE channels ADD COLUMN logoUrl TEXT NOT NULL DEFAULT ''")
    }
}
