# Phase 3 Gate — Movie List Parity Checklist

This checklist is the **Phase 3 go/no-go gate** for the movie-list vertical slice in `composeApp`.

## Scope
- Target: Movie list read-only slice.
- Legacy reference: `:app` main movie tab.
- New path: `:composeApp` movie tab and detail open/close flow.

## Visual parity
- [x] Brand colors match legacy shell (top/bottom pink bars, dark background).
- [x] Movie posters use a two-column grid and poster/title card ratio close to legacy.
- [x] Title overlay readability and truncation behavior are acceptable.
- [x] Detail header/poster/title spacing is acceptable compared to legacy.

## Behavior parity
- [x] Initial load opens on Movie tab and fetches data automatically.
- [x] Tapping a movie poster opens detail surface.
- [x] Back action from detail returns to the previous list state.
- [x] Bottom tab selection keeps expected navigation behavior.

## States parity
- [x] **Loading state** is visible during initial fetch.
- [x] **Error state** is visible and understandable when network fails.
- [x] **Empty state** is visible when list payload is empty.

## Rollback/readiness checks
- [x] Legacy `:app` path remains untouched and runnable.
- [x] Compose path can be validated independently via `:composeApp`.
- [x] Decision recorded: **Go** (advance) or **Hold** (fix parity gaps first).

## Recommended verification commands
Run from Android Studio terminal (JDK configured by IDE):

```bash
./gradlew :composeApp:assembleDebug
./gradlew :shared:assemble
```

If both pass and checklist items are accepted, proceed to **Phase 4 (database migration)**.


## Phase 3 Gate Decision

- Decision: **GO**
- Rationale: Movie-list parity checklist passed and rollback path remains available.
- Next phase: Start **Phase 4 — Database migration (SQLDelight)**.
