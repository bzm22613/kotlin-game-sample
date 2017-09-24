package work.katashin

import react.dom.ReactDOM
import react.dom.render
import kotlin.browser.document
import kotlinx.html.*
import react.RState
import react.ReactComponentNoProps
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.GameResult

fun main(args: Array<String>) {
    runtime.wrappers.require("kotlin-game.css")

    ReactDOM.render(document.getElementById("content")) {
        Application {}
    }
}

class Application : ReactDOMComponent<ReactComponentNoProps, ApplicationState>() {
    companion object : ReactComponentSpec<Application, ReactComponentNoProps, ApplicationState>

    init {
        state = ApplicationState(MainView.Lobby, null, GameResult.UNDEFINED)
    }

    override fun ReactDOMBuilder.render() {
        div {
            HeaderComponent {}
            when (state.selected) {
                MainView.Lobby -> {
                    LobbyViewComponent {
                        goToWaitView = { goToView(MainView.Game) }
                        setRoomId = { setState{roomId = it} }
                    }
                }
                MainView.Game -> {
                    GameViewComponent {
                        goToEndView = { goToView(MainView.End) }
                        gameRoomId = state.roomId
                        setGameResult = { setState{ gameResult = it} }
                    }
                }
                MainView.End -> {
                    EndViewComponent {
                        goToLobbyView = { goToView(MainView.Lobby) }
                        gameResult = state.gameResult
                    }
                }
            }
        }
    }

    private fun goToView(view: MainView) {
        setState {
            selected = view
        }
    }
}

enum class MainView {
    Lobby,
    Game,
    End,
}

class ApplicationState(var selected: MainView, var roomId: Int?, var gameResult: GameResult) : RState
