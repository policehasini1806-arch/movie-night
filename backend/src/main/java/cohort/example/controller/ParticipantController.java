package cohort.example.controller;

import cohort.example.dto.JoinRoomRequest;
import cohort.example.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinRoom(@Valid @RequestBody JoinRoomRequest request) {
        Map<String, Object> result = participantService.joinRoom(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/room/{roomCode}")
    public ResponseEntity<List<Map<String, Object>>> getParticipants(@PathVariable String roomCode) {
        List<Map<String, Object>> participants = participantService.getParticipantsByRoom(roomCode.toUpperCase());
        return ResponseEntity.ok(participants);
    }
}
