# Getting Started

## Setup

### Pre-requisities

To run the application you would require:

- [Java](https://www.azul.com/downloads/#zulu)
- [Docker](https://docs.docker.com/get-docker/)
- [Stripe API Keys](https://stripe.com/docs/keys)

### On macOS:

First, you need to install Java 21 or later. You can download it from [Azul](https://www.azul.com/downloads/#zulu) or
use [SDKMAN](https://sdkman.io/).

```sh
brew install --cask zulu21
```

You can install Docker using Homebrew

```sh
brew install docker
```

or visit [Docker Installation](https://docs.docker.com/get-docker/) for more information.

### Other platforms

Please check the official documentation for the installation of Java, Temporal, and Docker for your platform.

### Stripe API Keys

Sign up for a Stripe account and get your API keys from the [Stripe Dashboard](https://dashboard.stripe.com/apikeys).
Then in `application.properties` file add the following line with your secret key.

```properties
stripe.api-key=sk_test_51J3j
```

## Run

Run the application using the following command.

```sh
make start
```

### Other commands

#### Lint

To run lint checks, use the following command

```sh
./gradlew sonarlintMain
```

#### Code Formatting

To format the code, use the following command

```sh
./gradlew spotlessApply
```

#### Run Tests

- To run the tests use the following command

  ```sh
  ./gradlew test
  ```

## Guides

The following guides illustrate how to use some features concretely:

- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Temporal Quick Start](https://docs.temporal.io/docs/quick-start)
- [Temporal Java SDK Quick Guide](https://docs.temporal.io/dev-guide/java)
- [Stripe Quick Start](https://stripe.com/docs/quickstart)
- [Stripe Java SDK](https://stripe.com/docs/api/java)

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

- cockroach: [`cockroachdb/cockroach`](https://hub.docker.com/u/cockroachdb)
- temporal-admin-tools: [`temporalio/admin-tools:1.23.0`](https://hub.docker.com/r/temporalio/admin-tools)
- temporal-ui: [`temporalio/ui:2.22.3`](https://hub.docker.com/r/temporalio/ui)
- temporal-ui: [`temporalio/ui:2.22.3`](https://hub.docker.com/r/temporalio/ui)
- midas: [`postgres:latest`](https://hub.docker.com/_/postgres)

In order to run the app using `docker-compose` navigate to the root of the project and use the following command:

```bash
docker-compose up
```

or:

```bash
make start
```
