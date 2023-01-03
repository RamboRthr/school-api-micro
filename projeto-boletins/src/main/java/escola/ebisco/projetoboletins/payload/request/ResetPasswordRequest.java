package escola.ebisco.projetoboletins.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String newPassword;

    private String signedKey;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getSignedKey() {
        return signedKey;
    }

    public void setSignedKey(String signedKey) {
        this.signedKey = signedKey;
    }
}
