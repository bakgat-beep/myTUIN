Garden Planner & Manager — Contribution & Development Guidelines

Document: docs/CONTRIBUTION_AND_DEVELOPMENT_GUIDELINES.md
Version: 0.1
Status: Working specification
Last updated: August 2026

1. Purpose

This document defines the development principles, contribution practices and implementation standards for the Garden Planner & Manager application.

The project is intended to become a substantial, long-lived application with multiple independently developed modules.

The purpose of these guidelines is therefore not merely to make the code work today.

The purpose is to ensure that:

the core product remains reliable;

new functionality can be added without destabilising existing functionality;

modules can be developed with reasonable independence;

the codebase remains understandable;

future developers can safely modify the application;

user data remains protected;

architectural decisions remain deliberate;

technical debt does not accumulate unnecessarily.



---

2. Core development philosophy

The project should follow this principle:

> Build the simplest reliable system that can support the intended product, while deliberately preserving clean boundaries for future expansion.



The project should avoid both extremes:

Under-engineering

Building V1 so narrowly that every future feature requires major restructuring.

Over-engineering

Building elaborate abstractions, infrastructure or frameworks for hypothetical future requirements that do not yet justify the complexity.

The desired approach is:

Simple now + deliberate boundaries + extensible foundations.


---

3. Core product takes priority

The Garden Planner & Manager has a defined core product.

The core product must remain reliable even as additional features are introduced.

When a new feature conflicts with the reliability, usability or maintainability of a core feature, the new feature should be reconsidered rather than allowing the core product to become unnecessarily complicated.


---

4. Core product protection principle

Every new feature should be evaluated by asking:

> How could this feature adversely affect the core product?



Consider:

additional dependencies;

additional data relationships;

increased startup time;

increased application complexity;

new failure modes;

database changes;

UI complexity;

testing requirements;

maintenance burden.


If a feature provides little value while substantially increasing risk, it should be simplified, deferred or redesigned.


---

5. Modular development

The application should be constructed from reasonably independent modules.

A module should have:

a clear purpose;

defined responsibilities;

clear inputs;

clear outputs;

defined dependencies;

limited knowledge of unrelated modules.


The goal is not to make every file completely independent.

The goal is to prevent a small change in one area from requiring widespread changes throughout the project.


---

6. Minimise unnecessary coupling

Developers should actively avoid unnecessary coupling.

A module should not directly manipulate the internal implementation details of another module when a stable interface can be used instead.

Prefer:

Module A → defined interface → Module B

over:

Module A → internal implementation of Module B


---

7. Change isolation

A desirable development experience is:

> "I can improve this feature without having to rewrite half the application."



When a feature consistently requires changes across many unrelated files, this should trigger architectural review.

It may indicate:

excessive coupling;

unclear ownership;

inappropriate shared state;

poor abstraction boundaries;

duplicated responsibilities.



---

8. Do not over-modularise

Modularity itself can become a source of complexity.

Do not create separate modules merely because it is technically possible.

A module should represent a meaningful domain or responsibility.

Prefer a small number of cohesive modules over dozens of tiny abstractions.


---

9. Cohesion

Code belonging to the same responsibility should generally live together.

For example, functionality responsible for managing a particular domain concept should not be scattered across unrelated locations without a good reason.

High cohesion makes a module easier to:

understand;

test;

modify;

replace;

document.



---

10. Single responsibility

A component should have a reasonably clear responsibility.

This does not mean every class must contain only one method.

It means a component should not simultaneously become responsible for unrelated concerns.

For example, a component should not ordinarily:

render UI;

perform database migrations;

implement recommendation rules;

manage localisation;

perform file export.


Those concerns should have appropriate boundaries.


---

11. Separation of concerns

The project should maintain clear separation between:

presentation;

application behaviour;

domain logic;

persistence;

knowledge;

recommendation logic;

localisation;

import/export;

infrastructure.


The exact implementation architecture is defined in the technical architecture documentation.


