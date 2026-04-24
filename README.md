# Scroll Effect Lazy

Small Compose lib that makes `LazyColumn` / `LazyRow` feel a bit rubbery. Scroll fast and the items at the front lag behind a little, like they're on a stretchy band. Nothing weird happens to layout or measuring, it's all just `graphicsLayer` under the hood.

<img width="720" height="405" alt="Timeline_1x_24fps" src="https://github.com/user-attachments/assets/95bb6592-0502-498e-aa71-89d22431e6a1" />

---

## Usage

Swap your `LazyColumn` / `LazyRow` for the wrapper, toss in a `scrollEffect` lambda, done:

```kotlin
ScrollEffectLazyColumn(
    scrollEffect = { item -> elastic(item, ElasticStrength.Normal) }
) {
    items(myList) { MyCard(it) }
}
```

Don't want the effect? Pass `null` or just leave it out. You're back to a plain lazy list, no overhead.

---

## Custom effects

The built-in `elastic()` is just one thing you can do. The `scrollEffect` lambda runs per visible item on every frame, and `this` inside it is a `GraphicsLayerScope`, so you can do whatever you want with it.

```kotlin
ScrollEffectLazyColumn(
    // lambda runs per-item, every frame
    scrollEffect = { item ->
        // use the prebuilt rubber band
        elastic(item)

        // or roll your own with anything from item / item.velocity
        // (it's a GraphicsLayerScope, so all the usual stuff is here)
        alpha = 1f - item.velocity.stretch * 0.3f
        rotationZ = item.velocity.direction * 2f
    }
) {
    items(myList) { MyCard(it) }
}
```

You can also mix: call `elastic(item)` for the base feel and then layer your own tweaks on top.

---

## Why a whole new composable and not just a `Modifier`?

Short answer: the effect has to touch every item, not the list as a whole. To pull that off we have to sit between you and `LazyListScope` and wrap each item in its own `graphicsLayer`. A modifier on the list can't do that, and making you glue a modifier onto every item by hand would be a pain. The lambda also runs on a little custom scope (`ScrollEffectScope`) that bundles `GraphicsLayerScope` with list-aware stuff and shortcuts like `elastic()`, which a regular modifier just can't give you.

---

## API reference

### `ScrollEffectLazyColumn` / `ScrollEffectLazyRow`

Same as `LazyColumn` / `LazyRow`. All the usual params go straight through. The only extra thing is:

```kotlin
scrollEffect: (ScrollEffectScope.(item: ScrollingItemData) -> Unit)? = null
```

If it's `null`, we don't do anything fancy, just call the normal composable.

---

### `ScrollEffectScope`

Whatever `this` is inside your `scrollEffect` lambda. It's a `GraphicsLayerScope`, so all the usual toys are there (`alpha`, `scaleX`, `rotationZ`, ...), plus this one shortcut:

```kotlin
fun elastic(
    item: ScrollingItemData,
    strength: ElasticStrength = ElasticStrength.Normal,
    maxTranslationDp: Float = 150f,
)
```

---

### `ScrollingItemData`

One of these lands in your lambda for every visible item on every frame.

| Property | Type | What it is |
|----------|------|-------------|
| `index` | `Int` | Which spot this item sits in |
| `velocity` | `ScrollVelocity` | What the list's doing right now |
| `distanceFromDragged` | `Int` | How far from the item under your finger (0 means that one, positive means further down, negative means further back) |

---

### `ScrollVelocity`

Quick snapshot of how the list is moving. The built-in `elastic()` only looks at `lagPx`, the rest are there so you have stuff to work with when you write your own effects.

| Property | Type | What it is |
|----------|------|-------------|
| `lagPx` | `Float` | Signed lag in pixels. This is what `elastic()` uses to do its thing |
| `rawPxPerSec` | `Float` | Straight-from-`VelocityTracker` speed, px/s |
| `stretch` | `Float` | Same thing squashed into [0, 1] with `tanh` |
| `direction` | `Int` | +1 = forward, -1 = back, 0 = not moving |
| `isDragging` | `Boolean` | Finger's still on the screen |
| `isFlinging` | `Boolean` | Finger's off, list's still sliding |

---

### `ElasticStrength`

How floppy you want the rubber.

| Variant | Feel | Gap size |
|---------|------|----------|
| `Hard` | Stiff, barely there | Small |
| `Normal` | Just right | Medium |
| `Loose` | Soft, really obvious | Big |
