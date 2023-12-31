package com.example.backend.controller.space;

import com.example.backend.dto.Response;
import com.example.backend.dto.record.MyRecordDto;
import com.example.backend.dto.record.RecordCommentDto;
import com.example.backend.dto.record.RecordDetailDto;
import com.example.backend.dto.record.RecordDto;
import com.example.backend.service.RecordService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space")
@Tag(name = "공간 페이지(일반 방명록) API", description = "공간 페이지(일반 방명록) API")
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "방명록 작성", description = "방명록 작성")
    @PostMapping(consumes = "multipart/form-data")
    public Response<RecordDetailDto.Response> createRecord(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "request") RecordDetailDto.Request request,
            @RequestPart(required = false, value = "image") MultipartFile image
    ) throws IOException {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return new Response<>(201, "방명록 작성 성공",
                recordService.createRecord(memberId, request, image));
    }

    @Operation(summary = "방명록 댓글 작성", description = "방명록 댓글 작성")
    @PostMapping("/{recordId}")
    public Response<RecordCommentDto.Response> createComment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RecordCommentDto.Request request,
            @PathVariable("recordId") Long recordId) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        return new Response<>(201, "방명록 댓글 작성 성공",
                recordService.createComment(memberId, recordId, request.getContent()));
    }

    @Operation(summary = "방명록 상세보기", description = "방명록 상세보기")
    @GetMapping("/{recordId}")
    public Response<RecordDetailDto.Response> record(@PathVariable("recordId") Long recordId) {
        return new Response<>(200, "방명록 상세보기 성공", recordService.record(recordId));
    }

    @Operation(summary = "주변 방명록 조회", description = "주변 방명록 조회")
    @GetMapping
    public Response<List<RecordDto.Response>> aroundRecords(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return new Response<>(200, "주변 방명록 조회 성공", recordService.aroundRecords(memberId));
    }

    @Operation(summary = "내 방명록 목록", description = "내 방명록 목록")
    @GetMapping("/mine")
    public Response<List<MyRecordDto.Response>> myRecords (
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return new Response<>(200, "내 방명록 목록 조회 성공", recordService.myRecords(memberId));
    }

    @Operation(summary = "방명록 댓글 조회", description = "방명록 댓글 조회")
    @GetMapping("/comment/{recordId}")
    public Response<List<RecordCommentDto.Response>> recordComments(@PathVariable("recordId") Long recordId) {
        return new Response<>(200, "방명록 댓글 조회 성공", recordService.recordComments(recordId));
    }
}
