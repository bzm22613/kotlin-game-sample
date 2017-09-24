package work.katashin.kotlingamebackend.controller

import org.springframework.web.bind.annotation.*
import work.katashin.kotlingamebackend.data.EnterRoomData
import work.katashin.kotlingamebackend.data.EnterRoomResultData
import work.katashin.kotlingamebackend.data.RoomData
import work.katashin.kotlingamebackend.manager.RoomManager

@RestController
@CrossOrigin(origins = arrayOf("*"))
class GameController {

    @GetMapping("roomInfo")
    fun roomInfo() : Array<RoomData> {
        return RoomManager.getRoomInfo()
    }

    @PostMapping("enterRoom")
    fun enterRoom(@RequestBody enterRoomData: EnterRoomData) : EnterRoomResultData {
        return EnterRoomResultData(enterRoomData.roomId)
    }
}