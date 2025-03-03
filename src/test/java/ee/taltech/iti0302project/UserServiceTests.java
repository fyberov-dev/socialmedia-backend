package ee.taltech.iti0302project;

import ee.taltech.iti0302project.repository.UserRepository;
import ee.taltech.iti0302project.service.UserService;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class UserServiceTests {

    private UserRepository userRepository;
    public UserService userService;

//    @BeforeEach
//    void setUp() {
//        userRepository = mock(UserRepository.class);
//        userService = new UserService(userRepository);
//    }

    /**
     @Test void testRegisterUser_Success() {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("Password1");
     user.setName("Test");
     user.setSurname("User");

     when(userRepository.existsByUsername("testuser")).thenReturn(false);
     when(userRepository.existsByEmail("testuser@example.com")).thenReturn(false);

     assertDoesNotThrow(() -> userService.registerUser(user));

     verify(userRepository, times(1)).save(user);
     }

     @Test void testRegisterUser_UsernameExists() {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("Password1");
     user.setName("Test");
     user.setSurname("User");

     when(userRepository.existsByUsername("testuser")).thenReturn(true);

     InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> userService.registerUser(user));
     assertEquals("Username already exists.", thrown.getMessage());
     }

     @Test void testRegisterUser_EmailExists() {
     User user = new User();
     user.setUsername("newuser");
     user.setEmail("testuser@example.com");
     user.setPassword("Password1");
     user.setName("New");
     user.setSurname("User");

     when(userRepository.existsByUsername("newuser")).thenReturn(false);
     when(userRepository.existsByEmail("testuser@example.com")).thenReturn(true);

     InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> userService.registerUser(user));
     assertEquals("Email already exists.", thrown.getMessage());
     }

     @Test void testRegisterUser_PasswordTooShort() {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("short");
     user.setName("Test");
     user.setSurname("User");

     InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> userService.registerUser(user));
     assertEquals("Password must be at least 8 characters long.", thrown.getMessage());
     }

     @Test void testRegisterUser_PasswordWithoutUppercase() {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("password1");
     user.setName("Test");
     user.setSurname("User");

     InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> userService.registerUser(user));
     assertEquals("Password must contain at least one uppercase letter, one lowercase letter, and one digit.", thrown.getMessage());
     }

     @Test void testRegisterUser_PasswordContainsName() {
     User user = new User();
     user.setUsername("testuser");
     user.setEmail("testuser@example.com");
     user.setPassword("Test1234");
     user.setName("Test");
     user.setSurname("User");

     InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> userService.registerUser(user));
     assertEquals("Password must not contain username or surname.", thrown.getMessage());
     }

     @Test void testIsEmailInDatabase_Exists() {
     when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
     assertTrue(userService.isEmailInDatabase("existing@example.com"));
     }

     @Test void testIsEmailInDatabase_DoesNotExist() {
     when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);
     assertFalse(userService.isEmailInDatabase("notfound@example.com"));
     }
     **/
}