---

12. UI should not become the application

UI components should primarily coordinate presentation and user interaction.

They should not become repositories for large amounts of:

domain logic;

persistence logic;

recommendation logic;

migration logic.


This makes UI changes safer and allows the underlying functionality to be reused elsewhere.


---

13. Business logic should not depend unnecessarily on UI

Core domain behaviour should not require knowledge of:

screen layout;

widget structure;

navigation implementation;

translated strings;

specific UI components.


This allows the same logic to be used by:

different screens;

future platforms;

background operations;

tests;

future modules.



---

14. Persistence separation

Database access should be separated from domain/application logic.

Features should not scatter raw database operations throughout unrelated UI components.

A clear persistence boundary makes it easier to:

change storage technology;

migrate schemas;

test business logic;

inspect data access;

prevent accidental data corruption.



---

15. Domain models versus database models

Do not automatically assume that every database representation should become the application's public domain representation.

Where useful, maintain an appropriate boundary between:

persistence representation;

domain representation;

UI representation.


However, this separation should only be introduced where it provides genuine value.

Avoid unnecessary mapping layers for simple structures.


---

16. Avoid premature abstraction

Do not create elaborate abstractions simply because something might theoretically be reused someday.

First establish the actual requirement.

Then abstract where:

repetition is real;

responsibilities are clear;

variation is genuine;

the abstraction reduces complexity rather than increasing it.



---

17. Prefer simple abstractions

When an abstraction is needed, prefer the simplest abstraction that clearly expresses the responsibility.

A future developer should be able to understand why the abstraction exists.


---

18. Avoid speculative generalisation

Do not build generic systems for hypothetical future modules unless there is strong evidence that the generalisation is useful.

For example, do not create a massive universal "garden feature engine" merely because future features might exist.

Instead:

establish clear interfaces;

build the actual feature;

generalise when recurring requirements emerge.



---

19. Dependency direction

Dependencies should generally point toward stable foundational layers rather than unstable feature layers.

Conceptually:

UI

↓

Application/domain services

↓

Domain models

↓

Infrastructure interfaces

↓

Infrastructure implementations

The exact architecture may differ, but circular dependencies should be avoided.


---

20. Avoid circular dependencies

Modules should not depend on each other in ways that create cycles.

For example:

A → B → C → A

is a warning sign.

Circular dependencies make:

testing harder;

refactoring harder;

module removal harder;

understanding the system harder.



---

21. Stable interfaces

Where one module needs another module, define a deliberately small interface.

Do not expose internal implementation details unnecessarily.

A smaller interface generally provides greater future freedom.


---

22. Minimise blast radius

Before making a change, consider:

> How many unrelated parts of the application will have to change?



If a simple feature requires widespread changes, stop and assess whether the architecture should be adjusted first.

The objective is not zero affected files.

The objective is to keep the blast radius proportionate to the change.


---

23. Shared code

Shared utilities should be introduced carefully.

A shared component should have:

multiple genuine consumers;

a stable responsibility;

a clear API.


Do not move code into a shared utility merely because two files currently contain similar code.


---

24. Avoid the "god service"

Do not create a single enormous service that gradually becomes responsible for:

plants;

gardens;

observations;

recommendations;

weather;

tasks;

imports;

exports;

notifications;

settings.


Large services are difficult to understand and create extreme coupling.


---

25. Avoid the "god model"

Similarly, avoid creating one enormous model representing the entire garden and all application behaviour.

Entities should represent meaningful domain concepts.

Relationships should connect them where necessary.


---

26. Avoid global state

Global mutable state should be minimised.

Global state can make:

testing difficult;

module boundaries unclear;

bugs unpredictable;

feature interactions difficult to trace.


If shared state is genuinely required, its ownership and lifecycle should be explicit.


---

27. State ownership

Every significant piece of application state should have a clear owner.

Developers should be able to answer:

> "Which component is responsible for this state?"



