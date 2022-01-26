<!--
SPDX-FileCopyrightText: 2022 Contributors to the Equigy-client project 

SPDX-License-Identifier: MPL-2.0
-->

# Equigy client

A Java library with a client implementation to interact with the TenneT Equigy API.

## Example usage

To obtain activated energy for a single EAN for a single date, see the following example:

```java
final OAuthClient oAuthClient = new OAuthClient();
final ActivatedEnergyClient activatedEnergyClient = ActivatedEnergyClient.acceptance();

final EquigyCredentials equigyCredentials = new EquigyCredentials();
// Fill in the credentials for your organisation

final OAuthToken oAuthToken = oAuthClient.retrieveToken(equigyCredentials);
final List<ActivatedEnergy> activatedEnergies = activatedEnergyClient.getByQuery(
        new EAN18("130539057492609625"),
        LocalDate.of(2020, 12, 20),
        oAuthToken
);
```

## Building

To build the library, just run `./gradlew clean build` or `gradlew.bat clean build`.

# License
This project is licensed under the Mozilla Public License, version 2.0 - see [LICENSE](LICENSE) for details.

# Contributing
Please read [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md) and [CONTRIBUTING](CONTRIBUTING.md) for details on the process 
for submitting pull requests to us.

# Contact
Please read [SUPPORT](SUPPORT.md) for how to connect and get into contact with the Equigy-client project.
