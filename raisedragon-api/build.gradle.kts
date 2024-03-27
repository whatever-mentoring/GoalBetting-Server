tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

val swaggerVersion: String by project.extra

dependencies {
    implementation(project(":raisedragon-common"))
    implementation(project(":raisedragon-core"))
    implementation(project(":raisedragon-external"))
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")

    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    testImplementation("org.springframework.security:spring-security-test")
}
