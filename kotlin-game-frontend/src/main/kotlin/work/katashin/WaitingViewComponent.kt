package work.katashin

import kotlinx.html.div
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.*
import work.katashin.util.sendGameData
import kotlin.browser.window

class WaitingViewComponent : ReactDOMComponent<WaitingViewComponent.Props, WaitingViewComponent.State>() {
    companion object : ReactComponentSpec<WaitingViewComponent, Props, State>

    var isEnter: Boolean = false
    private val period = 1000

    init {
        state = State()
    }

    private fun sendEnterRoomMessage() {
        if (!isEnter) {
            if (props.gameWebSocket?.readyState == WebSocket.OPEN) {
                props.gameWebSocket?.sendGameData(WaitingData(props.gameRoomId!!))
                isEnter = true
            } else {
                window.setTimeout({ sendEnterRoomMessage() }, period)
            }
        }
    }

    override fun componentWillMount() {
        super.componentWillMount()
        props.gameWebSocket?.onmessage = {
            if (it is MessageEvent) {
                receiveMessage(it.data as String)
            }
        }
        sendEnterRoomMessage()
    }

    override fun ReactDOMBuilder.render() {
        div {
            +"対戦相手が来るのを待っています!"
        }
    }

    class State : RState
    class Props : RProps() {
        var goToPlayingView: () -> Unit = {}
        var gameWebSocket: WebSocket? = null
        var gameRoomId: Int? = null
        var setUserId: (Int) -> Unit = {}
        var setTurnUserId: (Int) -> Unit = {}
    }

    private fun receiveMessage(data: String) {
        val gameData = JSON.parse<GameData>(data)
        when (gameData.type){
            MessageType.READY.type -> {
                val readyData = JSON.parse<ReadyData>(gameData.messageJson)
                props.setUserId(readyData.userId)
                props.setTurnUserId(readyData.turnUserId)
                props.goToPlayingView()
            }
        }
    }
}