# RTL Phase 4 — Sidebar Interaction Checklist

## Target files
- `src/main/frontend/components/right_sidebar.cljs`
- `src/main/frontend/components/left_sidebar.cljs`
- `src/main/frontend/components/container.cljs`

## What was changed
1. Right sidebar resizer now derives ratio through `handler-position` using a shared `offset->ratio` helper.
2. Right sidebar resizer exposes `data-side` (`west`/`east`) for direction-aware hit area placement in CSS.
3. Right sidebar keyboard resize keeps consistent physical arrow semantics and applies side mapping through `handler-position`.
4. Left sidebar resizer exposes accessibility metadata (`role=separator`, `aria-orientation`, `aria-label`, `tabIndex`) and `data-side`.
5. Container keeps RTL propagation through root classes/states for sidebars/window-controls interactions.

## Manual verification matrix

### A) Open/close behavior
- LTR: open right sidebar, confirm resizer is on left edge of right sidebar.
- RTL: open right sidebar, confirm resizer is on right edge of right sidebar.
- Close/open repeatedly and confirm no hit-area drift.

### B) Keyboard resize
- Focus resizer and press ArrowLeft/ArrowRight in LTR and RTL.
- Confirm width changes track visual side location (no inversion caused by language switch).

### C) Fullscreen + window controls
- With `ls-window-controls` active, toggle right sidebar open/close in normal and fullscreen modes.
- Confirm header/right-sidebar topbar spacing remains correct with RTL/LTR state classes.

### D) Accessibility
- Verify resizer elements expose separator semantics and are keyboard-focusable.
- Verify `aria-label` is present and meaningful for left and right sidebar separators.
