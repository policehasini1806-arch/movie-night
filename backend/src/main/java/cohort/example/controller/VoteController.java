package cohort.example.controller;

import cohort.example.dto.VoteRequest;
import cohort.example.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitVote(@Valid @RequestBody VoteRequest request) {
        Map<String, Object> result = voteService.submitVote(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results/{roomCode}")
    public ResponseEntity<Map<String, Object>> getResults(@PathVariable String roomCode) {
        Map<String, Object> results = voteService.getVoteResults(roomCode.toUpperCase());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/check/{participantId}/{roomCode}")
    public ResponseEntity<Map<String, Object>> checkVoted(
            @PathVariable Long participantId,
            @PathVariable String roomCode) {
        boolean voted = voteService.hasParticipantVoted(participantId, roomCode.toUpperCase());
        return ResponseEntity.ok(Map.of("hasVoted", voted));
    }
}
