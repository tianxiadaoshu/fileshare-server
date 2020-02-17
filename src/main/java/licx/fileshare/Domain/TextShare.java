package licx.fileshare.Domain;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TextShare {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = Integer.MAX_VALUE)
    private String shareText;
    @Column
    private Timestamp timestamp;
    @Column
    private String textPreview;

    public TextShare() {
        this.id = -1;
    }
    public TextShare(String shareText, Timestamp timestamp) {
        this.id = 0;
        this.shareText = shareText;
        this.timestamp = timestamp;
        getPreview();
    }



    private void getPreview(){
        if (this.shareText.length() > 40){
            this.textPreview = this.shareText.substring(0,39);
        }else {
            this.textPreview = this.shareText;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTextPreview() {
        return textPreview;
    }

    public void setTextPreview(String textPreview) {
        this.textPreview = textPreview;
    }
    @Override
    public String toString() {
        return "TextShare{" +
                "id=" + id +
                ", shareText='" + shareText + '\'' +
                ", timestamp=" + timestamp +
                ", textPreview='" + textPreview + '\'' +
                '}';
    }
}
