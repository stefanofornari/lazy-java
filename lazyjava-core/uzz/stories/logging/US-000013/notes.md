# Notes — logging/US-000013

1. `application` is required for every system-folder token, including through the default pattern; an unset `application` leaves the token as it is and reports a warning naming the setting (no exception, see US-000014).
2. Tests compare with `ProjectDirectories` itself and clean up the files they create in the real data folder of the test applications (`lazytest`, `lazytest2`).
