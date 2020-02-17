package licx.fileshare.Service;

import licx.fileshare.Domain.TextShare;
import licx.fileshare.Persistence.TextShareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TextShareService {
    @Autowired
    private TextShareDao textShareDao;

    public List<TextShare> getAllTexts(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return textShareDao.findAll(sort);
    }

    public List<TextShare> getTextsBetween(String after, String before) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp afterTime = new Timestamp(sdf.parse(after).getTime());
        Timestamp beforeTime = new Timestamp(sdf.parse(before).getTime());
        return textShareDao.findAllByTimestampAfterAndTimestampBeforeOrderByIdDesc(afterTime,beforeTime);
    }

    public boolean deleteTextByID(Integer id){
        if (!textShareDao.existsById(id)){
            return false;
        }
        textShareDao.deleteById(id);
        return true;
    }

    public TextShare getTextByID(Integer id){
        if (textShareDao.existsById(id))
            return textShareDao.getById(id);
        return new TextShare();
    }

    public boolean modifyTextByID(Integer id, String shareText){
        if (!textShareDao.existsById(id)){
            addText(shareText);
        }
        TextShare textShare = new TextShare(shareText, new Timestamp(new Date().getTime()));
        textShareDao.modifyTextByID(id, shareText, textShare.getTimestamp(), textShare.getTextPreview());
        return true;
    }

    public TextShare addText(String shareText){
        TextShare textShare = new TextShare(shareText, new Timestamp(new Date().getTime()));
        return textShareDao.saveAndFlush(textShare);
    }
}
