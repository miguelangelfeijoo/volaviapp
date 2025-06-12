package com.app.Volavia.service;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Profile;
import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.repository.UserRepository;
import com.app.Volavia.validation.Validation; // Static methods, will use actual implementation

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DiaryService diaryService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder; // Mocking the encoder

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "testUser", "Password123!", Profile.USER);
        user.setId(1L);
        user.setFecha_registro(new Date());
        // Note: userService constructor creates its own BCryptPasswordEncoder instance.
        // To properly mock it for registerUser, it should be injectable into UserService.
        // For now, I will assume that if I mock bCryptPasswordEncoder.encode(), it will be used.
        // If not, I'll need to refactor UserService to inject the encoder.
        // Let's proceed and see if the current setup allows mocking.
        // It seems like UserService has `private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();`
        // This means the @Mock bCryptPasswordEncoder won't be injected there.
        // I will need to modify UserService to allow injection or test without mocking encode.
        // For now, I'll mock it and if tests fail due to it, I'll address the UserService structure.
    }

    // --- registerUser Tests ---

    @Test
    void testRegisterUser_Successful() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUsuario(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        List<String> result = userService.registerUser(new User("new@example.com", "newUser", "ValidPass123!", Profile.USER));

        assertEquals(1, result.size());
        assertEquals("Registro exitoso.", result.get(0));
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(userRepository.findByUsuario(anyString())).thenReturn(null);
        // No need to mock bCryptPasswordEncoder or saveAndFlush as it should return earlier

        List<String> result = userService.registerUser(new User("test@example.com", "anotherUser", "ValidPass123!", Profile.USER));

        assertTrue(result.contains("El email TEST@EXAMPLE.COM ya existe. Prueba con otro."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUsuario("testUser")).thenReturn(user);
        // No need to mock bCryptPasswordEncoder or saveAndFlush

        List<String> result = userService.registerUser(new User("newemail@example.com", "testUser", "ValidPass123!", Profile.USER));

        assertTrue(result.contains("El usuario TESTUSER ya existe. Prueba con otro."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_InvalidEmailFormat() {
        // Relies on actual Validation.validarEmail
        List<String> result = userService.registerUser(new User("invalidEmail", "newUser", "ValidPass123!", Profile.USER));
        assertTrue(result.contains("El campo email no es válido."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_InvalidUsernameFormat() {
        // Relies on actual Validation.validarUsuario
        List<String> result = userService.registerUser(new User("valid@example.com", "invalid user", "ValidPass123!", Profile.USER));
        assertTrue(result.contains("El campo usuario no es válido."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_InvalidPasswordFormat() {
        // Relies on actual Validation.validarContrasena
        List<String> result = userService.registerUser(new User("valid@example.com", "validUser", "short", Profile.USER));
        assertTrue(result.contains("El campo contraseña no es válido."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterUser_AllValidationsFail() {
        List<String> result = userService.registerUser(new User("invalid", "invalid user", "short", Profile.USER));
        assertTrue(result.contains("El campo email no es válido."));
        assertTrue(result.contains("El campo usuario no es válido."));
        assertTrue(result.contains("El campo contraseña no es válido."));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }


    @Test
    void testRegisterUser_SaveAndFlushThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUsuario(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(new RuntimeException("DB error"));

        List<String> result = userService.registerUser(new User("new@example.com", "newUser", "ValidPass123!", Profile.USER));

        assertEquals(1, result.size());
        assertTrue(result.get(0).startsWith("Error al registrar.")); // Actual message is "Error al registrar. Intenta nuevamente más tarde."
        verify(userRepository).saveAndFlush(any(User.class));
    }

    // --- deleteUserAndAssociatedData Tests ---

    @Test
    void testDeleteUserAndAssociatedData_UserExistsWithTripsAndDiaries() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        Trip trip1 = new Trip();
        trip1.setId(10L);
        trip1.setUser(mockUser);
        Diary diary1 = new Diary();
        diary1.setId(100L);
        diary1.setTrip(trip1);

        Trip trip2 = new Trip();
        trip2.setId(20L);
        trip2.setUser(mockUser);
        // Trip 2 has no diary for this test variation

        List<Trip> trips = new ArrayList<>();
        trips.add(trip1);
        trips.add(trip2);
        mockUser.setTrips(trips);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(diaryService.findByTrip(trip1)).thenReturn(diary1);
        when(diaryService.findByTrip(trip2)).thenReturn(null); // Trip 2 has no diary

        userService.deleteUserAndAssociatedData(userId);

        verify(userRepository).findById(userId);
        verify(diaryService).findByTrip(trip1);
        verify(diaryService).delete(diary1);
        verify(diaryService).findByTrip(trip2);
        verify(diaryService, never()).delete(null); // Ensure delete is not called with null
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUserAndAssociatedData_UserExistsWithNoTrips() {
        Long userId = 1L;
        User mockUser = new User(); // User with an empty list of trips by default
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.deleteUserAndAssociatedData(userId);

        verify(userRepository).findById(userId);
        verify(diaryService, never()).findByTrip(any(Trip.class));
        verify(diaryService, never()).delete(any(Diary.class));
        verify(userRepository).deleteById(userId);
    }


    @Test
    void testDeleteUserAndAssociatedData_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.deleteUserAndAssociatedData(userId);

        verify(userRepository).findById(userId);
        verify(diaryService, never()).findByTrip(any(Trip.class));
        verify(diaryService, never()).delete(any(Diary.class));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
