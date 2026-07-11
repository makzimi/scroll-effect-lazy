# Scroll Effect Lazy — Cleanup, Samples, and Publishing

**Date:** 2026-07-11
**Status:** Approved design

## Goal

Polish the `scroll-effect-lazy` Compose Multiplatform library and take it to a
publishable state:

1. Fix the velocity-timestamp bug in the scroll controller.
2. Fix the sample-package naming inconsistency.
3. Ensure runnable Android and iOS samples (Compose Multiplatform).
4. Configure Maven Central publishing.
5. Update all docs/READMEs.

## Working agreement (execution protocol)

- Work proceeds **one step at a time**, in the order below.
- **After each step, STOP** and ask the user to run the relevant sample /
  build and confirm it works before moving on.
- For the **Xcode project** and **Maven Central** steps, the assistant does all
  the file/config work it can, then **pauses and guides the user** through the
  parts that require their machine/accounts (opening Xcode, account signup, GPG
  keys, credentials). Nothing is published until the user explicitly says go.

## Context (as-is)

- Toolchain: Kotlin 2.3.21, AGP 9.1.1, Compose Multiplatform 1.11.0-beta03,
  vanniktech maven-publish 0.36.0 (already in the version catalog).
- Modules in `settings.gradle.kts`: `:scroll-effects` (library),
  `:sample-common` (shared demo UI), `:sample-android` (Android app).
- Shared UI root is `ElasticListTheme { AppNavigation() }`, duplicated across the
  JVM, Wasm, and Android entry points. iOS entry point is a stub `fun main()`.
- Library public API is already correct: `ScrollEffectScope.elastic(...)` exists
  and matches the README. **No library API changes.**
- `gradle.properties` already carries full POM metadata for publishing with
  `GROUP=io.github.makzimi`, `POM_ARTIFACT_ID=scroll-effects`,
  `VERSION_NAME=0.1.0`.
- A dead top-level `sample/` directory exists (only `.DS_Store` + empty `res/`),
  not referenced by `settings.gradle.kts`.

## Step 1 — Timing fix

**Problem:** `ScrollEffectController.kt:100` computes the VelocityTracker
timestamp as
`Clock.System.now().toLocalDateTime(TimeZone.UTC).nanosecond / 1_000_000L`.
That is the nanosecond-of-second field — it wraps to 0 every second and is not
monotonic, so velocity calculations are corrupted across each 1-second boundary.

**Fix:**
- Introduce a monotonic time base: capture
  `kotlin.time.TimeSource.Monotonic.markNow()` once when the controller is
  constructed (store as a private val).
- Replace the timestamp expression with
  `startMark.elapsedNow().inWholeMilliseconds`.
- Remove the now-unused `kotlinx.datetime` and `kotlin.time.Clock` imports.
- Remove the `kotlinx-datetime` dependency from `scroll-effects/build.gradle.kts`
  (it was used only for this timestamp). Leave the catalog entry in place in case
  the samples want it; only the library module drops the dependency.

**Verification:** library compiles (`./gradlew :scroll-effects:compileKotlin*`
or an assemble). User runs a sample (e.g. desktop `:sample-common:run` or
Android) and confirms fast scroll/fling still feels elastic and no longer glitches.

## Step 2 — Naming fix

Rename all sample code from `com.maxkach.elasticlist` (and
`com.maxkach.elasticlist.common`) to **`com.maxkach.scrolleffects.sample`**.

Scope:
- All Kotlin files under `sample-common/src/commonMain/...` — package decls +
  imports (navigation, ui.theme, ui.effects, ui.screens.*).
- Entry points: `sample-common/src/jvmMain/kotlin/Main.kt`,
  `sample-common/src/wasmJsMain/kotlin/Main.kt`,
  `sample-common/src/iosMain/kotlin/Main.kt` (replaced in Step 3),
  `sample-android/src/main/java/.../MainActivity.kt`.
- `sample-android/build.gradle.kts`: `namespace` and `applicationId`
  → `com.maxkach.scrolleffects.sample`.
- `sample-common/build.gradle.kts`: android `namespace`
  → `com.maxkach.scrolleffects.sample.common`.
- `sample-android` test files under `src/test` and `src/androidTest`
  (package + directory path).
- Move source directories to match the new package path.
- Rename the `ElasticListTheme` composable → `SampleTheme`, update all call
  sites and the desktop window title ("Elastic List" → "Scroll Effect Lazy").

