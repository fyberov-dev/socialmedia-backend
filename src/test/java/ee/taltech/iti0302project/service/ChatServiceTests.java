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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTests {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChats() {
        Pageable pageable = PageRequest.of(0, 10);
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        Page<ChatEntity> chatPage = new PageImpl<>(List.of(chatEntity));

        when(chatRepository.findAll(pageable)).thenReturn(chatPage);
        when(chatMapper.toDtoList(chatPage.getContent())).thenReturn(List.of(new ChatDto()));

        List<ChatDto> result = chatService.getChats(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chatRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetChatById() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(chatMapper.toDto(chatEntity)).thenReturn(new ChatDto());

        ChatDto result = chatService.getChatById(1L);

        assertNotNull(result);
        verify(chatRepository, times(1)).findById(1L);
    }

    @Test
    void testGetChatsByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        Page<ChatEntity> chatPage = new PageImpl<>(List.of(chatEntity));

        when(chatRepository.findByCreatorUsername("testUser", pageable)).thenReturn(chatPage);
        when(chatMapper.toDtoList(chatPage.getContent())).thenReturn(List.of(new ChatDto()));

        List<ChatDto> result = chatService.getChatsByUser("testUser", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chatRepository, times(1)).findByCreatorUsername("testUser", pageable);
    }

    @Test
    void testCreateChat() {
        ChatDto chatDto = new ChatDto();
        UserEntity userEntity = new UserEntity();
        ChatEntity chatEntity = new ChatEntity();

        when(chatMapper.toEntity(chatDto)).thenReturn(chatEntity);
        when(chatRepository.save(chatEntity)).thenReturn(chatEntity);
        when(chatMapper.toDto(chatEntity)).thenReturn(chatDto);

        ChatDto result = chatService.createChat(chatDto, userEntity);

        assertNotNull(result);
        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testDeleteChat() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity userEntity = new UserEntity();
        chatEntity.setCreator(userEntity);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));

        chatService.deleteChat(1L, userEntity);

        verify(chatRepository, times(1)).delete(chatEntity);
    }

    @Test
    void testAddUser() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);
        chatEntity.setMembers(new ArrayList<>(List.of(currentUser)));

        UserEntity newUser = new UserEntity();
        newUser.setId(2L);
        ControlUserToChatRequest request = new ControlUserToChatRequest();
        request.setUserId(2L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));

        chatService.addUser(1L, request, currentUser);

        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testKickUser() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(1L);
        chatEntity.setCreator(creator);

        UserEntity userToKick = new UserEntity();
        userToKick.setId(2L);
        ControlUserToChatRequest request = new ControlUserToChatRequest();
        request.setUserId(2L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userToKick));

        chatService.kickUser(1L, request, creator);

        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testUpdateChatName() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(1L);
        chatEntity.setCreator(creator);

        UpdateChatNameRequest request = new UpdateChatNameRequest();
        request.setName("New Chat Name");

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(chatRepository.save(chatEntity)).thenReturn(chatEntity);
        when(chatMapper.toDto(chatEntity)).thenReturn(new ChatDto());

        ChatDto result = chatService.updateChatName(1L, request, creator);

        assertNotNull(result);
        assertEquals("New Chat Name", chatEntity.getName());
        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testFindChatByIdChatNotFound() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> chatService.findChatById(1L));
    }

    @Test
    void testDeleteChatNoAccess() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(1L);
        chatEntity.setCreator(creator);

        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));

        assertThrows(ChatNoAccessException.class, () -> chatService.deleteChat(1L, otherUser));
    }

    @Test
    void testAddUserNoAccess() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));

        assertThrows(ChatNoAccessException.class, () -> chatService.addUser(1L, new ControlUserToChatRequest(), currentUser));
    }

    @Test
    void testAddUserAlreadyAdded() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);
        chatEntity.setMembers(new ArrayList<>(List.of(currentUser)));

        UserEntity newUser = new UserEntity();
        newUser.setId(2L);
        ControlUserToChatRequest request = new ControlUserToChatRequest();
        request.setUserId(2L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));

        chatEntity.addMember(newUser);

        assertThrows(AlreadyAddedToChatException.class, () -> chatService.addUser(1L, request, currentUser));
    }

    @Test
    void testKickUserNoAccess() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(1L);
        chatEntity.setCreator(creator);

        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));

        assertThrows(ChatNoAccessException.class, () -> chatService.kickUser(1L, new ControlUserToChatRequest(), otherUser));
    }

    @Test
    void testUpdateChatNameNoAccess() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(1L);
        chatEntity.setCreator(creator);

        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);

        UpdateChatNameRequest request = new UpdateChatNameRequest();
        request.setName("New Name");

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));

        assertThrows(ChatNoAccessException.class, () -> chatService.updateChatName(1L, request, otherUser));
    }
}
