package work.katashin

import org.w3c.dom.WebSocket
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.GameResult
import work.katashin.util.createWebsocketApiUrl

class GameViewComponent : ReactDOMComponent<GameViewComponent.Props, GameViewComponent.State>() {

    companion object : ReactComponentSpec<GameViewComponent, Props, State>

    var gameAllWebSocket: WebSocket? = null

    init {
        state = State(GameView.Waiting, null, null, null)
        gameAllWebSocket = WebSocket(createWebsocketApiUrl("/game"))
    }

    override fun ReactDOMBuilder.render() {
        when (state.gameView) {
            GameView.Waiting -> {
                WaitingViewComponent {
                    goToPlayingView = { setState { gameView = GameView.Playing } }
                    gameWebSocket = gameAllWebSocket
                    gameRoomId = props.gameRoomId
                    setUserId = { setState { userId = it } }
                    setTurnUserId = { setState { turnUserId = it } }
                }
            }
            GameView.Playing -> {
                PlayingViewComponent {
                    goToEndView = props.goToEndView
                    gameWebSocket = gameAllWebSocket
                    userId = state.userId
                    turnUserId = state.turnUserId
                    setGameResult = props.setGameResult
                }
            }
        }
    }

    override fun componentWillUnmount() {
        gameAllWebSocket?.close()
    }

    enum class GameView {
        Waiting,
        Playing,
    }

    class Props: RProps() {
        var goToEndView: () -> Unit = {}
        var gameRoomId: Int? = null
        var setGameResult: (GameResult) -> Unit = {}
    }
    class State(var gameView: GameView, var userId: Int?, var turnUserId: Int?, var winUserId: Int?): RState
}