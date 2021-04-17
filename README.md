# Meeting api

API for the meeting service from Dogood

- [Meeting api](#meeting-api)
  - [Development](#development)
    - [Building locally](#building-locally)
    - [Branches](#branches)
      - [Branch structure](#branch-structure)
      - [Scenario: Develop a feature](#scenario-develop-a-feature)
      - [Scenario: Make a release](#scenario-make-a-release)
    - [Start a local database server for development](#start-a-local-database-server-for-development)
      - [Using docker](#using-docker)
        - [Load the schema](#load-the-schema)
  - [Usage](#usage)
    - [Availible requests](#availible-requests)
    - [Full list](#full-list)
      - [Authentication](#authentication)
        - [Authenticate a device](#authenticate-a-device)
        - [Get token for an admin](#get-token-for-an-admin)
        - [Get token for a resident](#get-token-for-a-resident)
        - [Check caller identity (useful for debug)](#check-caller-identity-useful-for-debug)
      - [Creating stuff](#creating-stuff)
        - [Create an admin account](#create-an-admin-account)
        - [Create a resident](#create-a-resident)
        - [Create a relative](#create-a-relative)
      - [List stuff](#list-stuff)
        - [List residents](#list-residents)
        - [List relatives of a resident](#list-relatives-of-a-resident)
      - [Delete stuff](#delete-stuff)
        - [Delete a resident](#delete-a-resident)
  - [Deployment](#deployment)
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

### Start a local database server for development

#### Using docker

Ensure docker is installed on the host. Then run the following

```bash
docker run --name dd1369 -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```

If the database has been stopped, start it with the following command

```bash
docker container start dd1369
```

You can connect to the database server, either using a gui or psql

```bash
psql -h 127.0.0.1 -p 5432 -U postgres
```

##### Load the schema

Create the database inside of postgres

```bash
psql -h 127.0.0.1 -p 5432 -U postgres -c "CREATE DATABASE meetings;"
```

Then from the root of the repo, run

```bash
psql -h 127.0.0.1 -p 5432 -U postgres meetings < database/schema.sql
```

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

### Full list

Use either of the  following two commands to set the endpoint (which api to talk to).

For the production api:

```bash
ENDPOINT=http://master.api.dd1369-meetings.com
```

If you are running the api locally:

```bash
ENDPOINT=http://localhost:8080
```

#### Authentication
##### Authenticate a device

```bash
DEVICE_TOKEN=$(curl -X "POST" "$ENDPOINT/authenticate/device" \
     -H 'Content-Type: application/json' \
     -d $'{
  "passphrase": "hemlis"
}' | jq -r '.token')
echo "$DEVICE_TOKEN"
```

##### Get token for an admin

```bash
ADMIN_TOKEN=$(curl -X "POST" "$ENDPOINT/authenticate/admin" \
     -H 'Content-Type: application/json' \
     -H "Authorization: Bearer $DEVICE_TOKEN" \
     -d $'{
  "username": "foo",
  "password": "bar"
}' | jq -r '.token')
echo "$ADMIN_TOKEN"
```

##### Get token for a resident

```bash
RESIDENT_TOKEN=$(curl -X "POST" "$ENDPOINT/authenticate/resident" \
     -H 'Content-Type: application/json' \
     -H "Authorization: Bearer $DEVICE_TOKEN" \
     -d "{
  \"id\": $RESIDENT_ID
}" | jq -r '.token')
echo "$RESIDENT_TOKEN"
```

##### Check caller identity (useful for debug)

```bash
curl "$ENDPOINT/me" \
     -H 'Content-Type: application/json' \
     -H 'Authorization: Bearer some-token' | jq
```

#### Creating stuff

##### Create an admin account

```bash
curl -X "POST" "$ENDPOINT/admin" \
     -H 'Content-Type: application/json' \
     -H "Authorization: Bearer $DEVICE_TOKEN" \
     -d $'{
  "name": "someone",
  "username": "foo",
  "password": "bar"
}' | jq
```

##### Create a resident

```bash
RESIDENT_ID=$(curl -X "POST" "$ENDPOINT/residents" \
     -H 'Content-Type: application/json' \
     -H "Authorization: Bearer $ADMIN_TOKEN" \
     -d $'{
  "name": "cool person"
}' | jq -r '.id')
echo "$RESIDENT_ID"
```

##### Create a relative

```bash
RELATIVE_PHONE=+46...
```

```bash
RELATIVE_ID=$(curl -X "POST" "$ENDPOINT/residents/$RESIDENT_ID/relatives" \
     -H 'Content-Type: application/json' \
     -H "Authorization: Bearer $ADMIN_TOKEN" \
     -d "{
  \"name\": \"cool persons cooler relative\",
  \"phone\": \"$RELATIVE_PHONE\"
}" | jq -r '.id')
echo "$RELATIVE_ID"
```

#### List stuff

##### List residents

```bash
curl "$ENDPOINT/residents" \
     -H 'Content-Type: application/json' \
     -H "Authorization: $ADMIN_TOKEN" | jq
```

##### List relatives of a resident

```bash
curl "$ENDPOINT/residents/$RESIDENT_ID/relatives" \
     -H 'Content-Type: application/json' \
     -H "Authorization: $ADMIN_TOKEN" | jq
```

#### Delete stuff

##### Delete a resident

```bash
curl -X DELETE "$ENDPOINT/residents/$RESIDENT_ID" \
     -H 'Content-Type: application/json' \
     -H "Authorization: $ADMIN_TOKEN" | jq
```

## Deployment

There is a simple deployment script `redeploy.sh` (that expects to run in a configured environment, this is not documented).
