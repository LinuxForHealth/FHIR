## Contributing In General
Our project welcomes external contributions. If you have an itch, please feel
free to scratch it.

To contribute code or documentation, please submit a [pull request](https://github.com/ibm/fhir/pulls).

A good way to familiarize yourself with the codebase and contribution process is
to look for and tackle low-hanging fruit in the [issue tracker](https://github.com/ibm/fhir/issues).
Before embarking on a more ambitious contribution, please quickly [get in touch](#communication) with us.

**Note: We appreciate your effort, and want to avoid a situation where a contribution
requires extensive rework (by you or by us), sits in backlog for a long time, or
cannot be accepted at all!**

### Proposing new features

If you would like to implement a new feature, please [raise an issue](https://github.com/ibm/fhir/issues)
before sending a pull request so the feature can be discussed. This is to avoid
you wasting your valuable time working on a feature that the project developers
are not interested in accepting into the code base.

### Fixing bugs

If you would like to fix a bug, please [raise an issue](https://github.com/ibm/fhir/issues) before sending a
pull request so it can be tracked.

### Merge approval

The project maintainers use [GitHub reviews](https://github.com/features/code-review) to indicate acceptance.
A change requires approval from two of the maintainers of each component affected.
Sometimes, reviewers will leave a comment "LGTM" to indicate that the change "looks good to me".

For a list of the maintainers, see the [MAINTAINERS.md](MAINTAINERS.md) page.

## Legal

Each source file must include a license header for the Apache
Software License 2.0. Using the SPDX format is the simplest approach.
e.g.

```
/*
 * (C) Copyright <holder> <year of first update>[, <year of last update>]
 *
 * SPDX-License-Identifier: Apache-2.0
 */
```

We have tried to make it as easy as possible to make contributions. This
applies to how we handle the legal aspects of contribution. We use the
same approach - the [Developer's Certificate of Origin 1.1 (DCO)](https://github.com/hyperledger/fabric/blob/master/docs/source/DCO1.1.txt) - that the Linux® Kernel [community](https://elinux.org/Developer_Certificate_Of_Origin)
uses to manage code contributions.

We simply ask that when submitting a patch for review, the developer
must include a sign-off statement in the commit message.

Here is an example Signed-off-by line, which indicates that the
submitter accepts the DCO:

```
Signed-off-by: John Doe <john.doe@example.com>
```

You can include this automatically when you commit a change to your
local git repository using the following command:

```
git commit -s
```

## Communication
Connect with us through https://chat.fhir.org/ or open an [issue](https://github.com/IBM/FHIR/issues). The IBM FHIR Server has a dedicated "stream" at https://chat.fhir.org/#narrow/stream/212434-ibm (`#ibm`).

## Setup
See the [Setting up for development](https://github.com/IBM/FHIR/wiki/Setting-up-for-development) in the wiki.

## Testing
To ensure a working build, please run the full build from the root of the project before submitting your pull request.

## Coding style guidelines
The IBM FHIR Server has been written by many individuals over many years. Formatting has not been strictly enforced, but we'd like to improve it over time, so please consider the following points as you change the code:

1. Write tests. Pull Requests should include necessary updates to unit tests (src/test/java of the corresponding project) and integration tests (in the fhir-server-test project)

2. Use comments. Preferably javadoc.

3. Keep the documentation up-to-date. Project documentation exists under the docs directory. We have a Conformance page for documenting conformance to the specification, a User Guide for FHIR Server administrators, and other guides for select topics of interest. In addition to the project site, we also have README.md files for many of the modules.

4. Use spaces (not tabs) in java source. For this we have a checkstyle rule which will fail the build if you're using tabs. We also prefer spaces over tabs in JSON and XML, but its not strictly enforced.

5. Use spaces after control flow keywords (they're not function calls!); if/for/while blocks should always have { }

Leave the code better than you found it.

### Branch naming convention

issue-#<number>

### Commit message convention

issue #<number> - short description

long description

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
