package cohort.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    private String roomName;

    @NotBlank(message = "Host name is required")
    private String hostName;
}
