Garden Planner & Manager — V1 Scope Specification

Document: docs/V1_SCOPE.md
Version: 0.3
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the functional scope of Version 1 of the Garden Planner & Manager.

It establishes:

what V1 must provide;

what V1 may provide if implementation remains straightforward;

what is deliberately excluded from V1;

the boundaries between core functionality and future functionality;

the minimum level of capability required for V1 to be useful.


The purpose of this document is to prevent scope expansion while ensuring that the fundamental Garden Planner & Manager experience is complete.

V1 should provide a genuinely useful garden-management application rather than attempting to implement the entire long-term product vision.


---

2. V1 Product Goal

V1 should allow a gardener to:

> Represent their garden, understand what they can grow, plan what to do, record what actually happens, and learn from their garden history.



The application should work with incomplete information.

The gardener does not need:

laboratory soil testing;

complete plant information;

detailed measurements;

comprehensive activity logging;

advanced horticultural knowledge;

constant Internet access.


The application should become more useful as information is added.


---

3. V1 Scope Principles

V1 follows these principles.

3.1 Useful before complete

The application must provide value before the garden has been fully documented.


---

3.2 Record facts before building complexity

The system should prioritise reliable garden records over elaborate automation.


---

3.3 Recommendations must be explainable

A recommendation must be understandable and must expose important uncertainty.


---

3.4 History is a first-class capability

V1 must preserve and expose garden history.


---

3.5 Unknown information is valid

Missing information must not prevent ordinary garden use.


---

3.6 Offline-first core operation

Core garden functionality must work using locally stored information without requiring continuous Internet access.


---

3.7 Progressive complexity

Casual gardeners should be able to use the application without engaging with advanced functionality.


---

3.8 Modules should remain replaceable

V1 functionality should be divided into reasonably independent modules so that future improvements do not require redesigning the entire application.


---

4. V1 Core Capabilities

V1 consists of the following core capabilities:

1. Garden and spatial management


2. Plant knowledge and My Plants


3. Garden conditions and observations


4. Activity recording


5. Harvest recording


6. Garden history


7. Planning


8. Basic recommendation and suitability analysis


9. Garden Inbox


10. Search and filtering


11. Personal preferences


12. Basic experiments


13. Import/export


14. Local persistence and offline operation



These capabilities form the functional core of V1.


---

5. Garden and Spatial Management

5.1 Garden

A user can create and maintain a garden containing:

name;

location;

hemisphere;

geographic context;

growing spaces;

other relevant spatial objects.


Location should provide sufficient context for seasonal and geographic reasoning.


---

5.2 Growing spaces

Users can create growing spaces such as:

raised beds;

in-ground beds;

containers;

greenhouse areas;

orchard areas;

other supported growing-space types.


A growing space should support:

name;

type;

location;

geometry/dimensions;

relevant conditions;

plants;

plans;

activities;

observations;

history.



---

5.3 Spatial editing

Users must be able to modify the size and geometry of supported spatial objects.

Changing geometry must not erase historical information.

Spatial calculations that depend on geometry should update accordingly.


---

5.4 Map

V1 should provide an interactive garden map supporting:

pan;

zoom;

object selection;

object creation;

object editing;

layer visibility;

basic filtering;

current and historical views.


The map is a functional garden-management tool, not merely a visual representation.


---

5.5 Spatial history

The application must allow users to understand what occupied a space previously.

At minimum, users should be able to determine:

> What was planted here?



for a selected growing space and relevant historical period.


---

6. Plant Knowledge

6.1 Plant Library

V1 must provide a Plant Library containing structured plant knowledge.

It should support:

plant search;

plant browsing;

plant details;

growing requirements;

timing information;

plant relationships;

relevant problems;

source/provenance information where applicable.



---

6.2 Plant identity

Plant identity is reference data rather than a controlled vocabulary.

The system should support the distinction between:

plant species/types;

cultivars;

the user's actual plant instances.



---

6.3 Cultivars

V1 should support cultivar information where available.

Cultivar-specific information must remain distinguishable from species-level information.

The user may record a cultivar without being required to do so.


---

7. My Plants

V1 must provide a representation of plants actually grown or intended to be grown by the user.

A My Plant record should be associated where applicable with:

plant;

cultivar;

growing space;

planting/planned date;

activities;

observations;

problems;

harvests;

outcomes;

history.


The distinction between:

Plant Library

