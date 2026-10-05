package ste.lazyjava.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.lazyjava.bytecode.LazyJavaAgent;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class UserTest {

    @BeforeAll
    static void beforeAll() {
        java.lang.instrument.Instrumentation inst = net.bytebuddy.agent.ByteBuddyAgent.install();
        LazyJavaAgent.install(null, inst);
    }

    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User("Bob", "bob@example.com", "bob");
    }
    @Test
    void rename_shouldPass_withNonNullName() {
        user.rename("Alice");
        then(user.getName()).isEqualTo("Alice");
    }

    @Test
    void rename_shouldFail_withNullName() {
        thenThrownBy(() -> user.rename(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("newName must not be null");
    }

    @Test
    void updateEmail_shouldPass_withNonBlankEmail() {
        user.updateEmail("alice@example.com");
    }

    @Test
    void updateEmail_shouldFail_withBlankEmail() {
        thenThrownBy(() -> user.updateEmail("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("email must be non blank");
    }

    @Test
    void updateEmail_shouldFail_withNullEmail() {
        thenThrownBy(() -> user.updateEmail(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("email must be non blank");
    }

    @Test
    void changeUsername_shouldPass_withNonEmptyUsername() {
        user.changeUsername("alice123");
    }

    @Test
    void changeUsername_shouldFail_withEmptyUsername() {
        thenThrownBy(() -> user.changeUsername(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("username must be non empty");
    }

    @Test
    void changeUsername_shouldFail_withNullUsername() {
        thenThrownBy(() -> user.changeUsername(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("username must be non empty");
    }

    @Test
    void register_shouldPass_withValidInputs() {
        user.register("Alice", "alice@example.com", "alice123");
    }

    @Test
    void register_shouldFail_whenNameIsNull() {
        thenThrownBy(() -> user.register(null, "alice@example.com", "alice123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be null");
    }

    @Test
    void register_shouldFail_whenEmailIsBlank() {
        thenThrownBy(() -> user.register("Alice", "  ", "alice123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("email must be non blank");
    }

    @Test
    void register_shouldFail_whenUsernameIsEmpty() {
        thenThrownBy(() -> user.register("Alice", "alice@example.com", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("username must be non empty");
    }

    @Test
    void constructor_shouldPass_withValidInputs() {
        User u = new User("Alice", "alice@example.com", "alice123");
        then(u.getName()).isEqualTo("Alice");
        then(u.getEmail()).isEqualTo("alice@example.com");
        then(u.getUsername()).isEqualTo("alice123");
    }

    @Test
    void constructor_shouldFail_whenNameIsNull() {
        thenThrownBy(() -> new User(null, "alice@example.com", "alice123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be null");
    }

    @Test
    void constructor_shouldFail_whenEmailIsBlank() {
        thenThrownBy(() -> new User("Alice", "  ", "alice123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("email must be non blank");
    }

    @Test
    void constructor_shouldFail_whenUsernameIsEmpty() {
        thenThrownBy(() -> new User("Alice", "alice@example.com", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("username must be non empty");
    }
}
