dependencies {
    api(project(":raisedragon-core"))
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")

    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.0.4")
}

application {
    mainClass.set("com.whatever.raisedragon.RaiseDragonApiApplicationKt")
}
