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

## Contributing

To contribute to this project, please read [Contributing](https://github.com/alliander-opensource/equigy-client/blob/master/CONTRIBUTING.md).

