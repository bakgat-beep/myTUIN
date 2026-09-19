Garden Planner & Manager — V1 User Experience Specification

Document: docs/V1_USER_EXPERIENCE.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the Version 1 user experience for Garden Planner & Manager.

It translates the product vision, architecture, data model, and V1 scope into practical interaction principles and workflows.

It defines:

how users interact with the application;

how information is presented;

how common tasks are completed;

how complexity is progressively revealed;

how optional and advanced functionality is handled;

how the application remains useful at different levels of engagement.


It does not define pixel-level screen layouts. Those belong in:

docs/V1_SCREEN_SPECIFICATION.md


---

2. Core UX Principle

The application should answer:

> What do I need to know or do right now?



before asking:

> What information can I enter?



Garden Planner & Manager is not primarily a data-entry application. Data collection exists because it makes the garden more useful.

The interface should therefore prioritise:

1. actions;


2. decisions;


3. observations;


4. useful information;


5. recommendations;


6. relevant history.



The ideal experience is:

> A garden notebook that understands your garden.



The application should become more useful as the gardener uses it without requiring the gardener to become more technically sophisticated.


---

3. Gardener in Control

The application is an assistant, not an authority.

It may:

recommend;

warn;

suggest;

compare;

remind;

identify uncertainty;

surface historical patterns;

suggest investigations or actions.


The gardener must be able to:

accept;

reject;

modify;

postpone;

ignore;

override.


An override is a legitimate gardening decision and must not be treated as an error.

Where useful, the application may record the decision and explain that the gardener deliberately chose an alternative.


---

4. Progressive Complexity

The same application must support both:

> “A place to remember what I planted.”



and:

> “A detailed planning and analysis system for my entire garden.”



Basic functionality must work without advanced configuration.

Advanced functionality should become visible when useful rather than being presented everywhere from the beginning.

The application should support three broad usage styles:

Casual

Primarily:

records plants;

records harvests;

occasionally checks recommendations.


Active

Additionally:

records activities;

records observations;

plans planting;

maintains basic soil information.


Advanced

Additionally:

maintains detailed conditions;

records measurements;

uses experiments;

analyses history;

maintains cultivar-level information;

uses detailed recommendations.


Casual users must not be forced into Advanced behaviour.


---

5. Progressive Information

Information should normally be presented at three levels.

Level 1 — Immediate

Information needed to understand or act.

Example:

Tomatoes

Good candidate

Light: Good

Water: Good

Soil: Good

Timing: Now


Level 2 — Explanation

Available when the user wants to understand the result.

Example:

Why is this a good candidate?

Your bed receives sufficient summer sunlight and has suitable moisture conditions. Tomatoes were not grown here last year, so there is no significant rotation warning.

Level 3 — Technical detail

Available through progressive disclosure for users who want greater detail.

Potential information includes:

requirement source;

geographic context;

hemisphere;

growth stage;

requirement range;

evidence type;

confidence;

adaptation;

source date.


Technical information must not clutter ordinary workflows.


---

6. Unknown and Incomplete Information

Unknown information is valid information.

Examples:

Soil pH: Unknown

Winter sunlight: Unknown

Salinity: Unknown

Cultivar: Unknown


The application must not make users feel that missing information represents failure.

It should determine whether missing information actually matters.

For example, unknown winter sunlight may be irrelevant when planning a summer crop.

The application should not repeatedly ask users to complete information merely because the database supports it.

When missing information materially affects a decision, the application may explain why it would be useful and offer an optional way to provide it.


---

7. Progressive Precision

Information may become more precise over time without requiring precision at first entry.

Example:

Initial

Drainage: Unknown

Later

Observation: Water pools after heavy rain.

Later

Estimate: Probably slow drainage — Low confidence.

Later

Measurement: Water remains for approximately 6 hours.

Later

Repeated measurements: Typical drainage time: 5–7 hours.

The application must preserve the distinction between:

observation;

estimate;

inference;

measurement.


It must never present an estimate as though it were a measurement.


---

8. Navigation

V1 primary navigation is:

Home

Garden

Plants

Planner

Inbox


with a prominent:

+ Add

action.

