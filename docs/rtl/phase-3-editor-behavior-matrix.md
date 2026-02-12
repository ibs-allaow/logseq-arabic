# RTL Phase 3 — Editor Horizontal Navigation Behavior Matrix

This document defines expected editor behavior for horizontal navigation and block-boundary transitions after Phase 3 changes.

## Scope
- `src/main/frontend/extensions/code.cljs`
- `src/main/frontend/handler/editor.cljs`
- `src/main/frontend/util.cljc`

## Direction model
- Physical key intent is captured first (`ArrowLeft` => `:left`, `ArrowRight` => `:right`).
- Visual direction is then resolved through `util/logical-horizontal-direction` based on preferred language.
- In RTL languages, `:left` and `:right` are flipped before cursor/block-boundary actions are executed.

## Behavior matrix

| Scenario | Input key | Preferred language | Resolved visual direction | Expected behavior |
|---|---|---|---|---|
| LTR + English | ArrowLeft | en | :left | Move cursor toward line start; cross to previous block at boundary. |
| LTR + English | ArrowRight | en | :right | Move cursor toward line end; cross to next block at boundary. |
| RTL + Arabic | ArrowLeft | ar | :right | Move cursor visually forward in RTL text flow; cross boundary accordingly. |
| RTL + Arabic | ArrowRight | ar | :left | Move cursor visually backward in RTL text flow; cross boundary accordingly. |
| Mixed bidi in one block | ArrowLeft/Right | ar/en | language-resolved | Preserve visual movement contract at block boundaries even if content includes mixed scripts. |

## Code touchpoints
- `util.cljc`: `flip-horizontal-direction`, `logical-horizontal-direction`
- `extensions/code.cljs`: CodeMirror bridge applies logical direction before boundary checks
- `handler/editor.cljs`: `keydown-arrow-handler` and `open-selected-block!` apply same logical direction mapping

## Manual validation checklist
1. Switch language to English and verify ArrowLeft/ArrowRight boundary movement is unchanged.
2. Switch language to Arabic and verify horizontal boundary movement is visually correct (not physically inverted).
3. In a mixed bidi block, verify boundary crossing still targets expected neighboring block.
