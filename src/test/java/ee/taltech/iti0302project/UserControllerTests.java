package ee.taltech.iti0302project;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti0302project.controller.UserController;
import ee.taltech.iti0302project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@MockitoSettings
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     @Test void testRegisterUser_Success() throws Exception {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("Password1");
     user.setName("Test");
     user.setSurname("User");

     when(userService.registerUser(any(User.class))).thenReturn(true);

     mockMvc.perform(post("/user/register")
     .contentType(MediaType.APPLICATION_JSON)
     .content(objectMapper.writeValueAsString(user)))
     .andExpect(status().isOk())
     .andExpect(content().string("User registered successfully!"));

     verify(userService, times(1)).registerUser(any(User.class));
     }


     @Test void testRegisterUser_Invalid() throws Exception {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("short");
     user.setName("Test");
     user.setSurname("User");

     when(userService.registerUser(any(User.class))).thenThrow(new InvalidUserException("Password must be at least 8 characters long."));

     mockMvc.perform(post("/user/register")
     .contentType(MediaType.APPLICATION_JSON)
     .content(objectMapper.writeValueAsString(user)))
     .andExpect(status().isBadRequest())
     .andExpect(content().string("Password must be at least 8 characters long."));

     verify(userService, times(1)).registerUser(any(User.class));
     }


     @Test void testLoginUser_Success() throws Exception {
     when(userService.loginUser("testuser", "Password1")).thenReturn(true);

     mockMvc.perform(post("/user/login")
     .param("username", "testuser")
     .param("password", "Password1"))
     .andExpect(status().isOk())
     .andExpect(content().string("Login successful!"));

     verify(userService, times(1)).loginUser("testuser", "Password1");
     }

     @Test void testLoginUser_Failure() throws Exception {
     when(userService.loginUser("testuser", "WrongPassword")).thenReturn(false);

     mockMvc.perform(post("/user/login")
     .param("username", "testuser")
     .param("password", "WrongPassword"))
     .andExpect(status().isUnauthorized())
     .andExpect(content().string("Invalid credentials"));

     verify(userService, times(1)).loginUser("testuser", "WrongPassword");
     }


     @Test void testGetUserProfile_Success() throws Exception {
     User user = new User();
     user.setUsername("testuser");
     user.setName("Test");
     user.setEmail("testuser@example.com");

     when(userService.getUserByUsername("testuser")).thenReturn(user);

     mockMvc.perform(get("/user/profile/testuser"))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.user.username").value("testuser"))
     .andExpect(jsonPath("$.user.name").value("Test"))
     .andExpect(jsonPath("$.user.email").value("testuser@example.com"));

     verify(userService, times(1)).getUserByUsername("testuser");
     }

     @Test void testGetUserProfile_NotFound() throws Exception {
     when(userService.getUserByUsername("unknownuser")).thenReturn(null);

     mockMvc.perform(get("/user/profile/unknownuser"))
     .andExpect(status().isNotFound())
     .andExpect(jsonPath("$.message").value("User not found"));

     verify(userService, times(1)).getUserByUsername("unknownuser");
     }

     @Test void testUpdateUserProfile_Success() throws Exception {
     User user = new User();
     user.setUsername("testuser");
     user.setName("Updated Test");
     user.setEmail("updated@example.com");

     when(userService.getUserByUsername("testuser")).thenReturn(user);
     doNothing().when(userService).updateUserProfile(eq("testuser"), any(User.class));

     mockMvc.perform(put("/user/profile/update/testuser")
     .contentType(MediaType.APPLICATION_JSON)
     .content(objectMapper.writeValueAsString(user)))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.message").value("Profile updated successfully!"));

     verify(userService, times(1)).updateUserProfile(eq("testuser"), any(User.class));
     }

     @Test void testUpdateUserProfile_UserNotFound() throws Exception {
     User user = new User();
     user.setUsername("unknownuser");
     user.setName("Test");
     user.setEmail("testuser@example.com");

     when(userService.getUserByUsername("unknownuser")).thenReturn(null);

     mockMvc.perform(put("/user/profile/update/unknownuser")
     .contentType(MediaType.APPLICATION_JSON)
     .content(objectMapper.writeValueAsString(user)))
     .andExpect(status().isNotFound())
     .andExpect(jsonPath("$.message").value("User not found"));

     verify(userService, times(1)).getUserByUsername("unknownuser");
     }


     @Test void testDeleteUser_Success() throws Exception {
     User user = new User();
     user.setUsername("testuser");

     when(userService.getUserByUsername("testuser")).thenReturn(user);

     mockMvc.perform(delete("/user/delete/testuser"))
     .andExpect(status().isOk())
     .andExpect(content().string("User deleted successfully!"));

     verify(userService, times(1)).deleteUser("testuser");
     }

     @Test void testDeleteUser_NotFound() throws Exception {
     when(userService.getUserByUsername("unknownuser")).thenReturn(null);

     mockMvc.perform(delete("/user/delete/unknownuser"))
     .andExpect(status().isNotFound())
     .andExpect(content().string("User not found"));

     verify(userService, times(1)).getUserByUsername("unknownuser");
     }

     @Test void testUpdateUserProfile_WithNewUsername() throws Exception {
     User existingUser = new User();
     existingUser.setUsername("testuser");
     existingUser.setName("Old Name");
     existingUser.setEmail("oldemail@example.com");

     User updatedUser = new User();
     updatedUser.setUsername("newusername");
     updatedUser.setName("New Name");
     updatedUser.setEmail("newemail@example.com");
     updatedUser.setSurname("New Surname");
     updatedUser.setPassword("NewPassword1");

     when(userService.getUserByUsername("testuser")).thenReturn(existingUser);
     doNothing().when(userService).updateUserProfile(eq("testuser"), any(User.class));

     mockMvc.perform(put("/user/profile/update/testuser")
     .contentType(MediaType.APPLICATION_JSON)
     .content(objectMapper.writeValueAsString(updatedUser)))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.message").value("Profile updated successfully!"));

     verify(userService, times(1)).updateUserProfile(eq("testuser"), any(User.class));
     }
     **/

}
