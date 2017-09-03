buildscript {
	val springBootVersion = "1.5.6.RELEASE"
	val kotlinVersion = "1.1.4-2"

	repositories {
		mavenCentral()
	}

	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
		classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	}
}

apply {
	plugin("kotlin")
	plugin("kotlin-spring")
	plugin("kotlin-jpa")
	plugin("org.springframework.boot")
}

version = "1.0.0-SNAPSHOT"

configure<JavaPluginConvention> {
	setSourceCompatibility(1.8)
	setTargetCompatibility(1.8)
}

repositories {
	mavenCentral()
}

dependencies {
	compile("org.springframework.boot:spring-boot-starter-web")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("org.jetbrains.kotlin:kotlin-stdlib:1.1.4-2")
	compile("org.jetbrains.kotlin:kotlin-reflect:1.1.4-2")
	compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("org.telegram:telegrambots:3.3")
    compile("org.postgresql:postgresql:42.1.4")
	testCompile("org.springframework.boot:spring-boot-starter-test")
}