The navigation should remain stable.

New functionality should normally be placed within existing areas rather than creating additional top-level destinations.


---

9. Home

Home answers:

> What matters now?



It is not primarily a statistics dashboard.

It may surface:

current priorities;

upcoming activities;

planting opportunities;

important Inbox items;

current garden status;

recent activity;

active experiments requiring attention;

relevant planning opportunities.


Content should be dynamically determined by the user's garden and current circumstances.

New-garden state

A new garden should not look empty or intimidating.

Instead:

Let's set up your garden

1. Add your garden location.


2. Add your first growing space.


3. Tell us what you know about its conditions.


4. Add plants when ready.



Optional steps may be skipped.


---

10. Garden

Garden represents the physical and spatial garden.

It provides access to:

garden map;

areas;

growing spaces;

infrastructure;

plants;

spatial conditions;

history;

map layers;

filters.


The map is a functional planning tool, not decorative artwork.


---

11. Garden Map

The map should support intuitive touch interaction.

Users should be able to:

pan;

zoom;

select objects;

create objects;

move objects;

resize objects;

edit geometry;

inspect information;

toggle layers;

apply filters;

view historical states.


Users must not need to understand GIS concepts.

Creating a growing space

Conceptually:

+ Add → Growing Space

Then provide the minimum necessary information:

name;

type;

shape;

dimensions;

location.


Additional characteristics can be added later.

Example:

Bed 2

Type: Raised bed
Size: 3 m × 1 m

Spatial editing

Geometry must be editable without recreating the object.

Changing a bed from 3 m × 1 m to 4 m × 1 m should update relevant spatial calculations while preserving historical records associated with that space.

Overlapping objects

The application must support overlapping conceptual objects such as:

trees;

orchard areas;

root zones;

irrigation lines;

understory areas.


Users should not be forced to decide which object “owns” another object.

Selection and information panels must make overlapping objects understandable.


---

12. Map Layers and Filters

Potential V1 layers include:

growing spaces;

current plants;

planned planting;

historical planting;

infrastructure;

soil;

light;

water;

rotation;

problems;

experiments.


Sensible default layers should be used.

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


Active filters must be visible and easy to remove.


---

13. Historical Map

Historical information should be accessible through an obvious time control.

For example:

Current | 2025 | 2024

or:

History → Select date/year

Users should be able to answer:

> What was planted here?



without navigating through individual activity records.

Historical views must not modify current garden state.


---

14. Plants

The application must clearly distinguish between the Plant Library and My Plants.

Plant Library

The Plant Library represents general plant knowledge.

It supports:

search;

browsing;

filtering;

plant details;

cultivars;

growing requirements;

timing;

relationships;

problems;

interventions;

sources.


My Plants

My Plants represents actual or planned plants in the user's garden.

A plant instance may connect to:

location;

planting event;

cultivar;

activities;

observations;

problems;

harvests;

outcomes;

history.


Example:

Plant Library

Tomato

General knowledge about tomatoes.

My Plants

Tomato — Bed 2

The user's actual planting.

The interface must never make it unclear which type of record the user is viewing or editing.


---

15. Plant Detail

Plant detail should prioritise practical information.

Example:

Tomato

Suitability
Good candidate

Conditions

Light — High

Water — Moderate–High

Fertility — High

Frost — Sensitive


Timing

Now: Indoor sowing

Next: Transplant


Your garden

You have grown tomatoes in Bed 2 before.

Relationships

Basil, lettuce and other relevant plants.

Problems

Relevant pests and diseases.

Technical information should be expandable.


---

16. Cultivars

Cultivars should appear naturally within the plant experience.

Example:

Tomato
Cultivar: Black Krim

The interface must distinguish:

information inherited from the plant;

information specific to the cultivar.



---

17. Adding a Plant

A simple planting should be quick.

Conceptually:

+ Add → Plant

1. Search/select plant.


2. Select cultivar if known.


3. Select location.


4. Select planting or planned date.


5. Confirm.



Optional information can be added later.

A simple planting must not require a large form.


---

18. Planner

Planner supports two primary questions:

> What should I plant in the next few months?



and:

