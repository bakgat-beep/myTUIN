# Garden Planner & Manager — V1 Visual Design & Appearance Specification

**Document:** `docs/V1_VISUAL_DESIGN_SPECIFICATION.md`
**Version:** 0.1
**Status:** Working specification
**Last updated:** August 2026

---

# 1. Purpose

This document defines the visual language of Garden Planner & Manager.

It establishes the application's:

* colour palette;
* typography;
* spacing;
* shapes;
* icons;
* surfaces;
* status presentation;
* controls;
* map appearance;
* visual hierarchy;
* accessibility principles;
* light and dark themes.

The objective is to create an interface that feels:

> **calm, practical, natural and modern.**

The application contains substantial information and functionality. The visual design must therefore provide structure without making the interface feel dense or technical.

This is a functional gardening tool, not a decorative lifestyle application.

---

# 2. Design Direction

The overall visual character should be:

**Simple**

Avoid unnecessary decoration.

**Sleek**

Use clean typography, restrained shapes and consistent spacing.

**Earthy**

Use a natural palette derived from soil, foliage, stone and sunlight.

**Calm**

Avoid excessive saturation, visual noise and competing elements.

**Practical**

Controls and information should be immediately understandable.

**Information-rich but uncluttered**

The interface may contain considerable information, but only the information relevant to the current task should be prominent.

---

# 3. Core Visual Principle

> **Let hierarchy, spacing and typography organise information before colour and decoration do.**

Colour should reinforce meaning.

It should not be responsible for creating the entire hierarchy.

A screen should remain understandable if colour is removed.

---

# 4. Visual Hierarchy

Information should generally be presented in this order:

1. Primary task or decision
2. Important status
3. Key information
4. Available actions
5. Explanation
6. Secondary information
7. Technical detail

This corresponds with the progressive-information principles defined in the V1 UX specification.

---

# 5. Colour System

The colour system should be deliberately restrained.

The primary palette is inspired by:

* foliage;
* soil;
* stone;
* warm paper;
* sunlight.

Avoid bright greens and excessive use of conventional "garden green".

## 5.1 Primary colours

### Forest

**Primary**

`#356859`

Used for:

* primary actions;
* selected navigation;
* important interactive controls;
* key positive garden-related elements.

### Deep Forest

`#23483E`

Used for:

* headings where appropriate;
* high-emphasis text;
* dark-theme primary surfaces;
* strong visual anchors.

### Sage

`#7D9A82`

Used for:

* secondary garden accents;
* supporting indicators;
* selected map elements;
* secondary emphasis.

---

# 6. Neutral Palette

Neutral colours should occupy most of the interface.

### Warm White

`#FAF9F5`

Primary light-theme background.

### Paper

`#F2F0E9`

Secondary surface/background.

### Stone

`#DDD9CE`

Borders and subtle dividers.

### Mid Stone

`#9A978D`

Secondary text and inactive elements.

### Charcoal

`#2D302C`

Primary text.

The application should avoid pure black for normal text.

---

# 7. Semantic Colours

Semantic colours should be muted rather than highly saturated.

## Success

**Moss**

`#557A55`

Used for:

* successful actions;
* positive confirmation;
* strong suitability where appropriate.

## Warning

**Ochre**

`#B4863A`

Used for:

* warnings;
* moderate concerns;
* attention-required items.

## Error

**Clay**

`#A95645`

Used for:

* errors;
* serious problems;
* destructive warnings.

## Information

**Slate Blue**

`#60788A`

Used for:

* informational states;
* explanatory information;
* neutral system messages.

These colours should normally appear alongside text or icons.

---

# 8. Colour Usage Rules

Colour must not be used merely because it is available.

Use:

* Forest for primary interaction;
* Sage for supporting garden context;
* neutral colours for most surfaces;
* semantic colours only when they communicate meaning.

Avoid:

* large areas of saturated colour;
* rainbow-style dashboards;
* gradients as decoration;
* excessive coloured cards;
* using different colours for every category.

A screen should generally contain **one dominant accent colour and a small number of semantic accents**.

---

# 9. Suitability Colours

Suitability should use both text and visual indication.

Recommended presentation:

