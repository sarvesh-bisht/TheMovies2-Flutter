# Phase 3 Gate — Movie List Parity Checklist

This checklist is the **Phase 3 go/no-go gate** for the movie-list vertical slice in `composeApp`.

## Scope
- Target: Movie list read-only slice.
- Legacy reference: `:app` main movie tab.
- New path: `:composeApp` movie tab and detail open/close flow.

## Visual parity
- [ ] Brand colors match legacy shell (top/bottom pink bars, dark background).
- [ ] Movie posters use a two-column grid and poster/title card ratio close to legacy.
- [ ] Title overlay readability and truncation behavior are acceptable.
- [ ] Detail header/poster/title spacing is acceptable compared to legacy.

## Behavior parity
- [ ] Initial load opens on Movie tab and fetches data automatically.
- [ ] Tapping a movie poster opens detail surface.
- [ ] Back action from detail returns to the previous list state.
- [ ] Bottom tab selection keeps expected navigation behavior.

## States parity
- [ ] **Loading state** is visible during initial fetch.
- [ ] **Error state** is visible and understandable when network fails.
- [ ] **Empty state** is visible when list payload is empty.

## Rollback/readiness checks
- [ ] Legacy `:app` path remains untouched and runnable.
- [ ] Compose path can be validated independently via `:composeApp`.
- [ ] Decision recorded: **Go** (advance) or **Hold** (fix parity gaps first).

## Recommended verification commands
Run from Android Studio terminal (JDK configured by IDE):

```bash
./gradlew :composeApp:assembleDebug
./gradlew :shared:assemble
```

If both pass and checklist items are accepted, proceed to **Phase 4 (database migration)**.