and:

My Plants

must remain clear throughout the application.


---

8. Adding Plants

The common planting workflow should be short.

Minimum flow:

1. Select plant.


2. Optionally select cultivar.


3. Select growing space.


4. Record planned or actual planting date.


5. Save.



Additional information may be added later.

The user must not be required to complete a comprehensive plant record simply to record a planting.


---

9. Garden Conditions

V1 must support recording conditions relevant to garden decisions.

Potential condition domains include:

light;

water;

moisture;

soil;

drainage;

pH;

fertility;

temperature;

frost;

other materially relevant environmental conditions.


Not every garden needs every condition recorded.


---

10. Progressive Precision

V1 must support different levels of information quality.

For example:

Unknown

→

Observation

→

Estimate

→

Measurement

→

Repeated measurements

The system must preserve the distinction between these information states.

A user should not be required to provide measurements when observations are sufficient for the intended purpose.


---

11. Soil Information

V1 must support both measured and qualitative soil information.

Users should be able to record:

known measurements;

observations;

estimates;

test methods;

dates;

locations;

confidence where appropriate.


Examples include:

pH;

drainage;

moisture;

texture;

compaction;

salinity;

fertility;

visible organic matter.



---

12. Soil Proxy Estimates

Where direct measurement is unavailable, V1 may use structured observations to estimate relevant conditions.

For example:

> Water pools after heavy rain.



may contribute to:

> Estimated drainage: Probably slow
Confidence: Low



The estimate must remain clearly identified as an estimate.

The system must not represent an inferred soil property as a measured fact.


---

13. Soil Testing Guidance

When missing soil information materially limits an important recommendation, V1 may suggest obtaining a measurement.

For example:

> A soil test may help distinguish between the likely causes.



Such prompts must be optional.

The application must continue to work when the user declines.


---

14. Activities

V1 must provide a general activity/event mechanism for recording what actually happened.

Core activity types include:

planting;

watering;

feeding;

pruning;

intervention;

observation-related activity where appropriate;

other supported garden activities.


Activities should be associated with relevant garden objects where applicable.


---

15. Quick Add

Quick Add is a core V1 workflow.

The user should be able to rapidly record common events such as:

Plant;

Water;

Harvest;

Observe;

Feed;

Prune;

Intervention.


The interface should use current context to reduce data entry.

For example, opening Quick Add while viewing Bed 2 may preselect Bed 2.

The user must remain able to change the context.


---

16. Planned Versus Actual

V1 must distinguish between:

planned

and:

completed/actual.

A plan is never evidence that an activity occurred.

Actual activities must be independently recorded.


---

17. Repeated and Partial Activities

V1 should support recurring plans where they can be implemented without disproportionate complexity.

Example:

> Water every 3 days.



The resulting actual activities remain independent records.

Where a plan covers multiple targets, V1 should support partial completion.

Example:

> Water Beds 1–4



may result in:

> Beds 1–3 completed
Bed 4 remaining




---

18. Harvests

Harvest recording is a core V1 capability.

A harvest should support:

plant;

location;

date;

quantity;

unit.


Weight must not be mandatory.

Supported quantities may include:

count;

bunch;

basket;

container;

other appropriate units.


Optional size categories may include:

very small;

small;

regular;

large;

very large.



---

19. Harvest Losses

Harvest losses should be recordable separately from successful harvests.

A loss should support, where appropriate:

plant;

location;

quantity;

unit;

date;

cause.


The system must not invent lost yield.


---

20. Observations

V1 must provide an easy observation workflow.

Users should be able to record structured observations concerning:

plants;

soil;

growing spaces;

environmental conditions;

problems.


Observation forms should be context-sensitive.

The application should not present a universal form containing every possible observation field.


---

21. Observation Versus Diagnosis

V1 must maintain a clear distinction between:

Observation

and:

Diagnosis/inference.

For example:

> Leaves are yellowing.



is an observation.

The system may subsequently suggest:

> Possible causes



but must not silently convert the observation into a confirmed diagnosis.


---

22. Problems and Diagnostic Support

V1 may provide structured support for recording and reasoning about:

pests;

diseases;

symptoms;

damage;

other garden problems.


The application may present possible causes or relevant information.

V1 must not imply certainty where the available evidence does not justify it.

Full automated diagnosis is not a V1 requirement.


---

23. Garden History

Garden history is a core capability.

