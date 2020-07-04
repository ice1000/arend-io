# Arend IO

[![check](https://github.com/ice1000/arend-io/workflows/check/badge.svg)](https://github.com/ice1000/arend-io/actions/runs/157748792)

An experiment with adding IO in Arend.
Only works with Arend 1.4.0!

## Build

See [GitHub Actions workflow](/.github/workflows/gradle.yml).

## Example

For more examples, see [Example.ard](/test/Example.ard).

Given this code:

```arend
\func execution => performIO
    (readProc (java ++ space ++ --version) >>= print)
```

Typechecking it gives the following message (depends on environment):

```
[INFO] openjdk 11.0.7 2020-04-14
OpenJDK Runtime Environment JBR-11.0.7.10-944.16-jcef (build 11.0.7+10-b944.16)
OpenJDK 64-Bit Server VM JBR-11.0.7.10-944.16-jcef (build 11.0.7+10-b944.16, mixed mode)
```
