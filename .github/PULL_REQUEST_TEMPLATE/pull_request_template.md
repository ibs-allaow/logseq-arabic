<!--
Thank you for the pull request. Please provide a description. Screenshots and gifs are appreciated for UX enhancements, new features and bug fixes!

For bug fixes and new features, please include tests and possibly benchmarks.
-->

## RTL smoke test checklist (when change impacts direction-sensitive UI)
- [ ] Switch language to Arabic from Settings and verify `html[dir="rtl"]` + `html[lang="ar"]`.
- [ ] Verify left/right sidebar handles are on the expected visual side in RTL.
- [ ] Verify ArrowLeft/ArrowRight interactions behave correctly in RTL surfaces touched by this PR.
- [ ] Verify exported HTML includes `lang` and `dir` and visually check list/blockquote/table in at least one browser.

