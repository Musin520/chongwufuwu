package com.example.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.javaPro.myProject.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OssServiceTest {

    @Mock
    private OSS ossClient;

    @InjectMocks
    private FileUploadService fileUploadService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileUploadService, "bucketName", "test-bucket");
        ReflectionTestUtils.setField(fileUploadService, "urlPrefix", "https://test-bucket.oss.com/");
    }

    @Test
    void testUploadFile_Success() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(ossClient.putObject(anyString(), anyString(), any(java.io.InputStream.class))).thenReturn(mock(PutObjectResult.class));

        // When
        String result = fileUploadService.uploadFile(file);

        // Then
        assertThat(result).startsWith("https://test-bucket.oss.com/uploads/");
        assertThat(result).endsWith(".jpg");
        verify(ossClient).putObject(eq("test-bucket"), anyString(), any(java.io.InputStream.class));
    }

    @Test
    void testUploadFile_IOException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new IOException("File read error"));

        // When & Then
        assertThrows(IOException.class, () -> fileUploadService.uploadFile(file));
        verify(ossClient, never()).putObject(anyString(), anyString(), any(java.io.InputStream.class));
    }

    @Test
    void testUploadFile_NullFilename() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> fileUploadService.uploadFile(file));
    }
}