| State     | Visual treatment                         |
| --------- | ---------------------------------------- |
| Excellent | Moss + positive indicator                |
| Good      | Sage/Moss + positive indicator           |
| Marginal  | Ochre + caution indicator                |
| Poor      | Clay/Ochre + caution indicator           |
| Unknown   | Neutral + question/information indicator |

Do not rely on green/yellow/red alone.

For example:

**Good**

rather than simply displaying a green circle.

---

# 10. Recommendation Visual Language

Recommendations should feel informative rather than promotional.

A recommendation card should generally contain:

**Plant name**

**Recommendation state**

**Key factors**

**Why?**

**Confidence**

The visual treatment should not imply that the recommendation is an absolute decision.

Avoid large "score" graphics unless they provide genuine value.

Prefer:

> Good candidate

over:

> 82/100

---

# 11. Typography

Typography should be highly readable and restrained.

Use the standard Android/Compose typography system based on **Roboto** unless a later design review establishes a compelling reason for a custom typeface.

Avoid decorative or handwritten gardening fonts.

The application should feel like a serious tool.

## Suggested hierarchy

### Display

Large screen titles.

Used sparingly.

### Headline

Major section titles.

### Title

Cards, subsections and important objects.

### Body

Primary information.

### Body Small

Supporting information.

### Label

Buttons, filters and compact metadata.

Typography should create hierarchy through:

* size;
* weight;
* spacing;

before relying on colour.

---

# 12. Text Weight

Use a restrained weight hierarchy.

Prefer:

* Regular for body text;
* Medium for labels and controls;
* Semibold for important headings;
* Bold only for major emphasis.

Avoid excessive bold text.

---

# 13. Spacing System

Use a consistent spacing scale based primarily on **4 dp increments**.

Recommended values:

```text
4 dp
8 dp
12 dp
16 dp
24 dp
32 dp
40 dp
48 dp
```

Most ordinary spacing should use:

**8, 12, 16 or 24 dp.**

Avoid arbitrary spacing values.

---

# 14. Screen Margins

Standard mobile content margins should generally be:

**16 dp**

For larger layouts, content may expand while maintaining a comfortable maximum reading width.

Do not allow text-heavy screens to become excessively wide.

---

# 15. Component Spacing

Components should have visible breathing room.

Prefer:

```text
Heading

16 dp

Content

24 dp

Next section
```

rather than tightly stacking many cards and controls.

Whitespace is an important part of the visual design.

---

# 16. Cards

Cards should be used selectively.

They are appropriate for:

* recommendations;
* priorities;
* important information;
* plant summaries;
* Inbox items;
* contextual summaries.

They should not surround every piece of information.

Avoid the common pattern where every row becomes a floating rounded card.

Lists and simple sections should often use the background directly.

---

# 17. Card Appearance

Cards should generally use:

* warm neutral surfaces;
* subtle borders;
* small corner radius;
* minimal or no shadow.

Recommended radius:

**12 dp**

Use stronger visual treatment only for genuinely important information.

---

# 18. Elevation

Use elevation sparingly.

Most surfaces should appear visually flat.

Elevation is primarily appropriate for:

* floating controls;
* dialogs;
* bottom sheets;
* menus;
* prominent floating actions.

Avoid making the application look like a collection of floating cards.

---

# 19. Borders and Dividers

Prefer subtle borders over heavy separators.

Default border:

**1 dp**

using the Stone neutral.

Dividers should be used when they improve scanning.

Do not divide every row.

---

# 20. Buttons

Primary actions should use a filled Forest button.

Secondary actions should generally use:

* outlined buttons;
* tonal buttons;
* text buttons.

Avoid multiple visually dominant buttons in the same section.

Each screen should generally have one obvious primary action.

---

# 21. Primary Action

The most important action should be visually obvious without dominating the interface.

For example:

**Add plant**

should be visually stronger than:

**Cancel**

but neither should overwhelm the surrounding content.

---

# 22. Add Action

The global:

**+ Add**

action is one of the application's most important interaction patterns.

It should be visually consistent throughout the application.

The action should expose common activities such as:

* Plant;
* Water;
* Harvest;
* Observe;
* Feed;
* Prune;
* Intervention.

Additional actions may appear under:

**More**

The Add action should feel like a practical tool rather than a promotional floating button.

---

# 23. Icons

Use a single coherent icon family.

Material Symbols/Icons should be the default.

Icons should:

