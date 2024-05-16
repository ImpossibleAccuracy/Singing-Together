package org.singing.app.domain.repository.track

import com.singing.audio.player.model.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.singing.app.domain.model.RecentTrack
import org.singing.app.domain.repository.StateRepository

fun generateString(size: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..size)
        .map { allowedChars.random() }
        .joinToString("")
}

class RecentTrackRepository : StateRepository<RecentTrack>(DefaultItems) {
    companion object {
        private val DefaultItems: List<RecentTrack> = (0..7)
            .map { i ->
                RecentTrack(
                    audioFile = createTestFile(),
                    isFavourite = i % 3 == 0
                )
            }
    }


    fun getRecentTracks(): Flow<List<RecentTrack>> {
        return items
    }

    fun getFavouriteTracks(): Flow<List<RecentTrack>> {
        return items.map { list ->
            list.filter {
                it.isFavourite
            }
        }
    }

    suspend fun updateRecentTrackFavourite(track: RecentTrack, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            if (track.isFavourite == isFavourite) return@withContext

            updateSingle(track) {
                it.copy(
                    isFavourite = isFavourite,
                )
            }
        }
}

expect fun createTestFile(): AudioFile
