package luis.silvestre.lda;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import luis.silvestre.lda.model.Contact;
import luis.silvestre.lda.model.ContactFormRecord;
import luis.silvestre.lda.service.ContactFormService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.annotations.Param;

@Path("/contact")
public class ContactForm {

    @Inject
    private ContactFormService contactFormService;

    @ConfigProperty(name = "ls.wc.admin-key")
    private String adminKey;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response list() {
        return Response.ok(Contact.listAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/new")
    public Response contact(@Param ContactFormRecord contactFormRecord) {
        return Response.ok(contactFormService.newContact(contactFormRecord)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete")
    public Response delete(@QueryParam("contact_id") long id, @QueryParam("admin_key") String admin) {
        if (!adminKey.equals(admin))
            return Response.status(403).build();

        if (Contact.findByIdOptional(id).isPresent()) {
            contactFormService.delete(id);
            return Response.ok().build();
        } else {
            return Response.noContent().build();
        }
    }
}
