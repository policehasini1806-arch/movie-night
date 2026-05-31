package cohort.example.service;

import cohort.example.dto.JoinRoomRequest;
import cohort.example.model.Participant;
import cohort.example.model.Room;
import cohort.example.repository.ParticipantRepository;
import cohort.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Map<String, Object> joinRoom(JoinRoomRequest request) {
        Room room = roomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found with code: " + request.getRoomCode()));

        Participant participant = new Participant();
        participant.setName(request.getName());
        participant.setRoom(room);
        participant = participantRepository.save(participant);

        Map<String, Object> response = new HashMap<>();
        response.put("participantId", participant.getId());
        response.put("participantName", participant.getName());
        response.put("roomId", room.getId());
        response.put("roomCode", room.getRoomCode());
        response.put("roomName", room.getRoomName());
        return response;
    }

    public List<Map<String, Object>> getParticipantsByRoom(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return participantRepository.findByRoomId(room.getId()).stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            return map;
        }).toList();
    }
}
