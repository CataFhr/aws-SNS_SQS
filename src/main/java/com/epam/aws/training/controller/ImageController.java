package com.epam.aws.training.controller;

import com.epam.aws.training.dto.ImageMetadataDto;
import com.epam.aws.training.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping
  public void uploadImage(@RequestParam("file") MultipartFile multipartFile) {
    imageService.upload(multipartFile);
  }

  @DeleteMapping("/{name}")
  public void delete(@PathVariable String name) {
    imageService.delete(name);
  }

  @GetMapping("/{name}")
  public ResponseEntity<ByteArrayResource> download(@PathVariable String name) {
    byte[] data = imageService.download(name);
    return ResponseEntity.ok()
        .contentLength(data.length)
        .header("Content-type", "application/octet-stream")
        .header("Content-disposition", "attachment; fileName=\"" + name + "\"")
        .body(new ByteArrayResource(data));
  }

  @GetMapping("/metadata")
  public List<ImageMetadataDto> extractAllMetadata() {
    return imageService.extractMetadata();
  }

  @GetMapping("/random-metadata")
  public ImageMetadataDto extractRandomMetadata() {
    return imageService.extractRandomMetadata();
  }
}
