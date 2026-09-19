Garden Planner & Manager — V1 Screen Specification

Document: docs/V1_SCREEN_SPECIFICATION.md
Version: 0.2
Status: Working specification
Last updated: August 2026


---

1. Purpose

This document defines the functional screen specification for Version 1 of the Garden Planner & Manager.

It translates the product scope, UX specification, data model and vocabulary architecture into a practical screen structure.

It defines:

V1 screens;

screen responsibilities;

primary actions;

navigation;

information hierarchy;

important states;

relationships between screens;

progressive disclosure requirements.


It does not prescribe pixel-level visual design.

Visual design, component styling and implementation details should remain flexible unless they affect usability, accessibility or the functional requirements defined here.


---

2. Screen design principles

All V1 screens should follow these principles.

2.1 Action before data

The screen should make clear:

1. what matters;


2. what the user can do;


3. why it matters;


4. additional information.




---

2.2 Progressive disclosure

Show only the information needed for the current task.

Additional explanation and technical detail should be available through:

expandable sections;

detail views;

information controls;

secondary screens.



---

2.3 Context over complexity

Where the application already knows the relevant context, use it.

For example, opening:

> Add → Water



from Bed 2 should preselect Bed 2.

The user must always be able to change the context.


---

2.4 Unknown is a valid state

Screens must distinguish between:

unknown;

not recorded;

not measured;

not applicable;

observed;

measured;

estimated;

inferred.


Do not display missing information as though it were negative information.


---

2.5 History is preserved

Screens that edit garden information must not accidentally erase historical records.

Current state and historical state should remain distinguishable.


---

2.6 Recommendations are explainable

Important recommendations must provide access to:

factors considered;

positive factors;

negative factors;

missing information;

confidence;

source/personal-history effects where applicable;

conditions that could change the result.



---

3. Primary navigation

V1 uses five primary destinations:

1. Home


2. Garden


3. Plants


4. Planner


5. Inbox



A prominent + Add action is available globally.

Primary navigation should remain stable throughout V1.

Secondary features should normally live within one of these destinations rather than becoming additional top-level destinations.


---

4. Global Add action

The global Add action provides rapid access to common garden activities and records.

4.1 Primary options

Plant

Water

Harvest

Observe

Feed

Prune

Intervention


A More option may expose less-common actions.


---

4.2 Contextual behaviour

The Add action should inherit useful context.

Example:

Garden
  ↓
Bed 2
  ↓
+ Add
  ↓
Water

The Water screen should preselect Bed 2.

The user can change the location before saving.


---

4.3 Save behaviour

After saving, provide concise confirmation.

Examples:

Plant added.

Watering recorded.

Harvest recorded.

Observation saved.


Where practical, provide:

Undo;

View.



---

5. Home

Purpose

Home answers:

> What matters now?



It is not primarily a statistics dashboard.


---

5.1 Content hierarchy

Recommended order:

1. Current priorities


2. Upcoming activities or planting windows


3. Important Inbox items


4. Planning opportunities


5. Active experiments, where relevant


6. Garden status


7. Recent activity



The exact sections may be omitted when they contain nothing useful.


---

5.2 Current priorities

Show only items requiring meaningful attention.

Examples:

frost-sensitive planting approaching;

important garden task;

unresolved problem;

important soil issue;

experiment requiring measurement.


Each item should explain why it matters.


---

5.3 Upcoming

May include:

planned activities;

planting windows;

expected transitions;

upcoming reminders.


Planned activities must remain clearly distinct from completed activities.


---

5.4 New garden state

A newly created garden should show a useful setup state rather than an empty dashboard.

Example sequence:

1. Set garden location


2. Add a growing space


3. Add known conditions


4. Add plants



Optional steps can be skipped.


---

5.5 Recent activity

May show:

plantings;

watering;

harvests;

observations;

interventions;

other significant events.


Recent activity should be concise and link to the underlying record.


---

6. Garden

Purpose

