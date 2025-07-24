package com.aoao.blog.admin.controller;

import com.aoao.blog.common.model.admin.vo.file.UploadFileRspVO;
import com.aoao.blog.admin.service.FileService;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aoao
 * @create 2025-07-22-14:06
 */
@RestController
@RequestMapping("/admin/file")
@Api("文件模块")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public Result<UploadFileRspVO> uploadFile(@RequestParam MultipartFile file) {
        UploadFileRspVO vo = fileService.uploadFile(file);
        return Result.success(vo);
    }
}
