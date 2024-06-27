package luis.silvestre.lda.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import luis.silvestre.lda.model.Contact;
import luis.silvestre.lda.model.ContactFormRecord;

@ApplicationScoped
public class ContactFormService {

    @Transactional
    public long newContact(ContactFormRecord contactFormRecord) {
        Contact contact = new Contact().from(contactFormRecord);
        contact.persistAndFlush();

        return contact.id;
    }

    @Transactional
    public void delete(long id) {
        Contact.deleteById(id);
    }

}
