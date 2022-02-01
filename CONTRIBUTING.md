<!--
SPDX-FileCopyrightText: 2022 Contributors to the Equigy-client project 

SPDX-License-Identifier: MPL-2.0
-->

# How to Contribute

We'd love to accept your patches and contributions to this project. There are
just a few small guidelines you need to follow.

## Ways of contributing

Contribution does not necessarily mean committing code to the repository. 
We recognize different levels of contributions as shown below in increasing order of dedication:

1. Test and use the build env. Give feedback on the user experience or suggest new features.
2. Report bugs.

## Git branching

This project uses the [GitHub flow Workflow](https://guides.github.com/introduction/flow/) and branching model. 
The `main` branch always contains the latest release. 
New feature/fix branches are branched from `main`. 
When a feature/fix is finished it is merged back into `main` via a 
[Pull Request](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests).

In case of major version release with new features and/or breaking changes, we might temporarily create a 
`release/` branch to hold all the changes until they are merged into `main`.

## Filing bugs and change requests

You can file bugs against and change request for the project via GitHub issues. Consult [GitHub Help](https://docs.github.com/en/free-pro-team@latest/github/managing-your-work-on-github/creating-an-issue) for more
information on using GitHub issues.

## Signing the Developer Certificate of Origin (DCO)

This project utilize a Developer Certificate of Origin (DCO) to ensure that 
each commit was written by the author or that the author has the appropriate rights 
necessary to contribute the change. 
Specifically, we utilize [Developer Certificate of Origin, Version 1.1](http://developercertificate.org/),  
which is the same mechanism that the Linux® Kernel and many other communities use to manage code contributions. 
The DCO is considered one of the simplest tools for sign-offs from contributors as the representations are 
meant to be easy to read and indicating signoff is done as a part of the commit message.

This means that each commit must include a DCO which looks like this:

`Signed-off-by: Joe Smith <joe.smith@email.com>`

The project requires that the name used is your real name and the e-mail used is your real e-mail. 
Neither anonymous contributors nor those utilizing pseudonyms will be accepted.

There are other great tools out there to manage DCO signoffs for developers to make it much easier to do signoffs:
* Git makes it easy to add this line to your commit messages. Make sure the `user.name` and `user.email` are set in your git configs. Use `-s` or `--signoff` to add the Signed-off-by line to the end of the commit message.
* [GitHub UI integrations]( https://github.com/scottrigby/dco-gh-ui ) for adding the signoff automatically to commits made with the GitHub browser UI
* Additionally, it is possible to use shell scripting to automatically apply the sign-off. For an example for bash to be put into a .bashrc file, see [here](https://wiki.lfenergy.org/display/HOME/Contribution+and+Compliance+Guidelines+for+LF+Energy+Foundation+hosted+projects). 
* Alternatively, you can add `prepare-commit-msg hook` in .git/hooks directory. For an example, see [here](https://github.com/Samsung/ONE-vscode/wiki/ONE-vscode-Developer's-Certificate-of-Origin).

## Code reviews

All submissions, including submissions by project members, require review. We
use GitHub pull requests for this purpose. Consult
[GitHub Help](https://help.github.com/articles/about-pull-requests/) for more
information on using pull requests.

## Pull Request Process
Contributions should be submitted as Github pull requests. See [Creating a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request) if you're unfamiliar with this concept.

The process for a code change and pull request you should follow:

1. Create a topic branch in your local repository, following the naming format
"feature-###" or "fix-###". For more information see the Git branching guideline.
1. Make changes, compile, and test thoroughly. Ensure any install or build dependencies are removed before the end of the layer when doing a build. Code style should match existing style and conventions, and changes should be focused on the topic the pull request will be addressed. For more information see the style guide.
1. Push commits to your fork.
1. Create a Github pull request from your topic branch.
1. Pull requests will be reviewed by one of the maintainers who may discuss, offer constructive feedback, request changes, or approve
the work. For more information see the Code review guideline.
1. Upon receiving the sign-off of one of the maintainers you may merge your changes, or if you
   do not have permission to do that, you may request a maintainer to merge it for you.

## Design criteria

The library is designed to be compatible with Java 8 and up.

This library has been designed to have only minimal dependencies, in order to
be very light-weight while still being compatible with the widest range of
application frameworks, like Java EE or Spring Boot.

## OAuth credentials handling

To keep the risk of leaking sensitive credential information, the organisation
client secret and the password are not provided as String objects (see
[this question on StackExchange](https://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords)
for a proper explanation why). To aid developers in properly safely processing
those credentials, the `com.alliander.equigy.client.oauth.sensitive` package
contains utility classes that automatically clear memory buffers that contains
sensitive information after usage.

Please not that the organisation client secret will end up in a String object
in a base64 encoded state, which might end up in a memory dump. Care has been
taken to make sure the value of the password field doesn't end up in any
object that lives in memory for longer than the duration of the OAuth token
request.

## Community Guidelines

This project follows the following [Code of Conduct](https://github.com/alliander-opensource/equigy-client/blob/master/CODE_OF_CONDUCT.md).

## Attribution

This Conitrbuting.md is adapted from Google
available at
https://github.com/google/new-project/blob/master/docs/contributing.md
