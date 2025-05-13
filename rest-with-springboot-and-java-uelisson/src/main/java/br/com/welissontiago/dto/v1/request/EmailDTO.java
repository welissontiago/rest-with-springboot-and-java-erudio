package br.com.welissontiago.dto.v1.request;

import java.util.Objects;

public class EmailDTO {

    private String subject;
    private String body;
    private String to;

    public EmailDTO() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmailDTO emailDTO)) return false;
        return Objects.equals(getSubject(), emailDTO.getSubject()) && Objects.equals(getBody(), emailDTO.getBody()) && Objects.equals(getTo(), emailDTO.getTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubject(), getBody(), getTo());
    }
}
