# Compose Multiplatform Migration Plan (Android + iOS)

## 1) Current-state analysis of this repository

This codebase is a single Android app module (`:app`) with XML/DataBinding-based UI, Fragments/Activities, Room persistence, Hilt DI, Retrofit networking, and Android-specific adapters/custom views.

### What this means for migration

- **UI rewrite is mandatory** for iOS support because current screens are XML + Fragment/Activity based.
- **Business/data layer is partially reusable** because repositories already use Kotlin + coroutines + flows.
- **Persistence/DI/network stacks need KMP-compatible replacements or abstractions**.

## 2) Target architecture (practical end state)

Use a multi-module Kotlin Multiplatform setup while keeping Android shipping during migration:

- `shared:core-model` (common data models / mappers)
- `shared:core-network` (Ktor client + API interfaces)
- `shared:core-database` (SQLDelight)
- `shared:core-domain` (use-cases + repository interfaces)
- `shared:feature-*` (state holders / view models in common)
- `composeApp` (Compose Multiplatform UI target: Android + iOS)
- Optional temporary `app` (legacy Android app for incremental coexistence)

## 3) Migration strategy (incremental, low-risk)

### Phase 0 — Discovery and baseline (1 week)

1. Freeze feature development except critical bug fixes.
2. Record baseline quality gates:
   - startup time
   - key screen render times
   - crash-free sessions
   - API error rate
3. Catalog all Android-only dependencies and map replacements.
4. Define “done” criteria for each migrated screen.

**Exit criteria:** baseline metrics + dependency replacement matrix approved.

---

### Phase 1 — Build system and module scaffolding (1–2 weeks)

1. Upgrade Gradle setup to modern plugin management (`settings.gradle.kts`, version catalogs).
2. Add Kotlin Multiplatform + Compose Multiplatform plugins.
3. Create `shared` KMP modules with `commonMain`, `androidMain`, `iosMain` source sets.
4. Add iOS targets (`iosArm64`, `iosSimulatorArm64`, `iosX64`) and CocoaPods/SPM integration.

**Exit criteria:** Android and iOS sample screen builds from Compose Multiplatform shell app.

---

### Phase 2 — Data + domain extraction first (2–3 weeks)

1. Move pure models from `app` into `shared:core-model`.
2. Introduce repository interfaces in common code.
3. Replace Retrofit/OkHttp usage with Ktor-based client in `shared:core-network`.
4. Migrate Room entities/DAO to SQLDelight schema + queries in `shared:core-database`.
5. Keep old Android repository implementation temporarily as fallback behind interface toggles.

**Exit criteria:** movie list/detail data flows run from shared module on Android and iOS test harness.

---

### Phase 3 — State management for shared UI logic (1–2 weeks)

1. Replace Android `ViewModel` dependencies in shared features with platform-neutral state holders
   (e.g., Molecule/Flow-based Store or KMP ViewModel library).
2. Define unidirectional state/events/effects contracts per feature.
3. Move pagination/caching/error states into common layer.

**Exit criteria:** screen state logic executes in common tests without Android runtime.

---

### Phase 4 — Compose UI migration by vertical slices (4–6 weeks)

Migrate feature-by-feature, not layer-by-layer:

1. Home tabs (Movies / TV / People)
2. Movie detail
3. TV detail
4. Person detail

For each slice:
- build Compose screen in `commonMain`
- add platform wrappers for navigation/system UI
- compare behavior with legacy screen
- ship behind feature flag on Android first
- then enable on iOS

**Exit criteria:** all core screens available in Compose Multiplatform.

---

### Phase 5 — Platform integration and parity hardening (2 weeks)

1. Image loading abstraction (Coil 3 KMP or Kamel) with caching checks.
2. Navigation strategy finalized (Voyager/Decompose/Navigation Compose MP).
3. Deep links, lifecycle/background handling, and analytics parity.
4. Theme parity for Android/iOS and accessibility fixes.

**Exit criteria:** parity checklist signed off by QA and product.

---

### Phase 6 — Cutover and cleanup (1 week)

1. Remove legacy XML/DataBinding screens after stable rollout.
2. Remove legacy adapters/custom view dependencies.
3. Collapse temporary compatibility layers.
4. Update CI/CD for Android + iOS artifacts and automated tests.

**Exit criteria:** no production path through legacy Android UI.

## 4) Dependency replacement map (recommended)

- **DataBinding + XML + Fragments/Activities** → Compose Multiplatform UI + navigation wrapper
- **Hilt (Android only)** → Koin (KMP-friendly) or manual DI composition root
- **Room** → SQLDelight (shared DB)
- **Retrofit/OkHttp annotations** → Ktor client
- **Glide** → Coil 3 KMP/Kamel image loader
- **BaseRecyclerViewAdapter/custom adapters** → Compose lazy lists
- **Custom Android Views** → Compose equivalents or platform-specific expect/actual wrappers

## 5) Risk analysis (practical)

| Risk | Probability | Impact | Why it matters here | Mitigation |
|---|---|---|---|---|
| UI rewrite scope under-estimated | High | High | Current app is XML/DataBinding + Fragment-heavy | Migrate by vertical slice with feature flags; avoid big-bang |
| Data layer portability blockers | Medium | High | Room/Hilt/Retrofit are Android-first patterns in current code | Interface-first extraction; keep fallback Android impl during transition |
| Performance regressions in lists/detail screens | Medium | Medium-High | App is media/list heavy with images and rich detail layouts | Benchmark lazy list/image cache early; baseline and gate releases |
| iOS team/tooling ramp-up | Medium | Medium | Existing repo is Android-centric | Add iOS CI lane early; pair Android+iOS reviews on first 2 slices |
| Build complexity and CI instability | High | Medium | Going from single module to KMP multi-target | Incremental module introduction + deterministic dependency versions |
| Feature parity gaps (navigation/deeplink/analytics) | Medium | Medium | Android integrations currently embedded in Activities/Fragments | Explicit parity checklist per slice before rollout |
| QA matrix expansion | High | Medium | Two platforms, multiple OS/device targets | Prioritize critical journeys; automate snapshot + API contract tests |

## 6) Recommended timeline (example)

- **Weeks 1–2:** Phases 0–1
- **Weeks 3–5:** Phase 2
- **Weeks 6–7:** Phase 3
- **Weeks 8–13:** Phase 4 (vertical slices)
- **Weeks 14–15:** Phase 5
- **Week 16:** Phase 6 and decommission

## 7) Team and delivery model

- 1 tech lead (KMP architecture)
- 1–2 Android engineers (migration + parity)
- 1 iOS engineer (integration + native concerns)
- 1 QA automation engineer
- Product/design support for parity decisions

Use **weekly go/no-go** checkpoints with objective metrics:
- Build health
- Crash-free rate
- Startup/perf deltas
- Parity checklist completion per feature

## 8) Immediate next actions for this repository

1. Create a branch `kmp-bootstrap` and scaffold KMP modules without removing `:app`.
2. Migrate one low-risk path first (e.g., movie list read-only).
3. Add shared API client + SQLDelight proof-of-concept wired into Android.
4. Validate iOS simulator rendering of the same feature before expanding scope.

This keeps delivery practical: maintain current Android behavior while progressively moving logic/UI into common code.