If the answer is unclear, the architecture should be reconsidered.


---

28. Immutable data where appropriate

Where practical, data structures that represent facts or historical records should be treated as immutable.

This is particularly important for:

historical observations;

completed events;

audit-like records.


Changes should generally create a new state or explicit update rather than silently mutating history.


---

29. Historical data is special

Historical garden information should not be treated like temporary UI state.

Once recorded, it may become valuable evidence for:

future planning;

recommendations;

trend analysis;

comparison;

learning about the garden.


Changes to historical data therefore require additional care.


---

30. Do not overwrite history unnecessarily

If the application needs to correct or update historical information, the implementation should consider whether the correction should be:

an explicit edit;

a correction record;

a new event;

a versioned value.


The appropriate choice depends on the entity.


---

31. User data protection

Development decisions must treat user-generated garden data as valuable.

Developers should never casually:

delete production data;

change schemas without migration;

replace databases during testing;

alter identifiers;

change semantics without considering historical records.



---

32. Database changes

Any persistent schema change must be deliberate.

Before changing the database:

1. identify affected entities;


2. identify affected modules;


3. identify historical data;


4. determine migration requirements;


5. update the relevant documentation;


6. implement migration;


7. test migration.




---

33. Follow the migration strategy

Database changes must follow:

docs/DATA_MIGRATION_STRATEGY.md

A developer should not invent an ad hoc migration approach for an individual feature.


---

34. Controlled vocabularies

Canonical identifiers must follow the project's controlled vocabulary rules.

Do not use:

translated labels;

arbitrary spelling variants;

display strings;

user-facing text


as machine-readable canonical identifiers.


---

35. Localisation

Localisation belongs to the user experience.

The underlying data remains in canonical English identifiers.

A developer adding a new concept should therefore distinguish between:

Canonical identifier

and

Displayed translation.


---

36. No UI text as domain identity

Do not make application logic depend on strings such as:

"Full Sun";

"Volle son";

"Sunny";

"Sun loving".


The domain should use a canonical identifier.

The UI translates that identifier.


---

37. Knowledge versus user data

General gardening knowledge should not be confused with the user's personal garden history.

For example:

Knowledge:

"This plant generally prefers well-drained soil."

User data:

"This garden area remained waterlogged after heavy rain."

The second must not be overwritten because the first changed.


---

38. Recommendation logic

Recommendation logic should be separated from the core data model.

Recommendations should consume relevant information rather than embedding recommendation rules throughout unrelated entities.

This allows recommendation logic to evolve independently.


---

39. Recommendations must remain explainable

Where recommendations are surfaced to users, the system should be capable of explaining the relevant reasoning at an appropriate level.

Developers should avoid creating opaque logic that cannot be understood or tested.


---

40. Recommendation changes

Changing a recommendation rule should not silently alter historical user observations.

If recommendations are persisted, historical recommendation records should follow the rules in the migration and knowledge-base documentation.


---

41. Feature development sequence

For a substantial feature, development should generally proceed through:

1. clarify the user problem;


2. confirm scope;


3. identify affected domain entities;


4. identify module boundaries;


5. identify dependencies;


6. assess impact on the core product;


7. define UX;


8. define data changes;


9. define tests;


10. implement;


11. validate;


12. document.




---

42. Challenge the design before coding

Before implementing a substantial feature, ask:

Is the feature actually needed?

Is there a simpler way?

Is the data model necessary?

Does this belong in this module?

Does it create coupling?

Does it affect the core product?

Can it be deferred?

Can it be implemented independently?


The project explicitly values challenging designs before implementation.


---

43. Avoid feature creep

A feature request should not automatically become an opportunity to add several adjacent features.

Keep implementation aligned with the approved scope.

Additional ideas should be recorded for later consideration.


---

44. V1 discipline

V1 should deliver the core product rather than attempting to implement every planned future feature.

V1 should, however, avoid architectural decisions that unnecessarily prevent future modules from being added.