> I have an empty space. What could I plant here?



The same underlying recommendation system may support both.


---

19. Planning an Empty Space

When a user selects a growing space and asks what could grow there, the application may consider:

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

plant relationships;

user preferences;

personal garden history.


Only information relevant to the decision should materially affect the result.


---

20. Recommendations

Recommendations must be explainable rather than unexplained rankings.

Example:

Tomato

Good candidate

Light — Excellent

Water — Good

Soil — Excellent

Temperature — Marginal

Rotation — Poor

Space — Excellent


Overall: Good

Why?

Your space has sufficient summer sunlight and suitable soil. Temperature is currently marginal, and tomatoes were grown here recently, creating a rotation concern.

The recommendation should distinguish between:

suitability;

confidence;

evidence;

missing information.


A high suitability result does not automatically mean high confidence.


---

21. Suitability and Recommendation States

Relevant suitability dimensions may include:

light;

water;

moisture;

soil;

pH;

fertility;

nutrients;

temperature;

frost;

timing;

space;

rotation;

relationships;

infrastructure;

history.


A recommendation does not need to expose every dimension.

Factor results may use:

Excellent;

Good;

Marginal;

Poor;

Unknown.


Recommendation states may include:

Recommended;

Conditional;

Possible;

Marginal;

Not recommended;

Unsuitable;

Insufficient information;

Expired.


Unknown factors must remain visible when they materially affect confidence.


---

22. Conditional and Negative Recommendations

Recommendations may be conditional.

Examples:

> Good candidate if drainage is improved.



> Suitable once frost risk decreases.



> Potentially suitable, but soil pH should be confirmed.



Do nothing is also a valid recommendation.

Examples:

> Soil conditions should be addressed before planting.



> No suitable crop currently fits the available space and timing.



The recommendation system must not manufacture a positive recommendation simply because the user asked for one.


---

23. Recommendation Explanations

Every significant recommendation should provide a Why? action.

The explanation should identify, where relevant:

1. what information was used;


2. what it means;


3. how it affected the result;


4. confidence;


5. important missing information;


6. what could change the recommendation;


7. whether personal history or preferences affected it.



Explanations should be understandable to an ordinary gardener.

Technical evidence should be available through progressive disclosure.


---

24. Adapted Information

When information has been adapted from another geographic or hemispheric context, this should be made clear.

Example:

Adapted timing

This planting window originated from Northern Hemisphere guidance and has been seasonally translated for your Southern Hemisphere garden.

Technical source information should remain available.


---

25. User Preferences

Optional preferences may include:

favourite plants;

plants to avoid;

food-production priority;

native preference;

pollinator preference;

low-maintenance preference;

water conservation;

experimentation;

space utilisation.


Preferences should influence recommendations without silently hiding relevant alternatives.

Example:

Lettuce — Excellent suitability

Excluded by preference

You have marked lettuce as a plant to avoid.


---

26. Quick Add

Quick Add is a core V1 workflow.

Potential actions:

Plant;

Water;

Harvest;

Observe;

Feed;

Prune;

Intervention.


Additional actions may appear under More.

Quick Add should optimise for:

> Record something that just happened.



It should not require creation of a perfect database record.

Additional detail can be added later.


---

27. Context-Aware Quick Add

Context should reduce data entry.

For example, if the user is viewing Bed 2:

+ Add → Water

should default to Bed 2.

The user must still be able to change the context.

Context assists the user but must never trap them.


---

28. Planned Versus Actual

The interface must clearly distinguish:

Planned

from:

Completed

Example:

Planned: Water Bed 2 tomorrow.

Completed: Watered Bed 2 today.

A plan must never automatically become evidence that an activity occurred.

Recurring plans may generate planned occurrences, but every actual occurrence remains an independent record.


---

29. Partial Completion

Plans must support partial completion.

Example:

Plan

Water Beds 1–4

Completed

Beds 1–3

Remaining

Bed 4

The user must not be forced to mark an entire plan complete when only part was completed.


---

30. Harvest

Harvest should be fast.

Conceptually:

+ Add → Harvest

Then:

plant;

location;

quantity;

unit;

optional size category;

