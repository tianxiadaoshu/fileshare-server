package licx.fileshare.Controller;

import com.alibaba.fastjson.JSONArray;
import licx.fileshare.Domain.FileInformation;
import licx.fileshare.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String getInitFiles(Model model){
        List<FileInformation> fileInformationList = fileService.getInitFiles();
        model.addAttribute("parent_url", "#");
        model.addAttribute("cur_url", "root");
        model.addAttribute("fileList", fileInformationList);
        return "index";
    }

    @GetMapping("/client/init/browser")
    @ResponseBody
    public Map<String, Object> getClientInitBrowser(){
        Map<String, Object> result = new HashMap<>();
        List<FileInformation> fileInformationList = fileService.getInitFiles();
        result.put("parent_url", "#");
        result.put("cur_url", "root");
        result.put("fileList", fileInformationList);
        return result;
    }

    @GetMapping("/dir/get")
    public String getSomeDirectory(String destination, Model model){
        List<FileInformation> fileInformationList = fileService.getDir(destination);
        String parentUrl = fileService.getParentDir(destination);
        model.addAttribute("parent_url", parentUrl);
        model.addAttribute("fileList", fileInformationList);
        model.addAttribute("cur_url", destination);
        return "index";
    }

    @GetMapping("/client/dir")
    @ResponseBody
    public Map<String, Object> getClientSomeDirectory(String destination){
        Map<String, Object> result = new HashMap<>();
        List<FileInformation> fileInformationList = fileService.getDir(destination);
        String parentUrl = fileService.getParentDir(destination);
        result.put("parent_url", parentUrl);
        result.put("cur_url", destination);
        result.put("fileList", fileInformationList);
        return result;
    }

    @GetMapping("/client/type")
    @ResponseBody
    public Map<String, Object> getTargetTypeAndChildren(String destination){
        Map<String, Object> result = new HashMap<>();
        if (fileService.targetIsExists(destination)){
            if (fileService.targetIsDir(destination)){
                result.put("type", "dir");
                JSONArray jsonArray = new JSONArray();
                fileService.getAllFileInPath(destination, jsonArray);
                result.put("children",jsonArray);
                result.put("absolutelyUrl", destination);
                result.put("directory", true);
            }else {
                result.put("type", "file");
            }
        }
        return result;
    }

    @GetMapping("/share")
    public String getFileSharingDir(Model model) {
        List<FileInformation> fileInformationList = fileService.getFileShareList();

        model.addAttribute("fileList", fileInformationList);
        return "share";
    }

    @PostMapping("/share")
    @ResponseBody
    public Map<String, String> addShareFile(String destination) {
        Map<String, String> result = new HashMap<>();
        File file = new File(destination);
        if (!file.exists()){
            result.put("back_message", "文件不存在。");
            return result;
        }
        if (fileService.addShareFile(destination))
            result.put("back_message", "成功添加文件 ‘" + destination + "’ 到文件共享目录。");
        else
            result.put("back_message", "添加失败。");

        return result;
    }

    @DeleteMapping("/share")
    @ResponseBody
    public Map<String, String> deleteObjectFromShare(String destination) throws IOException {
        Map<String, String> result = new HashMap<>();
        if (fileService.deleteFileInShare(destination))
            result.put("back_message", "成功从文件共享目录删除文件 ‘" + destination + "’ 。");
        else
            result.put("back_message", "删除失败。");
        return result;
    }


    @GetMapping("/download/file")
    public ResponseEntity<FileSystemResource> downloadFile(String destination) {

        File file = new File(destination);
        if (!file.exists()) {
            return null;
        }
        String fileName = file.getName();
        fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        String contentType = fileService.getContentType(destination);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(contentType))
                .body(new FileSystemResource(file));
    }

//    @GetMapping("/download/file")
//    public void downloadFile(String destination, HttpServletResponse response) throws UnsupportedEncodingException {
//        File file = new File(destination);
//        if (file.exists()){
//            String fileName = file.getName();
//            String contentType = fileService.getContentType(destination);
//            fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
//            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//            response.setContentType(contentType);
//            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
//            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
//            ServletOutputStream out;
//
//            try {
//                FileInputStream inputStream = new FileInputStream(file);
//                //3.通过response获取ServletOutputStream对象(out)
//                out = response.getOutputStream();
//                int b = 0;
//                byte[] buffer = new byte[512];
//                while (b != -1){
//                    b = inputStream.read(buffer);
//                    //4.写到输出流(out)中
//                    out.write(buffer,0,b);
//                }
//                inputStream.close();
//                out.close();
//                out.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @PostMapping("/file")
    @ResponseBody
    public HashMap<String, String> singleFileUpload(@RequestParam("file") MultipartFile[] files,
                                   @RequestParam("curUrl") String curUrl) {
        HashMap<String, String> result = new HashMap<>();
        String fileNameList = "";
        if (files.length == 0){
            result.put("back_message", "请先选择要上传的文件");
            return result;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                result.put("back_message", "请先选择要上传的文件");
                return result;
            }
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(curUrl + file.getOriginalFilename());
                Files.write(path, bytes);

                fileNameList = String.format("%s%s", fileNameList, "','" + file.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        result.put("back_message", "成功上传文件 " + fileNameList + "' ");

        return result;
    }

    @DeleteMapping("/file")
    @ResponseBody
    public HashMap<String, String> deleteFile(@RequestParam("fileUrl") String fileUrl){
        File desFile = new File(fileUrl);
        HashMap<String, String> result = new HashMap<>();

        if (!desFile.exists()){
            result.put("back_message", "文件不存在！");
            return result;
        }

        if (fileService.deleteFolder(fileUrl))
            result.put("back_message", "文件(夹)删除成功。");
        else
            result.put("back_message", "文件(夹)删除失败。");
        return result;
    }

    @PostMapping("/dir")
    @ResponseBody
    public HashMap<String, String> createDirectory(@RequestParam("desUrl") String desUrl,
                                                   @RequestParam("dirName") String dirName){
        File file = new File(desUrl, dirName);
        HashMap<String, String> result = new HashMap<>();
        if (file.exists() && file.isDirectory()){
            result.put("back_message", "文件夹已存在！");
            return result;
        }

        if (file.mkdir())
            result.put("back_message", "文件夹创建成功！");
        else
            result.put("back_message", "文件夹创建失败！");
        return result;
    }
}
