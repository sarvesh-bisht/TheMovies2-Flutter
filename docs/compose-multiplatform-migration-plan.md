# Compose Multiplatform Migration Plan (Android + iOS)

## Short answer to “any changes in plan if we actually execute this?”

Yes — I would **change the sequencing** to reduce risk:

1. Add a **Phase 0.5 (Android hardening)** before KMP extraction.
2. Start with a **single thin vertical slice** (Movie list read-only) as the only POC scope.
3. Delay full database migration (Room → SQLDelight) until after network/domain are stable in KMP.
4. Keep a **dual-run fallback path on Android** for at least 2 releases.

This avoids a big-bang rewrite and gives measurable checkpoints before committing to full cutover.

---

## 1) Current-state analysis of this repository

This repo is currently an Android app (`:app`) using:
- XML/DataBinding + Fragment/Activity UI
- Hilt DI
- Room persistence
- Retrofit/OkHttp networking
- Android-specific adapters/custom views

### Migration implications

- **UI must be rewritten** for iOS (current XML/Fragment stack is Android-only).
- **Repositories and coroutine/Flow patterns are reusable** conceptually.
- **DI, DB, networking need platform-neutral abstractions** for KMP.

---

## 2) Target architecture (practical end state)

Recommended modules:

- `composeApp` — Compose Multiplatform app shell (Android + iOS entry)
- `shared:core-model` — DTO/domain model + mappers
- `shared:core-network` — Ktor client + TMDB service layer
- `shared:core-database` — SQLDelight schema + queries
- `shared:core-domain` — use cases + repository interfaces
- `shared:feature-home` / `shared:feature-detail-*` — state holders + UI contracts
- legacy `:app` — temporary coexistence until full cutover

---

## 3) Revised practical migration strategy (execution-oriented)

### Phase 0 — Baseline & constraints (1 week)

1. Freeze non-critical feature work.
2. Capture baseline metrics (startup, list scroll FPS, crash-free rate, API failure rate).
3. Build dependency replacement matrix with owner per dependency.
4. Define acceptance criteria per screen (UI parity + behavior parity).

**Exit gate:** measurable baseline and written parity checklist.

### Phase 0.5 — Android hardening before KMP (1 week) **(new)**

1. Add/expand tests around current critical flows (movie list, movie detail).
2. Introduce stable repository interfaces in current app without behavior changes.
3. Isolate Android framework calls behind wrappers where needed.

**Why this change:** lowers migration risk by separating refactor risk from platform migration risk.

**Exit gate:** no regression on Android after interface extraction.

### Phase 1 — KMP bootstrap (1–2 weeks)

1. Add Kotlin Multiplatform + Compose Multiplatform setup.
2. Create `commonMain/androidMain/iosMain` source sets.
3. Configure iOS targets (`iosArm64`, `iosSimulatorArm64`, `iosX64`).
4. Build a hello-world Compose screen on both Android and iOS simulator.

**Exit gate:** repeatable CI build for Android + iOS simulator.

### Phase 2 — Network/domain first, DB later (2–3 weeks) **(changed sequencing)**

1. Move API contracts and response mapping into `shared:core-network` (Ktor).
2. Move use-cases/repository interfaces into `shared:core-domain`.
3. Keep Room temporarily on Android via adapter implementation.

**Why this change:** DB migration is the riskiest early task; defer until service/domain contracts stabilize.

**Exit gate:** shared domain flow powers movie list on Android and iOS mock/stub.

### Phase 3 — Vertical slice POC (Movie list read-only) (2 weeks) **(narrowed scope)**

1. Build Movie list UI in Compose Multiplatform.
2. Use shared state holder (Flow-based state/events).
3. Run A/B on Android behind feature flag.
4. Render same slice on iOS simulator.

**Exit gate:** one feature in production-like quality across both platforms.

### Phase 4 — DB migration + caching parity (2 weeks)

1. Introduce SQLDelight schema for movie/tv/people.
2. Implement repository adapters for SQLDelight and deprecate Room path gradually.
3. Validate cache behavior and offline/empty/error states.

**Exit gate:** SQLDelight-backed flow parity with old behavior.

### Phase 5 — Remaining screens by vertical slices (4–6 weeks)

Migrate in order:
1. TV list + detail
2. Person list + detail
3. Remaining shared UI components and transitions

For each slice:
- feature flag rollout (Android first)
- parity validation checklist
- iOS enablement after Android stability

**Exit gate:** all core journeys available in Compose Multiplatform.

### Phase 6 — Cutover & decommission (1–2 weeks)

1. Keep legacy Android stack as fallback for 2 releases (recommended).
2. Remove XML/DataBinding/adapters once adoption metrics are stable.
3. Clean module graph and simplify CI to new targets.

**Exit gate:** no production traffic on legacy UI path.

---

## 4) Dependency replacement map

- XML/DataBinding/RecyclerView adapters → Compose Multiplatform UI (`LazyColumn`, composables)
- Hilt (Android-only) → Koin KMP or manual DI composition root
- Retrofit/OkHttp annotations → Ktor client
- Room → SQLDelight
- Glide → Coil 3 KMP (or Kamel)
- Android custom views → Compose replacements / `expect-actual` wrappers

---

## 5) Risk analysis and mitigation

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Underestimated UI rewrite effort | High | High | Vertical-slice rollout + strict definition-of-done per screen |
| Room→SQLDelight migration bugs | Medium | High | Defer DB migration until network/domain stable; dual-run comparison tests |
| Performance regressions in media lists | Medium | High | Baseline FPS/startup + perf gates before rollout expansion |
| KMP/iOS tooling ramp-up | Medium | Medium | Start iOS CI in Phase 1; pair-review first 2 slices |
| CI/build instability in multi-module setup | High | Medium | Lock plugin versions + incremental module introduction |
| Feature parity drift | Medium | Medium | Mandatory parity checklist + release gates per slice |
| Rollback complexity | Medium | High | Keep Android legacy fallback for at least 2 releases |

---

## 6) Timeline (revised realistic example)

- Weeks 1–2: Phase 0 + 0.5
- Weeks 3–4: Phase 1
- Weeks 5–7: Phase 2
- Weeks 8–9: Phase 3 (POC slice)
- Weeks 10–11: Phase 4
- Weeks 12–16: Phase 5
- Weeks 17–18: Phase 6

---

## 7) Team model

Minimum effective team:
- 1 KMP tech lead
- 1–2 Android engineers
- 1 iOS engineer
- 1 QA automation engineer

Weekly review gates:
- Build health (Android + iOS)
- Crash/perf deltas vs baseline
- Slice parity score
- Rollout readiness decision (go/hold/rollback)

---

## 8) Immediate next actions (this repository)

1. Bootstrap `composeApp` + `shared:*` modules while keeping `:app` untouched.
2. Extract repository interfaces from current Android implementations.
3. Implement TMDB network path in `shared:core-network` with Ktor.
4. Deliver only **Movie list read-only** as first end-to-end KMP slice.
5. Decide go/no-go for full migration based on objective metrics from that slice.

This revised plan is intentionally conservative: prove value with one real slice, then scale.
