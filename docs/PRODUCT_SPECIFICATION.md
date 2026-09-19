# Garden Planner & Manager — Product Specification

**Document:** `PRODUCT_SPECIFICATION.md`  
**Version:** 1.0 consolidated baseline  
**Status:** Pre-development baseline  
**Date:** August 2026

## 1. Product purpose

Garden Planner & Manager is an offline-first Android application that helps a gardener understand the garden, decide what to do, record what happened, and learn from the resulting history.

**Core loop:**

> Understand → Plan → Decide → Act → Observe → Learn → Understand

The application is a garden notebook that understands the user's garden, not a database-management interface.

## 2. Product goals

V1 must let an ordinary gardener:

- create and represent a garden and its growing spaces;
- record garden conditions, plants, activities, observations, soil information, harvests and losses;
- browse plant knowledge and maintain **My Plants** separately from general plant knowledge;
- plan future planting and activities;
- ask what could grow in a space and receive deterministic, explainable recommendations;
- accept, reject, modify, defer or override recommendations;
- review current and historical garden information;
- use search, filtering and the Inbox to find what matters;
- export and import garden data safely;
- perform core tasks without an Internet connection.

## 3. Product principles

### Useful before complete
The garden does not need to be fully documented before the application becomes useful.

### Facts before automation
Reliable records are more important than elaborate automation.

### Gardener in control
Recommendations are assistance. User decisions are authoritative.

### History is first-class
Past plantings, activities, observations, measurements, harvests, decisions and recommendations must remain meaningful over time.

### Unknown is valid
Unknown, not recorded, not applicable, observed, measured, estimated and inferred are distinct information states where relevant.

### Explainable recommendations
Recommendations must expose their important factors, evidence, uncertainty and conditions.

### Progressive complexity
Casual use must remain simple. Advanced capabilities are available when useful, not mandatory.

### Offline-first
Core garden functionality must not depend on network availability.

## 4. V1 functional scope

### 4.1 Garden and spatial management

- Garden profile: name, location, hemisphere/seasonal context, timezone and preferences.
- Areas and growing spaces: beds, containers, greenhouse areas, orchard areas and other supported types.
- Dimensions, characteristics and geometry.
- Spatial objects/infrastructure.
- Map viewing and basic editing: pan, zoom, select, create, move, edit/resize, layers and filters.
- Historical spatial context sufficient to answer questions such as "What was planted here?".

### 4.2 Plant knowledge and My Plants

- Plant Library search and browsing.
- General growing requirements, lifecycle, timing, relationships and known problems.
- Cultivars as optional detail.
- My Plants as user-specific plant instances linked to plant knowledge.
- Planting/planned planting workflow.
- Source/provenance information for knowledge where appropriate.

### 4.3 Garden records

- Activities: planting, watering, feeding, pruning, intervention and other supported types.
- Quick Add for common actions.
- Context-aware Add flow.
- Planned vs actual activity distinction.
- Repeated and partially completed activities.
- Structured observations.
- Measurements with value, unit, date, method/source and context where appropriate.
- Soil observations and measurements, with estimates distinguished from measurements.
- Harvests and losses.

### 4.4 Planning and recommendations

- Plans and targets.
- Empty-space planning.
- Deterministic suitability analysis.
- Factors such as light, water, soil, pH, fertility, temperature, frost, timing, space, rotation, relationships, infrastructure and personal history where relevant.
- Suitability distinct from confidence.
- Conditional, possible and insufficient-information outcomes.
- "Do nothing" as a valid result.
- Structured recommendation explanations.
- User decisions and overrides retained as records.

### 4.5 Inbox and history

- Home view answering "What matters now?".
- Inbox categories such as action, warning, observation request, information request, experiment and knowledge.
- Priority levels without excessive notifications.
- Mute vs dismiss semantics.
- Chronological and contextual history views.

### 4.6 Search and preferences

- Search across relevant garden and knowledge content.
- Filters by garden, location, plant, activity, status, date and other applicable dimensions.
- User preferences that can influence recommendations without changing underlying facts.

### 4.7 Experiments

V1 includes a basic experiment capability: hypothesis, treatment/control structure where appropriate, measurements/results and conclusions. It must reuse existing garden records rather than duplicate them.

### 4.8 Import/export and data safety

- Complete canonical export of supported user data.
- Import flow: select → validate → preview → show errors/conflicts → cancel/confirm → import → report.
- Versioned schema/interchange format.
- Stable IDs and relationships preserved.
- Round-trip validation.

## 5. Core user workflows

### New garden
Create garden → add location → create first growing space → optionally record conditions → add first plant.

### Planting
Find plant → optional cultivar → choose space → record planned/actual date → save → view in My Plants and history.

### Daily activity
Open Quick Add → choose action → inherit context → enter minimum required information → save → history updates.

### Planning
Select space → ask what could grow → review suitability → inspect explanation → accept/override/modify → save plan/decision.

### Observation
Observe → choose structured observation type → record information/evidence → save → review later.

### Harvest
Harvest → choose plant/location → enter quantity and unit when known → save → history updated.

### Import/export
Export → validate file → import compatible data → verify relationships and history are preserved.

## 6. V1 non-goals

V1 does **not** require:

- social/community features;
- mandatory cloud accounts or cloud sync;
- AI chat as the primary interface;
- automatic garden design;
- professional GIS;
- sophisticated weather platform;
- advanced statistical analysis;
- fully automated disease diagnosis;
- complex chemical-treatment workflows;
- elaborate gamification/achievement systems;
- commercial marketplace/retailer integrations.

## 7. UX requirements

Primary destinations are:

**Home | Garden | Plants | Planner | Inbox**

A prominent global **+ Add** action is used for common garden records.

The UI must prioritise actions and decisions over data entry, use progressive disclosure, inherit useful context, preserve unknown states, and make important recommendations explainable.

## 8. V1 acceptance threshold

V1 is product-complete when the application can reliably support the core workflows above using local data, preserve historical information, provide deterministic explainable recommendations, and safely export/import supported data.
