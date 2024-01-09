package com.xyhc.cms.vo.taskRequest;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFilesDto {
    private MultipartFile[] files;
}
