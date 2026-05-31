package cohort.example.service;

import cohort.example.dto.CreateRoomRequest;
import cohort.example.model.Participant;
import cohort.example.model.Room;
import cohort.example.repository.ParticipantRepository;
import cohort.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Map<String, Object> createRoom(CreateRoomRequest request) {
        String roomCode = generateUniqueRoomCode();

        Room room = new Room();
        room.setRoomCode(roomCode);
        room.setRoomName(request.getRoomName());
        room = roomRepository.save(room);

        // Add host as first participant
        Participant host = new Participant();
        host.setName(request.getHostName());
        host.setRoom(room);
        host = participantRepository.save(host);

        Map<String, Object> response = new HashMap<>();
        response.put("roomCode", roomCode);
        response.put("roomName", room.getRoomName());
        response.put("roomId", room.getId());
        response.put("participantId", host.getId());
        response.put("participantName", host.getName());
        return response;
    }

    public Map<String, Object> getRoomByCode(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));

        List<Participant> participants = participantRepository.findByRoomId(room.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("roomId", room.getId());
        response.put("roomCode", room.getRoomCode());
        response.put("roomName", room.getRoomName());
        response.put("createdAt", room.getCreatedAt());
        response.put("participantCount", participants.size());

        List<Map<String, Object>> participantList = participants.stream().map(p -> {
            Map<String, Object> pm = new HashMap<>();
            pm.put("id", p.getId());
            pm.put("name", p.getName());
            return pm;
        }).toList();
        response.put("participants", participantList);

        return response;
    }

    private String generateUniqueRoomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String code;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (roomRepository.existsByRoomCode(code));
        return code;
    }
}
