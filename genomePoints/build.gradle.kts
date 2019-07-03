import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow")
    `java-library`
}

dependencies {
    compile("info.picocli:picocli:3.9.6")
    compile("com.opencsv:opencsv:4.6")
}

tasks.getByName<Zip>("shadowDistZip").archiveClassifier.set("shadow")
tasks.getByName<Tar>("shadowDistTar").archiveClassifier.set("shadow")

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    isZip64 = true
}

application {
    mainClassName = "query.genome.CliRunner"
}

