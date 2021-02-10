# Meeting api

API for the meeting service from Dogood

## Development

### Building locally

```bash
# Get the source
git clone git@github.com:TheExpendablesKTH/meeting-api.git
cd meeting-api

# Build and run the application
./gradlew build
./gradlew run

# The application should now be available on port 8080
$ curl http://localhost:8080 | jq
{
  "message": "I am the api!"
}
```

### Branches

Propose we use this as a base for our branching: [A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/)

#### Branch structure

```
- master
- release branches
- develop
    - feature branch one
    - feature branch two
```

#### Scenario: Develop a feature

All feature branches are made from `develop`

```
# make sure you are on the develop branch
git checkout develop

# then open your new branch
git checkout -b feature/my-awesome-feature
git push -u origin feature/my-awesome-feature
```

Make your commits, then open up a PR in github to merge your feature branch into `develop`.

#### Scenario: Make a release

Open a PR to merge `develop` into a given release branch eg. `release-v1`. Then Merge that release branch into `master`. Tag the master branch with a version number eg. `v1.x.x`

## Usage

### Availible requests

See the [docs folder](docs/requests-postman-collection.json) for a postman collection of all available requests.

Or run them in the console, eg:

```bash
# Create Call
$ curl -X "POST" "http://localhost:8080/calls" \
     -H 'Content-Type: application/json' \
     -H 'Authorization: Bearer TOKEN' | jq
{
  "call": {
    "for_user": 2,
    "connection_details": {
      "...": "..."
    },
    "id": 1
  },
  "message": "call was created"
}
```

> these examples use `jq` for readability
