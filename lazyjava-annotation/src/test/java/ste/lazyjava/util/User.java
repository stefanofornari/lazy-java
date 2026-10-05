

package ste.lazyjava.util;

import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;

/**
 * Sample class demonstrating @NonNull, @NonBlank, and @NonEmpty annotations.
 * The £Processor will generate a £SafeUser class with validate methods.
 */
public class User {

    private String name;
    private String email;
    private String username;

    public User(@NonNull String name, @NonBlank String email, @NonEmpty String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public void rename(@NonNull String newName) {
        this.name = newName;
    }

    public void updateEmail(@NonBlank String email) {
        this.email = email;
    }

    public void changeUsername(@NonEmpty String username) {
        this.username = username;
    }

    public void register(@NonNull String name, @NonBlank String email, @NonEmpty String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
