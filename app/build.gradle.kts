plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-parcelize")
	id("org.jetbrains.kotlin.plugin.compose")
	id("com.google.devtools.ksp")
	id("org.jetbrains.kotlin.plugin.serialization")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.dicoding.asclepius"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.dicoding.asclepius"
		minSdk = 24
		//noinspection OldTargetApi
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
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_17.toString()
	}

	buildFeatures {
		compose = true
		viewBinding = true
		mlModelBinding = true
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.15.0")
	implementation("androidx.activity:activity-ktx:1.9.3")
	implementation("androidx.fragment:fragment-ktx:1.8.5")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("com.google.android.material:material:1.12.0")
	implementation("androidx.constraintlayout:constraintlayout:2.2.0")
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	implementation("androidx.exifinterface:exifinterface:1.3.7")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

	implementation(platform("androidx.compose:compose-bom:2024.10.01"))
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
	implementation("androidx.compose.runtime:runtime")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.foundation:foundation")
	implementation("androidx.compose.foundation:foundation-layout")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.runtime:runtime-livedata")
	implementation("androidx.compose.ui:ui-tooling")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material:material-icons-extended")
	implementation("androidx.compose.ui:ui-text-google-fonts:1.7.5")

	implementation("com.github.yalantis:ucrop:2.2.9-native")
	implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
	implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")

	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-runtime:2.6.1")

	implementation("com.google.dagger:hilt-android:2.52")
	ksp("com.google.dagger:hilt-compiler:2.52")

	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
	implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
	implementation("com.github.skydoves:sandwich:2.0.10")
	implementation("com.github.skydoves:sandwich-retrofit:2.0.10")
	implementation("com.github.skydoves:sandwich-retrofit-serialization:2.0.10")
}