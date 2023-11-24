plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "raisedragon"
include("raisedragon-core")
include("raisedragon-api")
include("raisedragon-external")
