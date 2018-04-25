group = "com.jtwalraven"
version = "1.0-SNAPSHOT"

apply {
    plugin("java")
}

repositories {
    mavenCentral()
    maven {
        setUrl("http://maven.restlet.org")
    }
}

dependencies {
    compile("org.parboiled", "parboiled-java", "1.1.8")
    compile("org.apache.solr", "solr-core", "5.5.5")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}