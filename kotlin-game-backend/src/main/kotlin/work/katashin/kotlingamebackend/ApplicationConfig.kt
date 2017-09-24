package work.katashin.kotlingamebackend

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import work.katashin.kotlingamebackend.handler.GameHandler

@Configuration
@EnableWebSocket
class ApplicationConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry?) {
        registry?.addHandler(GameHandler(), "/game")?.setAllowedOrigins("*")
    }
}