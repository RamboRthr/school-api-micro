package com.service.mail.Data;

import Classes.ForgotPasswordRequest;
import Classes.SignupRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;
import java.util.Set;

import static com.service.mail.Data.EmailAttachment.csv;
import static java.util.Set.of;
import static org.springframework.util.CollectionUtils.isEmpty;

@Value
@Builder(toBuilder = true)
public class EmailData {
    String recipient;
    String message;
    String subject;
    Set<EmailAttachment> attachments;

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
