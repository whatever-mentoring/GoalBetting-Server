plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("application")
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("kapt") version "1.6.21"
    jacoco
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
    group = "com.whatever"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "kotlin")
    apply(plugin = "java-library")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "application")
    apply(plugin = "jacoco")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.github.microutils:kotlin-logging:2.0.8")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // SpringMockk
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.ninja-squad:springmockk:3.1.1")

        testRuntimeOnly("com.h2database:h2")
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            html.required = true
            html.outputLocation = layout.buildDirectory.dir("reports/test-coverage.html").get().asFile
            csv.required = false
            xml.required = false
        }

        val excludes = listOf(
                "com/whatever/raisedragon/config",
                "com/whatever/raisedragon/common",
                "com/whatever/raisedragon/infra",
                "com/whatever/raisedragon/common/config",
                "com/whatever/raisedragon/aws",
                "com/whatever/raisedragon/applicationservice/*/dto"
        )

        classDirectories.setFrom(
                sourceSets.main.get().output.asFileTree.matching {
                    exclude(excludes)
                }
        )

        finalizedBy(tasks.jacocoTestCoverageVerification)
    }

    tasks.jacocoTestCoverageVerification {
        val qDomains = mutableListOf<String>()

        for (qPattern in 'A'..'Z') {
            qDomains.add("*.Q${qPattern}*")
        }

        violationRules {
            rule {
                enabled = true
                element = "CLASS"

                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    // TODO Fix minimum ratio after increasing test coverage ratio
                    minimum = "0.00".toBigDecimal()
                }

                limit {
                    counter = "LINE"
                    value = "TOTALCOUNT"
                    maximum = "200".toBigDecimal()
                    // TODO Fix minimum ratio after increasing test coverage ratio
                    minimum = "0.00".toBigDecimal()
                }

                excludes = qDomains
            }
        }

        val excludes = listOf(
                "com/whatever/raisedragon/config",
                "com/whatever/raisedragon/common",
                "com/whatever/raisedragon/infra",
                "com/whatever/raisedragon/common/config",
                "com/whatever/raisedragon/aws",
                "com/whatever/raisedragon/applicationservice/*/dto"
        )

        classDirectories.setFrom(
                sourceSets.main.get().output.asFileTree.matching {
                    exclude(excludes)
                }
        )
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
