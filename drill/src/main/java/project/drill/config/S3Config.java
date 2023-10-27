package project.drill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Value("${cloud.aws.credentials.access-key}")
	private String iamAccessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String iamSecretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public AmazonS3Client amazonS3Client(){
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);

		System.out.println("awsCredentials" + awsCredentials.toString());

		AmazonS3Client build = (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withRegion(region).enablePathStyleAccess()
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			.build();

		System.out.println(build.toString());

		return build;
	}
}