date.


Weight must not be required.

Supported quantities may include:

count;

bunch;

basket;

container;

other user-selected units.


Optional size categories:

Very small;

Small;

Regular;

Large;

Very large.



---

31. Harvest Loss

Losses should be recorded separately from successful harvest.

Example:

Harvest loss

Plant: Tomato
Quantity: 3
Cause: Rot

The application must not infer an exact lost yield unless the user explicitly provides or estimates it.


---

32. Observations

Observation should be one of the easiest records to create.

Conceptually:

+ Add → Observe

1. Select context.


2. Select observation type.


3. Record relevant structured observations.


4. Optionally add notes.


5. Optionally record confidence.



Only fields relevant to the selected observation type should be shown.


---

33. Structured Observations

Examples:

Observe → Soil

Potential fields:

moisture;

pooling;

texture;

compaction;

visible organic matter.


Observe → Plant

Potential fields:

growth;

flowering;

fruiting;

damage;

symptoms.


The application should avoid a single enormous generic observation form.


---

34. Observation Versus Diagnosis

The application must keep observation and diagnosis separate.

A user may record:

> Leaves yellowing.



The application may then suggest:

Possible causes

Nutrient deficiency — Low confidence

Water stress — Moderate confidence

Disease — Low confidence


A user observation must never silently become a diagnosis.


---

35. Confidence

User-facing confidence levels may include:

Very low;

Low;

Moderate;

High;

Very high;

Not assessed.


Confidence must be contextual.

Where useful, the UI should identify whether confidence relates to:

what the user observed;

source reliability;

application inference;

recommendation confidence.


These must not be conflated.


---

36. Soil

Soil UX must support users with different levels of knowledge.

A user must be able to say:

> I don't know my soil pH.



The application may ask whether the soil has been tested.

If not, it may offer observation-based estimation.

It should never imply that an estimate is equivalent to a measurement.


---

37. Soil Proxy Workflow

Example:

Soil → Drainage → Add observation

Possible questions:

Does water pool after rain?

How long does it remain?

Does the soil feel saturated?

Does water drain quickly after digging?


The application may produce:

Estimated drainage: Probably slow

Confidence: Low

Why?

This estimate is based on your observations and is not a measured soil property.


---

38. Soil Measurements

Measured information should preserve:

value;

unit;

date;

method/source;

location;

confidence.


Example:

pH: 6.7

Source: Home test kit
Confidence: High

If missing information materially limits a recommendation, the application may suggest an optional test.

Example:

> A soil test may be worthwhile.



Your current information cannot distinguish between several likely causes.


---

39. Inbox

Inbox is a prioritised work queue.

It should contain things that may actually matter to the gardener.

Potential categories:

Action;

Warning;

Observation request;

Information request;

Experiment;

Knowledge.


Priority should distinguish at least:

Important — likely requires attention.

Useful — worth considering.

Informational — interesting but not urgent.

The application should avoid notification fatigue.


---

40. Inbox Explanations

Significant Inbox items should explain why they exist.

Example:

Rotation warning

Solanaceae were grown in this space last season.

Why this matters

Repeated planting of related crops can increase certain disease and soil-related risks.


---

41. Inbox Mute and Dismiss

Mute and dismiss are different actions.

Mute

> Don't bother me about this for now.



Possible options:

temporarily;

until a date;

by category;

by context;

permanently.


Muted items should remain reviewable.

Dismiss

> I have dealt with or otherwise resolved this item.



Dismissal must not automatically mean that the user never wants similar items again.


---

42. Search

Search should be available where users reasonably expect it.

It should support concepts including:

plants;

cultivars;

spaces;

problems;

activities;

observations.


Aliases should resolve to canonical concepts where configured.


---

43. Filtering

Filters should appear when a list is sufficiently large to benefit from them.

Filters should be:

understandable;

reversible;

visible when active;

persistent only when useful.


Ordinary tasks should not require complex filter configuration.


---

44. Empty States

Every major screen must have a useful empty state.

Avoid:

> No data.



Prefer:

> You haven't added any plants yet.



Then offer the most useful next action, such as:

Add a plant

or:

Browse Plant Library

