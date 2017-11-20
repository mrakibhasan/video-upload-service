package com.spring.upload.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import com.spring.upload.model.FileBucket;

/*
 * This class catch multipart expception if file size exceded 100 mb. 
 * 
 */

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger=LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<FileBucket> handleError1(MultipartException e,HttpServletRequest request) {
 
    	logger.info("The request content length: "+request.getContentLength());
    	long fileSize=((request.getContentLength())/(1024*1024));
    	String errorMessage ="Requested file size "+fileSize+" MB Excceeded limit 15 MB";
    	logger.error("The Error message for large file size: "+errorMessage);
    	FileBucket fileInfo=new FileBucket();
    	fileInfo.setFileName(errorMessage);
    	return  new ResponseEntity<FileBucket>(fileInfo, HttpStatus.BAD_REQUEST);
    }
}