package luis.silvestre.lda.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.quarkus.logging.Log;
import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import luis.silvestre.lda.config.EmailConfig;
import luis.silvestre.lda.model.Contact;
import luis.silvestre.lda.model.ContactFormRecord;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ApplicationScoped
public class ContactFormService {

    @Inject
    private EmailConfig emailConfig;

    @Transactional
    public long newContact(ContactFormRecord contactFormRecord) {
        Contact contact = new Contact().from(contactFormRecord);
        contact.persistAndFlush();

        sendEmailToCentral(contact);

        return contact.id;
    }

    @Transactional
    public void delete(long id) {
        Contact.deleteById(id);
    }

    private void sendEmailToCentral(Contact contact) {
        Request request = new Request();
        Mail mail = new Mail();
        mail.from = new Email(emailConfig.getFrom());
        mail.subject = "Novo Contacto via Website";
        mail.templateId = emailConfig.templateId();
        Personalization personalization = new Personalization();
        personalization.addTo(new Email(emailConfig.getSendTo()));
        personalization.addBcc(new Email(emailConfig.getSendCC()));
        personalization.addDynamicTemplateData("client", contact.fullName);
        personalization.addDynamicTemplateData("email", contact.email);
        personalization.addDynamicTemplateData("phone", contact.phone);
        personalization.addDynamicTemplateData("subject", contact.subject);
        personalization.addDynamicTemplateData("message", contact.message);

        mail.personalization = new ArrayList<>();
        mail.personalization.add(personalization);

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = emailConfig.sendGrid().api(request);

            Log.info(response.getStatusCode());
            Log.info(response.getBody());

            if (response.getStatusCode() == HttpStatus.SC_ACCEPTED)
                updateEmailFieldsForContact(contact);
            else
                Log.error("FAILED");
        } catch (IOException ex) {
            Log.error(ex);
        }
    }

    private void updateEmailFieldsForContact(@NotNull Contact contact) {
        contact.emailSentToCentral = true;
        contact.dateTimeOfSendEmail = LocalDateTime.now();
        contact.persist();
    }

}
