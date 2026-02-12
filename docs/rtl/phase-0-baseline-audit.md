# RTL Phase 0 — Baseline & Audit

This document is the execution output for **Roadmap Phase 0 (Baseline & Audit)**.

## Scope

Phase 0 audits the current codebase to identify all high-impact RTL gaps before implementation phases.

Primary areas:
- Global direction and locale binding
- Layout and styling with physical left/right properties
- Editor keyboard behavior and cursor navigation
- Sidebar resize/drag behavior
- Exported HTML direction consistency
- Test coverage and verification gaps

---

## 1) Current baseline (what is already implemented)

### 1.1 Global language -> direction wiring
- `theme.cljs` now sets `lang` and `dir` on `documentElement` and toggles `is-rtl` based on selected language. This is a solid starting point.
  - `src/main/frontend/components/theme.cljs`
- `util.cljc` contains `rtl-language?` and `rtl-languages` helpers.
  - `src/main/frontend/util.cljc`

### 1.2 Small CSS logical migration started
- `common.css` migrated list spacing from `margin-left` to `margin-inline-start`.
  - `src/main/frontend/common.css`

### 1.3 Remaining gap
The baseline changes are foundational, but not enough for full RTL parity. Most UI surfaces still include physical left/right assumptions.

---

## 2) Audit findings by subsystem

## 2.1 Global direction state and language pipeline

### Status
- Direction is currently derived in UI layer (`theme/container`) from preferred language.
- Preferred language is updated from settings and saved in state/storage.

### Verified files
- `src/main/frontend/components/theme.cljs`
- `src/main/frontend/state.cljs`
- `src/main/frontend/components/settings.cljs`
- `src/main/frontend/dicts.cljc`

### Risk
- Direction behavior is not explicitly tested (unit or e2e).
- No explicit contract doc for downstream components/plugins to consume `dir`.

---

## 2.2 CSS/layout physical-direction debt (high)

A repository scan shows extensive use of physical-direction CSS (`left/right`, `margin-left/right`, `padding-left/right`, `border-left/right`, `text-align:left/right`, `float:left/right`) across frontend components.

### High-impact hotspots
- `src/main/frontend/components/container.css`
- `src/main/frontend/components/block.css`
- `src/main/frontend/components/header.css`
- `src/main/frontend/components/right_sidebar.cljs` (runtime math + classes)
- `src/main/frontend/components/plugins.css`
- `src/main/frontend/extensions/pdf/pdf.css`
- `src/main/frontend/ui.css`

### Risk
- Sidebars and overlays may appear mirrored incorrectly.
- Positioning and drag handles may operate opposite to visual direction.
- Incremental fixes without a migration policy will regress quickly.

---

## 2.3 Editor keyboard/cursor directional logic (high)

Editor behavior contains explicit assumptions for `ArrowLeft/ArrowRight` and `:left/:right` semantics.

### Verified files
- `src/main/frontend/extensions/code.cljs`
- `src/main/frontend/handler/editor.cljs`
- `src/main/frontend/modules/shortcut/config.cljs`
- `src/main/frontend/util/cursor.cljs`

### Risk
- In RTL mode, visual cursor movement and semantic block actions can diverge.
- Indent/outdent and boundary crossing may feel inverted for Arabic users.

---

## 2.4 Sidebar resize and interaction model (medium-high)

Right sidebar resize code computes width ratio using fixed assumptions about edge orientation and key direction.

### Verified file
- `src/main/frontend/components/right_sidebar.cljs`

### Risk
- Drag and keyboard resize may be counterintuitive under RTL.
- Cursor-resize classes may not match expected affordances.

---

## 2.5 Export/publishing direction output (medium)

Current HTML export flow renders content HTML but does not attach explicit RTL/LTR direction in the output root structure.

### Verified file
- `src/main/frontend/handler/export/html.cljs`

### Risk
- Exported HTML for Arabic content may render with browser defaults (LTR), causing visual inconsistency.

---

## 2.6 Test coverage and quality gates (high)

Project has lint/test tasks (`bb dev:lint-and-test`, `bb dev:test`) but no explicit RTL-focused assertions discovered in baseline audit.

### Verified file
- `bb.edn`

### Risk
- RTL regressions can slip in silently after unrelated UI work.

---

## 3) Prioritized execution backlog from audit

### P0 (must-do first)
1. Add RTL behavior tests for language-to-direction contract (`rtl-language?`, `dir` application).
2. Create CSS migration policy: all new UI must use logical properties; no new physical direction props without exception notes.
3. Audit + fix editor left/right behavior in key handlers for RTL mode.

### P1
4. Refactor sidebar resize and keyboard controls to be direction-aware.
5. Add explicit `dir`/`lang` to HTML export root wrappers.

### P2
6. Systematic migration of large CSS hotspots (`container.css`, `block.css`, `header.css`, `plugins.css`, `pdf.css`).
7. Add e2e smoke suite for RTL (settings switch -> editor typing -> sidebar resize -> export check).

---

## 4) Suggested implementation slices (next phases)

- **Phase 1:** Direction contract hardening + unit tests
- **Phase 2:** Core layout logical CSS migration (container/header/sidebar)
- **Phase 3:** Editor keyboard/cursor RTL semantics
- **Phase 4:** Export/publishing + e2e coverage

---

## 5) Audit command set used

- CSS physical-direction scan:
  - `rg -n "margin-left|margin-right|padding-left|padding-right|left:|right:|inset-left|inset-right|float:\\s*left|float:\\s*right|text-align:\\s*left|text-align:\\s*right" src/main/frontend -g"*.css"`
- RTL-sensitive logic scan:
  - `rg -n "ArrowLeft|ArrowRight|:left|:right|handler-position|setAttribute doc \"dir\"|preferred-language|export-blocks-as-html|render-html" src/main/frontend`

