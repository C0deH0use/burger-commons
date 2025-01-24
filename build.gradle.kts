plugins {
	java
	idea
	jacoco
	checkstyle
}

checkstyle {
	toolVersion = "10.12.1"
	configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
	sourceSets = listOf(project.sourceSets.main.get())
}

group = "pl.codehouse.commons"
version = "0.0.1-SNAPSHOT"
val junitVersion = "5.11.4"
val commonsLang = "3.17.0"
val reactorCore = "3.7.2"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.apache.commons:commons-lang3:$commonsLang")
	implementation("io.projectreactor:reactor-core:$reactorCore")

	testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)

	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		csv.required = true
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

tasks.withType<JacocoReport>().configureEach {
	dependsOn(project.tasks.withType<Test>())
	// execution data needs to be aggregated from all exec files in the project for multi jvm test suite testing
	tasks.withType<Test>().forEach(::executionData) // confusing
}

tasks.withType<JacocoCoverageVerification>().configureEach {
	dependsOn(project.tasks.withType<JacocoReport>())
	// execution data needs to be aggregated from all exec files in the project for multi jvm test suite testing
	executionData(project.tasks.withType<JacocoReport>().map { it.executionData })
}

tasks.withType<Test>().configureEach {
	maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
	setForkEvery(100)
	reports.html.required.set(true)
}