This distinction is important:

Do not build everything now.

but:

Do not unnecessarily make future development difficult.


---

45. Future-proofing

Future-proofing should focus on:

stable data;

stable identifiers;

module boundaries;

clean interfaces;

migrations;

extensibility.


It should not mean implementing unused infrastructure simply because it might eventually be useful.


---

46. Feature flags and experimental features

Experimental functionality should be isolated where possible.

If a feature is incomplete or unstable, it should not be allowed to destabilise core functionality.

The exact feature-flag mechanism is an implementation decision.


---

47. Dependencies

External dependencies should be added deliberately.

Before adding a dependency, consider:

why it is needed;

whether the functionality can reasonably be implemented without it;

maintenance activity;

licence;

platform support;

security;

performance;

future compatibility;

impact on application size.



---

48. Minimise dependency surface

A dependency should not be introduced for a trivial function that can easily be implemented using existing platform/framework capabilities.

Every dependency creates future maintenance responsibility.


---

49. Dependency ownership

A dependency should have a clear reason for existing.

If a dependency is only used by an optional module, it should not unnecessarily become a dependency of the core application.


---

50. Offline-first considerations

The application should favour local operation for core functionality where practical.

Features requiring external services should not unnecessarily compromise the ability to use core garden-planning functionality offline.


---

51. External services

When a feature requires:

weather services;

external plant databases;

cloud services;

AI services;

mapping services;


the dependency should be isolated from core domain logic.

The core application should not become structurally dependent on a particular external provider unless that dependency is intentional.


---

52. External service failure

Optional external services should fail gracefully.

A network failure should not cause unrelated garden data or core planning functions to fail.


---

53. Error handling

Errors should be handled at the appropriate layer.

Do not catch every error at the lowest level and silently ignore it.

Errors should either:

be handled meaningfully;

be transformed into an appropriate domain/application error;

propagate to a layer capable of handling them.



---

54. Never silently swallow errors

Avoid patterns where an exception/error is caught and ignored without explanation.

Silent failure is particularly dangerous for:

data writes;

migrations;

imports;

exports;

recommendations;

historical records.



---

55. User-facing error messages

Technical errors should be translated into useful user-facing messages.

A user should generally learn:

what went wrong;

whether their data is safe;

whether anything needs to be done.


They should not need to understand stack traces.


---

56. Logging

Logging should help developers diagnose problems without unnecessarily exposing user data.

Logs should generally avoid:

sensitive personal information;

unnecessary user-entered content;

credentials;

authentication tokens.



---

57. Debugging

When investigating a bug, developers should attempt to reproduce it using the smallest realistic example.

Avoid immediately changing multiple unrelated components.

A focused reproduction often reveals the actual architectural problem.


---

58. Fix the cause, not only the symptom

A bug fix should address the underlying cause where practical.

If a workaround is necessary, document why.

Repeated symptom fixes in different locations are a sign that the underlying abstraction may be wrong.


---

59. Refactoring

Refactoring is encouraged when it clearly improves:

maintainability;

clarity;

testability;

modularity;

reliability.


However, refactoring should be proportionate to the task.


---

60. Do not combine unrelated refactors

Avoid making large unrelated architectural changes while implementing a small feature.

This makes:

testing harder;

review harder;

regression diagnosis harder.



---

61. Refactor deliberately

When a refactor is substantial:

1. identify the problem;


2. define the desired structure;


3. preserve behaviour;


4. make the change;


5. test existing functionality;


6. document important architectural changes.




---

62. Code readability

Code should be written primarily for future maintainers.

Prefer:

clear names;

understandable control flow;

small cohesive functions;

explicit behaviour.


Avoid clever code when straightforward code would be easier to maintain.


---

63. Naming

Names should communicate purpose.

Prefer:

plannedPlantingDate

over ambiguous names such as:

date2

or:

value

Names should use the project's established terminology consistently.


---

64. Terminology consistency

