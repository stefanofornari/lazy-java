# LazyJava

LazyJava is a Java annotation processor that generates boilerplate code for you.

## Features

### `@£` - Factory Method Generation

Annotate your class with `@£` to generate static factory methods:

```java
@£
public class Person {
    public Person() {}
    public Person(String name, int age) {}
}

// Usage:
£Person.Person();
£Person.Person("John", 30);
```

**Alias:** `@LClass` is also supported for users who cannot type £.

### `@NonNull`, `@NonBlank`, `@NonEmpty` - Parameter Validation

Annotate method parameters to generate validation methods that call `Safe` default methods:

```java
@£
public class UserService {
    public void register(@NonNull String name, @NonBlank String email, @NonEmpty String username) {
        // method body
    }
}

// £Processor generates:
// public class £UserService implements Safe {
//     public static UserService UserService(String name, String email, String username) {
//         return new UserService(name, email, username);
//     }
// }
//
// £SafeProcessor generates:
// public class £SafeUserService extends £UserService {
//     public void validateRegister(String name, String email, String username) {
//         requireNonNull(name, "name");
//         requireNonBlank(email, "email");
//         requireNonEmpty(username, "username");
//     }
// }

// Usage:
£UserService service = £UserService.UserService();
£SafeUserService validator = new £SafeUserService();
validator.validateRegister(name, email, username);
service.register(name, email, username);
```

## Available Validation Annotations

| Annotation | Target | Generated Call |
|------------|--------|----------------|
| `@NonNull` | `PARAMETER` | `Safe.requireNonNull(param, "param")` |
| `@NonBlank` | `PARAMETER` | `Safe.requireNonBlank(param, "param")` |
| `@NonEmpty` | `PARAMETER` | `Safe.requireNonEmpty(param, "param")` |

## Requirements

- Java 11+
- Maven 3.6+

## Installation

Add the processor dependency to your project:

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>lazyjava-processor</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## License

Apache License 2.0
