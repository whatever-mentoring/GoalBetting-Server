dependencies {
    implementation(project(":raisedragon-core"))

    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.0.4")

    // aws
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
}