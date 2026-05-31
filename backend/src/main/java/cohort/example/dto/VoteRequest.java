package cohort.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotBlank(message = "Room code is required")
    private String roomCode;

    @NotBlank(message = "Genre is required")
    private String genre;
}
