package project.drill.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

	private AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@PostMapping
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		System.out.println("업로드 한다잉?");
		try {
			String fileName = file.getOriginalFilename();
			String fileUrl = "https://" + bucket + "/test" + fileName;
			ObjectMetadata metadata = new ObjectMetadata();

			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());

			System.out.println(bucket);
			System.out.println(fileName);
			System.out.println(file.getInputStream());
			System.out.println(metadata);
			System.out.println("amazonS3Client" + amazonS3Client.toString());

			amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
			return ResponseEntity.ok(fileUrl);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