Garden represents the physical garden and its spatial information.

The Garden area contains:

map;

areas;

growing spaces;

infrastructure;

plants;

conditions;

layers;

filters;

history.



---

7. Garden map

The map is the primary Garden screen.

7.1 Required interactions

The user can:

pan;

zoom;

select objects;

create objects;

move objects;

resize objects;

edit geometry;

inspect objects;

toggle layers;

filter visible information;

view historical states.



---

7.2 Map objects

Objects may include:

areas;

growing spaces;

plants;

infrastructure;

soil information;

experiments;

problems;

other supported spatial records.


Overlapping objects must be supported.


---

7.3 Object selection

Selecting an object should display a concise information panel.

Example:

Bed 2

Raised bed
3 m × 1 m

Current plants
Tomato
Lettuce

Conditions
Light: Good
Water: Unknown

[Details]


---

7.4 Map layers

V1 should support the layers required by implemented functionality.

Potential layers include:

Growing spaces

Current plants

Planned planting

Historical planting

Infrastructure

Soil

Light

Water

Rotation

Problems

Experiments


Layers should have sensible defaults.


---

7.5 Map filters

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

8. Growing space creation

Entry

Garden → + Add → Growing Space

Required information

Name

Type

Shape/geometry

Dimensions or geometry


Optional information

conditions;

notes;

characteristics;

infrastructure relationships.


The minimum viable record should be quick to create.


---

9. Growing space detail

Purpose

Provides a complete practical view of a growing space.

Content

Identity

name;

type;

dimensions;

location.


Current contents

plants;

planned plants.


Conditions

light;

water;

soil;

other recorded conditions.


Planning

suitable planting opportunities;

current plans;

rotation information.


History

previous plantings;

observations;

harvests;

interventions;

other relevant events.


Actions

Add plant;

Plan;

Observe;

Add condition;

View history;

Edit.



---

10. Spatial editing

Editing a growing space should allow geometry and dimensions to be changed directly.

Changing:

3 m × 1 m

to:

4 m × 1 m

must update current spatial calculations without deleting historical information.

Historical records remain associated with the growing space.


---

11. Garden history

Purpose

Allows the user to understand what happened in a location over time.

The user should be able to select:

current;

year;

date/range where supported.


Example:

History

Current
2025
2024
2023

The user should be able to answer:

> What was planted here?



without navigating through individual activity records.


---

12. Plants

Purpose

Plants is the user's primary entry point for plant knowledge and garden plant records.

It must clearly distinguish:

Plant Library;

My Plants.



---

13. Plant Library

Purpose

Provides general plant knowledge.

Required capabilities

search;

browse;

filter;

open plant;

view growing requirements;

view timing;

view relationships;

view problems;

view cultivars;

view sources where relevant.


Plant Library information is not the user's personal garden record.


---

14. My Plants

Purpose

Shows plants actually grown or planned by the user.

Each record may connect to:

plant;

cultivar;

location;

planting event;

activities;

observations;

problems;

harvests;

outcomes;

history.



---

15. Plant search

Search should support:

common names;

canonical names;

configured aliases;

cultivars where appropriate.


Results should clearly indicate whether the result is:

a plant;

a cultivar;

a user's plant instance.



---

16. Plant detail

Plant detail should prioritise practical gardening information.

Header

plant name;

image where available;

lifecycle;

basic classification.


Suitability

Show an accessible summary such as:

Suitability
Good candidate
Confidence: Moderate

Conditions

Relevant dimensions only:

Light

Water

Soil

Fertility

Temperature

Frost

Timing

Space

Other applicable requirements


Timing

Show relevant current timing.

Example:

Now
Indoor sowing

Next
Transplant

Garden

Show relevant personal information.

Example:

You have grown this plant in Bed 2.

Relationships

Show relevant relationships such as:

companion plants;

related plants;

rotation relationships.


Problems

Show relevant pests, diseases and other problems.

Technical information should be expandable.


