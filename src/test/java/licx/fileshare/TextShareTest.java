package licx.fileshare;

import licx.fileshare.Domain.TextShare;
import licx.fileshare.Persistence.TextShareDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TextShareTest extends FileshareApplicationTests {
    @Autowired
    private TextShareDao textShareDao;

    @Test
    public void getAll(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<TextShare> textShareList = textShareDao.findAll(sort);
        for (TextShare textShare : textShareList) {
            System.out.println(textShare.toString());
        }
    }

}
