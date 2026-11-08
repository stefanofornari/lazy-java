# Notes — logging/US-000013

1. `application` is required for every system-folder token, including through the default pattern; an unset `application` fails with a message naming the setting.
2. Tests compare with `ProjectDirectories` itself and clean up the files they create in the real data folder of the test applications (`lazytest`, `lazytest2`).