An empty state should explain what the screen is for.


---

45. Errors

Errors should use plain language.

Avoid:

> Database exception 14.



Prefer:

> We couldn't save that change.



Then offer appropriate actions such as:

Retry;

Cancel;

Details, where useful.


Technical information may be available for troubleshooting but should not be the primary message.


---

46. Undo and Destructive Actions

Where practical, provide immediate Undo after actions such as:

deletion;

moving an object;

completing an activity;

changing an important record.


Significant or difficult-to-reverse actions should require confirmation.

Historical information should generally be archived or corrected rather than silently destroyed.


---

47. Historical Data Protection

Garden history is a core product asset.

Updates must not accidentally erase:

past plantings;

observations;

harvests;

outcomes;

soil measurements;

experiments;

recommendations;

decisions.


Future versions must migrate stored data rather than discard it.

Corrections to historical facts should preserve the distinction between the original record and the correction where that distinction matters.


---

48. Personal History and Learning

Personal garden history may supplement general horticultural knowledge.

Example:

General suitability

Tomato — Good

Your garden history

Poor results with this cultivar in this space during the previous two seasons.

Consider

Another cultivar or growing space.

When personal history affects a recommendation, the UI should explicitly identify this:

> Personal history affected this recommendation.



The relevant records should be inspectable.

The application must never silently change its rules based on personal history.


---

49. Personal Patterns

When a meaningful pattern is identified, it should be explicitly labelled as a garden-specific observation.

Example:

Your garden pattern

Carrots have had poor establishment in this bed during three recorded seasons when drainage was poor.

> This is based on your garden history and is not necessarily a universal horticultural rule.



Personal patterns must not automatically be presented as general horticultural knowledge.


---

50. Experiments

Experiments are an optional advanced feature.

Ordinary gardening activities must not require an experiment.

An experiment may define:

question;

hypothesis;

treatment;

control;

measurements;

timeframe.


Experiments should reuse ordinary garden records rather than creating a separate activity system.


---

51. Experiment Results

The application must distinguish:

Observed result

from:

Conclusion

Example:

> Mulched bed required fewer recorded watering events.



Then:

> This suggests mulch may have reduced watering requirements in this garden.



It must not present this as a universal horticultural rule.


---

52. Notifications and Reminders

Notifications should be useful rather than exhaustive.

V1 should prioritise reminders where timing genuinely matters.

The system should avoid building an unnecessarily complex notification framework.

Where notification controls exist, users should eventually be able to control:

categories;

frequency;

quiet periods;

muted contexts.



---

53. Explainability and Provenance

Concepts that may not be self-explanatory should provide a consistent explanation control such as ⓘ.

Explanations may cover:

what the concept means;

why it matters;

where information came from;

how it affects a result;

limitations;

uncertainty.


Users should be able to distinguish information such as:

Verified guidance;

Community observation;

Your observation;

Estimated;

Imported.


Detailed provenance should generally be progressively disclosed rather than displayed everywhere.


---

54. Dates, Seasons and Location

Dates should follow the user's locale.

Seasonal interpretation must respect geographic location and hemisphere.

The application must not assume:

> March = spring.



Location and hemisphere determine seasonal interpretation.


---

55. Units

The interface should use user-friendly units appropriate to the user's locale.

The underlying system may retain canonical units where required.

Conversions must preserve precision.

Potential conversions include:

metres ↔ feet;

litres ↔ gallons;

kilograms ↔ pounds.


Users should not need to understand internal unit standards.


---

56. Localisation

Underlying data uses English canonical identifiers.

The UI may eventually support:

English;

Afrikaans;

other languages.


Localisation applies to user-facing content only.

Translation must not alter:

canonical IDs;

database identifiers;

vocabulary codes;

import/export identifiers;

internal relationships.


UI layouts must accommodate translated text rather than assuming English text length.


---

57. Accessibility

V1 should support:

readable text;

sufficient contrast;

touch-friendly controls;

clear icons with accompanying meaning;

non-colour-only status indicators;

predictable navigation;

accessible labels;

sensible text scaling.


Colour may communicate information but must never be the sole indicator of important state.

