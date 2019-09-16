package licx.fileshare.Service;

import com.alibaba.fastjson.JSONArray;
import licx.fileshare.Domain.FileInformation;
import licx.fileshare.FileshareApplicationTests;
import licx.fileshare.Persistence.FileInformationDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class FileServiceTest extends FileshareApplicationTests {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileInformationDao fileInformationDao;

    @Test
    public void getFileShareList() {

        List<FileInformation> fileInformationList = fileInformationDao.findAll();
        System.out.println("结果");
        for (FileInformation fileInformation : fileInformationList){
            System.out.println(fileInformation);
        }
    }

    @Test
    public void addShareFile() {
        fileService.addShareFile("C:/Users/licx/IdeaProjects/bank/src/main");
        fileService.addShareFile("C:/Users/licx/IdeaProjects/bank/src");
        fileService.addShareFile("C:/Users/licx/IdeaProjects/bank");
        fileService.addShareFile("C:/Users/licx/IdeaProjects");
        fileService.addShareFile("C:/Users/licx");

        List<FileInformation> fileInformationList = fileInformationDao.findAll();
        System.out.println("结果");
        for (FileInformation fileInformation : fileInformationList){
            System.out.println(fileInformation);
        }

    }

    @Test
    public void getAllFileName(){
        JSONArray jsonArray = new JSONArray();
        fileService.getAllFileInPath("D:\\idm\\", jsonArray);
        System.out.println(jsonArray.toJSONString());
    }
}