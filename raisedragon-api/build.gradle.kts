tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

val swaggerVersion: String by project.extra

dependencies {
    api(project(":raisedragon-core"))
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")

    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.0.4")
    
    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
}