The same domain concept should not be given several competing names throughout the codebase.

If terminology changes, update the appropriate documentation and vocabulary rather than introducing ad hoc synonyms.


---

65. Comments

Comments should explain:

why something is done;

important constraints;

non-obvious decisions;

temporary workarounds.


Comments should not merely restate obvious code.


---

66. TODO comments

TODOs should be meaningful.

A TODO should ideally identify:

what remains;

why;

relevant issue/documentation if available.


Avoid leaving vague TODOs indefinitely.


---

67. Temporary code

Temporary implementations should be clearly identifiable.

Do not allow a prototype implementation to become indistinguishable from production architecture.


---

68. Prototype versus production

A prototype may prioritise speed.

Production code must additionally prioritise:

correctness;

maintainability;

data safety;

testability.


Before promoting prototype code into the core product, review it against these standards.


---

69. Testing during development

Developers should test changes as they work rather than relying entirely on a final test pass.

At minimum, verify:

the feature works;

affected existing functionality still works;

error conditions behave appropriately.



---

70. Test boundaries

Tests should reflect module boundaries.

A module should ideally be testable without requiring the entire application to run.

This is one of the major benefits of modular architecture.


---

71. Unit tests

Use unit tests for focused logic such as:

calculations;

validation;

transformations;

recommendation rules;

migration transformations.



---

72. Integration tests

Use integration tests where behaviour depends on interaction between components.

Examples:

persistence;

repositories;

import/export;

module integration.



---

73. UI tests

UI tests should focus on important user journeys and behaviour.

Do not attempt to test every implementation detail of the UI.


---

74. Regression tests

When fixing a significant bug, add a regression test where practical.

The test should demonstrate that the original failure does not return.


---

75. Migration tests

Any schema change requires appropriate migration testing.

Refer to:

docs/DATA_MIGRATION_STRATEGY.md

and:

docs/TESTING_STRATEGY.md.


---

76. Test data

Test fixtures should be representative and should not contain unnecessary real personal data.

Use synthetic or appropriately anonymised data.


---

77. Test determinism

Tests should be deterministic wherever practical.

Avoid unnecessary dependencies on:

current time;

network;

random state;

external services.


Where such dependencies are required, they should be controllable.


---

78. External service testing

External services should generally be abstracted so tests do not depend on live network services.

Use mocks, fakes or controlled test environments as appropriate.


---

79. Code review

Substantial changes should be reviewed against:

requirements;

architecture;

data model;

module boundaries;

testing;

migration implications;

user impact.


The purpose of review is not merely stylistic.


---

80. Review questions

A reviewer should ask:

Does this solve the intended problem?

Is there a simpler solution?

Does it affect core functionality?

Does it introduce unnecessary coupling?

Does it create data migration risk?

Is the module boundary still clear?

Are tests adequate?

Does documentation need updating?



---

81. Small changes preferred

Where possible, make changes in small, coherent increments.

Small changes are easier to:

understand;

test;

review;

revert;

diagnose.



---

82. Commits

Where version control is used, commits should represent coherent changes.

Avoid mixing:

unrelated features;

large formatting changes;

architectural changes;

bug fixes


into one opaque change.


---

83. Commit messages

Commit messages should describe the purpose of the change.

For example:

Add planting observation persistence

is preferable to:

changes


---

84. Documentation updates

Documentation is part of implementation.

If a change alters:

architecture;

data model;

scope;

UX;

module boundaries;

migration behaviour;

important design decisions;


the relevant project documentation must be updated.


---

85. Documentation is authoritative

When documentation is designated as the project specification, implementation should follow it unless a deliberate decision is made to change the specification.

Do not silently diverge from documented architecture.


---

86. Decision log

Important architectural decisions should be recorded in:

docs/DECISION_LOG.md

Examples include:

changing a core entity;

choosing a persistence technology;

adding a major dependency;

changing module boundaries;

changing migration strategy.



---

