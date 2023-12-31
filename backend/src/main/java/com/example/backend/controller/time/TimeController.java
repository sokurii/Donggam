package com.example.backend.controller.time;

import com.example.backend.dto.image.BestImageDto;
import com.example.backend.dto.image.ImageDetailDto;
import com.example.backend.dto.image.ImageDto;
import com.example.backend.dto.Response;
import com.example.backend.dto.image.ImageLikeDto;
import com.example.backend.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/time")
@RequiredArgsConstructor
@Tag(name = "시간 페이지 API", description = "시간 페이지 API")
public class TimeController {

  private final TimeService timeService;
  
  @Operation(summary = "사진 등록", description = "사진 등록")
  @PostMapping(consumes = "multipart/form-data")
  public Response<String> postImage(
      @AuthenticationPrincipal UserDetails userDetails,
      String title,
      @RequestParam MultipartFile img) throws IOException {
    Long memberId = Long.parseLong(userDetails.getUsername());

    timeService.postImage(memberId, img, title);

    return new Response<>(201, "사진 업로드 성공", "사진 업로드 성공");
  }

  @Operation(summary = "전체 사진목록 조회", description = "전체 사진목록 조회")
  @GetMapping
  public Response<List<ImageDto.Response>> getImages(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
  ) {
    Long memberId = Long.parseLong(userDetails.getUsername());
    return new Response<>(200, "전체 사진 조회 성공", timeService.getImages(memberId));
  }

  @Operation(summary = "사진 상세보기", description = "사진 상세보기")
  @GetMapping("/{imageId}")
  public Response<ImageDetailDto.Response> getImage(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable("imageId") Long imageId
  ) {
    Long memberId = Long.parseLong(userDetails.getUsername());
    return new Response<>(200, "사진 상세 조회 성공", timeService.getImage(memberId, imageId));
  }

  @Operation(summary = "공감/공감 취소", description = "공감/공감 취소")
  @PostMapping("/{imageId}")
  public Response<ImageLikeDto.Response> likeImage(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable("imageId") Long imageId
  ) {
    Long memberId = Long.parseLong(userDetails.getUsername());

    return new Response<>(200, "사진 공감/공감 취소 성공", timeService.likeImage(memberId, imageId));
  }

  @Operation(summary = "베스트 사진 목록", description = "베스트 사진 목록")
  @GetMapping("/best")
  public Response<BestImageDto.Response> bestImages(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
  ) {
    Long memberId = Long.parseLong(userDetails.getUsername());
    return new Response<>(200, "베스트 사진 조회 성공", timeService.bestImages(memberId));
  }
}