---

17. Cultivar detail

Cultivars should be accessible from the relevant plant.

The screen must distinguish:

Plant characteristics

from:

Cultivar-specific characteristics

Where information is inherited from the parent plant, the UI should not imply that it was independently established for the cultivar.


---

18. Add plant

Entry

+ Add → Plant

Minimum workflow

1. Select plant


2. Select cultivar if known


3. Select location


4. Enter actual or planned date


5. Save



Optional details may be added afterwards.

The workflow should remain short.


---

19. Plant instance detail

This screen represents the user's actual plant or planting record.

Content

plant;

cultivar;

location;

planting date;

status;

relevant activities;

observations;

problems;

harvests;

outcomes;

history.


Actions

Record activity;

Observe;

Harvest;

Move;

Edit;

View history.



---

20. Planner

Purpose

Planner answers:

> What should I grow or do next?



It supports both:

forward planning;

planning a selected space.



---

21. Planner overview

The Planner may contain:

current plans;

upcoming planting opportunities;

empty spaces;

recommendations;

unresolved planning decisions.


The user should be able to start from either:

What should I plant next?

or:

What can I grow here?


---

22. Empty-space planning

Entry

Garden → Growing Space → Plan

or:

Planner → Select space

Workflow

1. Select space


2. Select planning period if needed


3. Review available information


4. Generate candidates


5. Compare candidates


6. Open explanations


7. Select candidate


8. Create plan or leave unplanned




---

23. Recommendation list

Each candidate should provide a concise explanation.

Example structure:

Tomato

Good candidate

Light       Excellent
Water       Good
Soil        Excellent
Temperature Marginal
Rotation    Poor
Space       Excellent

Confidence
Moderate

Why?
Your space has suitable summer light and soil.
Recent tomato planting creates a rotation concern.

Only materially relevant dimensions should be shown.


---

24. Recommendation detail

The user can open:

> Why?



The detail view should explain:

1. information used;


2. factors supporting the recommendation;


3. factors reducing suitability;


4. missing information;


5. confidence;


6. personal history effects;


7. user preferences affecting the result;


8. conditions that could change the recommendation.




---

25. Conditional recommendations

The screen must support recommendations such as:

Good candidate if drainage is improved.

Suitable once frost risk decreases.

Potentially suitable, but soil pH should be confirmed.

The UI should make the condition obvious.


---

26. Recommendation states

The UI should support the core recommendation states:

Recommended

Conditional

Possible

Marginal

Not recommended

Unsuitable

Insufficient information

Expired


Do not reduce all states to a simple good/bad indicator.


---

27. Recommendation confidence

Confidence must be displayed separately from suitability.

Example:

Suitability
Good

Confidence
Moderate

Why?
Winter sunlight information is incomplete.

A strong recommendation can still have limited confidence when important information is uncertain.


---

28. Recommendation with insufficient information

When information is insufficient, the application should explain what is missing.

Example:

Potentially suitable

Confidence: Low

Missing information
Winter sunlight

[Add information]
[Continue anyway]

The user must be allowed to continue.


---

29. "Nothing now" recommendation

The Planner must support a valid result of:

Nothing suitable right now

The explanation should state why.

Example:

No suitable crop currently fits this space and timing.

Consider improving drainage before planting.


---

30. Planning decision

When the user chooses a recommendation, the application may record a UserDecision.

Possible decisions include:

accepted;

rejected;

modified;

overridden;

deferred;

cancelled.


A decision does not prove that planting or another activity occurred.


---

31. Planning override

If the user chooses an option that the application considers unsuitable, allow it.

Example:

Tomato

Not recommended

Reason:
Recent tomato planting creates a rotation concern.

[Plant anyway]

If selected:

You chose to plant this despite the recommendation.

[Continue]

The application may record the override.

It must not block the user merely because a recommendation disagrees.


---

32. Plan detail

A plan represents intended future activity.

It should show:

planned activity;

