package com.squarecross.photoalbum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/cors")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class CorsApiController {

    @GetMapping
    public ResponseEntity<String> getCors(){
        System.out.println("----------------------");
        System.out.println("request");
        System.out.println("----------------------");
        return ResponseEntity.ok("this is getMapping");
    }
    @PostMapping
    public ResponseEntity<String> postCors(){
        System.out.println("----------------------");
        System.out.println();
        System.out.println("----------------------");
        return ResponseEntity.ok("this is postMapping");
    }
    @DeleteMapping
    public ResponseEntity<String> deleteCors(){
        System.out.println("----------------------");
        System.out.println();
        System.out.println("----------------------");
        return ResponseEntity.ok("this is deleteMapping");
    }
}