For example:

Good

should be accompanied by an appropriate visual indicator rather than represented only by colour.


---

58. Mobile-First Interaction

The application is primarily designed for touch interaction.

Common actions should require minimal taps.

Particular attention should be given to:

Quick Add;

map interaction;

plant lookup;

harvest recording;

observation recording;

planner actions.


Complex configuration may use deeper screens.


---

59. Offline Behaviour

Core garden functionality must remain usable without Internet access.

Ordinary offline operations should not produce confusing connection errors.

If a feature genuinely requires connectivity, the dependency should be explicit.

Example:

> Weather unavailable offline.



> Your saved garden information and planning tools continue to work normally.



A failure in an optional future module must not prevent core local functionality.


---

60. Data Safety and Backup

The UX should make local data loss difficult.

Significant operations such as:

deleting a garden;

deleting large amounts of historical data;

importing conflicting data;

resetting application data


must require clear confirmation.

Where practical, export and backup should be easy to access.


---

61. Import and Export

Import/export is primarily an advanced capability.

Ordinary users should not need to understand the underlying schema.

An import workflow should:

1. select a file;


2. validate it;


3. show a summary;


4. identify errors and conflicts;


5. allow cancellation;


6. confirm the import;


7. report the result.



Invalid data must never be silently imported.

Example:

> 14 records imported successfully.



> 2 records require attention.



The user should be able to identify and correct affected records.


---

62. Context Preservation

Navigation should preserve useful context.

Example:

Garden → Bed 2 → Plant → Tomato → History

Returning should ideally return the user to the relevant Bed 2 context rather than the Garden root.

Related information should be cross-linked.

Examples:

Plant → My Plants;

Plant instance → history;

Problem → affected plants;

Growing space → current plants;

Growing space → history;

Growing space → soil;

Growing space → recommendations.


The user should experience a connected garden rather than a collection of unrelated screens.


---

63. Consistency

The same conceptual action should behave consistently throughout the application.

Examples:

ⓘ = explanation;

+ Add = create or record something;

← = predictable back navigation;

filters behave consistently;

selecting a plant always resolves to the same canonical plant concept.



---

64. Avoiding Database UI

The application must not expose its internal entity model unnecessarily.

Avoid:

> Create PlantInstance relationship.



Prefer:

> Add plant to Bed 2.



Avoid:

> Create Observation entity.



Prefer:

> Record observation.



The database may be sophisticated.

The interface should be simple.


---

65. Avoiding Wizard Overload

Use the shortest appropriate interaction.

Simple tasks should remain short:

Quick Add → Harvest → Save

Complex tasks may legitimately use multiple steps:

Create Experiment

must not become a multi-step wizard merely because the underlying data model is complex.


---

66. Confirmation Philosophy

Do not ask for confirmation after every ordinary action.

Confirmation is appropriate when:

data could be lost;

the action is difficult to undo;

the consequence is significant;

the user could reasonably misunderstand what will happen.



---

67. Feedback After Actions

Actions should produce clear confirmation.

Examples:

> Harvest recorded.



> Bed 2 updated.



> Plant added to My Plants.



> Plan saved.



Where useful, provide:

Undo

or:

View

Feedback should be immediate for ordinary local actions.


---

68. Modular UX

Major functional modules should be able to evolve independently while using shared interaction conventions.

Examples:

map;

plant library;

planner;

recommendations;

observations;

harvests;

Inbox;

experiments.


Modules should communicate through well-defined interfaces rather than unnecessary internal coupling.

Optional modules should fail independently wherever practical.

For example, a future weather module failing must not prevent:

viewing the garden;

recording a harvest;

viewing plants;

viewing history;

using recommendations based on stored information.


Future community functionality must likewise not affect private garden operation.


---

69. Core Workflows

The following are protected V1 workflows:

1. Open the application and understand what matters.


2. View and interact with the garden.


3. View plants.


4. Plan what to grow.


5. Determine whether something is suitable.


6. Record what actually happened.


7. Review garden history.


8. Understand why an important recommendation was made.



New functionality must not materially degrade these workflows.


---

70. Performance

Common local interactions should feel immediate.

