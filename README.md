# Overview
VPartnerLib is a SDK that allows Partners to easily integrate Vodafone World into their apps.
The SDK will allow partners to centralize and do everything related with their devices under one app. This means:

- Onboard users into the Vodafone CIoT Platform, being the users Vodafone or Non-Vodafone.
- Manage their device subscriptions. This includes, adding, editing, removing and perform paying operations related to their devices and subscriptions.

The SDK is built under Vodafone's design system, but allows for the Partners to use their own logos and colours for a seamless experience.

# Library Usage

In order to run the SDK a PartnerPassword. Each partner is entitled to one. Contact the SquaDK team in order to get yours.

### Initialization expose a class VPartnerLib that initializes the SDK

VPartnerLib The initialization accepts the following parameters:

• PartnerPassword (mandatory)

• PartnerCode (mandatory)

• ClientID (optional)

• GrantID (optional)

• PartnerLogo (optional)

• SponsorLogo (optional)

• Locale (optional)

• useDeeplink (optional)

• VisualMode (optional)

Integration:

- VPartnerLib(partnerPassword = PARTNER_PASSWORD, clientId = CLIENT_ID, grantId = GRANT_ID, partnerCode = PARTNER_CODE, partnerLogo = PARTNER_LOGO, sponsorLogo = SPONSOR_LOGO, locale = LOCALE, useDeeplink = USE_DEEPLINK, visualMode = VISUAL_MODE)

