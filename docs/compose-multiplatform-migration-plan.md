# Compose Multiplatform Migration Plan (Android + iOS)

## Objective

Deliver a **safe, incremental migration** from the current Android-only app to Compose Multiplatform (Android + iOS) without stopping Android releases.

## Scope and non-goals

### In scope
- Build KMP shared modules for data/domain/ui state.
- Deliver core user journeys on Android + iOS:
  - Home lists (Movies, TV, People)
  - Movie detail
  - TV detail
  - Person detail
- Keep rollback capability during migration.

### Not in scope (initial migration)
- New product features unrelated to migration.
- Full visual redesign.
- Rewriting every utility library on day one.

---

## Current repo reality (why sequencing matters)

This repository is Android-only (`:app`) and depends on:
- XML/DataBinding + Fragments/Activities
- Hilt
- Room
- Retrofit/OkHttp
- Android adapters/custom views

So, UI must be rewritten for iOS, while data/domain should be reused through interfaces and adapters.

---

## Practical target architecture

- `composeApp` — Compose Multiplatform shell (Android + iOS entrypoint)
- `shared:core-model` — shared models/mappers
- `shared:core-network` — Ktor TMDB client
- `shared:core-domain` — use-cases + repository contracts
- `shared:core-database` — SQLDelight (introduced after POC success)
- `shared:feature-home`, `shared:feature-movie-detail`, `shared:feature-tv-detail`, `shared:feature-person-detail`
- `:app` (legacy Android) remains active until cutover complete

---

## Execution plan (achievable sequence)

## Phase 0 — Baseline + migration guardrails (Week 1)

### Tasks
1. Freeze non-critical feature work.
2. Capture baselines:
   - cold start time
   - list scrolling smoothness
   - crash-free sessions
   - API error rate
3. Define migration Definition of Done (DoD) per screen:
   - visual parity
   - behavior parity
   - loading/error/empty states
4. Create dependency replacement matrix and owners.

### Exit criteria
- Baseline report published.
- Screen parity checklist approved.

---

## Phase 0.5 — Android hardening before KMP extraction (Week 2)

### Tasks
1. Add regression tests for movie list + movie detail critical flows.
2. Extract repository interfaces from current implementations.
3. Wrap Android-only calls behind small abstractions where needed.

### Exit criteria
- No behavior change in Android production path.
- Tests pass and release candidate can be built.

---

## Phase 1 — KMP bootstrap (Weeks 3–4)

### Tasks
1. Add KMP + Compose Multiplatform plugins.
2. Add source sets: `commonMain`, `androidMain`, `iosMain`.
3. Configure iOS targets: `iosArm64`, `iosSimulatorArm64`, `iosX64`.
4. Wire CI to compile Android + iOS simulator targets.
5. Render one hello-world Compose screen on both platforms.

### Exit criteria
- Green CI for Android + iOS simulator.
- Team can run app shell locally on both platforms.

---

## Phase 2 — Shared network/domain first (Weeks 5–7)

### Tasks
1. Move TMDB API contracts and response mapping to `shared:core-network` (Ktor).
2. Move use-cases and repository contracts to `shared:core-domain`.
3. Keep Room-backed Android adapter temporarily to reduce risk.
4. Add contract tests for key API responses.

### Exit criteria
- Shared domain flow feeds Movie list via mock/stub on Android + iOS.
- No production cutover yet.

---

## Phase 3 — Single vertical slice POC (Weeks 8–9)

### Scope (strict)
Only **Movie list read-only**.

### Tasks
1. Build Movie list UI in Compose Multiplatform.
2. Use shared state holder (Flow-based state/events).
3. Add Android feature flag for old/new UI toggle.
4. Validate same behavior on iOS simulator.

### Exit criteria
- Slice passes parity checklist.
- Android can rollback via feature flag.
- Go/No-Go decision for full migration.

---

## Phase 4 — Database migration (Weeks 10–11)

### Tasks
1. Add SQLDelight schema for movie/tv/people.
2. Implement SQLDelight repository adapters.
3. Run dual-read/compare checks against existing Room behavior in test/staging.
4. Validate offline/cache/error behavior.

### Exit criteria
- SQLDelight path matches Room behavior for critical flows.

---

## Phase 5 — Remaining features by vertical slices (Weeks 12–16)

### Order
1. TV list + detail
2. Person list + detail
3. Shared components and transitions

### Rule for each slice
- Android rollout behind feature flag first.
- Parity checklist signoff.
- Enable on iOS after Android stability window.

### Exit criteria
- All core journeys available in Compose Multiplatform.

---

## Phase 6 — Cutover + cleanup (Weeks 17–18)

### Tasks
1. Keep legacy Android fallback for 2 releases.
2. Remove XML/DataBinding/adapters after stability thresholds are met.
3. Simplify module graph + CI to final architecture.

### Exit criteria
- No production traffic on legacy path.
- Legacy code removed.

---

## Dependency migration map

- XML/DataBinding/RecyclerView → Compose Multiplatform composables
- Hilt (Android only) → Koin KMP or manual DI composition root
- Retrofit/OkHttp annotations → Ktor
- Room → SQLDelight
- Glide → Coil 3 KMP (or Kamel)
- Android custom views → Compose equivalents / `expect-actual`

---

## Practical risks and controls

| Risk | Probability | Impact | Control |
|---|---|---|---|
| UI rewrite larger than expected | High | High | Strict vertical slices + explicit DoD per screen |
| DB migration defects | Medium | High | Delay DB migration; dual-read compare before cutover |
| Performance regressions | Medium | High | Baseline metrics + perf gates before rollout expansion |
| KMP/iOS setup delays | Medium | Medium | Start iOS CI in Phase 1; pair reviews |
| Rollback complexity | Medium | High | Keep legacy Android path for two releases |
| CI instability in multi-module build | High | Medium | Lock versions + gradual module introduction |

---

## Team and operating model

Minimum team:
- 1 KMP lead
- 1–2 Android engineers
- 1 iOS engineer
- 1 QA automation engineer

Weekly governance:
- build health
- parity score per slice
- crash/perf delta vs baseline
- explicit go/hold/rollback decision

---

## First 4 weeks: concrete deliverables

- Week 1: Baseline report + parity checklist.
- Week 2: Android hardening merged without behavior regressions.
- Week 3: KMP modules compile in CI.
- Week 4: iOS simulator build + shared hello screen running.

If these are not met, pause and re-scope before Phase 2.

---

## Immediate next actions for this repository

1. Keep `:app` untouched for now.
2. Bootstrap `composeApp` + `shared:*` modules.
3. Extract repository interfaces from current repositories.
4. Implement shared TMDB network path in Ktor.
5. Ship only Movie list read-only slice first, then reassess.

This plan is intentionally conservative and achievable: prove one slice end-to-end, then scale.