V1 must preserve and allow review of:

plantings;

activities;

observations;

harvests;

losses;

soil measurements;

interventions;

plans;

experiments;

relevant recommendations and decisions.


Historical information must not be silently overwritten when doing so would destroy useful historical meaning.


---

24. Historical Corrections

Ordinary corrections should remain easy.

Where changing a record would alter the meaning of a historical fact, V1 should preserve the original history or record the correction appropriately.

Historical preservation takes priority over simplistic record replacement.


---

25. Planner

The Planner must support two primary workflows.

Forward planning

> What should I plant or do next?



Space-driven planning

> What could I grow here?



Both should use the same underlying planning and recommendation capabilities where appropriate.


---

26. Empty-Space Planning

A user should be able to select a growing space and request suitable options.

The application may consider:

current conditions;

season;

timing;

available space;

soil;

light;

water;

rotation;

existing plants;

infrastructure;

relationships;

personal preferences;

relevant garden history.


Only information relevant to the decision should materially affect the result.


---

27. Recommendation Engine

V1 requires a deterministic, explainable recommendation capability.

Recommendations should evaluate relevant factors rather than simply producing unexplained rankings.

Potential factor dimensions include:

light;

water;

moisture;

soil;

pH;

fertility;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

history.


A recommendation does not need to evaluate every dimension for every plant.


---

28. Recommendation Results

V1 recommendations may produce states such as:

recommended;

conditional;

possible;

marginal;

not recommended;

unsuitable;

insufficient information;

expired.


Individual factors may be presented using:

excellent;

good;

marginal;

poor;

unknown.


These values must remain distinct from confidence.


---

29. Recommendation Confidence

Recommendation suitability and confidence are separate concepts.

Example:

> Good candidate
Confidence: Moderate



because:

> Winter sunlight information is incomplete.



V1 must make material uncertainty visible.


---

30. Recommendation Explanations

Significant recommendations must provide a clear explanation.

The explanation should identify, where relevant:

important information used;

positive factors;

negative factors;

missing information;

uncertainty;

conditions that could change the result.


The user should be able to understand why a recommendation was made without understanding the recommendation engine's implementation.


---

31. Conditional Recommendations

Recommendations may be conditional.

Examples:

> Good candidate if drainage is improved.



> Suitable once frost risk decreases.



> Potentially suitable, but soil pH should be confirmed.



Conditional recommendations are preferable to false certainty.


---

32. Do Nothing

V1 must permit recommendations equivalent to:

> Nothing suitable right now.



For example:

> Address the drainage problem before planting.



The recommendation system should not be forced to produce a plant merely because the user requested one.


---

33. User Override

Users must be able to reject, modify, defer or override recommendations.

An override is a legitimate user decision.

The application may record the decision and relevant reason where useful.

It must not treat disagreement with the recommendation as an error.


---

34. Personal History in Recommendations

V1 should allow relevant garden history to influence recommendations.

For example:

> General suitability: Good



combined with:

> Poor results with this cultivar in this space during the previous two seasons.



may produce:

> Consider another cultivar or growing space.



When personal history materially affects a recommendation, the application should identify this.


---

35. Personal Learning

V1 may identify useful patterns in a user's own garden history.

Such patterns must be explicitly labelled as garden-specific.

Example:

> Your garden pattern



rather than presenting the result as universal horticultural knowledge.

V1 must not silently modify recommendation rules based on personal history.


---

36. Preferences

V1 should support basic user preferences where they materially improve recommendations.

Potential preferences include:

favourite plants;

plants to avoid;

food-production priority;

native preference;

pollinator preference;

low-maintenance preference;

water conservation;

experimentation;

space utilisation.


Preferences should influence recommendations without obscuring their effect.

A plant excluded by preference should remain explainable where appropriate.


---

37. Inbox

V1 must provide a prioritised Inbox for meaningful garden information.

Inbox items may include:

actions;

warnings;

observation requests;

information requests;

experiments;

useful knowledge.


The Inbox should prioritise importance rather than attempting to report every possible event.


---

38. Inbox Priority

V1 should distinguish broadly between:

Important

Likely requiring attention.

Useful

Worth considering.

Informational

Interesting but not urgent.

The system should avoid excessive notifications.


---

39. Inbox Item Explanation

Significant Inbox items must explain why they exist.

For example:

> Rotation warning



followed by:

> Solanaceae were grown in this space last season.



