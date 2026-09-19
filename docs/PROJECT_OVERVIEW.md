Garden Planner & Manager — Project Overview

Document: docs/PROJECT_OVERVIEW.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

Garden Planner & Manager is an offline-first application for helping gardeners understand, plan, manage and learn from their gardens over time.

Its central purpose is:

> Understand the garden → decide what to do → record what happened → learn from the results.



The application combines:

garden and spatial management;

plant knowledge;

planning;

activity and observation recording;

harvest and outcome history;

soil and environmental information;

explainable recommendations;

optional experiments;

historical analysis;

import/export.


The application should become more useful as garden information accumulates without requiring the gardener to become an expert in the application's internal data model.


---

2. Product Vision

The ideal experience is:

> A garden notebook that understands your garden.



It should be:

simple enough for casual use;

powerful enough for detailed garden management;

useful without extensive configuration;

transparent about uncertainty;

respectful of the gardener's decisions;

protective of historical information;

usable offline;

extensible without unnecessary architectural complexity.


The application should feel like a gardening tool, not a database interface.


---

3. Core Product Loop

The fundamental product loop is:

UNDERSTAND
    ↓
What is happening in my garden?

PLAN
    ↓
What could or should I do?

DECIDE
    ↓
What do I want to do?

ACT
    ↓
What did I actually do?

OBSERVE
    ↓
What happened?

LEARN
    ↓
What does my garden history tell me?

UNDERSTAND
    ↓

The system should support this loop without requiring every stage to be completed for every garden activity.

A gardener may simply:

Plant → Record → Continue

while another gardener may:

Plan → Measure → Experiment → Record → Analyse → Adjust

Both are valid.


---

4. V1 Product Goal

V1 should provide a coherent and useful garden-management system rather than attempting to solve every horticultural problem.

An ordinary gardener should be able to:

1. create a garden;


2. represent growing spaces;


3. record basic conditions;


4. find plants;


5. add plants to their garden;


6. plan future planting;


7. ask what could grow in a space;


8. understand recommendations;


9. override recommendations;


10. record activities;


11. record observations;


12. record harvests and losses;


13. review garden history;


14. understand how history affects recommendations;


15. record soil observations and measurements;


16. use the application without an Internet connection.




---

5. V1 Scope

5.1 Garden management

V1 includes:

garden;

location;

growing spaces;

spatial geometry;

dimensions;

basic conditions;

plants and plant locations;

infrastructure;

map representation;

current and historical spatial information.


The spatial model must support real garden geometry without requiring professional GIS knowledge.


---

5.2 Plant management

V1 includes:

Plant Library;

user's My Plants;

plant identity;

cultivars where known;

growing requirements;

planting timing;

relevant relationships;

plant history;

plant activities;

plant observations;

harvests;

problems.


Plant knowledge and the user's actual plant records remain separate concepts.


---

5.3 Planning

V1 includes:

future planting;

selecting an empty or available growing space;

suitability evaluation;

planting windows;

rotation considerations;

relevant conditions;

user preferences;

recommendation explanations;

confidence;

conditional recommendations;

user decisions;

recommendation overrides.


Planning is advisory.

The application does not automatically convert recommendations into actions.


---

5.4 Activity recording

V1 includes rapid recording of common garden events:

planting;

watering;

feeding;

pruning;

harvesting;

observation;

intervention.


The Activity model provides a common event foundation while activity-specific information is stored only where required.


---

5.5 Observations

V1 supports structured observations such as:

soil moisture;

drainage;

pooling;

texture;

compaction;

visible organic matter;

plant growth;

flowering;

fruiting;

damage;

symptoms.


Observations must remain distinct from diagnoses.


---

5.6 Soil

V1 supports both measured and estimated soil information.

Examples include:

pH;

drainage;

moisture;

texture;

compaction;

salinity;

fertility;

nutrient status.


The system must distinguish:

measured;

observed;

estimated;

inferred;

unknown.


A gardener does not need laboratory testing to use the soil functionality.


---

5.7 History

Historical records are a core product capability.

The application should preserve:

previous plantings;