* have consistent stroke/weight;
* be recognisable;
* accompany text where meaning may be ambiguous;
* not be used as decoration merely to make screens more colourful.

Do not rely on emoji as the primary application iconography.

Emoji may appear in appropriate user-facing contexts, but should not define the visual language.

---

# 24. Icon Semantics

Common actions should retain consistent icons.

For example:

* Add → plus
* Search → magnifying glass
* Filter → filter
* Information → information
* Edit → pencil
* Delete → trash
* Undo → undo
* History → clock/history
* Location → location marker
* Plant → plant/leaf
* Harvest → basket
* Observation → eye
* Warning → warning triangle

The exact Material icon may be selected during implementation.

---

# 25. Navigation

The primary navigation should be visually quiet.

Recommended:

**Navigation bar**

with:

* Home;
* Garden;
* Plants;
* Planner;
* Inbox.

Selected destinations should use the Forest accent.

Unselected destinations should use muted neutral tones.

Avoid excessive badges and notification counts.

---

# 26. Home Screen

Home should feel calm and useful.

It should not resemble a business analytics dashboard.

Recommended visual hierarchy:

1. Greeting/context
2. Current priorities
3. Upcoming actions
4. Important Inbox items
5. Garden status
6. Recent activity
7. Optional planning opportunities

Sections should be separated by whitespace rather than heavy containers.

---

# 27. Garden Screen

The Garden screen should give the map visual priority.

The map should occupy most of the available space.

Controls should appear as lightweight overlays or compact controls.

Avoid covering the map with large opaque panels.

The user should feel that they are interacting with **their garden**, not a GIS application.

---

# 28. Map Appearance

The map should use a restrained natural palette.

Base map colours should be:

* warm neutral;
* low contrast;
* visually quiet.

Garden objects should have stronger visual contrast than the base map.

The map must remain readable when multiple layers are enabled.

---

# 29. Spatial Object Colours

Spatial objects should use a consistent semantic palette.

Examples:

**Growing spaces**

Muted Forest/Sage.

**Infrastructure**

Slate/Stone.

**Water**

Muted blue.

**Problems**

Muted Clay.

**Experiments**

Muted Ochre.

**Planned planting**

Lighter/dashed treatment.

**Historical objects**

Reduced opacity or subdued treatment.

Do not assign arbitrary colours to individual objects.

---

# 30. Map Selection

The selected spatial object should be immediately recognisable.

Selection should use a combination of:

* stronger outline;
* subtle fill change;
* contextual information panel.

Do not rely solely on colour.

---

# 31. Map Layers

Layer controls should be compact.

The interface should clearly indicate:

* which layers are active;
* which filters are active;
* whether historical mode is enabled.

Active filters should be visually obvious but not visually dominant.

---

# 32. Historical Mode

Historical views should look recognisably different from the current garden without becoming confusing.

Use:

* subdued colours;
* reduced opacity;
* a clear date/year indicator.

For example:

**Garden — 2025**

should be unmistakable.

---

# 33. Plant Library

Plant Library should feel like a searchable reference rather than a catalogue storefront.

Prioritise:

* plant name;
* image where available;
* key characteristics;
* search;
* filtering.

Avoid excessive decorative imagery.

---

# 34. Plant Images

Plant imagery should be:

* consistent;
* naturalistic;
* clearly identifiable;
* visually clean.

Images should support identification rather than function as decoration.

Where an image is unavailable, use a restrained placeholder rather than an oversized empty image frame.

---

# 35. My Plants

My Plants should emphasise the user's own garden.

The interface should clearly distinguish:

**Plant Library**

from:

**My Plants**

Garden-specific information should use contextual accents and location information.

---

# 36. Plant Detail

Plant detail should begin with a concise summary.

Example hierarchy:

**Tomato**

Good candidate

Conditions

Timing

Your garden

Problems

Relationships

Technical information

The user should not encounter a large wall of horticultural data immediately.

---

# 37. Planner

Planner should visually prioritise decisions.

Recommendations should use generous spacing and clear hierarchy.

Avoid dense comparison tables unless the user explicitly requests more detail.

A recommendation should be understandable at a glance.

---

# 38. Recommendation Factors

Use compact factor rows.

Example:

**Light**
Excellent

**Water**
Good

**Rotation**
Poor

