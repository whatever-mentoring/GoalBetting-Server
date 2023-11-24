dependencies {
    api(project(":raisedragon-core"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}