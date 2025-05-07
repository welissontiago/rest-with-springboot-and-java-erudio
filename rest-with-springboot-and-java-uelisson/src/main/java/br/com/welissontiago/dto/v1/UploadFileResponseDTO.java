package br.com.welissontiago.dto.v1;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String downloadURI;
    private String fileType;
    private Long size;

    public UploadFileResponseDTO(String fileName, String downloadURI, String fileType, Long size) {
        this.fileName = fileName;
        this.downloadURI = downloadURI;
        this.fileType = fileType;
        this.size = size;
    }

    public UploadFileResponseDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadURI() {
        return downloadURI;
    }

    public void setDownloadURI(String downloadURI) {
        this.downloadURI = downloadURI;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UploadFileResponseDTO that)) return false;
        return Objects.equals(getFileName(), that.getFileName()) && Objects.equals(getDownloadURI(), that.getDownloadURI()) && Objects.equals(getFileType(), that.getFileType()) && Objects.equals(getSize(), that.getSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName(), getDownloadURI(), getFileType(), getSize());
    }
}