Each factor should have:

* a label;
* a clear state;
* optional explanation.

Avoid excessive iconography.

---

# 39. Explanation Panels

The:

**Why?**

interaction should reveal explanation without navigating the user away from the current context.

A bottom sheet, expandable section or contextual panel may be appropriate.

The explanation should be visually calmer than the recommendation itself.

---

# 40. Confidence

Confidence should be visually secondary.

Example:

**Good candidate**

Confidence: Moderate

Do not make confidence visually compete with the recommendation.

Use neutral styling unless confidence itself is an important warning.

---

# 41. Inbox

Inbox should use visual hierarchy based on importance.

Suggested treatment:

**Important**

Stronger emphasis.

**Useful**

Normal emphasis.

**Informational**

Muted emphasis.

Avoid using red for every item that requires attention.

Red/Clay should be reserved for genuinely serious conditions.

---

# 42. Quick Add

Quick Add should be extremely simple.

Use large, recognisable action choices with:

* icon;
* short label.

Avoid displaying every possible field immediately.

After choosing an action, show only the information required for that activity.

---

# 43. Forms

Forms should follow progressive disclosure.

Required information should appear first.

Optional information should be:

* below the primary fields;
* expandable;
* or available after saving.

Avoid long forms with dozens of empty fields.

---

# 44. Unknown Values

Unknown information should use neutral presentation.

For example:

**Soil pH**
Unknown

not:

**Soil pH**
⚠ Missing!

Unknown is a legitimate state, not an error.

---

# 45. Observations

Observations should visually distinguish:

**Observed**

from:

**Estimated**

and:

**Inferred**

Use small provenance labels where useful.

Do not use warning colours simply because information is uncertain.

---

# 46. Measurements

Measurements should look authoritative without implying more certainty than the evidence supports.

Example:

**pH 6.7**

Home test kit · 24 Aug 2026

Confidence: High

The measurement itself should remain visually prominent.

Its provenance should be secondary.

---

# 47. Empty States

Empty states should be calm and useful.

They should contain:

1. What is missing.
2. Why the area matters.
3. The most useful next action.

Avoid large illustrations or excessive decorative graphics.

---

# 48. Error States

Error states should use Clay sparingly.

The primary message should be clear.

Example:

**We couldn't save that change.**

Then:

**Retry**

Additional technical information should remain secondary.

---

# 49. Success Feedback

Successful actions should use subtle feedback.

Examples:

**Harvest recorded**

**Plant added to My Plants**

**Plan saved**

Avoid large success animations.

A brief snackbar or inline confirmation is usually sufficient.

---

# 50. Undo

Undo should be presented through a consistent snackbar pattern.

Example:

**Plant removed**

**Undo**

The user should not need to navigate elsewhere to reverse a recent accidental action.

---

# 51. Dialogs

Dialogs should be reserved for:

* destructive actions;
* important confirmations;
* decisions requiring focused attention.

Avoid confirmation dialogs for routine actions.

---

# 52. Bottom Sheets

Bottom sheets are appropriate for:

* contextual details;
* map object information;
* Quick Add;
* recommendation explanations;
* filters;
* secondary actions.

They should not become a replacement for proper navigation.

---

# 53. Dark Theme

V1 should support Android's dark theme.

Dark mode should not simply invert the light palette.

Use:

* deep charcoal/green backgrounds;
* muted earthy surfaces;
* softened semantic colours;
* high-contrast readable text.

Avoid pure black backgrounds where possible.

The earthy character should remain present in dark mode without making the interface muddy.

---

# 54. Light Theme

The default light theme should use:

**Warm White**

as the primary background.

The overall impression should resemble:

* warm paper;
* natural materials;
* muted foliage.

It should not look yellow or sepia.

---

# 55. Theme Consistency

Light and dark themes must preserve the same semantic hierarchy.

For example:

**Good**

must remain visually positive in both themes.

**Warning**

must remain recognisably a warning.

**Unknown**

must remain neutral.

Theme changes must not alter the meaning of colours.

---

# 56. Imagery

Imagery should be used where it improves recognition or understanding.

Good uses include:

* plant identification;
* garden objects;
* educational explanations.

Poor uses include:

* decorative backgrounds;
* large hero images;
* ornamental section headers;
* unnecessary illustrations.

