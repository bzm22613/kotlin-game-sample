package work.katashin.kotlingamebackend.data

data class RoomData(val id:Int, val number:Int)
data class EnterRoomData(val roomId: Int)
data class EnterRoomResultData(val roomId: Int)
data class GameData(val type: String, val messageJson: String)

sealed class GameInternalData
data class WaitingData(val roomId: Int): GameInternalData()
data class ReadyData(val userId: Int, val turnUserId: Int): GameInternalData()
data class PlayingData(val userId: Int, val squareId: Int): GameInternalData()
data class PlayingResultData(val turnUserId:Int, val gameBoard: Array<Int>): GameInternalData()
data class EndResultData(val winUserId: Int): GameInternalData()

enum class MessageType(val type: String) {
    WAITING("WAITING"),
    READY("READY"),
    PLAYING("PLAYING"),
    PLAYING_RESULT("PLAYING_RESULT"),
    END("END"),
}
