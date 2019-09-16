package licx.fileshare;

//import licx.fileshare.Domain.FileInformation;
//import net.sf.jmimemagic.*;
//import org.junit.Test;
//
//import javax.activation.MimetypesFileTypeMap;
//import java.io.*;
//import java.net.FileNameMap;
//import java.net.URLConnection;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;

import org.junit.Test;
import org.springframework.util.DigestUtils;

public class SomeTests extends FileshareApplicationTests {
//    @Test
//    public void test() {
//        String pathname = "D:\\雪\\Camera\\20180824_225831.jpg";
//
//        try {
//            Magic parser = new Magic() ;
//            MagicMatch match = Magic.getMagicMatch(new File(pathname),false);
//            System.out.println("第一种Magic: " + match.getMimeType()) ;
//        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
//            e.printStackTrace();
//        }
//
//        String type = new MimetypesFileTypeMap().getContentType(new File(pathname));
//        System.out.println("第二种javax.activation: "+type);
//
//        try {
//            String s = Files.probeContentType(new File(pathname).toPath());
//            System.out.println("第三种java.nio: "+s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        FileNameMap fileNameMap = URLConnection.getFileNameMap();
//        String contentType = fileNameMap.getContentTypeFor(pathname);
//        System.out.println("第四种java.net: "+contentType);
//    }

//    @Test
//    public void textTest() throws IOException {
//        List<FileInformation> fileShareList = new ArrayList<>();
//        String userHomePath = System.getProperty("user.home");
//        String fileShareListPath = userHomePath + "\\fileshare\\filesharelist.txt";
//
//        File fileShareListFile = new File(fileShareListPath);
//        InputStream inputStream = new FileInputStream(fileShareListFile);
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//
//            System.out.println(line);
//        }
//        bufferedReader.close();
//        inputStreamReader.close();
//        inputStream.close();
//    }

    @Test
    public void md5test(){
        String base = "licx" + "+" + "990202lcx" + "fileshare";
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        System.out.println(md5);
    }
}
