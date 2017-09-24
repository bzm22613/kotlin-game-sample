package work.katashin.util

import org.w3c.dom.WebSocket
import work.katashin.data.*

private val apiHost = "localhost:8080"

fun createWebsocketApiUrl(path: String): String {
    return "ws://$apiHost$path"
}

fun createHttpApiUrl(path: String): String {
    return "http://$apiHost$path"
}

fun WebSocket.sendGameData(gameInternalData: GameInternalData) {
    val messageType = when (gameInternalData) {
        is WaitingData -> MessageType.WAITING
        is ReadyData -> MessageType.READY
        is PlayingData -> MessageType.PLAYING
        is PlayingResultData -> MessageType.PLAYING_RESULT
        is EndResultData -> MessageType.END
    }
    send(JSON.stringify(createGameData(messageType, gameInternalData)))
}

private fun createGameData(messageType: MessageType, message: GameInternalData): GameData {
    return GameData(messageType.type, JSON.stringify(message))
}
