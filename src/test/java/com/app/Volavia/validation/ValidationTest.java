package com.app.Volavia.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    //--- Tests for validarUsuario ---

    @Test
    void testValidarUsuario_Valid() {
        assertTrue(Validation.validarUsuario("validUser123"));
    }

    @Test
    void testValidarUsuario_Null() {
        assertFalse(Validation.validarUsuario(null));
    }

    @Test
    void testValidarUsuario_Empty() {
        assertFalse(Validation.validarUsuario(""));
    }

    @Test
    void testValidarUsuario_WithSpaces() {
        assertFalse(Validation.validarUsuario("user with spaces"));
    }

    @Test
    void testValidarUsuario_WithSpecialChars() {
        assertFalse(Validation.validarUsuario("user!@#"));
    }

    //--- Tests for validarContrasena ---

    @Test
    void testValidarContrasena_Valid() {
        assertTrue(Validation.validarContrasena("ValidPass123!"));
    }

    @Test
    void testValidarContrasena_TooShort() {
        assertFalse(Validation.validarContrasena("Shrt1@")); // 7 chars
    }

    @Test
    void testValidarContrasena_OnlySpacesInvalid() {
        // Current regex ^[a-zA-Z0-9!@#$%^&*]+$ does not allow only spaces
        // and the contains(" ") check also makes it invalid.
        assertFalse(Validation.validarContrasena("        "));
    }

    @Test
    void testValidarContrasena_InvalidChars() {
        // Character like `~` is not in the allowed set [a-zA-Z0-9!@#$%^&*]
        assertFalse(Validation.validarContrasena("InvalidPass~"));
    }

    @Test
    void testValidarContrasena_MeetsLengthButInvalidChars() {
        // assertFalse(Validation.validarContrasena("PasswordWithUnsupported^Char")); // Caret is allowed by current regex ^[a-zA-Z0-9!@#$%^&*]+$
        assertFalse(Validation.validarContrasena("PasswordWithUnsupported|Char")); // Pipe is not in the allowed set
    }


    @Test
    void testValidarContrasena_Null() {
        assertFalse(Validation.validarContrasena(null));
    }

    @Test
    void testValidarContrasena_Empty() {
        assertFalse(Validation.validarContrasena(""));
    }

    //--- Tests for validarEmail ---

    @Test
    void testValidarEmail_Valid() {
        assertTrue(Validation.validarEmail("test.user@example.com"));
    }

    @Test
    void testValidarEmail_ComplexValid() {
        assertTrue(Validation.validarEmail("user.name+tag@sub.example.co.uk"));
         assertTrue(Validation.validarEmail("firstname.lastname@example.com"));
        assertTrue(Validation.validarEmail("email@subdomain.example.com"));
        assertTrue(Validation.validarEmail("firstname+lastname@example.com"));
        assertTrue(Validation.validarEmail("1234567890@example.com"));
        assertTrue(Validation.validarEmail("email@example-one.com"));
        assertTrue(Validation.validarEmail("_______@example.com"));
        assertTrue(Validation.validarEmail("email@example.name"));
        assertTrue(Validation.validarEmail("email@example.museum"));
        assertTrue(Validation.validarEmail("email@example.co.jp"));
        assertTrue(Validation.validarEmail("firstname-lastname@example.com"));
    }

    @Test
    void testValidarEmail_ValidWithNumbersInDomain() {
        assertTrue(Validation.validarEmail("test@domain123.com"));
    }


    @Test
    void testValidarEmail_NoAtSign() {
        assertFalse(Validation.validarEmail("test.userexample.com"));
    }

    @Test
    void testValidarEmail_NoDomain() {
        assertFalse(Validation.validarEmail("test.user@"));
    }

    @Test
    void testValidarEmail_DomainStartsWithHyphen() {
        assertFalse(Validation.validarEmail("test@-example.com"));
    }

    @Test
    void testValidarEmail_DomainEndsWithHyphen() {
        assertFalse(Validation.validarEmail("test@example-.com"));
    }


    @Test
    void testValidarEmail_NoTopLevelDomain() {
        assertFalse(Validation.validarEmail("test.user@example"));
    }

    @Test
    void testValidarEmail_TldTooShort() {
        // The regex is [a-zA-Z]{2,7} for TLD
        assertFalse(Validation.validarEmail("test.user@example.c"));
    }

    @Test
    void testValidarEmail_WithSpaces() {
        assertFalse(Validation.validarEmail("test.user@example.com org"));
        assertFalse(Validation.validarEmail("test user@example.com"));
    }

    @Test
    void testValidarEmail_Null() {
        assertFalse(Validation.validarEmail(null));
    }

    @Test
    void testValidarEmail_Empty() {
        assertFalse(Validation.validarEmail(""));
    }

    @Test
    void testValidarEmail_DoubleDotInDomain() {
        assertFalse(Validation.validarEmail("email@example..com"));
    }

    @Test
    void testValidarEmail_LocalPartStartsWithDot() {
        assertFalse(Validation.validarEmail(".email@example.com"));
    }

    @Test
    void testValidarEmail_LocalPartEndsWithDot() {
        assertFalse(Validation.validarEmail("email.@example.com"));
    }

    @Test
    void testValidarEmail_NoLocalPart() {
        assertFalse(Validation.validarEmail("@example.com"));
    }

    @Test
    void testValidarEmail_UnicodeCharsInLocalPartNotAllowedByRegex() {
        assertFalse(Validation.validarEmail("josé@example.com"));
    }

    @Test
    void testValidarEmail_UnicodeCharsInDomainNotAllowedByRegex() {
        assertFalse(Validation.validarEmail("user@exámple.com"));
    }
}
