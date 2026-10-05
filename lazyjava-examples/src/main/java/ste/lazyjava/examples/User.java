

package ste.lazyjava.examples;

import ste.lazyjava.annotation.NonBlank;
import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.£;

/**
 * Sample class demonstrating basic Java patterns.
 */
@£
public class User extends Person {

    private String username;

    public User(@NonBlank String name, String email, @NonBlank String username, @NonEmpty String password ) {
        super(name);
        this.username = username;
    }

    public void changeUsername(@NonBlank String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }
}
