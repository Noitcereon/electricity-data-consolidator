# This script is for copying any files needed for the installer into the target dir or otherwise doing simple actions,
# which is requires plugins with Maven.

# This is meant to be run from the root folder of the project (where the main README.md file is).

echo "Start of prepare-installer.sh script"

# The logging configuration file.
cp "./src/main/resources/tinylog.properties" ./target || echo "Error: Failed to copy tinylog.properties to target folder!"

echo "End of prepare-installer.sh script"