Initializing does not trigger the SDK, it only sets these values internally. At the exception of partnerLogo and sponsorLogo, all of these parameters are mandatory.
If partnerLogo or sponsorLogo are empty, the splash screen won’t have the partners or the sponsor logos. To fill in either of them, define the desired parameter with the name of your image file without the extensions (png, jpeg, etc), which you need to put into “assets” folder in the project. If Locale is empty, the selected language will be the SIM card language, if you want another language, just pass it as string(ex: "it","es","de","za","gr,"pt","ie").

The useDeeplink optional parameter defines if the app should use the predefined deeplink or just close the SDK. It should receive a Boolean value (true or false), which will be used to define if should navigate to deeplink (true) or not (false). (DEPRECATED)

For the VisualMode parameter, you'll have access to the Enum declared on the SDK. It needs one of the two following values:
VisualMode.LightMode or VisualMode.DarkMode. If no value is passed, the SDK will use the what is defined by the user on the system settings.

It's also required to set targetSdkVersion and minSdkVersion to 33 since we are using updated libs.

We use kotlin version 1.8.0

### Add Device After initialize the VPartnerLib

To call this method use `VPartnerLib.addDevice(context, productId, productCode, callback)`. At the exception of `productId and callback`, all of these parameters are mandatory.

If productId is filled goes straight to identifying your product screen after login. If it is empty, starts the AddDevice flow.

`callback` is passed to AddDevice so you can have the onboarding status every time it gets updated. Remember: it must be a function that receives a string as parameter, and you do anything you want with it. The string is a stringified json object, containing:

{
status: "fail" | "pending" | "success"
}

### Manage Subscriptions After initialize the VPartnerLib

To call this method use VPartnerLib.manageSubscriptions(context).

### Extend MyApplication class from Library to main Application App to initialize Idtm and Library

	class MyApp : MyApplication() {

   	 override fun onCreate() {
       	 super.onCreate()
    	}
	}

### Step 1: Update build.gradle.kts inside the application module

inside dependencies of the build.gradle.kts of app module, use the following code

      dependencies {
            // Firebase
            implementation platform ('com.google.firebase:firebase-bom:28.2.0')
            implementation 'com.google.firebase:firebase-messaging'
            
            // Dagger-Hilt
            implementation "com.google.dagger:hilt-android:$hilt_version"
            kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
            
            implementation 'com.google.guava:guava:30.1-jre'
            
            // IDTM
            implementation files('libs/idtmlib-release-v3.0.3.aar')

            // VpartnerLib
            implementation 'com.vpartnerlib.sdk:prod:3.0.0'
	...}

## Step 2: Hilt

We are now using Dagger-Hilt for DI, to get it up and running with your app, you have to annotate your app's entry point, like we are doing here: app/src/main/java/com/example/vduk_iot_v_app/MyApp.kt

## Step 3: Other Configs

In gradle make sure that build features include databinding and viewbinding with value true inside android group

buildFeatures { dataBinding = true // for view binding: viewBinding = true }

• Also add multiDexEnabled true to your defaultConfig on gradle.

• Bear in mind that minSdkVersion needs to be updated to at least version 28.

• This SDK needs Google-services so please make sure to download and install Google-Services on the SDK Manager. Afterwards, add the google-services plugin to your gradle. You will also need to include kotlin-parcelize.

• In general, make sure you have these plugins already on your gradle. If not, it’s mandatory to add them.
```
apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'com.google.gms.google-services'
apply plugin: 'kotlin-parcelize'
apply plugin: 'dagger.hilt.android.plugin'
```

### Step 4: Jumio, Hilt and JitPack dependencies

Add Jumio SDK url to your maven under repositories in allprojects maven { url 'https://mobile-sdk.jumio.com' }

Add SecLib (Smapi) url to your maven under repositories in allprojects maven { url 'https://nexus.analytics.vodafone.com/repository/maven-releases/' } classpath "com.vodafone.smapi.analytics:android-plugin:2.0.0"

	// Top-level build file where you can add configuration options common to all sub-projects/modules.
	buildscript {
    ext.kotlin_version = "1.8.0"
    ext.hilt_version = "2.43.2"

    repositories {
        google()
        mavenCentral()

        maven { url 'https://nexus.analytics.vodafone.com/repository/maven-releases/' }
    }

    dependencies {
        classpath "com.android.tools.build:gradle:7.4.2"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"

        //SecLib
        classpath "com.vodafone.smapi.analytics:android-plugin:2.0.0"
        classpath 'com.google.gms:google-services:4.3.15'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
	}

	def githubProperties = new Properties() //needed  for download partner lib
	githubProperties.load(
        new FileInputStream(rootProject.file("github.properties"))
	)

	allprojects {
        repositories {
            google()
            mavenCentral()
            mavenLocal()
            jcenter() // Warning: this repository is going to shut down soon

            maven { url 'https://mobile-sdk.jumio.com' }
            maven { url 'https://nexus.analytics.vodafone.com/repository/maven-releases/' }
            maven { url 'https://jitpack.io' }
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.github.com/squadk-celfocus/public-partner-sdk-example-android")
                credentials {
                    username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
                    password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
                }
            }
        }
	}

	task clean(type: Delete) {
    delete rootProject.buildDir
	}

### Step 5: Gradle.properties

• Set jetifier to true android.enableJetifier = true

## Step 6: IDTM Library integration

Add IdtmLib implementation to your gradle as a dependency:

implementation files('libs/idtmlib-release-v3.0.3.aar')

Follow this link https://developer.android.com/studio/projects/android-library#psd-add-aar-jar-dependency to add aar files to the main project.

Package whitelisting requirements:

• For Idtm Library, first it is needed to whitelist the package of your project. Only after the whitelisting is successful you’ll be able to make use of the SDK. To check the package of your project to to Manifest and you’ll find it there.

• Also for Idtm Library to work, google-services.json is needed on your project root. To generate this file, go to https://console.firebase.google.com/ , create a new project and select android to generate this file with the correct package of your android root app.

## Step 7: Add following meta data to your AndroidManifest.xml

        <meta-data
            android:name="com.vodafone.lib.seclibng.CUSTOM_TRACE_SOURCE"
            android:value="com.vodafone.smartlife.vpartner"
            tools:replace="android:value" />

### network_security_config.xml

On app/src/main/res/xml you'll find the network_security_config xml. This file is needed to trust our staging domain. Include it on your project, same path.


## Contact
If you have any questions regarding our implementation guide and/or the SDK itself, please contact SquaDK team at squadk@celfocus.com.