The user should be able to understand the basis for the warning.


---

40. Inbox Management

V1 should support:

completing/resolving items;

muting items;

dismissing items.


Mute and dismiss must remain distinct.

Muted items should remain reviewable.

A dismissal must not automatically mean permanent suppression of the category.


---

41. Search

V1 must support search across commonly used garden concepts.

Search should include, where appropriate:

plants;

cultivars;

growing spaces;

problems;

activities;

observations.


Aliases and common names may resolve to canonical plant identities where configured.


---

42. Filtering

V1 should provide filtering where lists become sufficiently large to benefit from it.

Potential filters include:

area;

growing space;

plant;

family;

cultivar;

lifecycle;

current/planned/historical;

season;

problem;

suitability;

rotation group;

experiment.


Filtering should remain understandable and easily reversible.


---

43. Empty and Error States

Every major V1 feature must provide useful empty and error states.

Empty states should:

explain what the screen represents;

explain why there is currently no information;

provide the most useful next action.


Errors should use user-understandable language and provide recovery where possible.

Technical details may be available separately.


---

44. Experiments

V1 may include a lightweight experiment capability.

An experiment can define:

question;

hypothesis;

treatment;

control;

measurements;

timeframe.


Experiments should reuse ordinary garden records rather than creating a separate activity/event system.


---

45. Experiment Results

V1 experiment results must distinguish:

Observed result

from:

Conclusion.

The application should avoid presenting a result from one garden as a universal horticultural rule.


---

46. Import and Export

V1 should provide reliable import/export of garden data.

Export is particularly important for data ownership and preservation.

Import must:

1. select a file;


2. validate it;


3. show a summary;


4. identify errors and conflicts;


5. allow cancellation;


6. require confirmation;


7. report the result.



Invalid records must not be silently imported.


---

47. Import Validation

Import results should distinguish:

information;

warnings;

errors;

critical failures.


Users should be able to identify affected records and understand what needs correction.

Raw database errors must not be the primary user-facing explanation.


---

48. Offline Operation

The following core capabilities must work offline using locally available data:

opening the application;

viewing the garden;

viewing plants;

recording activities;

recording observations;

recording harvests;

viewing history;

using stored garden information for planning;

using available recommendation logic.


Features that genuinely require external information may be unavailable offline without preventing the remainder of the application from working.


---

49. Data Safety

V1 must protect garden data from accidental loss.

Significant destructive operations should require appropriate confirmation.

This includes:

deleting a garden;

deleting large quantities of history;

resetting application data;

importing conflicting data.


Where practical, export/backup functionality should be easy to access.


---

50. Accessibility

V1 must support basic accessibility principles including:

readable text;

sufficient contrast;

touch-friendly controls;

accessible labels;

predictable navigation;

text scaling;

icons accompanied by understandable meaning;

status information that does not rely solely on colour.



---

51. Localisation Readiness

V1's underlying data remains English and canonical.

The user interface should be designed so that future localisation can support:

English;

Afrikaans;

other languages.


UI text must not be used as database identifiers or rule inputs.

The layout must accommodate translated text without relying on fixed English text lengths.


---

52. Dates, Seasons and Units

V1 must respect:

user locale;

geographic location;

hemisphere;

seasonal context;

configured units.


Internal canonical representations may differ from user-facing representations.

Conversions must preserve appropriate precision.


---

53. V1 Navigation

The primary navigation should consist of:

Home;

Garden;

Plants;

Planner;

Inbox.


A prominent:

+ Add

action should provide access to common recording workflows.

V1 should avoid continually adding top-level navigation destinations for individual features.


---

54. Home

Home must answer:

> What matters now?



It may surface:

current priorities;

upcoming activities;

important Inbox items;

garden status;

recent activity;

planning opportunities;

active experiments.


A new garden must have a useful setup state rather than an empty dashboard.


---

55. Plant and Garden Separation

V1 must preserve the distinction between:

knowledge about plants

and:

the user's plants.

Similarly, the application must distinguish:

general horticultural knowledge

from:

information observed or measured in the user's garden.

This distinction is fundamental to trustworthy recommendations.


---

56. Provenance

V1 should retain provenance for information where it materially affects interpretation.

Examples include:

verified guidance;

published research;

institutional guidance;

user observation;

user measurement;

imported information;

derived information.


Provenance should normally be exposed progressively rather than cluttering ordinary screens.


