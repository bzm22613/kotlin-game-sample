package work.katashin

import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.js.onClickFunction
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.GameResult

class EndViewComponent : ReactDOMComponent<EndViewComponent.Props, EndViewComponent.State>() {
    companion object : ReactComponentSpec<EndViewComponent, Props, State>

    init {
        state = State()
    }

    override fun ReactDOMBuilder.render() {
        div {
            div {
                +when (props.gameResult) {
                    GameResult.WIN -> "あなたの勝ちです！"
                    GameResult.LOSE -> "あなたの負けです！"
                    GameResult.DRAW -> "引き分けです"
                    GameResult.UNDEFINED -> "不明な状態"
                }
            }
            div {
                button {
                    +"ロビーへ戻る"
                    onClickFunction = { props.goToLobbyView() }
                }
            }
        }
    }

    class Props : RProps() {
        var goToLobbyView : () -> Unit = {}
        var gameResult: GameResult = GameResult.UNDEFINED
    }
    class State : RState
}