package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.ChatDto;
import ee.taltech.iti0302project.dto.chat.ControlUserToChatRequest;
import ee.taltech.iti0302project.dto.chat.UpdateChatNameRequest;
import ee.taltech.iti0302project.service.ChatService;
import ee.taltech.iti0302project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ChatDto>> getChats(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(chatService.getChats(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable long chatId) {
        return new ResponseEntity<>(chatService.getChatById(chatId), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ChatDto>> getChatsByUser(
            @PathVariable String username,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(chatService.getChatsByUser(username, pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestBody ChatDto chatDto) {
        return new ResponseEntity<>(chatService.createChat(chatDto, userService.getCurrentUser()), HttpStatus.CREATED);
    }

    @PatchMapping("/{chatId}")
    public ResponseEntity<HttpStatus> addUserToChat(@PathVariable long chatId, @RequestBody ControlUserToChatRequest request) {
        chatService.addUser(chatId, request, userService.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{chatId}/kick")
    public ResponseEntity<HttpStatus> kickUserFromChat(@PathVariable long chatId, @RequestBody ControlUserToChatRequest request) {
        chatService.kickUser(chatId, request, userService.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{chatId}/name")
    public ResponseEntity<ChatDto> updateChatName(@PathVariable long chatId, @RequestBody UpdateChatNameRequest request) {
        return new ResponseEntity<>(chatService.updateChatName(chatId, request, userService.getCurrentUser()), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable long chatId) {
        chatService.deleteChat(chatId, userService.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
