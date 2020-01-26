package licx.fileshare.Service;

import com.alibaba.fastjson.JSONArray;
import licx.fileshare.FileshareApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FileServiceTest extends FileshareApplicationTests {

    @Autowired
    private FileService fileService;


    @Test
    public void getAllFileName(){
        JSONArray jsonArray = new JSONArray();
        fileService.getAllFileInPath("D:\\idm\\", jsonArray);
        System.out.println(jsonArray.toJSONString());
    }
}