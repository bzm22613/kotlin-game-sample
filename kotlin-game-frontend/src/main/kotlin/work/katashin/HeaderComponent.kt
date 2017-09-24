package work.katashin

import kotlinx.html.div
import kotlinx.html.h1
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent

class HeaderComponent : ReactDOMComponent<HeaderComponent.Props, HeaderComponent.State>() {
    companion object : ReactComponentSpec<HeaderComponent, Props, State>

    init {
        state = State()
    }

    override fun ReactDOMBuilder.render() {
        div {
            h1 {
                +"○×ゲーム with (React + Spring Boot) by Kotlin"
            }
        }
    }

    class Props: RProps()
    class State: RState
}