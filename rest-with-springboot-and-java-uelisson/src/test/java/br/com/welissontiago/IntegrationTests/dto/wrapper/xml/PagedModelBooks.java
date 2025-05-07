package br.com.welissontiago.IntegrationTests.dto.wrapper.xml;

import br.com.welissontiago.IntegrationTests.dto.BookDTO;
import br.com.welissontiago.IntegrationTests.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelBooks implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<BookDTO> content;

    public PagedModelBooks() {}

    public List<BookDTO> getContent() {
        return content;
    }

    public void setContent(List<BookDTO> content) {
        this.content = content;
    }
}
