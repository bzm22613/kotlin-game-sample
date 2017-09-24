package work.katashin.kotlingamebackend.manager

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.socket.WebSocketSession
import work.katashin.kotlingamebackend.data.*
import work.katashin.kotlingamebackend.util.sendGameData
import java.util.*

object RoomManager {
    // それぞれの部屋情報
    private val roomList = listOf(Room(), Room(), Room(), Room(), Room())
    // sessionIdとroom numberのmap
    private var sessionIdRoomIdMap = mutableMapOf<String, Int>()

    private val winLineList = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    fun dealWithGameData(session: WebSocketSession, gameData: GameData) {
        when(gameData.type) {
            MessageType.WAITING.type -> {
                val waitingData = jacksonObjectMapper().readValue<WaitingData>(gameData.messageJson)
                enterRoom(session, waitingData.roomId)
            }
            MessageType.PLAYING.type -> {
                val playingData = jacksonObjectMapper().readValue<PlayingData>(gameData.messageJson)
                playGame(session, playingData.squareId)
            }
        }
    }

    fun removeSession(session: WebSocketSession) {
        sessionIdRoomIdMap[session.id]?.let {
            roomList[it].leaveRoom(session)
        }
        sessionIdRoomIdMap.remove(session.id)
    }

    private fun enterRoom(session: WebSocketSession, roomId: Int) {
        sessionIdRoomIdMap.put(session.id, roomId)
        roomList[roomId].enterRoom(session)
    }

    private fun playGame(session: WebSocketSession, squareId: Int) {
        sessionIdRoomIdMap[session.id]?.let { roomId ->
            roomList[roomId].playGame(session, squareId)
        }
    }

    fun getRoomInfo() = Array(roomList.size, { RoomData(it, roomList[it].memberNumber) })

    internal class Room {

        internal data class User(val session: WebSocketSession, val userId: Int)

        private val sessionIdUserDataMap = mutableMapOf<String, User>()

        private val gameBoard = GameBoard()

        private var userId = 0

        fun enterRoom(session: WebSocketSession) {
            sessionIdUserDataMap.put(session.id, User(session, ++userId))
            if (memberNumber == 2) {
                gameBoard.resetBoard()
                val turnUserId = sessionIdUserDataMap.map { entry -> entry.value.userId }[Random().nextInt(2)]
                sessionIdUserDataMap.forEach { entry ->
                    entry.value.session.sendGameData(ReadyData(entry.value.userId, turnUserId))
                }
            }
        }

        fun leaveRoom(session: WebSocketSession) {
            sessionIdUserDataMap.remove(session.id)
        }

        fun playGame(session: WebSocketSession, squareNum: Int) {
            val currentUserId = sessionIdUserDataMap[session.id]?.userId!!
            gameBoard.setSquare(currentUserId, squareNum)
            when {
                gameBoard.isWin(currentUserId) -> {
                    sessionIdUserDataMap.forEach { entry ->
                        entry.value.session.sendGameData(EndResultData(currentUserId))
                    }
                }
                gameBoard.isDraw() -> {
                    sessionIdUserDataMap.forEach { entry ->
                        entry.value.session.sendGameData(EndResultData(0))
                    }
                }
                else -> {
                    val remainUserIdList = sessionIdUserDataMap.filter { it.key != session.id }.map { entry -> entry.value.userId }
                    val turnUserId = remainUserIdList[Random().nextInt(remainUserIdList.size)]
                    sessionIdUserDataMap.filter { it.key != session.id }.forEach { entry ->
                        entry.value.session.sendGameData(PlayingResultData(turnUserId, gameBoard.squareList.toTypedArray()))
                    }
                }
            }
        }

        val memberNumber: Int
          get() = sessionIdUserDataMap.size

        internal class GameBoard {

            private val _squareList = MutableList(9, {0})
            val squareList
                get() = _squareList as List<Int>

            fun setSquare(id: Int, squareNum: Int) {
                _squareList[squareNum] = id
            }

            fun resetBoard() {
                _squareList.forEachIndexed { index, _ ->
                    _squareList[index] = 0
                }
            }

            /*
             * 0 1 2
             * 3 4 5
             * 6 7 8
             *
             * -> 0, 1, 2
             * -> 3, 4, 5,
             * -> 6, 7, 8,
             * -> 0, 3, 6,
             * -> 1, 4, 7,
             * -> 2, 5, 8,
             * -> 0, 4, 8,
             * -> 2, 4, 6,
             * の８パターンの並びが存在
             */
            fun isWin(id: Int): Boolean {
                return winLineList.any { squareList[it[0]] == id && squareList[it[1]] == id && squareList[it[2]] == id }
            }

            fun isDraw(): Boolean {
                return !(squareList.any { it == 0 })
            }
        }
    }
}