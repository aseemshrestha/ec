package ec.cloud;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AWSS3Service {

    @Autowired
    private AmazonS3 s3Client;
    @Value("${aws.s3.bucket}")
    private String bucket;

    /* file upload using putObjectApi */
    public String uploadFilev1(final MultipartFile multipartFile, String path) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            return uploadFileToS3Bucket(bucket + "/" + path, file);

        } catch (final AmazonServiceException ex) {
            log.info("File upload is failed.");
            log.info(("Error= {} while uploading file." + ex.getMessage()));
        }
        return "n/a";
    }


    /* file upload using TransferMgr Api */
    public List<String> uploadFileList(MultipartFile[] multipartFile, String path) throws Exception {
        List<String> fileNames = new ArrayList<>();
        Arrays.stream(multipartFile).forEach(mf -> {
            try {
               fileNames.add(uploadFile(mf, path));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return fileNames;
    }

    public void deleteFile(final String keyName) {
        log.info("Deleting file with name= " + keyName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, keyName);
        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully.");
        } catch (AmazonServiceException e) {
            log.info("Amazing Service Exception::Error deleting object from S3" + e);
        } catch (Exception e) {
            log.info("Exception while deleting a project" + e);
        }
    }


    public int deleteFiles(String[] keys) throws Exception {
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucket)
                .withKeys(keys)
                .withQuiet(false);
        int successfulDeletes = 0;
        try {
            DeleteObjectsResult deleteObjectsResult = s3Client.deleteObjects(delObjReq);
            successfulDeletes = deleteObjectsResult.getDeletedObjects().size();
            log.info(successfulDeletes + " objects successfully deleted.");
        } catch (AmazonServiceException e) {
            log.info("Amazing Service Exception::Error deleting object from S3" + e);
            throw new Exception("Amazing Service Exception::Error deleting object from S3", e);
        } catch (Exception e) {
            log.info("Exception while deleting a project" + e);
            throw new Exception("Amazing Service Exception::Error deleting object from S3", e);
        }
        return successfulDeletes;

    }

    public List<String> uploadFileListNew(MultipartFile[] multipartFile,
                                          final String path) throws Exception {

        List<File> files = new ArrayList<>();
        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile mf : multipartFile) {
            File file = convertMultiPartFileToFile(mf);
            files.add(file);
            uploadedFiles.add(file.getName());
        }

        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3Client)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1024))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(10))
                .build();

        MultipleFileUpload upload = tm.uploadFileList(bucket, path
                , new File("."), files);
        TransferMgr.waitForCompletion(upload);

        return uploadedFiles;

    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            System.out.println("Error converting the multi-part file to file= " + ex.getMessage());
        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final File file) {
        String dateTime = String.valueOf(LocalDateTime.now());
        String encodedDate = Base64.getEncoder().encodeToString(dateTime.getBytes());
        final String uniqueFileName = encodedDate + "_" + file.getName();
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        s3Client.putObject(putObjectRequest);
        return uniqueFileName;
    }

    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        String fileName = generateFileName(multipartFile, path);

        PutObjectRequest request = new PutObjectRequest(bucket, fileName, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        file.delete();
        return fileName;
    }

    private String generateFileName(MultipartFile multiPart, String path) {
        return path + "-" + multiPart.getOriginalFilename().replaceAll(" ", "_");
    }

}