87. Challenge existing documentation

Documentation should not be treated as immutable.

If implementation reveals that a documented decision is flawed, challenge it.

The correct process is:

1. identify the problem;


2. propose an alternative;


3. evaluate consequences;


4. update the relevant documentation;


5. record the decision;


6. implement the revised approach.




---

88. Do not silently work around architecture

If the documented architecture makes a task unexpectedly difficult, do not simply create a workaround that introduces hidden technical debt.

First ask whether the architecture itself needs improvement.


---

89. Architecture debt

Architecture debt should be recorded rather than forgotten.

Examples:

temporary coupling;

deferred abstraction;

known migration limitation;

technical workaround.


A known problem is easier to manage than an invisible one.


---

90. Development in a no-code/low-code-assisted environment

Because the project may be developed collaboratively with AI-assisted coding, developers should be particularly careful about generated code.

Generated code must be treated as proposed implementation, not automatically authoritative implementation.

It must be:

inspected;

tested;

understood sufficiently;

checked against project documentation.



---

91. AI-assisted development

When using AI assistance, prompts should provide the relevant project context.

For substantial changes, provide:

relevant documentation;

affected module;

requirements;

constraints;

existing architecture.


Avoid asking an AI coding tool to redesign unrelated portions of the application merely because it is convenient.


---

92. AI-generated architectural changes

AI should not be allowed to introduce major architectural changes implicitly.

If generated code:

adds a new framework;

changes persistence;

reorganises modules;

changes data structures;

introduces a new architectural pattern;


stop and review the change explicitly.


---

93. Protect against generated-code coupling

AI coding tools may naturally modify multiple files to make a local feature work.

This is one reason the project explicitly values modularity.

If implementing a small feature repeatedly requires broad edits, review whether the architecture is becoming too coupled.


---

94. Explain changes

For significant implementation changes, the developer should be able to explain:

what changed;

why;

which module owns it;

what dependencies it has;

what tests cover it;

what future changes it enables or constrains.


If that explanation is difficult, the implementation may be too complicated.


---

95. Feature completion

A feature should not be considered complete merely because the happy path works.

Completion should consider:

validation;

error handling;

persistence;

migration;

tests;

UX;

accessibility where applicable;

documentation.



---

96. Definition of done

For a substantial feature, the practical definition of done is:

[ ] Requirements satisfied.

[ ] UX implemented.

[ ] Data model implemented if required.

[ ] Module boundary appropriate.

[ ] Core product unaffected adversely.

[ ] Error handling implemented.

[ ] Tests added.

[ ] Existing tests pass.

[ ] Migration handled if necessary.

[ ] Documentation updated.

[ ] No known critical regression remains.



---

97. Security

Security should be considered whenever handling:

external services;

user accounts;

cloud storage;

synchronisation;

imported files;

credentials;

tokens;

potentially sensitive user information.


Security-sensitive functionality should not be improvised.


---

98. Privacy

The application should minimise unnecessary collection and transmission of user data.

Features should not send garden information to external services unless there is a clear product requirement and appropriate user expectations/controls.


---

99. Local-first principle

The project's local/offline capability should be protected.

Adding a cloud or external-service feature should not unnecessarily turn previously local functionality into a network dependency.


---

100. Performance

Performance optimisation should be evidence-driven.

Do not prematurely optimise every component.

However, avoid obviously inefficient designs when the cost is clear.


---

101. Startup performance

New modules should not unnecessarily increase startup work.

Optional or expensive functionality should be loaded or initialised only when appropriate.


---

102. Large data sets

The architecture should remain capable of handling a garden with substantial accumulated history.

Developers should avoid designs that unnecessarily load all historical data into memory for simple operations.


---

103. UI performance

UI components should avoid unnecessary repeated work.

Long-running operations should not block the interface where practical.


---

104. Accessibility

Accessibility should be considered during feature design rather than added only at the end.

Important considerations include:

readable text;

sufficient interaction targets;