plant/location where applicable;

planned date or period;

status;

related recommendation where applicable;

notes;

completion status.


Planned information must never automatically become completed activity evidence.


---

33. Recurring plans

A recurring plan may specify:

Water every 3 days

The interface should show:

recurrence;

upcoming occurrences;

completed occurrences;

skipped occurrences.


Each actual occurrence remains an independent activity record.


---

34. Partial completion

Plans involving multiple targets must support partial completion.

Example:

Water Beds 1–4

Completed
Beds 1–3

Remaining
Bed 4

The plan should remain partially completed until resolved.


---

35. Quick Add

Quick Add is the primary rapid-recording workflow.

Actions

Plant

Water

Harvest

Observe

Feed

Prune

Intervention


Additional actions may appear under More.


---

36. Quick Add activity screens

Simple activity records should normally fit on one screen.

Example:

Harvest

Plant       Tomato
Location    Bed 2
Quantity    5
Unit        Count
Date        Today

[Save]

Optional information should be hidden or secondary.


---

37. Water

Minimum fields:

target/location;

date/time;

amount or method where known.


The user should be able to save without knowing an exact volume if that information is unavailable.


---

38. Feed

Minimum fields:

target/location;

date;

material/product if known;

amount if known.


Complex application details should remain optional.


---

39. Prune

Minimum fields:

target;

date;

category/type where useful.


Optional notes can be added afterwards.


---

40. Intervention

Intervention should provide enough structure to record what was done without requiring a complex treatment workflow.

Possible information:

target;

problem;

intervention type;

date;

material/method where relevant;

result;

notes.


V1 should avoid unnecessary chemical-treatment complexity.


---

41. Harvest

Minimum fields

plant;

location;

quantity;

unit;

date.


Weight is optional.

Supported units may include:

count;

bunch;

basket;

container;

other.



---

42. Harvest size

Optional size categories:

Very small

Small

Regular

Large

Very large


Size must not be required.


---

43. Harvest loss

Loss is recorded separately from harvest.

Minimum information:

plant;

quantity;

unit;

cause if known;

date.


The application must not invent a precise lost yield.


---

44. Observation

Observation must be one of the simplest records to create.

Workflow

1. Select context


2. Select observation type


3. Record relevant fields


4. Optionally add notes


5. Optionally record confidence


6. Save




---

45. Structured observation types

The application should show fields according to the selected observation type.

Examples:

Soil

moisture;

pooling;

texture;

compaction;

visible organic matter.


Plant

growth;

flowering;

fruiting;

damage;

symptoms.


The application should not show one enormous generic observation form.


---

46. Observation versus diagnosis

Observation screens must never imply that an observation is a diagnosis.

Example:

Observation

Leaves yellowing

A subsequent analysis may show:

Possible causes

Nutrient deficiency
Low confidence

Water stress
Moderate confidence

Disease
Low confidence

The original observation remains unchanged.


---

47. Soil

Soil information should support both measured and approximate information.

The user can record:

measured properties;

observations;

estimates;

inferred properties.



---

48. Soil overview

The soil screen should show known information first.

Example:

Soil

pH
6.7
Measured — Home test kit

Drainage
Probably slow
Estimated from observations

Salinity
Unknown

Each value should identify its information state where useful.


---

49. Soil observation

The user may record proxy observations when measurements are unavailable.

Example workflow:

Soil → Drainage → Add observation

Potential questions:

Does water pool after rain?

How long does it remain?

Does the soil feel saturated?

Does water drain quickly after digging?



---

50. Soil estimate

An estimate should clearly distinguish itself from measurement.

Example:

Estimated drainage

Probably slow

Confidence
Low

Why?
Based on your recorded observations.

This is not a measured soil property.


---

51. Soil measurement

Measured values should preserve:

value;

unit;

date;

method/source;

location;

confidence where applicable.


Example:

pH
6.7

Method
Home test kit

Date
12 March 2026


---

52. Soil testing prompt

