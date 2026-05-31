package cohort.example.controller;

import cohort.example.dto.CreateRoomRequest;
import cohort.example.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Map<String, Object> result = roomService.createRoom(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<Map<String, Object>> getRoom(@PathVariable String roomCode) {
        Map<String, Object> room = roomService.getRoomByCode(roomCode.toUpperCase());
        return ResponseEntity.ok(room);
    }
}