---

57. Explainability

V1 recommendations and important inferred information must be explainable.

Users should be able to determine:

what the application knows;

what the user recorded;

what was measured;

what was estimated;

what was inferred;

what came from external knowledge;

how uncertainty affects the result.



---

58. Core V1 User Workflows

V1 must support these end-to-end workflows.

Workflow 1 — Set up a garden

Create garden → add location → add growing space → optionally record conditions.


---

Workflow 2 — Add a plant

Find plant → select cultivar if known → select space → record planting → save.


---

Workflow 3 — Plan an empty space

Select space → ask what could grow → review recommendations → inspect explanation → create plan or override.


---

Workflow 4 — Record an activity

Quick Add → choose activity → use contextual information → save.


---

Workflow 5 — Record an observation

Quick Add → Observe → select observation type → record relevant information → save.


---

Workflow 6 — Record a harvest

Quick Add → Harvest → select plant/location → record quantity → save.


---

Workflow 7 — Review history

Select garden/space/plant → view relevant historical records → inspect previous outcomes.


---

Workflow 8 — Understand a recommendation

Open recommendation → inspect factors → view Why? → inspect uncertainty/history → decide.


---

Workflow 9 — Manage Inbox

Open Inbox → understand priority → inspect reason → complete, mute or dismiss.


---

59. Minimum V1 Plant Knowledge

V1 does not require an exhaustive plant database.

The initial knowledge base should instead contain a sufficiently useful set of plants to demonstrate the full system.

The architecture must support expansion without requiring structural changes.

Plant knowledge should be treated as reference data rather than hard-coded application logic wherever practical.


---

60. Minimum V1 Recommendation Knowledge

The recommendation engine does not need to encode every horticultural rule.

It must instead demonstrate reliable reasoning across the most important V1 dimensions, particularly:

timing;

light;

water/moisture;

soil;

space;

temperature/frost where relevant;

rotation;

basic relationships;

user history where sufficiently supported.


A smaller reliable rule set is preferable to a large opaque rule set.


---

61. V1 Knowledge Quality

V1 knowledge should distinguish:

source;

provenance;

applicability;

confidence where appropriate;

geographic/seasonal context.


Knowledge should not be presented with greater certainty than its supporting evidence warrants.


---

62. V1 Non-Goals

The following are explicitly outside the required V1 scope.

62.1 Social network

V1 does not require:

public profiles;

following users;

feeds;

comments;

likes;

social activity streams.



---

62.2 Mandatory cloud account

V1 must not require a cloud account simply to use the core garden application.


---

62.3 Automatic community sharing

Garden data must not automatically become community knowledge.


---

62.4 AI chat as the primary interface

Natural-language AI assistance may be considered later.

The primary V1 interface remains structured garden interaction.


---

62.5 Professional GIS

V1 does not attempt to provide:

professional GIS tooling;

advanced coordinate editing;

survey-grade mapping;

complex spatial analysis.


The map only needs to support useful garden planning and management.


---

62.6 Fully automated garden design

V1 does not need to automatically design an entire garden.

The user remains responsible for decisions.


---

62.7 Sophisticated weather platform

V1 does not attempt to provide a comprehensive weather service or weather dashboard.

Weather information may become an optional future integration.


---

62.8 Complex chemical treatment system

V1 does not attempt to provide sophisticated chemical treatment planning, dosing or regulatory workflows.

Basic intervention recording may still be supported.


---

62.9 Excessive gamification

V1 does not require:

points;

achievements;

streaks;

badges;

leaderboards.



---

62.10 Advanced statistical analysis

V1 does not require sophisticated statistical dashboards or modelling.

Basic historical comparisons and experiment results are sufficient.


---

63. V1 Optional / Lower-Priority Capabilities

The following may be included when they can be implemented without materially increasing complexity or delaying the core product:

recurring plans;

partial completion;

lightweight experiments;

personal pattern detection;

advanced filtering;

richer diagnostic suggestions;

more detailed map layers;

additional soil proxy estimates;

additional recommendation dimensions;

expanded import/export validation;

richer personal preferences.


These must not compromise the required V1 capabilities.


---

64. V1 Completion Threshold

A feature is considered sufficiently complete for V1 when an ordinary gardener can use it reliably for its intended purpose without understanding the application's internal architecture.

V1 does not require every possible edge case or advanced workflow.

It does require that the core workflows be coherent from beginning to end.


