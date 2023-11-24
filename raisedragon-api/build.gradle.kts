dependencies {
    api(project(":raisedragon-core"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
}

application {
    mainClass.set("com.whatever.raisedragon.RaiseDragonApiApplicationKt")
}
