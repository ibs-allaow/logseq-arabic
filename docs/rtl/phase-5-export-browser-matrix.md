# RTL Phase 5 — Export HTML Compatibility Matrix

## Scope
- `src/main/frontend/handler/export/html.cljs`
- Exported HTML root attributes (`lang`, `dir`) driven by the selected/preferred language.

## Behavior implemented
1. Export language is resolved in priority order:
   - `options.other-options.preferred-language` (if provided by caller)
   - `document.documentElement.lang` (browser runtime fallback)
   - `"en"` as safe default
2. Exported HTML root node always includes:
   - `lang=<normalized-language-code>`
   - `dir=<ltr|rtl>` (derived via centralized `util/language-direction`)

## Browser validation checklist

### Chromium
- Open exported HTML in Chromium.
- Verify ordered/unordered list indentation remains logical in RTL and LTR.
- Verify `blockquote` marker/indent is rendered on the expected visual side.
- Verify `table` header/body cells align and flow correctly for RTL Arabic content.

### Firefox
- Repeat the same checks for lists, blockquote, and tables.
- Verify mixed-direction lines (Arabic + English terms) do not flip punctuation unexpectedly.

### WebKit
- Repeat checks for list/blockquote/table.
- Verify no clipping or overflow regressions with long RTL lines in table cells.

## Suggested sample cases
- LTR + English-only block tree.
- RTL + Arabic-only block tree.
- Mixed bidi block tree (Arabic sentence containing inline English words and numbers).
