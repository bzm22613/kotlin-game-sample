package work.katashin

import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import work.katashin.data.EnterRoomData
import work.katashin.data.EnterRoomResultData
import work.katashin.data.RoomData
import work.katashin.util.createHttpApiUrl
import kotlin.browser.window
import kotlin.js.json

class LobbyViewComponent : ReactDOMComponent<LobbyViewComponent.Props, LobbyViewComponent.State>() {
    companion object : ReactComponentSpec<LobbyViewComponent, Props, State>

    init {
        state = State(listOf())
    }

    private var timerId : Int? = null
    private val period = 5000

    private fun fetchRoomInfo() {
        window.fetch(createHttpApiUrl("/roomInfo"))
            .then({
                it.text().then({
                    val rooms = JSON.parse<Array<RoomData>>(it)
                    setState {
                        roomDataList = rooms.toList()
                    }
                })
            })
    }

    // TODO:部屋の入室の失敗を考えていない
    // kotlinJavaScriptのfetch.then()の中の返り値がPromiseの時もラップしてしまう仕様が面倒
    //
    private fun enterRoom(roomId: Int) {
            window.fetch(
                createHttpApiUrl("/enterRoom"),
                object : RequestInit {
                    override var mode: RequestMode? = RequestMode.CORS
                    override var headers: dynamic = json("Content-Type" to "application/json")
                    override var method: String? = "POST"
                    override var body: dynamic = JSON.stringify(EnterRoomData(roomId))
                })
                .then({
                     it.text().then({
                         // TODO 現状の実装では送信時と受信時のメッセージが同一だが今後変更必要
                         val result = JSON.parse<EnterRoomResultData>(it)
                         props.setRoomId(result.roomId)
                         // TODO 得られたレスポンスから入室可否を書く処理が必要
                         props.goToWaitView()
                    })
                })
        }

    override fun componentDidMount() {
        super.componentDidMount()
        fetchRoomInfo()
        timerId = window.setInterval({ fetchRoomInfo() }, period)
    }

    override fun componentWillUnmount() {
        super.componentWillUnmount()
        timerId?.let {
            if (it > 0) {
                window.clearInterval(it)
            }
        }
    }

    override fun ReactDOMBuilder.render() {
        div {
            h2 {
                +"ここはロビーです"
            }
            state.roomDataList?.forEach { room ->
                div {
                    div("float-div") {
                        +"ルーム番号:${room.id}"
                        span("margin-left") {
                            +"${room.number}/2"
                        }
                    }
                    button {
                        +"入室！"
                        onClickFunction = { enterRoom(room.id) }
                    }
                }
            }
        }
    }

    class Props : RProps() {
        var goToWaitView: () -> Unit = {}
        var setRoomId: (Int) -> Unit = {}
    }
    class State(var roomDataList: List<RoomData>?) : RState
}