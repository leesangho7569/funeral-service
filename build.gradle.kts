plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.pcp"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

//	implementation("org.apache.commons:commons-lang3")
//	implementation("org.apache.httpcomponents.client5:httpclient5:5.1")

	implementation("org.apache.httpcomponents:httpclient:4.5.13")

	implementation ("com.google.code.findbugs:jsr305:3.0.2")

	implementation ("javax.annotation:javax.annotation-api:1.3.2")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
