package ec.cloud;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${s3.end.point}")
    private static String S3_END_POINT;
    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKeyId;
    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretAccessKey;
    @Value("${AWS_REGION}")
    private String region;

    public static String getS3EndPoint() {
        return "https://ec-students.s3.amazonaws.com/";

    }

    @Bean
    public AmazonS3 getAmazonS3Client() {

        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}