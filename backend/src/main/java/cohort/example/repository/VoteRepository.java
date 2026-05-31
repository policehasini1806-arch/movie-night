package cohort.example.repository;

import cohort.example.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByRoomId(Long roomId);

    boolean existsByParticipantIdAndRoomId(Long participantId, Long roomId);

    @Query("SELECT v.genre as genre, COUNT(v) as count FROM Vote v WHERE v.room.id = :roomId GROUP BY v.genre ORDER BY COUNT(v) DESC")
    List<Object[]> countVotesByGenreForRoom(@Param("roomId") Long roomId);
}
