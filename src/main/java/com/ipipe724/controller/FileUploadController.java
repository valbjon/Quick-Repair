package com.ipipe724.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ipipe724.model.RequestRepair;
import com.ipipe724.model.User;
import com.ipipe724.service.RequestRepairService;
import com.ipipe724.service.UserService;

import ch.qos.logback.classic.pattern.Util;

/**
 * @author imssbora
 */
@Controller
public class FileUploadController {
	
	@Autowired
	RequestRepairService requestRepairService;
	
	@Autowired
    private UserService userService;


	
	   @GetMapping("/file/upload")
	   public String fileUploadForm(Model model) {
	      return "work/generic";
	   }
	 
   // Handling single file upload request
   @PostMapping("/singleFileUpload")
   public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model)
         throws IOException {
	  

      // Save file on system
      if (!file.getOriginalFilename().isEmpty()) {
         BufferedOutputStream outputStream = new BufferedOutputStream(
               new FileOutputStream(
                     new File("D:/test", file.getOriginalFilename())));
         outputStream.write(file.getBytes());
         outputStream.flush();
         outputStream.close();
         
         
         model.addAttribute("msg", "File uploaded successfully.");
      } else {
         model.addAttribute("msg", "Please select a valid file..");
      }
      return "work/customer";
   }

   // Handling multiple files upload request
   @PostMapping("/multipleFileUpload")
   public String multipleFileUpload(@RequestParam("file") MultipartFile[] files,
         Model model) throws IOException {
	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       User user = userService.findUserByEmail(auth.getName());

      // Save file on system
      for (MultipartFile file : files) {
         if (!file.getOriginalFilename().isEmpty()) {
        	String fileName =  FileUploadController.getRandomNumber()+""+file.getOriginalFilename();
            BufferedOutputStream outputStream = new BufferedOutputStream(
                  new FileOutputStream(
                        new File("data/", fileName)));

            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            
            
            Page<RequestRepair> repair = requestRepairService.findTheLastByID(0, 50, user.getId());
            RequestRepair req = (RequestRepair)repair.getContent().get(0);
            req.setDescription(fileName);
            requestRepairService.saveGrade(req);

         } else {
            model.addAttribute("msg", "Please select at least one file..");
            return "fileUploadForm";
         }
      }
      model.addAttribute("msg", "Multiple files uploaded successfully.");
      return "work/customer"; // I have changed here
   }
   
   @GetMapping("/download1")
   public ResponseEntity<InputStreamResource> downloadFile1(
		   Model model, @RequestParam("name") Optional<String> name){
	  try {
		  String FILE_PATH = "data/"+name.get();
		  
		  File file = new File(FILE_PATH);
	      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	      return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                  "attachment;filename=" + file.getName())
	            .contentType(MediaType.MULTIPART_FORM_DATA).contentLength(file.length())
	            .body(resource);
		  
	  }catch(Exception e) {
		  System.out.println(e);
	  }
	  return null;
   }
   
   public static int getRandomNumber(){
	    double x = Math.random();
	    return (int)x;
	}
}