Particular priorities are:

opening Home;

opening Garden;

viewing a plant;

opening Quick Add;

recording an activity;

saving an observation.


Complex analysis may take longer, but ordinary garden interaction should remain responsive.


---

71. Never Collect Information Without Purpose

Every significant request for user information should satisfy at least one of:

the benefit is explained;

the field is optional;

the question is deferred until relevant;

the information is necessary for the task.


The user should be able to understand:

> Why should I bother entering this?



If there is no useful answer, the application should not request the information.


---

72. Contextual Prompts

Additional information may be requested when there is a clear reason.

Example:

> You are planning tomatoes for this bed, but spring temperature information is uncertain.



Then offer:

Add temperature information

or:

Continue anyway

The user must understand why the information was requested and must generally be able to proceed when the information is optional.


---

73. User Decision and Recommendation Interaction

When a user acts on a recommendation, the application should distinguish the decision from the subsequent activity.

For example:

Recommendation

Consider planting beans.

User decision

Accepted.

Actual activity

Beans planted in Bed 2 on 14 August.

Accepting a recommendation does not prove that the recommendation was acted upon.

Similarly, rejecting or overriding a recommendation does not imply that the recommendation was incorrect.


---

74. V1 UX Success Criteria

V1 should allow an ordinary gardener to accomplish the following without understanding the application's internal architecture.

Garden setup

Create a garden.

Add a growing space.

Represent its real dimensions.

Record basic conditions.


Plant management

Find a plant.

Add a plant to the garden.

Record a cultivar where known.

View relevant growing information.


Planning

Select an empty space.

Ask what could grow there.

Understand the recommendation.

Create a planting plan.

Override a recommendation.


Recording

Quickly record planting.

Record watering.

Record feeding.

Record pruning.

Record observations.

Record harvests.

Record losses.


History

See what was planted previously.

Review observations.

Review harvests.

Review outcomes.

Understand how history affects recommendations.


Soil

Record known soil information.

Record observations when measurements are unavailable.

Distinguish estimates from measurements.

Understand when additional testing may be useful.


Inbox

See important actions.

Understand why they matter.

Complete, mute or dismiss them appropriately.



---

75. V1 UX Non-Goals

V1 should not attempt to provide:

a social network;

mandatory cloud accounts;

automatic community sharing;

AI chat as the primary interface;

complex statistical dashboards;

professional GIS functionality;

fully automated garden design;

sophisticated weather dashboards;

complex chemical treatment workflows;

excessive gamification;

unnecessary achievement systems.


These may be considered in future versions only where they support the core product.


---

76. UX Design Test

Every proposed feature should be evaluated against these questions:

1. Does it make the gardener's job easier?


2. Does it support a core gardening decision or activity?


3. Does it require unnecessary data entry?


4. Can a casual user ignore it?


5. Can an advanced user access greater detail where useful?


6. Does it preserve garden history?


7. Is the result explainable?


8. Can it work offline, or is connectivity genuinely necessary?


9. Can it operate independently of unrelated modules?


10. Does it simplify the user's experience rather than expose internal complexity?



If the answers reveal unnecessary complexity or weak user value, the feature should be simplified, deferred, or removed from V1.


---

77. UX Decision Hierarchy

When design choices conflict, prioritise:

1. Reliability of core garden functions


2. Ease of common gardening tasks


3. Clarity


4. Data integrity and historical preservation


5. Explainability


6. Progressive precision


7. Accessibility


8. Advanced functionality


9. Visual polish



A visually impressive feature must never compromise a core function.


---

78. Final UX Principle

Garden Planner & Manager should feel like:

> A garden notebook that understands your garden.



It should be:

simple enough for casual use;

powerful enough for detailed garden management;

transparent about what it knows;

transparent about what it does not know;

clear about what it thinks;

clear about why it thinks it;

respectful of the gardener's decisions;

protective of garden history;

useful offline;

capable of becoming more sophisticated without becoming more complicated.


The application's goal is not to make the gardener manage more data.

Its goal is to make the gardener's existing knowledge, observations, plans, and history more useful.

> The gardener should experience increasing usefulness, not increasing complexity.