# Scroll Effect Lazy

A Jetpack Compose library that adds an **elastic stretch effect** to `LazyColumn` and `LazyRow`. Items on the leading edge of a fast scroll lag behind proportionally to speed, creating a rubbery, physics-driven feel. The effect is implemented entirely via `graphicsLayer` — no changes to layout, measure, or item positions.

---

## Usage

Replace `LazyColumn` / `LazyRow` with the drop-in wrappers and pass a `scrollEffect` lambda:

```kotlin
ScrollEffectLazyColumn(
    scrollEffect = { item -> elastic(item, ElasticStrength.Normal) }
) {
    items(myList) { MyCard(it) }
}
```

Pass `scrollEffect = null` (or omit it) to fall back to a plain `LazyColumn` / `LazyRow` with zero overhead.

---

## API reference

### `ScrollEffectLazyColumn` / `ScrollEffectLazyRow`

Drop-in replacements for `LazyColumn` / `LazyRow`. All standard parameters are forwarded unchanged. The only addition is:

```kotlin
scrollEffect: (ScrollEffectScope.(item: ScrollingItemData) -> Unit)? = null
```

When `null`, delegates directly to the standard composable with no overhead.

---

### `ScrollEffectScope`

Receiver of the `scrollEffect` lambda. Extends `GraphicsLayerScope`, so any standard graphics layer mutation (`alpha`, `scaleX`, `rotationZ`, …) is available alongside:

```kotlin
fun elastic(
    item: ScrollingItemData,
    strength: ElasticStrength = ElasticStrength.Normal,
    maxTranslationDp: Float = 150f,
)
```

---

### `ScrollingItemData`

Passed to the effect lambda for every visible item on each frame.

| Property | Type | Description |
|----------|------|-------------|
| `index` | `Int` | Sequential index of this item in the list |
| `velocity` | `ScrollVelocity` | Current scroll state snapshot |
| `distanceFromDragged` | `Int` | Signed distance from the item under the finger (0 = touched item, positive = ahead, negative = behind) |

---

### `ScrollVelocity`

Snapshot of the list's scroll state at a given frame.

| Property | Type | Description |
|----------|------|-------------|
| `rawPxPerSec` | `Float` | Raw velocity from `VelocityTracker` in px/s |
| `stretch` | `Float` | Normalised stretch in [0, 1] via `tanh` |
| `lagPx` | `Float` | Signed lag in pixels — the core value driving the elastic offset |
| `direction` | `Int` | +1 forward, −1 backward, 0 still |
| `isDragging` | `Boolean` | Finger is currently on screen |
| `isFlinging` | `Boolean` | Post-lift momentum phase |

---

### `ElasticStrength`

Controls the feel of the rubber.

| Variant | Feel | Gap size |
|---------|------|----------|
| `Hard` | Stiff, subtle | Small |
| `Normal` | Balanced | Moderate |
| `Loose` | Soft, exaggerated | Large |