activities;

observations;

harvests;

losses;

soil measurements;

interventions;

experiments;

decisions;

relevant recommendation outcomes.


Current information must not silently overwrite historically important information.


---

5.8 Experiments

Experiments are an optional advanced feature.

They may capture:

question;

hypothesis;

treatment;

control;

measurements;

timeframe;

observations;

outcome;

conclusion.


Experiments reuse normal garden records rather than creating an entirely separate activity system.


---

5.9 Import/export

V1 includes structured import/export for data portability and preservation.

Export must be:

versioned;

human-readable where practical;

machine-readable;

capable of preserving historical information;

based on canonical identifiers.


Import must validate data before changing the garden.


---

6. Explicit V1 Non-Goals

V1 does not attempt to provide:

a social network;

mandatory cloud accounts;

automatic community sharing;

AI chat as the primary interface;

professional GIS functionality;

fully automated garden design;

sophisticated weather dashboards;

complex chemical treatment workflows;

excessive gamification;

achievement systems;

a large notification platform;

dependence on continuous Internet access.


These may be considered in later versions where they provide clear value without compromising the core product.


---

7. User Experience Principles

The application follows these principles:

7.1 Answer the immediate question first

The primary question is:

> What do I need to know or do right now?



The application should prioritise:

actions;

decisions;

observations;

useful information;

recommendations;

relevant history.



---

7.2 Progressive complexity

Information is presented progressively:

Immediate
    ↓
Explanation
    ↓
Technical detail

Advanced information should be available without being forced on casual users.


---

7.3 Unknown is valid

Missing information is explicitly represented.

The system must not imply that:

unknown = poor

or:

unknown = unsuitable

unless a specific rule justifies such a conclusion.


---

7.4 Progressive precision

Information may begin as:

Unknown

and later become:

Observed
→ Estimated
→ Measured
→ Repeated measurement

The system must preserve the distinction between these states.


---

7.5 The gardener remains in control

Recommendations may be:

accepted;

rejected;

modified;

overridden;

deferred;

cancelled.


An override is a legitimate user decision, not an application error.


---

7.6 Do not collect information without purpose

Every significant request for information should either:

provide an obvious benefit;

explain why it matters;

remain optional;

be deferred until relevant.



---

8. Primary Navigation

V1 uses:

Home
Garden
Plants
Planner
Inbox

with a prominent:

+ Add

action.

The navigation should remain stable as the application grows.

New functionality should normally be incorporated into existing areas rather than continually creating new top-level destinations.


---

9. Core Screens

Home

Answers:

> What matters now?



Potential content includes:

priorities;

upcoming activities;

important Inbox items;

garden status;

recent activity;

planning opportunities;

active experiments.


Home is not intended to be a statistics dashboard.


---

Garden

Provides the spatial representation of the garden.

It supports:

map;

areas;

growing spaces;

plants;

infrastructure;

conditions;

history;

layers;

filters.



---

Plants

Provides access to:

Plant Library;

My Plants;

plant search;

cultivars;

plant requirements;

plant history.



---

Planner

Supports:

future planning;

empty-space planning;

suitability;

planting windows;

rotation;

preferences;

recommendation explanations.



---

Inbox

Provides a prioritised work queue containing useful items such as:

actions;

warnings;

observation requests;

information requests;

experiments;

knowledge.


Notifications should be useful rather than exhaustive.


---

10. Quick Add

Quick Add is a core interaction.

Typical actions include:

Plant
Water
Harvest
Observe
Feed
Prune
Intervention

The workflow should optimise for:

> recording something that just happened



rather than:

> creating a perfect database record.



Context should reduce data entry.

For example:

Garden → Bed 2 → + Add → Water

should default to Bed 2 while still allowing the user to change the context.


---

11. Planned Versus Actual

The system must maintain a strict distinction between:

Planned

and:

Completed

A plan is not evidence that an activity occurred.

Recurring plans may generate planned occurrences, but each actual activity remains an independent historical event.

Partial completion must be supported.


---

12. Recommendation System

The recommendation engine evaluates garden-specific conditions against plant and planning knowledge.

