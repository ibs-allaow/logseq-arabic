(ns logseq.e2e.settings
  (:require [logseq.e2e.assert :as assert]
            [logseq.e2e.keyboard :as k]
            [wally.main :as w]))

(defn open-settings!
  []
  (w/click "button[title='More'] .ls-icon-dots")
  (w/click ".ls-icon-settings")
  (w/wait-for ".ui__dialog-content[label=app-settings]"))

(defn close-settings!
  []
  (k/esc)
  (assert/assert-in-normal-mode?))

(defn set-language!
  [lang-code]
  (open-settings!)
  (.selectOption (w/-query ".ui__dialog-content[label=app-settings] select.form-select.is-small") lang-code)
  (w/wait-for (format "html[lang=\"%s\"]" lang-code))
  (close-settings!))

(defn developer-mode
  []
  (open-settings!)
  (w/click "[data-id='advanced']")
  (let [q (.last (w/-query ".ui__toggle [aria-checked='false']"))]
    (when (.isVisible q)
      (w/click q)))
  (k/esc)
  (assert/assert-in-normal-mode?))
