

# electricity-data-consolidator
[![GitHub current branch CI Status](https://github.com/Noitcereon/electricity-data-consolidator/actions/workflows/maven.yml/badge.svg)](https://github.com/Noitcereon/electricity-data-consolidator/actions/workflows/maven.yml)

An application that communicates with external APIs to retrieve their data automatically at certain intervals.

The idea is to get the data and make the accumulated data available in different formats (such as a .csv, .json or .xml file). This can then be used in e.g. an Excel sheet.

This project is still in its infancy and does not have any working parts yet.

## Install
Not ready for installation.

Temporary "Install" section:

1. Get API key for ElOverblikAPI
2. `cd /config`
3. Make file called `api-key.conf` with content similar to:
   ```conf
    api-key=your.api.key.all.in.one-line
    ```
4. Run `mvn clean install -DisMavenSurefireTesting=true`

## Usage
Not ready for usage.

## Maintainer
[@Noitcereon](https://github.com/Noitcereon/)
