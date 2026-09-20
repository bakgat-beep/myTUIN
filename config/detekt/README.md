# detekt configuration

`detekt.yml` contains overrides only. Gradle sets
`buildUponDefaultConfig = true`, so any rule not listed uses detekt's
default settings.

## Why overrides and not a generated file

`detektGenerateConfig` produces the full default config. Committing
that would mean new rules in a detekt upgrade stay inactive until
someone regenerates the file.

Building on defaults means a new rule activates immediately and its
findings surface. For a greenfield project that direction is correct:
findings are visible rather than silently not running.

## When to add an exception

Add a rule override here when:

- the rule conflicts with a documented project idiom (Compose
  PascalCase composables, Room entity naming, etc.);
- the rule's default threshold is calibrated for a different kind of
  codebase;
- the rule duplicates a check another tool already performs
  (formatting).

Do not add an exception to silence a single finding. That is what
`@Suppress` with a reason comment is for.

## Baseline

`baseline.xml` is referenced by the Gradle config but is not
committed. It exists so that `detektBaseline` can generate one if the
project ever needs it. If a baseline file appears, its contents should
be reviewed as carefully as the findings it suppresses.
