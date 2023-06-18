docker run --rm -v ${PWD}:${PWD} -e PACT_BROKER_BASE_URL -e PACT_BROKER_TOKEN pactfoundation/pact-cli:latest \
  publish ${PWD}/target/pacts --consumer-app-version 1.0.0 --branch main