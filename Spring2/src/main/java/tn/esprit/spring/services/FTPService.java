package tn.esprit.spring.services;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FTPService {

    static FTPClient ftp = new FTPClient();
    static String TMP_UPLOAD_FOLDER = "/tmp/";

    public static String fileUpload(MultipartFile file, Long courseId) throws IOException {
        if (file.isEmpty()) {
            System.out.println("Empty File");
            return "Empty File";
        } else {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(TMP_UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("File successfully uploaded to local storage : " + file.getOriginalFilename());
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply;
            ftp.connect("ftpupload.net");
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("Exception in connecting to FTP Server");
            }
            ftp.login("username", "password");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            try {
                InputStream input = new FileInputStream(new File(TMP_UPLOAD_FOLDER + file.getOriginalFilename()));
                System.out.println(input);
                ftp.makeDirectory("/htdocs/Courses/"+courseId.toString());
                ftp.storeFile("/htdocs/Courses/"+courseId.toString()+"/" + file.getOriginalFilename(), input);
                ftp.logout();
                ftp.disconnect();
                System.out.println("File Uploaded !");
                Files.delete(path);

            } catch (Exception e) {
                System.out.println("Error uploading file to remote server");
            }

        }
        return "";

    }
    public static String removeFile(String fileName,Long courseId) throws IOException {
        try {

            ftp.connect("ftpupload.net");

            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connect failed");
                return "Connect failed";
            }

            boolean success = ftp.login("username", "password");

            if (!success) {
                System.out.println("Could not login to the server");
                return "Could not login to the server";
            }
            String fileToDelete = "/htdocs/Courses/"+ courseId.toString()+"/"+fileName;

            boolean deleted = ftp.deleteFile(fileToDelete);
            if (deleted) {
                System.out.println("The file was deleted successfully.");
            } else {
                System.out.println("Could not delete the  file, it may not exist.");
            }

        } catch (IOException ex) {
            System.out.println("Oh no, there was an error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // logs out and disconnects from server
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }
}