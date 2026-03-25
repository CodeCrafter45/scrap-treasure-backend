package  com.scraptreasure.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    public AuthResponse(AuthResponse token2) {
        //TODO Auto-generated constructor stub
    }
    private String token;
    private String role;
}
