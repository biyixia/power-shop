package com.bjpowernode.exception;

import com.bjpowernode.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException e) {
        log.error(e.getMsg(), e);
        return ResponseEntity.status(e.getCode()).body(e.getMsg());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据库中已存在该记录");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuffer sb = new StringBuffer();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.error(sb.toString(), e);
        //return Result.error("没有权限，请联系管理员授权");
        return ResponseEntity.status(405).body(sb.toString());
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件大小超出10MB限制, 请压缩或降低文件质量! ");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("执行数据库异常,违反了完整性例如：违反惟一约束、违反非空限制、字段内容超出长度等");
    }

    @ExceptionHandler(PoolException.class)
    public ResponseEntity<?> handlePoolException(PoolException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redis 连接异常!");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器端异常");
    }
}
