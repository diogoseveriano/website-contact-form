package luis.silvestre.lda.config;

import com.sendgrid.SendGrid;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EmailConfig {

    @ConfigProperty(name = "send.grid.api.key")
    private String sendGridApiKey;

    @ConfigProperty(name = "send.grid.templateId")
    private String templateId;

    @ConfigProperty(name = "send.grid.to")
    private String sendTo;

    @ConfigProperty(name = "send.grid.from")
    private String sendFrom;

    @ConfigProperty(name = "send.grid.cc")
    private String sendCC;

    public SendGrid sendGrid() {
        return new SendGrid(this.sendGridApiKey);
    }

    public String templateId() { return templateId; }

    public String getFrom() { return sendFrom; }

    public String getSendTo() { return sendTo; }

    public String getSendCC() { return sendCC; }
}
