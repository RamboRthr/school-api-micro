package escola.ebisco.projetoboletins.Domain.email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import escola.ebisco.projetoboletins.payload.request.ForgotPasswordRequest;
import escola.ebisco.projetoboletins.payload.request.SignupRequest;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

import static escola.ebisco.projetoboletins.Domain.email.EmailAttachment.csv;
import static java.util.Set.of;
import static org.springframework.util.CollectionUtils.isEmpty;

@Value
@Builder(toBuilder = true)
public class EmailData {
    private String recipient;
    private String message;
    private String subject;
    private Set<EmailAttachment> attachments;

    @JsonIgnore
    public boolean hasAttachments() {
        return (!isEmpty(attachments));
    }

    public static EmailData from(final SignupRequest request) {
        String confirmationString = "Registration confirmed";
        return EmailData.builder()
                .message("Registration confirmed")
                .recipient(request.getEmail())
                .subject("Registrartion")
                .attachments(of(csv(confirmationString.getBytes(), "Registration confirmed")))
                .build();
    }
    public static EmailData from(final ForgotPasswordRequest request){
        return EmailData.builder()
                .message("To reset your password, copy the code below\n" + request.getResetKey())
                .recipient(request.getEmail())
                .subject("Reset password")
                .attachments(of(csv("Reset password".getBytes(), "Forgot my password")))
                .build();

    }
}
