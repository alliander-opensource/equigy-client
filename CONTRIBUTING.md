# How to Contribute

We'd love to accept your patches and contributions to this project. There are
just a few small guidelines you need to follow.

## Code reviews

All submissions, including submissions by project members, require review. We
use GitHub pull requests for this purpose. Consult
[GitHub Help](https://help.github.com/articles/about-pull-requests/) for more
information on using pull requests.

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

## Dependencies

This library has been designed to have only minimal dependencies, in order to
be very light-weight while still being compatible with the widest range of
application frameworks, like Java EE or Spring Boot.

## Community Guidelines

This project follows the following [Code of Conduct](https://github.com/alliander-opensource/equigy-client/blob/master/CODE_OF_CONDUCT.md).

## Attribution

This Conitrbuting.md is adapted from Google
available at
https://github.com/google/new-project/blob/master/docs/contributing.md
