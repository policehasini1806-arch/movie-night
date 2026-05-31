package cohort.example.repository;

import cohort.example.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByRoomId(Long roomId);
    long countByRoomId(Long roomId);
}
