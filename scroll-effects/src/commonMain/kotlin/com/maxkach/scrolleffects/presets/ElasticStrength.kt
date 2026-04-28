package com.maxkach.scrolleffects.presets

/**
 * How "stiff" the elastic rubber feels. Controls both the maximum gap size
 * (how far items drift from their natural positions) and how aggressively
 * items separate as distance from the dragged item grows.
 *
 * - [Hard]: stiff rubber, small gaps.
 * - [Normal]: balanced, moderate gaps.
 * - [Loose]: soft rubber, large gaps.
 */
enum class ElasticStrength(
    internal val closeCoefficient: Float,
    internal val farCoefficient: Float,
    internal val capSpacingMultiplier: Float,
) {
    Hard(closeCoefficient = 0.2f, farCoefficient = 0.05f, capSpacingMultiplier = 1f),
    Normal(closeCoefficient = 0.5f, farCoefficient = 0.2f, capSpacingMultiplier = 3f),
    Loose(closeCoefficient = 0.9f, farCoefficient = 0.6f, capSpacingMultiplier = 3f);

    internal val maxCoefStep: Float = closeCoefficient - farCoefficient
}