The application should prioritise useful information over visual decoration.

---

# 57. Motion

Animations should be subtle and purposeful.

Use motion for:

* navigation;
* expanding/collapsing information;
* map selection;
* adding/removing items;
* state transitions.

Avoid:

* decorative animations;
* excessive bouncing;
* continuous movement;
* animations that slow down frequent workflows.

The application should feel responsive rather than animated.

---

# 58. Touch Interaction

Interactive elements should provide comfortable touch targets.

Aim for at least:

**48 × 48 dp**

for primary interactive controls.

Frequently used controls should be easy to reach with one hand where practical.

---

# 59. Accessibility

Visual design must support:

* text scaling;
* high contrast;
* colour-blind users;
* screen readers;
* touch accessibility;
* predictable focus;
* non-colour status communication.

Every important state should communicate through at least two channels, such as:

**colour + text**

or:

**icon + text**.

---

# 60. Information Density

The application should favour:

**one clear screen over three dense screens**

but should not hide important information merely to make the interface look clean.

The solution to complexity is:

* hierarchy;
* progressive disclosure;
* contextual information;

rather than simply removing information.

---

# 61. Visual Restraint

Avoid:

* excessive cards;
* excessive borders;
* excessive shadows;
* excessive icons;
* excessive colour;
* decorative gradients;
* oversized headings;
* unnecessary animations;
* dashboard-style charts;
* visual badges for everything.

The garden itself provides the visual richness.

The application should provide the structure.

---

# 62. Consistency Rules

The following visual conventions should remain consistent throughout the application:

| Element                 | Convention                    |
| ----------------------- | ----------------------------- |
| Primary action          | Forest filled button          |
| Secondary action        | Tonal/outlined/text           |
| Information             | Slate Blue + information icon |
| Warning                 | Ochre + warning icon          |
| Error                   | Clay + error icon             |
| Success                 | Moss + confirmation icon      |
| Unknown                 | Neutral                       |
| Explanation             | `ⓘ` / information icon        |
| Add                     | `+`                           |
| Search                  | Search icon                   |
| Filter                  | Filter icon                   |
| Undo                    | Undo icon                     |
| History                 | History icon                  |
| Selected navigation     | Forest                        |
| Default background      | Warm White                    |
| Secondary surface       | Paper                         |
| Standard radius         | 12 dp                         |
| Standard content margin | 16 dp                         |
| Standard spacing        | 4 dp scale                    |

---

# 63. Design Tokens

The visual system should be implemented as central design tokens rather than scattered literal values.

At minimum, define tokens for:

* colours;
* typography;
* spacing;
* corner radii;
* elevation;
* icon sizes;
* touch targets.

Components should consume these tokens.

Changing the visual identity should therefore require changing the design system rather than searching through individual screens.

---

# 64. Material Design Relationship

The application should use Jetpack Compose's Material 3 foundations where appropriate.

Material components should be **the implementation foundation, not the visual identity**.

The Garden Planner should not look like an unmodified Material sample application.

The custom earthy palette, restrained surfaces, spacing and information hierarchy should establish the product identity.

---

# 65. Brand Character

The application should communicate:

**Practical**

It helps the gardener do things.

**Natural**

Its visual language reflects the garden.

**Calm**

It reduces cognitive load rather than adding to it.

**Trustworthy**

Recommendations and uncertainty are presented honestly.

**Modern**

The interface feels current without following every design trend.

**Unobtrusive**

The application supports the garden rather than competing with it.

---

# 66. Visual Design Test

When reviewing a new screen, ask:

1. Is the primary task immediately obvious?
2. Is the most important information visually dominant?
3. Is there unnecessary decoration?
4. Is colour being used to communicate meaning rather than decoration?
5. Could the screen remain understandable without colour?
6. Is there enough whitespace?
7. Are there too many cards or controls?
8. Can a casual user ignore advanced information?
9. Can an advanced user reach additional detail?
10. Does the screen feel like part of the same application?

---

# 67. Final Design Principle

> **The garden is complex. The interface should not be.**

Garden Planner & Manager should use a restrained earthy visual language, strong hierarchy, generous whitespace and progressive disclosure to make a sophisticated information system feel calm and approachable.

The application should look less like a database and more like a **well-designed, modern garden notebook that happens to understand the garden deeply**.
