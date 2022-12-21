package escola.ebisco.projetoboletins.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgotPasswordRequest {

    private final String subject = "Registration";
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String resetKey;


    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }
}
