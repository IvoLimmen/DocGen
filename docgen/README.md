# README

Converts:

* Swagger 3 YML and JSON files to HTML
* Converts AsciiDoc files to HTML

Generates:

* Index.html files

## Requirements

Minimal requirements:

* Java 17 or higher
* Maven 3.8.* or higher

## Building the application

Run:

    mvn clean install

## Running the application

After compiling you can run:

    mvn -pl app exec:java

## More information

See the documentation in the docs directory for more information

## Features

* Converts AsciiDoc files
* Creates an index
* Indexes your documentation files for searching

## Current status

Works but is really basic. Following needs to be done:

- [ ] Add customisation options for customisation on output of AsciiDoc generation (CSS)
- [ ] Add extra PDF output for download options (?)
- [ ] Better exception handling during index generation
- [ ] Add docker image for easy use in build pipeline
- [ ] Add customisable team name and extra information
- [ ] Add project info and dependencies (separate page?)