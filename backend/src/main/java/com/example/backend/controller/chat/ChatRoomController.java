package com.example.backend.controller.chat;

import com.example.backend.dto.Response;
import com.example.backend.dto.chat.ChatDto;
import com.example.backend.dto.chat.ChatRoomDto;
import com.example.backend.dto.chat.InviteChatDto;
import com.example.backend.dto.chat.LeaveChatDto;
import com.example.backend.service.chat.ChatRoomService;
import com.example.backend.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅방 API")
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Operation(summary = "채팅방 목록 조회 API", description = "채팅방 목록 조회 API")
    @GetMapping("/room/list")
    public Response<List<ChatRoomDto.Response>> getRoomList(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return new Response<>(200, "채팅방 조회 완료", chatRoomService.getRoomList(memberId));
    }


    @Operation(summary = "채팅 내용 조회", description = "채팅 내용 조회")
    @GetMapping("/chat/list/{roomId}")
    public Response<List<ChatDto.Response>> getChatList(@PathVariable Long roomId) {
        return new Response<>(200, "채팅 내용 조회 완료", chatService.getChatList(roomId));
    }

    @Operation(summary = "채팅 신청", description = "채팅 신청")
    @PostMapping("/chat/invite")
    public Response<InviteChatDto.Response> inviteChat(@RequestBody InviteChatDto.Request request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return new Response<>(201, "채팅 신청 완료", chatService.inviteChat(request, memberId));
    }

    @Operation(summary = "채팅방 나가기", description = "채팅 나가기")
    @PostMapping("/chat/leave")
    public Response<List<ChatRoomDto.Response>> leaveChat(
            @RequestBody LeaveChatDto.Request request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long myId = Long.parseLong(userDetails.getUsername());
        return new Response<>(200, "채팅방 나가기 완료", chatRoomService.leaveChat(request.getRoomId(), myId));
    }

    @Operation(summary = "채팅 읽음 처리", description = "채팅방 떠날때 전부 읽음 처리")
    @PostMapping("/chat/list/{roomId}")
    public Response<String> readChats(
            @PathVariable("roomId") Long roomId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        chatRoomService.readChats(roomId, memberId);
        return new Response<>(200, "읽음 처리 완료", "읽음 처리 완료");
    }
}