When testing could materially improve a recommendation:

A soil test may be worthwhile.

Your current information cannot distinguish between several likely causes.

[Add test result]
[Continue without testing]

Testing must remain optional.


---

53. Inbox

Purpose

Inbox is a prioritised work queue.

It contains actionable or useful items rather than every possible notification.


---

54. Inbox categories

Items may include:

Action;

Warning;

Observation request;

Information request;

Experiment;

Knowledge.



---

55. Inbox priority

Priorities should distinguish:

Important

Likely to require attention.

Useful

Worth considering.

Informational

Interesting but not urgent.

The visual treatment must not rely on colour alone.


---

56. Inbox item detail

Each significant item should explain:

what it is;

why it exists;

what the user can do;

relevant evidence;

what happens if ignored where meaningful.


Example:

Rotation warning

Solanaceae were grown here last season.

Why this matters
Repeated planting of related crops can increase certain disease
and soil-related risks.

[View history]
[Plan alternative]
[Mute]


---

57. Inbox actions

Users may:

complete;

mute;

dismiss;

open related information.



---

58. Mute

Mute options may include:

temporarily;

until a date;

category;

context;

permanently.


Muted items remain reviewable.


---

59. Dismiss

Dismiss means:

> I have dealt with this or otherwise consider it resolved.



Dismissal must not automatically mean:

> Never show this type of item again.




---

60. Search

Search should be available from appropriate list screens and globally where practical.

Searchable concepts include:

plants;

cultivars;

growing spaces;

problems;

activities;

observations.


Aliases should resolve to canonical entities where configured.


---

61. Filters

Filters should appear when a list is large enough to benefit from them.

Filters must be:

understandable;

reversible;

visible when active;

easy to clear.


Avoid complex filter configuration for ordinary tasks.


---

62. Empty states

Every major screen must have a useful empty state.

Example:

You haven't added any plants yet.

Add a plant to start building your garden record.

[Add plant]
[Browse Plant Library]

Empty states should explain the purpose of the screen where useful.


---

63. Error states

Errors should use plain language.

Example:

We couldn't save that change.

[Retry]
[Cancel]

Details

Technical information may be available through Details but should not be the primary message.


---

64. Undo

Where practical, provide Undo after actions such as:

deletion;

moving an object;

completing an activity;

changing important information.


Actions that can destroy significant historical information require stronger protection.


---

65. Editing historical information

Ordinary corrections may update a record directly.

Where an edit changes the meaning of a historical measurement or event, preserve the historical distinction.

Example:

Soil pH
6.7
Measured in 2025

A later correction should not silently make it appear that the original measurement was always different.


---

66. Import/export

Import/export is an advanced function.

The user-facing workflow is:

1. Select file


2. Validate


3. Review summary


4. Review errors/conflicts


5. Cancel or continue


6. Confirm


7. Import


8. Review result



The application must not silently import invalid data.


---

67. Import result

Example:

Import complete

14 records imported successfully.

2 records require attention.

[Review issues]
[Done]

Each problem should identify:

affected record;

problem;

required action.


Raw database errors should not be required.


---

68. Preferences

Preferences should remain optional.

Potential preferences include:

favourite plants;

plants to avoid;

food production priority;

native preference;

pollinator preference;

low-maintenance preference;

water conservation;

experimentation;

space utilisation.


Preferences influence recommendations but should not silently hide information.


---

69. Preference conflict

Example:

Lettuce

Suitability
Excellent

Preference
You marked lettuce as a plant to avoid.

Result
Excluded by preference

The application should make the reason visible.


---

70. Personal history

When personal history materially affects a recommendation, show it explicitly.

Example:

Your garden history

Carrots have had poor establishment in this bed
during three recorded seasons with poor drainage.

This is based on your garden history and is not necessarily
a universal horticultural rule.


---

71. Personal-history recommendation

Example:

Tomato

General suitability
Good

