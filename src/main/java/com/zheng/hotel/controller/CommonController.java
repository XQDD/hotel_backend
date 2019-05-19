package com.zheng.hotel.controller;

import com.zheng.hotel.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "通用")
@RequestMapping(value = "common", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommonController {
    @Value("${file.upload-dir}")
    private String fileUploadDir;

    @RequiresPermissions("sys:common:upload")
    @ApiOperation("文件上传")
    @PostMapping(value = "upload}")
    public ResponseEntity upload(@NotEmpty List<MultipartFile> files) throws IOException {
        var date = new Date();
        var path = "/" + DateFormatUtils.format(date, "yyyy-MM") + "/" + DateFormatUtils.format(date, "dd") + "/";
        var paths = new ArrayList<String>();
        for (MultipartFile uploadedFile : files) {
            //相对路径
            var relativePath = path + RandomStringUtils.randomAlphanumeric(8) + getExtensionWithDot(uploadedFile.getOriginalFilename());
            File file = new File(fileUploadDir + relativePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            uploadedFile.transferTo(file);
            paths.add(relativePath);
        }
        return Result.ok(paths);
    }


    private String getExtensionWithDot(String path) {
        var extension = StringUtils.getFilenameExtension(path);
        if (extension == null) {
            return "";
        }
        return "." + extension;
    }
}
