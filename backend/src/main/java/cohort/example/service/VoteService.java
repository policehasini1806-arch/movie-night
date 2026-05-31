package cohort.example.service;

import cohort.example.dto.VoteRequest;
import cohort.example.model.Participant;
import cohort.example.model.Room;
import cohort.example.model.Vote;
import cohort.example.repository.ParticipantRepository;
import cohort.example.repository.RoomRepository;
import cohort.example.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Map<String, Object> submitVote(VoteRequest request) {
        Room room = roomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Participant participant = participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        if (voteRepository.existsByParticipantIdAndRoomId(participant.getId(), room.getId())) {
            throw new RuntimeException("Participant has already voted in this room");
        }

        Vote vote = new Vote();
        vote.setGenre(request.getGenre());
        vote.setParticipant(participant);
        vote.setRoom(room);
        voteRepository.save(vote);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Vote submitted successfully");
        response.put("genre", request.getGenre());
        return response;
    }

    public Map<String, Object> getVoteResults(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<Object[]> rawResults = voteRepository.countVotesByGenreForRoom(room.getId());

        Map<String, Long> genreVotes = new LinkedHashMap<>();
        for (Object[] row : rawResults) {
            genreVotes.put((String) row[0], (Long) row[1]);
        }

        String winnerGenre = genreVotes.isEmpty() ? null : genreVotes.entrySet().iterator().next().getKey();

        Map<String, Object> response = new HashMap<>();
        response.put("roomCode", roomCode);
        response.put("roomName", room.getRoomName());
        response.put("genreVotes", genreVotes);
        response.put("winnerGenre", winnerGenre);
        response.put("totalVotes", genreVotes.values().stream().mapToLong(Long::longValue).sum());
        return response;
    }

    public boolean hasParticipantVoted(Long participantId, String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return voteRepository.existsByParticipantIdAndRoomId(participantId, room.getId());
    }
}
