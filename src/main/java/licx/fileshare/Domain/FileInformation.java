package licx.fileshare.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class FileInformation {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean directory;
    @Id
    private String absolutelyUrl;

    public FileInformation() {
    }

    public FileInformation(String name, boolean directory, String absolutelyUrl) {
        this.name = name;
        this.directory = directory;
        this.absolutelyUrl = absolutelyUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getAbsolutelyUrl() {
        return absolutelyUrl;
    }

    public void setAbsolutelyUrl(String absolutelyUrl) {
        this.absolutelyUrl = absolutelyUrl;
    }

    @Override
    public String toString() {
        return "FileInformation{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                ", absolutelyUrl='" + absolutelyUrl + '\'' +
                '}';
    }
}