---

65. V1 Acceptance Criteria

V1 should be considered functionally successful when a gardener can:

Garden

create a garden;

add a growing space;

represent its dimensions;

edit its geometry;

record basic conditions;

view the garden spatially.


Plants

search the Plant Library;

view plant information;

add a plant to My Plants;

record a cultivar;

associate a plant with a growing space.


Planning

select an empty space;

request suitable plants;

receive explainable recommendations;

see important suitability factors;

understand uncertainty;

create a plan;

override a recommendation.


Recording

record planting;

record watering;

record feeding;

record pruning;

record interventions;

record observations;

record harvests;

record losses.


History

see previous plantings;

review activities;

review observations;

review harvests;

review soil information;

see relevant historical outcomes;

understand when history affects recommendations.


Soil

record known soil information;

record observations;

record measurements;

distinguish measurements from estimates;

understand when additional testing may be useful.


Inbox

see meaningful outstanding items;

understand why they matter;

complete them;

mute them;

dismiss them.


Data

continue using core functionality offline;

export garden data;

import validated data;

avoid accidental historical data loss.



---

66. V1 Scope Boundary Test

When considering a new feature, ask:

1. Does it directly support garden management?


2. Does it improve a core V1 workflow?


3. Does it provide meaningful value to an ordinary gardener?


4. Can it operate without requiring unnecessary data entry?


5. Can a casual user ignore it?


6. Is its behaviour explainable?


7. Does it preserve historical information?


8. Can it remain modular?


9. Does it work offline where the core workflow requires it?


10. Would excluding it materially weaken the V1 product?



If the answer to the final question is no, the feature should generally be deferred.


---

67. Scope Priorities

When implementation trade-offs are required, prioritise:

1. Reliable garden data storage


2. Garden and spatial management


3. Plant management


4. Recording actual garden events


5. Garden history


6. Basic planning


7. Explainable recommendations


8. Soil and condition recording


9. Inbox


10. Import/export and data safety


11. Advanced analysis


12. Optional integrations and enhancements



A lower-priority feature must not delay or destabilise a higher-priority capability.


---

68. Architectural Boundary

V1 functionality should be implemented as cooperating modules rather than one monolithic feature system.

Core modules include:

Garden;

Spatial;

Plants;

Conditions/Soil;

Activities;

Observations;

Harvests;

Planning;

Recommendations;

History;

Inbox;

Experiments;

Import/Export.


Modules should communicate through stable domain concepts and shared identifiers.

A future replacement of one module should not require rewriting unrelated modules.


---

69. Data Boundary

V1 must preserve the distinction between:

Reference knowledge

and:

User garden data.

Reference knowledge includes things such as:

plants;

cultivars;

requirements;

relationships;

problems;

source information.


User data includes:

gardens;

growing spaces;

plantings;

observations;

activities;

harvests;

measurements;

plans;

decisions;

experiments;

outcomes.


This distinction is essential for future knowledge updates without corrupting historical garden records.


---

70. Knowledge and Recommendation Boundary

The recommendation system should consume structured knowledge and garden information.

It should not become the storage location for horticultural knowledge.

Likewise, UI screens should not contain hidden recommendation rules.

The separation should remain approximately:

Knowledge → Rules → Recommendation → Explanation → User decision

This makes the system easier to test, explain and extend.


---

71. V1 Data Ownership Principle

The user's garden information belongs to the user's garden.

V1 must make it possible to preserve and extract that information independently of future services or integrations.

No future community or cloud feature should be allowed to make the core garden dependent on external infrastructure.


---

72. V1 Simplicity Principle

V1 should favour:

> A small number of dependable capabilities that work together well



over:

> A large number of partially implemented features.



The application should feel complete in its core workflows even though the long-term product will eventually be much larger.


---

73. Final V1 Definition

Version 1 is:

> An offline-capable garden management and planning application that lets a gardener represent their garden, manage plants and spaces, record observations and activities, preserve garden history, plan future planting, and receive explainable recommendations based on garden conditions, horticultural knowledge and personal history.



V1 is not intended to automate gardening.

It is intended to make the gardener's own decisions easier, better informed and easier to remember.

The defining test is therefore not:

> How many features does V1 contain?



It is:

> Can a gardener use V1 to understand their garden, decide what to do, record what happened, and become more informed by their own garden history?



If it can, V1 has achieved its purpose.