Conceptually:

Garden Context
      +
Plant Knowledge
      +
Timing
      +
History
      +
Preferences
      +
Relevant Relationships
      ↓
Recommendation Engine
      ↓
Suitability + Confidence + Reasons

Recommendations should be explainable and deterministic where based on deterministic rules.

A recommendation should not simply present an unexplained score.


---

13. Recommendation Output

A recommendation may include:

Recommendation:
Good candidate

Confidence:
Moderate

Factors:
Light — Excellent
Water — Good
Soil — Excellent
Temperature — Marginal
Rotation — Poor
Space — Excellent

Why?
Your space has suitable light and soil.
Temperature is currently marginal.
A related crop was grown here recently.

Missing information:
Winter temperature is unknown.

Not every recommendation requires every factor.


---

14. Recommendation States

Recommendations may be:

recommended
conditional
possible
marginal
not_recommended
unsuitable
insufficient_information
expired

The system must distinguish:

Suitability

from:

Confidence

A recommendation can be highly suitable but poorly supported by available information.


---

15. Explainability

Every significant recommendation should provide a way to understand:

1. what information was used;


2. what it means;


3. how it affected the result;


4. how confident the result is;


5. what information is missing;


6. what could change the recommendation.



Where information has been geographically or hemispherically adapted, that adaptation should be disclosed.


---

16. Personal History

General horticultural knowledge and garden-specific history are separate evidence sources.

Example:

General suitability:
Good

Your garden history:
Poor results in this space during the previous two seasons.

Result:
Consider another cultivar or growing space.

Personal history may influence recommendations, but it must not silently rewrite general rules.

The user must be able to see when personal history affected a recommendation.


---

17. Knowledge and Provenance

The application distinguishes information according to origin and evidence.

Examples include:

verified reference;

curated guidance;

published research;

institutional guidance;

expert guidance;

community observation;

user observation;

imported dataset;

experimental result;

derived analysis.


The system also records appropriate information state, origin, applicability and confidence.

Technical provenance should normally be exposed through progressive disclosure rather than cluttering ordinary screens.


---

18. Data Model Philosophy

The data model should represent real garden concepts rather than UI screens.

Core concepts include:

Garden;

Area;

Growing Space;

Spatial Object;

Plant;

Cultivar;

Plant Instance;

Activity;

Observation;

Harvest;

Problem;

Soil information;

Plan;

Recommendation;

User Decision;

Experiment;

Source;

Knowledge/requirement information.


Relationships should represent meaningful relationships between these concepts.

The UI should translate these concepts into ordinary gardening language.

For example:

Database concept:
Plant Instance

User experience:
Tomato in Bed 2

The application should avoid exposing database terminology to users.


---

19. Authoritative Garden History

User-created garden records are authoritative records of what the gardener recorded.

Knowledge and recommendations are downstream consumers of that history.

The architecture should therefore avoid making the recommendation engine the owner of garden history.

Conceptually:

Garden Records
      ↓
Knowledge / Context
      ↓
Recommendations
      ↓
User Decision
      ↓
Garden Activity
      ↓
Garden History

This keeps recommendations replaceable without compromising the user's data.


---

20. Vocabulary Architecture

Canonical data uses stable English identifiers.

Example:

partial_shade
perennial
user_observation
moderate

User-facing labels are supplied through localisation.

Vocabulary categories are separated into:

Core
Garden
Plant
Soil
Activity
Problem
Planning
Provenance

Vocabularies should remain small and stable.

Reference data such as actual plants, cultivars, pests and diseases should not be forced into fixed enums merely because they have controlled names.


---

21. Localisation

The internal data model is language-independent.

The application should be designed for future support of:

English;

Afrikaans;

other languages.


Canonical IDs, database identifiers and import/export identifiers must not change with UI language.

The interface must also avoid assumptions about text length.


---

22. Geographic and Seasonal Context

The application must account for:

garden location;

hemisphere;

season;

climate context;

geographic applicability.


Seasonal information must not assume that a particular month represents the same season globally.