Your history
Poor results with this cultivar in Bed 2
during the previous two seasons.

Recommendation
Consider another cultivar or growing space.

The user should be able to inspect the relevant historical records.


---

72. Experiments

Experiments are an advanced feature.

Creation

Create experiment

Possible fields:

question;

hypothesis;

treatment;

control;

measurements;

timeframe.


The experiment should reuse ordinary activities, observations and measurements.


---

73. Experiment detail

Show:

question;

hypothesis;

setup;

treatment/control;

observations;

measurements;

results;

conclusion.


Clearly distinguish:

Observed result

from:

Conclusion


---

74. Notifications

V1 notifications should remain limited.

Prioritise genuinely time-sensitive information.

Potential settings include:

category;

reminder frequency;

quiet periods;

muted contexts.


Avoid building a complex notification-management system unless required by actual V1 functionality.


---

75. Cross-linking

Screens should provide direct access to related records.

Examples:

Plant
  → My Plants

Plant instance
  → History

Problem
  → Affected plants

Growing space
  → Current plants
  → History
  → Soil
  → Recommendations

The user should never need to understand the underlying relationship model.


---

76. Context preservation

Navigation should preserve useful context.

Example:

Garden
→ Bed 2
→ Tomato
→ History

Returning should ideally return to the relevant Bed 2 context.


---

77. Accessibility requirements

All V1 screens should support:

readable text;

sufficient contrast;

touch-friendly controls;

accessible labels;

text scaling;

predictable navigation;

meaningful icons;

non-colour-only status communication.


For example:

Good ✓

should communicate meaning without relying solely on green.


---

78. Mobile interaction

The primary interaction model is touch.

Prioritise minimal taps for:

Quick Add;

plant search;

map interaction;

harvest;

observation;

planning.


Complex configuration may use deeper screens.


---

79. Offline behaviour

Core screens must work from locally stored data without Internet access.

Offline functionality must include, where applicable:

Home;

Garden;

Plants;

Planner using stored knowledge;

Quick Add;

observations;

harvests;

history.


If a feature genuinely requires connectivity, the screen should state that dependency without implying that the entire application is unavailable.

Example:

Weather unavailable offline.

Your saved garden information and planning tools
continue to work normally.


---

80. Screen-state requirements

Every major screen should consider at least these states:

1. populated;


2. empty;


3. loading where applicable;


4. error;


5. offline;


6. filtered;


7. partially configured;


8. insufficient information.



Not every state requires a unique visual treatment, but the user must understand what is happening.


---

81. Minimum V1 screen set

The following functional screens are sufficient as the initial V1 screen architecture.

Primary

Home

Garden Map

Plants

Plant Library

My Plants

Planner

Inbox


Garden

Growing Space Detail

Add/Edit Growing Space

Garden History


Plants

Plant Search

Plant Detail

Cultivar Detail where required

Add Plant

Plant Instance Detail


Planning

Planner Overview

Empty Space Planning

Recommendation List

Recommendation Detail

Plan Detail


Recording

Quick Add

Plant Activity

Water

Feed

Prune

Intervention

Harvest

Harvest Loss

Observation

Soil Observation

Soil Measurement


Supporting

Soil Detail

Inbox Item Detail

Search

Filter

Preferences

Experiment Detail/Create

Import/Export


Some of these may be implemented as sheets, dialogs or nested views rather than full standalone pages.


---

82. Screen ownership

Each screen should have a clear primary domain.

Screen	Primary responsibility

Home	Current priorities
Garden	Spatial garden
Growing Space	Individual growing space
Plants	User plant management
Plant Library	General plant knowledge
Plant Detail	General plant knowledge
Plant Instance	User's actual plant
Planner	Planning decisions
Recommendation	Suitability explanation
Inbox	Prioritised work
Quick Add	Rapid recording
Soil	Soil information
History	Past garden state
Experiment	Optional structured experimentation
Preferences	Recommendation preferences
Import/Export	Advanced data management


