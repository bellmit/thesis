package platform.controller;

import org.springframework.web.bind.annotation.*;

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