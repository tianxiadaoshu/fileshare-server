package licx.fileshare.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import licx.fileshare.Domain.FileInformation;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {


//    private static String userHomePath = System.getProperty("user.home");
    private static String fileShareListPath = System.getProperty("user.home") + "/fileshare/filesharelist.txt";
    private ArrayList<File> sharedFiles;

    {
        try {
            sharedFiles = getSharedObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FileInformation> getInitFiles(){
        File[] fileList = File.listRoots();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        return fileListToFileInforList(fileList);
    }

    public String getParentDir(String destination){
        File file = new File(destination);
        if (file.getParent() == null)
            return "$";
        return file.getParent();
    }

    public List<FileInformation> getDir(String destination){
        File parentFile = new File(destination);
        File[] children = parentFile.listFiles();
        if (children == null || children.length == 0) {
            return null;
        }
        return fileListToFileInforList(children);
    }

    public boolean targetIsExists(String destination){
        File targetFile = new File(destination);
        return targetFile.exists();
    }

    public boolean targetIsDir(String destination){
        File targetFile = new File(destination);
        return targetFile.isDirectory();
    }

    private List<FileInformation> fileListToFileInforList(File[] files){
        List<FileInformation> fileInformationList = new ArrayList<>();
        for (File file : files) {
            fileInformationList.add(fileToFileInformation(file));
        }
        return fileInformationList;
    }

    private FileInformation fileToFileInformation(File file){
        String a_path = file.getAbsolutePath();
        String path = a_path.replace("\\", "/");
        String name = file.getName();
        if (file.getName().equals(""))
            name = path;
       return new FileInformation(name, file.isDirectory(), path);
    }

    private ArrayList<File> getSharedObjects() throws IOException {
        ArrayList<File> fileList = new ArrayList<>();

        File fileShareListFile = new File(fileShareListPath);
        InputStream inputStream = new FileInputStream(fileShareListFile);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            File tempFile = new File(line);
            if (tempFile.exists()){
                fileList.add(tempFile);
            }
            System.out.println(line);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return fileList;
    }

    public List<FileInformation> getFileShareList() {
//        ArrayList<File> fileList = getSharedObjects();
        File[] fileArray = new File[sharedFiles.size()];
        sharedFiles.toArray(fileArray);
        return fileListToFileInforList(fileArray);
    }

    public boolean addShareFile(String des) {
//        ArrayList<File> fileList = getSharedObjects();
        File desFile = new File(des);
        if (sharedFiles.contains(desFile))
            return true;
        else
            sharedFiles.add(desFile);
        try {
            File f = new File(fileShareListPath);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(des);
            writer.newLine();
            writer.close();

        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteFileInShare(String des) {
//        ArrayList<File> fileList = sharedFiles;
//        System.out.println("删除"+des);
        File desFile = new File(des);
        if (!sharedFiles.contains(desFile))
            return true;
        boolean result = sharedFiles.remove(desFile);
//        sharedFiles.remove(desFile);

        File[] fileArray = new File[sharedFiles.size()];
        sharedFiles.toArray(fileArray);
        List<FileInformation> fileInformationList = fileListToFileInforList(fileArray);
        try {
            File f = new File(fileShareListPath);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(write);
            for (FileInformation fileInformation : fileInformationList) {
                writer.write(fileInformation.getAbsolutelyUrl());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
            return false;
        }

        return result;
    }


//    private ArrayList<File> getDiskInformation() {
//
//        File[] disks = File.listRoots();
//        if (disks.length == 0) {
//            return null;
//        }
//
//        return new ArrayList<>(Arrays.asList(disks));
//    }


    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean deleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }


    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files == null){
            return dirFile.delete();
        }
        for (File file : files) {
            //删除子文件
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }

    public String getContentType(String pathToFile) {

        Path path = Paths.get(pathToFile);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (contentType == null)
            contentType = "application/octet-stream";
        return contentType;
    }

    /**
     * 获取一个文件夹下的所有文件全路径
     * @param path 文件夹路径
     * @param listFile 存储文件的arrylist
     */
    public void getAllFileInPath(String path, JSONArray listFile){

        File file = new File(path);
        File [] files = file.listFiles();
        if (files != null){
            for(File f:files){


                if(f.isDirectory()){//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                    JSONObject jsonObject = (JSONObject)JSON.toJSON(fileToFileInformation(f));
                    JSONArray new_json = new JSONArray();
                    getAllFileInPath(f.getAbsolutePath()+"\\",new_json);
                    jsonObject.put("children", new_json);
                    listFile.add(jsonObject);
                }else {
                    JSONObject jsonObject = (JSONObject)JSON.toJSON(fileToFileInformation(f));
                    listFile.add(jsonObject);
                }
            }
        }

    }
}
