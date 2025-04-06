plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.app.connectcare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.connectcare"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.vertexai)
    implementation(libs.firebase.inappmessaging.display)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-database:20.2.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.google.android.gms:play-services-wallet:19.2.0")
    implementation ("com.google.android.gms:play-services-wallet:18.1.3")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("com.google.firebase:firebase-database:21.0.0")
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.firebase:firebase-firestore:24.9.1")
    implementation ("com.google.firebase:firebase-storage:20.3.0")

    // Glide for image loading
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.firebase:firebase-inappmessaging-display:21.0.2")
    implementation ("com.google.firebase:firebase-firestore:25.1.3")
    implementation ("com.google.firebase:firebase-vertexai:16.3.0")
}