A screen may display related information but should not become the owner of unrelated functionality.


---

83. Avoiding unnecessary screens

The screen architecture should not become more complicated simply because the data model is sophisticated.

A simple action should remain simple.

For example:

+ Add
→ Harvest
→ Save

should not require several separate screens merely to populate optional database fields.

Conversely, a complex operation such as importing data may legitimately require multiple stages.


---

84. Relationship to the data model

Screens must present user concepts rather than database entities.

Do not expose terms such as:

PlantInstance entity;

Observation entity;

Activity relationship;

RecommendationFactor record.


Use:

My plant;

Record observation;

Activity;

Why this recommendation?


The screen architecture may map to sophisticated underlying entities without exposing that complexity.


---

85. Relationship to vocabularies

Screens use canonical vocabulary identifiers internally.

User-facing labels come from localisation.

For example:

Stored:
partial_shade

Displayed:
Partial shade

Business logic must not depend on displayed labels.


---

86. Relationship to recommendations

Recommendation screens must remain independent from any particular recommendation algorithm.

The screen should consume structured results such as:

recommendation state;

confidence;

factors;

reasons;

missing information;

evidence;

personal-history effects.


This allows the recommendation engine to evolve without requiring a redesign of the entire application.


---

87. Relationship to future modules

Future functionality should preferably connect to existing screens.

Examples:

Weather:

Garden → Conditions
Planner → Timing

Community knowledge:

Plant → Sources
Recommendation → Evidence

Advanced analytics:

History → Analysis

New functionality should not unnecessarily create additional top-level navigation.


---

88. V1 screen acceptance criteria

The screen architecture is acceptable when an ordinary gardener can:

Garden setup

create a garden;

add a growing space;

represent its dimensions;

record basic conditions.


Plant management

find a plant;

understand general plant information;

add a plant to the garden;

record a cultivar;

view the plant's garden history.


Planning

select an empty space;

ask what could grow there;

understand the recommendation;

create a plan;

override a recommendation.


Recording

record planting;

record watering;

record feeding;

record pruning;

record an observation;

record a harvest;

record a loss.


History

see previous plantings;

review observations;

review harvests;

understand how history affects recommendations.


Soil

record known soil information;

record observations;

record measurements;

distinguish estimates from measurements;

understand when testing may help.


Inbox

identify important items;

understand why they matter;

complete, mute or dismiss them.



---

89. V1 screen non-goals

The V1 screen architecture does not need dedicated primary screens for:

social networking;

community feeds;

mandatory accounts;

AI chat;

professional GIS;

complex statistical dashboards;

sophisticated weather dashboards;

fully automated garden design;

complex chemical treatment management;

gamification;

achievement systems.


These may be added later without changing the core navigation if justified.


---

90. Screen design test

Every new screen should be challenged with the following questions:

1. What user problem does this screen solve?


2. Could the task be completed more simply within an existing screen?


3. Is the screen exposing database structure rather than a user concept?


4. Does it support a core gardening workflow?


5. Can a casual user ignore advanced functionality?


6. Are important explanations available without cluttering the primary view?


7. Are unknown and uncertain information represented honestly?


8. Does the screen preserve historical information?


9. Does it work offline where the underlying function should work offline?


10. Does it fit the existing navigation model?



If a new screen cannot justify itself, prefer integrating its function into an existing screen.


---

91. V1 UX hierarchy

Across all screens, information should generally appear in this order:

1. What matters


2. What can I do


3. What happened / what is known


4. Why


5. What is uncertain


6. Additional detail



This hierarchy should be preserved even where the underlying data model is complex.


---

92. Final principle

The screen architecture should make the application feel like:

> A garden notebook that understands your garden.



The user should experience:

simple navigation;

fast recording;

useful planning;

understandable recommendations;

preserved history;

progressive detail.


The application may contain sophisticated data, rules and relationships behind the scenes.

The screens should make that sophistication useful without making it visible unnecessarily.