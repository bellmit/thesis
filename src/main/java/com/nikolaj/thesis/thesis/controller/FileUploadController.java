package com.nikolaj.thesis.thesis.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nikolaj.thesis.thesis.storage.StorageFileNotFoundException;
import com.nikolaj.thesis.thesis.storage.StorageService;

@RestController
@RequestMapping(value = "files")
public class FileUploadController {

//    private final StorageService storageService;
//
//    @Autowired
//    public FileUploadController(StorageService storageService) {
//        this.storageService = storageService;
//    }
//
//    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
//    public String listUploadedFiles(Model model) throws IOException {
//
//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }
//
////    @GetMapping("/files/{filename:.+}")
////    @ResponseBody
////    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
////
////        Resource file = storageService.loadAsResource(filename);
////        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
////                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
////    }
//
//    @RequestMapping(value = "/{filename:.+}", method = {RequestMethod.GET})
//    public String serveFile(@PathVariable String filename) {
//
//        return "OK";
//    }
//
//    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
//    public String handleFileUpload(@RequestParam("file") MultipartFile file,
//                                   RedirectAttributes redirectAttributes) {
//
//        storageService.storeProjectFile(file);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");
//
//        return "Uploaded " + file.getOriginalFilename();
//    }
//
//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }

}