Also extract the duplicated root into a single `App()` composable in
`sample-common/src/commonMain` (`SampleTheme { AppNavigation() }`), and have the
JVM, Wasm, Android, and iOS entry points call `App()`.

**Verification:** Android + desktop + wasm build. User runs a sample and confirms
it still works and nothing regressed.

## Step 3 — iOS sample (Compose Multiplatform)

Android sample already exists; this step delivers the iOS one.

1. **Framework binary** — in `sample-common/build.gradle.kts`, configure the iOS
   targets with a framework:
   ```
   listOf(iosArm64(), iosSimulatorArm64()).forEach {
       it.binaries.framework {
           baseName = "SampleCommon"
           isStatic = true
       }
   }
   ```
2. **Kotlin entry point** — replace `iosMain/Main.kt` stub with
   `MainViewController.kt`:
   ```
   fun MainViewController() = ComposeUIViewController { App() }
   ```
3. **Xcode project** — hand-write a minimal `iosApp/` project:
   - `iosApp/iosApp.xcodeproj/project.pbxproj`
   - `iosApp/iosApp/iOSApp.swift` (SwiftUI `@main` App)
   - `iosApp/iosApp/ContentView.swift` (`UIViewControllerRepresentable` wrapping
     `MainViewControllerKt.MainViewController()`)
   - `iosApp/iosApp/Info.plist`
   - Asset catalog (`Assets.xcassets`) with app icon + accent placeholders
   - A "Run Script" build phase that runs
     `./gradlew :sample-common:embedAndSignAppleFrameworkForXcode`
     and adds `$(SRCROOT)/../sample-common/build/xcode-frameworks/...` to the
     framework search paths.

**Assistant does:** all Gradle + Kotlin + Swift + pbxproj files, then iterates
with `xcodebuild` against a booted simulator until the app compiles and launches.

**Pause-and-guide:** if hand-written pbxproj wiring needs the user to open Xcode
(e.g. to fix signing team, confirm scheme, or trust the run-script), the
assistant pauses with precise instructions.

**Verification:** `xcodebuild ... -sdk iphonesimulator build` succeeds; app
boots in the simulator showing the demo. User confirms on their machine.

## Step 4 — Verify all targets + housekeeping

- Confirm Android, desktop (JVM), and Wasm samples all build/run after Steps 1–3.
- Delete the dead top-level `sample/` directory.

**Verification:** user runs each sample they care about and confirms.

## Step 5 — Maven Central publishing

**Assistant does (build config):**
- Fill the empty `mavenPublishing {}` block in `scroll-effects/build.gradle.kts`:
  - `publishToMavenCentral()` (Central Portal host)
  - `signAllPublications()`
  - POM is auto-populated from the existing `gradle.properties` `POM_*` props.
- Remove the JitPack path: delete `jitpack.yml`.
- Run `./gradlew :scroll-effects:publishToMavenLocal` as a dry run to prove the
  artifact + POM + sources + javadoc jars assemble correctly.

**Pause-and-guide (user actions):** the assistant provides a precise checklist:
1. Create a Sonatype Central Portal account.
2. Verify the `io.github.makzimi` namespace (GitHub-based verification).
3. Generate a GPG key, upload the public key to a keyserver.
4. Put `mavenCentralUsername`, `mavenCentralPassword`, and signing key/password
   into `~/.gradle/gradle.properties`.
5. Run the real publish only when the user says go.

Nothing is published without explicit user confirmation.

## Step 6 — Docs

- `README.md`: change the install section to Maven Central coordinates
  (`implementation("io.github.makzimi:scroll-effects:0.1.0")` with
  `mavenCentral()` in `settings.gradle.kts`), remove the JitPack repository/coord
  instructions, add a "Running the samples" section covering Android, iOS,
  desktop, and web. Fix branding/coordinate inconsistencies.
- Update any other stale references discovered during the work.

**Verification:** user reviews the README.

## Out of scope

- No changes to the library's public API surface.
- No new effect presets or features.
- No CI/CD automation for publishing (manual publish for now).

## Risks

- **Xcode pbxproj by hand** is the highest-risk item; mitigated by iterative
  `xcodebuild` runs and the pause-and-guide fallback.
- **CMP 1.11.0-beta03 / Kotlin 2.3.21** are recent; if any target fails to build
  for version reasons, surface it rather than silently downgrading.