clear feedback;

understandable navigation;

appropriate semantic information.



---

105. Internationalisation readiness

The application should be designed so localisation can expand without rewriting domain logic.

This is particularly important because Afrikaans is an intended future user-interface language, alongside other potential languages.

The architecture should therefore avoid assuming that English is the only displayed language.


---

106. User language versus data language

The rule is:

User experience language may change.

Canonical data language does not.

This distinction must remain clear throughout development.


---

107. Naming localisation resources

Localisation resources should be kept separate from:

domain models;

database identifiers;

business rules.


A translated string should not become the canonical representation of a domain concept.


---

108. Internationalisation testing

Where localisation is introduced, test that:

longer translations do not break layouts;

text expansion is handled;

canonical identifiers remain unchanged;

language switching does not alter user data.



---

109. Release stability

Before release, the project should prioritise:

1. data integrity;


2. core functionality;


3. critical bugs;


4. migration reliability;


5. user experience;


6. optional enhancements.



A release should not sacrifice core reliability merely to include additional features.


---

110. Feature prioritisation

When deciding what to build next, consider:

user value;

core-product importance;

implementation complexity;

architectural risk;

data implications;

future extensibility;

maintenance cost.


A technically exciting feature is not automatically a high-priority feature.


---

111. Defer when appropriate

It is acceptable to deliberately defer a feature.

A deferred feature should not be allowed to distort V1 architecture unnecessarily.

Record important deferred decisions where appropriate.


---

112. Experimental modules

Experimental functionality should preferably be developed behind a clear boundary.

If an experiment fails, removing it should not require reconstructing the core application.


---

113. Module lifecycle

Modules should conceptually have a lifecycle:

Planned

→ Designed

→ In development

→ Integrated

→ Stable

→ Maintained

→ potentially Deprecated

Each stage should have appropriate documentation.


---

114. Module independence goal

The long-term development goal is:

> A developer should be able to work on one substantial module without needing to understand the implementation details of the entire application.



They still need to understand the module's:

public interfaces;

dependencies;

data contracts;

relevant domain concepts.


But unrelated implementation should remain isolated.


---

115. Core module stability

The core module should be especially conservative.

New modules should generally depend on the core rather than repeatedly forcing the core to depend on every new module.


---

116. Optional feature dependency direction

Where practical:

Core

provides stable capabilities.

Feature modules

consume those capabilities.

This reduces the risk that optional functionality destabilises the core.


---

117. Shared services

Some services may legitimately be shared.

Examples may eventually include:

persistence infrastructure;

localisation;

import/export;

notification infrastructure;

date/time handling.


Shared services should remain infrastructure rather than becoming repositories for arbitrary feature logic.


---

118. Domain events

If the application eventually needs communication between loosely coupled modules, domain/application events may be appropriate.

However, event-driven architecture should only be introduced where it genuinely reduces coupling or provides clear value.

Do not introduce a complex event bus merely because modularity is desired.


---

119. Avoid architecture fashion

The project should not adopt a technology, framework or architectural pattern merely because it is currently popular.

Every architectural decision should be justified by the application's needs.


---

120. Technology replacement

The architecture should avoid unnecessary dependence on implementation-specific details.

Where practical, this allows future replacement of:

database technology;

UI implementation;

external service;

recommendation engine;

localisation mechanism.


Replacement is not guaranteed, but unnecessary lock-in should be avoided.


---

121. Simplicity test

Before accepting a complex implementation, ask:

> Could a competent developer explain this architecture to another competent developer in a few minutes?



If not, determine whether the complexity is genuinely justified.


---

122. Maintenance test

Another useful test is:

> If I return to this code in two years, will I understand why it works this way?



If the answer is no, the implementation needs better:

structure;

naming;

documentation;

tests;

decision records.



---

123. Future developer test

Every important design should consider a developer who did not participate in the original implementation.

The project should not depend on:

undocumented assumptions;

knowledge held only in a conversation;

