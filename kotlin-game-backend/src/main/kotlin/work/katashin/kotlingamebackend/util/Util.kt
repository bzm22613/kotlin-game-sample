package work.katashin.kotlingamebackend.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import work.katashin.kotlingamebackend.data.*

fun WebSocketSession.sendGameData(gameInternalData: GameInternalData) =
    sendMessage(
        TextMessage(
            jacksonObjectMapper().writeValueAsString(
                GameData(
                    when(gameInternalData) {
                        is WaitingData -> MessageType.WAITING.type
                        is ReadyData -> MessageType.READY.type
                        is PlayingData -> MessageType.PLAYING.type
                        is PlayingResultData -> MessageType.PLAYING_RESULT.type
                        is EndResultData -> MessageType.END.type
                    },
                    jacksonObjectMapper().writeValueAsString(gameInternalData)
                )
            )
        )
    )
