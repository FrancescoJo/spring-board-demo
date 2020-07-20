app-main-integrationTest
=======================

Integration test suite for module `app-main`. Moreover, this module generates API Documentation via Spring RESTDocs.

Therefore, all tests in this module must cover all public endpoints of `app-main`.

# Troubleshoot

Under some systems, especially Microsoft Windows, some tests could not be run because of encoding problems.
You must enforce encoding to `UTF-8` for all Java tools as setting environment variable as following:

```
JAVA_TOOL_OPTIONS =  -Dfile.encoding=UTF8
```