personal memory;

accidental behaviour.



---

124. Conversation-derived decisions

When an important decision is made during development discussion, it should eventually be incorporated into the appropriate project documentation.

The chat should not be treated as the permanent source of truth.


---

125. Source of truth hierarchy

When sources disagree, the project should generally resolve them in this order:

1. current approved product requirements;


2. current approved architecture/data specifications;


3. current decision log;


4. implementation;


5. historical discussion.



If implementation conflicts with approved documentation, determine whether the implementation or documentation is wrong and correct the appropriate source.


---

126. Documentation drift

Documentation drift is a significant project risk.

Periodically check whether:

documented architecture matches implementation;

module boundaries remain accurate;

data model matches database;

V1 scope remains accurate;

tests reflect current requirements.



---

127. Architecture review triggers

An architectural review should be considered when:

a small change affects many modules;

a new shared dependency is proposed;

multiple modules need the same new behaviour;

database changes become difficult;

migrations become complicated;

UI components contain substantial business logic;

testing becomes disproportionately difficult;

a feature requires extensive special cases.



---

128. Refactoring trigger

A repeated pattern of:

small requirement

→ many files changed

→ many regressions

should be treated as an architectural warning.

The solution may be to refactor before continuing feature development.


---

129. Technical debt budget

Technical debt is sometimes acceptable when it is:

deliberate;

understood;

documented;

temporary or strategically justified.


Undocumented accidental technical debt should be minimised.


---

130. Development priority hierarchy

When making implementation decisions, use this broad priority:

1. protect user data;


2. protect core functionality;


3. preserve correctness;


4. maintain architectural clarity;


5. maintain good UX;


6. add optional capability;


7. optimise implementation details.




---

131. What not to do

Developers should avoid:

rewriting unrelated modules to implement small features;

storing translated strings as canonical identifiers;

bypassing migration requirements;

adding dependencies casually;

hiding errors;

silently deleting data;

creating giant shared services;

creating abstractions for purely hypothetical requirements;

embedding business logic in UI;

embedding database logic throughout the application;

introducing cloud dependencies into offline core functionality without explicit justification.



---

132. Development checklist

Before implementing a significant change:

[ ] What user problem does this solve?

[ ] Is it within current scope?

[ ] Is there a simpler implementation?

[ ] Which module owns it?

[ ] Which modules does it depend on?

[ ] Does it affect the core product?

[ ] Does it change persistent data?

[ ] Does it require migration?

[ ] Does it affect import/export?

[ ] Does it affect localisation?

[ ] Does it affect knowledge or recommendations?

[ ] What tests are required?

[ ] What documentation must change?



---

133. Implementation checklist

During implementation:

[ ] Keep responsibilities separated.

[ ] Avoid unnecessary coupling.

[ ] Keep interfaces small.

[ ] Keep business logic out of UI where practical.

[ ] Keep persistence concerns isolated.

[ ] Preserve canonical identifiers.

[ ] Handle errors explicitly.

[ ] Add tests alongside functionality.

[ ] Avoid unrelated refactoring.

[ ] Review generated code carefully if AI assistance is used.



---

134. Pre-release checklist

Before releasing a substantial change:

[ ] Core functionality works.

[ ] Existing functionality still works.

[ ] New functionality works.

[ ] Tests pass.

[ ] Migration tests pass if applicable.

[ ] Import/export tests pass if applicable.

[ ] No critical data-loss path is known.

[ ] Documentation is updated.

[ ] Known limitations are documented.

[ ] Significant architectural decisions are recorded.



---

135. Final development principle

The ultimate development goal is not to produce the largest feature set as quickly as possible.

It is to build a system that can continue improving for years without becoming fragile.

The desired outcome is:

Reliable core


Clear modules


Small interfaces


Protected data


Testable behaviour


Deliberate architecture


Controlled evolution

→

A Garden Planner & Manager that can grow substantially without becoming unmaintainable.