package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.ChatDto;
import ee.taltech.iti0302project.dto.chat.ControlUserToChatRequest;
import ee.taltech.iti0302project.dto.chat.UpdateChatNameRequest;
import ee.taltech.iti0302project.entity.ChatEntity;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.chat.AlreadyAddedToChatException;
import ee.taltech.iti0302project.exceptions.chat.ChatNoAccessException;
import ee.taltech.iti0302project.exceptions.chat.ChatNotFoundException;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import ee.taltech.iti0302project.mapper.ChatMapper;
import ee.taltech.iti0302project.repository.ChatRepository;
import ee.taltech.iti0302project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    public List<ChatDto> getChats(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return chatMapper.toDtoList(chatRepository.findAll(pageable).getContent());
    }

    public ChatDto getChatById(long chatId) {
        return chatMapper.toDto(findChatById(chatId));
    }

    public List<ChatDto> getChatsByUser(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return chatMapper.toDtoList(chatRepository.findByCreatorUsername(username, pageable).getContent());
    }

    public ChatDto createChat(ChatDto chatDto, UserEntity userEntity) {
        ChatEntity chatEntity = chatMapper.toEntity(chatDto);
        chatEntity.setCreator(userEntity);
        chatEntity.setMembers(List.of(userEntity));
        userEntity.addToChat(chatEntity);
        return chatMapper.toDto(chatRepository.save(chatEntity));
    }

    public void deleteChat(long chatId, UserEntity currentUser) {
        ChatEntity chatEntity = findChatById(chatId);
        if (!chatEntity.getCreator().equals(currentUser)) {
            throw new ChatNoAccessException("You are not chat creator to delete that");
        }
        chatRepository.delete(chatEntity);
    }

    public void addUser(long chatId, ControlUserToChatRequest request, UserEntity currentUser) {
        ChatEntity chatEntity = findChatById(chatId);
        if (!chatEntity.getMembers().contains(currentUser)) {
            throw new ChatNoAccessException("You have no access to this chat");
        }
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (chatEntity.getMembers().contains(user)) {
            throw new AlreadyAddedToChatException("User is already added to the chat");
        }
        chatEntity.addMember(user);
        user.addToChat(chatEntity);
        chatRepository.save(chatEntity);
    }

    public void kickUser(long chatId, ControlUserToChatRequest request, UserEntity currentUser) {
        ChatEntity chatEntity = findChatById(chatId);
        if (!chatEntity.getCreator().equals(currentUser)) {
            throw new ChatNoAccessException("You have no access to this chat");
        }
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        chatEntity.kickMember(user);
        user.kickFromChat(chatEntity);
        chatRepository.save(chatEntity);
    }

    public ChatDto updateChatName(long chatId, UpdateChatNameRequest request, UserEntity currentUser) {
        ChatEntity chatEntity = findChatById(chatId);
        if (!chatEntity.getCreator().equals(currentUser)) {
            throw new ChatNoAccessException("You have no rights to update chat name");
        }
        chatEntity.setName(request.getName());
        return chatMapper.toDto(chatRepository.save(chatEntity));
    }

    public ChatEntity findChatById(long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));
    }
}