Knowledge adapted from another geographic or hemispheric context should retain its source relationship and adaptation information.


---

23. Offline-First Architecture

Core garden functionality should work without Internet access.

Offline functionality should include, where locally available:

garden viewing;

garden editing;

plant records;

activity recording;

observations;

history;

stored knowledge;

planning;

recommendations based on stored information.


Internet-dependent functionality must remain optional and clearly identify its dependency.

A future weather service failing must not prevent ordinary garden operation.


---

24. Data Integrity

Historical garden information is a primary asset.

The system should protect against accidental loss of:

past plantings;

activities;

observations;

harvests;

measurements;

outcomes;

experiments;

decisions.


Important destructive operations should use appropriate confirmation, undo, archival or migration mechanisms.


---

25. Import and Export

Import/export uses stable canonical identifiers rather than display labels.

Import flow:

Select file
    ↓
Validate
    ↓
Show summary
    ↓
Identify errors/conflicts
    ↓
Allow cancellation
    ↓
Confirm
    ↓
Import
    ↓
Report result

Invalid records must not be silently accepted.

Export must preserve enough information to reconstruct the user's garden and its history.


---

26. Modular Architecture

The application should use a modular architecture without unnecessarily fragmenting the product.

Major functional areas include:

Garden
Plants
Planning
Recommendations
Activities
Observations
Harvests
Inbox
Experiments
Import/Export

Modules should communicate through well-defined domain interfaces.

A failure or future change in an optional module should not unnecessarily break unrelated core functionality.


---

27. Implementation Philosophy

The application should favour:

a simple architecture;

local-first operation;

clear domain models;

deterministic business rules;

replaceable recommendation logic;

explicit provenance;

versioned data;

migrations;

testable modules;

stable interfaces.


It should avoid architectural complexity that exists only to support hypothetical future requirements.


---

28. Performance

Common local interactions should feel immediate.

Priority operations include:

opening Home;

opening Garden;

viewing a plant;

opening Quick Add;

recording an activity;

saving an observation;

viewing history.


Complex analysis may take longer, but ordinary garden interaction should not feel like a database query.


---

29. Accessibility

The application should support:

readable text;

sufficient contrast;

touch-friendly controls;

clear icons with accompanying meaning;

non-colour-only status communication;

accessible labels;

predictable navigation;

sensible text scaling.


Important states should communicate meaning through text or other non-colour indicators as well as colour.


---

30. Units and Dates

User-facing units should follow appropriate locale preferences.

The underlying system should retain canonical units where required.

Conversions must preserve appropriate precision.

Dates should follow the user's locale.

Seasonal interpretation must use geographic context rather than assuming a universal calendar.


---

31. User Engagement Levels

The application supports three broad usage patterns.

Casual

Primarily:

records plants;

records harvests;

occasionally uses recommendations.


Active

Additionally:

records activities;

records observations;

plans planting;

maintains soil information.


Advanced

Additionally:

records detailed conditions;

records measurements;

uses experiments;

analyses history;

maintains cultivar-level information;

uses detailed recommendations.


The application must not require Advanced behaviour from Casual users.


---

32. Feature Exposure

Advanced functionality should appear when useful.

For example:

Water:
Moderate

may be sufficient for an ordinary user.

An advanced user may expand:

Growth-stage water requirements

to see:

establishment;

vegetative;

flowering;

fruiting;

dormancy.


The underlying data can therefore be sophisticated without making the ordinary interface complicated.


---

33. Notifications and Inbox

The system should prioritise genuinely useful information.

Inbox items may be:

Important
Useful
Informational

Users should be able to:

complete;

mute;

defer;

dismiss.


Mute and dismiss are different operations.

Muted items remain reviewable.

V1 should avoid building a complex notification-management platform.


---

34. Experiments and Learning

Experiments are optional.

When used, the system should distinguish:

Observed result

from:

Conclusion

For example:

Observed:
Mulched bed required fewer recorded watering events.

Conclusion:
This suggests mulch may have reduced watering requirements in this garden.

The application must not turn a local observation into a universal horticultural rule without appropriate evidence.


---

35. Future Community Knowledge

