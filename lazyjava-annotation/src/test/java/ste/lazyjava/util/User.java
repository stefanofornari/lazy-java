/**
 * Copyright 2026 the original author or authors from the lazy-java project
 * (https://github.com/stefanofornari/lazy-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
