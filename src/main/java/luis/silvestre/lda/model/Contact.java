package luis.silvestre.lda.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_form")
public class Contact extends PanacheEntity implements Serializable {

    public String fullName;
    public String email;
    public String phone;
    public String subject;
    public String message;

    public LocalDateTime dateTimeOfMessage;
    public boolean emailSentToCentral;
    public LocalDateTime dateTimeOfSendEmail;

    public Contact from(@NotNull ContactFormRecord record) {
        this.fullName = record.clientName();
        this.phone = record.phone();
        this.email = record.email();
        this.subject = record.subject();
        this.message = record.message();
        this.dateTimeOfMessage = LocalDateTime.now();
        this.emailSentToCentral = false;

        return this;
    }

}