Future versions may optionally allow gardeners to contribute information toward broader knowledge.

This must remain separate from private garden management.

The system should distinguish:

Personal observation
→ Potential contribution
→ Aggregated observations
→ Emerging pattern
→ Local finding
→ Reviewed guidance
→ Established knowledge

Promotion between these states must not be automatic merely because more observations exist.

Privacy and location-sharing must be explicit.


---

36. Security and Privacy Principles

The user's garden belongs to the user.

The application should:

keep private garden information private by default;

avoid mandatory community sharing;

avoid exposing precise location without explicit permission;

separate private observations from shared knowledge;

make contribution decisions explicit.



---

37. Core Product Constraints

The following constraints should guide implementation:

Offline-first

The application must remain useful without continuous connectivity.

Historical durability

Garden history must survive application evolution.

Explainability

Important recommendations must be understandable.

Progressive complexity

Advanced capabilities must not burden ordinary users.

Data integrity

The system must distinguish planned actions from actual events.

User control

Recommendations are advisory.

Stable vocabulary

Canonical identifiers must remain language-independent and durable.

Modular design

Optional functionality should not unnecessarily affect core functionality.

Simplicity

Do not introduce architecture, data fields or workflows without a clear product benefit.


---

38. V1 Success Criteria

V1 is successful if an ordinary gardener can use it without understanding:

database entities;

relationships;

controlled vocabularies;

provenance models;

confidence models;

recommendation algorithms;

spatial data structures.


The gardener should instead experience:

My garden
    ↓
What is happening?
    ↓
What could I do?
    ↓
What did I choose?
    ↓
What happened?
    ↓
What have I learned?


---

39. Documentation Structure

The project documentation should remain separated by concern.

Core documents include:

PROJECT_OVERVIEW.md
V1_SCOPE.md
DATA_MODEL.md
VOCABULARY_INDEX.md
vocabularies/CORE_VOCABULARIES.md
V1_USER_EXPERIENCE.md
V1_SCREEN_SPECIFICATION.md
RECOMMENDATION_ENGINE.md
IMPORT_EXPORT_SPECIFICATION.md

Supporting specifications should define implementation details without duplicating the product vision.

Where two documents describe the same rule, the more specialised document should provide the detailed specification while the overview should retain only the principle and reference the specialised document.


---

40. Documentation Principle

Project documentation should answer different questions without repeatedly answering the same question.

PROJECT_OVERVIEW
    What is this product and how does it fit together?

SCOPE
    What is and is not included?

DATA_MODEL
    What information does the system represent?

VOCABULARIES
    What controlled identifiers does the system use?

USER EXPERIENCE
    How should the product feel and behave?

SCREEN SPECIFICATION
    What should individual screens and interactions contain?

RECOMMENDATION ENGINE
    How are suitability and recommendations determined?

IMPORT/EXPORT
    How is garden data moved into and out of the application?

This separation should be maintained as the project develops.


---

41. Design Test

A proposed feature should be challenged with the following questions:

1. Does it make gardening easier?


2. Does it support a meaningful gardening activity or decision?


3. Does it require unnecessary data entry?


4. Can a casual user ignore it?


5. Is it explainable?


6. Does it preserve history?


7. Can it work offline where appropriate?


8. Can it remain modular?


9. Does it introduce unnecessary architectural complexity?


10. Is the additional complexity justified by a real user benefit?



If the answer to the final question is no, the feature should normally be simplified or excluded.


---

42. Product Priority

When design or implementation choices conflict, prioritise:

1. Reliability of core garden functions


2. Ease of common gardening tasks


3. Clarity


4. Data integrity and historical preservation


5. Explainability


6. Progressive precision


7. Accessibility


8. Advanced functionality


9. Visual polish



A visually impressive feature must not compromise a core gardening function.


---

43. Final Principle

> The application should become more useful as the gardener uses it, without becoming more complicated to use.



Garden Planner & Manager is fundamentally a system for helping a gardener:

> understand their garden, make better decisions, record what actually happens, and learn from their own garden over time.



Everything else exists to support that loop.