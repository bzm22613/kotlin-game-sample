package work.katashin

import kotlinx.html.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.*
import work.katashin.util.sendGameData

class PlayingViewComponent : ReactDOMComponent<PlayingViewComponent.Props, PlayingViewComponent.State>() {
    companion object : ReactComponentSpec<PlayingViewComponent, Props, State>

    init {
        state = State(props.turnUserId!!, Array(9, {0}))
    }

    override fun componentWillMount() {
        super.componentWillMount()
        props.gameWebSocket?.onmessage = {
            if (it is MessageEvent) {
                receiveMessage(it.data as String)
            }
        }
    }

    override fun ReactDOMBuilder.render() {
        div {
            div {
                +if (props.userId == state.turnUserId) { "あなたのターンです○を置いてください" } else { "相手のターンです" }
            }
            div {
                state.gameBoard.forEachIndexed { index, el ->
                    div("square") {
                        when (el) {
                            props.userId -> {
                                div("circle") {}
                            }
                            0 -> {
                                onClickFunction = { handleClickSquare(index) }
                            }
                            else -> {
                                div("cross") {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun receiveMessage(text: String) {
        val gameData = JSON.parse<GameData>(text)
        when (gameData.type) {
            MessageType.PLAYING_RESULT.type -> {
                val playingResultData = JSON.parse<PlayingResultData>(gameData.messageJson)
                setState {
                    turnUserId = playingResultData.turnUserId
                    gameBoard = playingResultData.gameBoard
                }
            }
            MessageType.END.type -> {
                val endResultData = JSON.parse<EndResultData>(gameData.messageJson)
                when {
                    endResultData.winUserId == props.userId -> {
                        props.setGameResult(GameResult.WIN)
                    }
                    endResultData.winUserId == 0 -> {
                        props.setGameResult(GameResult.DRAW)
                    }
                    else -> {
                        props.setGameResult(GameResult.LOSE)
                    }
                }
                props.goToEndView()
            }
        }
    }

    private fun handleClickSquare(squareId: Int) {
        if (state.turnUserId != props.userId) {
            return
        }
        setState {
            // TODO turnUserIdはとりあえず自身と違うものにしておけばよいが分かりづらいので何とかしたい
            turnUserId++
            gameBoard = let {
                state.gameBoard[squareId] = props.userId!!
                state.gameBoard
            }
        }
        props.gameWebSocket?.sendGameData(PlayingData(props.userId!!, squareId))
    }

    class Props : RProps() {
        var goToEndView : () -> Unit = {}
        var gameWebSocket : WebSocket? = null
        var userId : Int? = null
        var turnUserId : Int? = null
        var setGameResult : (GameResult) -> Unit = {}
    }
    class State(var turnUserId: Int, var gameBoard: Array<Int>) : RState
}